package com.github.yulichang.interceptor;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.mapper.MPJTableMapperHelper;
import com.github.yulichang.method.MPJResultType;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.MPJReflectionKit;
import com.github.yulichang.toolkit.support.SelectColumn;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.resultmap.MybatisLabel;
import com.github.yulichang.wrapper.resultmap.Result;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 连表拦截器
 * 用于实现动态resultType
 * 之前的实现方式是mybatis-plus的Interceptor,无法修改args,存在并发问题
 * 所以将这个拦截器独立出来
 *
 * @author yulichang
 */
@SuppressWarnings("unchecked")
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class MPJInterceptor implements Interceptor {


    private static final Log logger = LogFactory.getLog(MPJInterceptor.class);

    private static final List<ResultMapping> EMPTY_RESULT_MAPPING = new ArrayList<>(0);

    /**
     * 缓存MappedStatement,不需要每次都去重新构建MappedStatement
     */
    private static final Map<String, Map<Configuration, MappedStatement>> MS_CACHE = new ConcurrentHashMap<>();


    @Override
    @SuppressWarnings("Java8MapApi")
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (args[0] instanceof MappedStatement) {
            MappedStatement ms = (MappedStatement) args[0];
            if (args[1] instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) args[1];
                Object ew = map.containsKey(Constants.WRAPPER) ? map.get(Constants.WRAPPER) : null;
                if (!map.containsKey(Constant.PARAM_TYPE)) {
                    map.put(Constant.PARAM_TYPE, Objects.nonNull(ew) && (ew instanceof MPJBaseJoin));
                } else {
                    logger.warn(String.format("请不要使用MPJ预留参数名 %s", Constant.PARAM_TYPE));
                }
                if (CollectionUtils.isNotEmpty(map) && map.containsKey(Constant.CLAZZ)) {
                    Class<?> clazz = (Class<?>) map.get(Constant.CLAZZ);
                    if (Objects.nonNull(clazz)) {
                        List<ResultMap> list = ms.getResultMaps();
                        if (CollectionUtils.isNotEmpty(list)) {
                            ResultMap resultMap = list.get(0);
                            if (resultMap.getType() == MPJResultType.class) {
                                args[0] = getMappedStatement(ms, clazz, ew);
                            }
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }


    /**
     * 获取MappedStatement
     */
    @SuppressWarnings("rawtypes")
    public MappedStatement getMappedStatement(MappedStatement ms, Class<?> resultType, Object ew) {
        String id = ms.getId() + StringPool.UNDERSCORE + resultType.getName();
        if (ew instanceof MPJLambdaWrapper) {
            MPJLambdaWrapper wrapper = (MPJLambdaWrapper) ew;
            wrapper.setEntityClass(MPJTableMapperHelper.getEntity(getEntity(ms.getId())));
            //TODO 重构缓存 -> 根据sql缓存
            //不走缓存
        }
        if (ew instanceof MPJQueryWrapper) {
            MPJQueryWrapper wrapper = (MPJQueryWrapper) ew;
            return getCache(ms, id + StringPool.UNDERSCORE + wrapper.getSqlSelect(), resultType, ew);
        }
        return buildMappedStatement(ms, resultType, ew, id);
    }

    /**
     * 走缓存
     */
    private MappedStatement getCache(MappedStatement ms, String id, Class<?> resultType, Object ew) {
        Map<Configuration, MappedStatement> statementMap = MS_CACHE.get(id);
        if (CollectionUtils.isNotEmpty(statementMap)) {
            MappedStatement statement = statementMap.get(ms.getConfiguration());
            if (Objects.nonNull(statement)) {
                return statement;
            }
        }
        MappedStatement mappedStatement = buildMappedStatement(ms, resultType, ew, id);
        if (statementMap == null) {
            statementMap = new ConcurrentHashMap<>();
            MS_CACHE.put(id, statementMap);
        }
        statementMap.put(ms.getConfiguration(), mappedStatement);
        return mappedStatement;
    }


    /**
     * 构建新的MappedStatement
     */
    private MappedStatement buildMappedStatement(MappedStatement ms, Class<?> resultType, Object ew, String id) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), id, ms.getSqlSource(), ms.getSqlCommandType())
                .resource(ms.getResource())
                .fetchSize(ms.getFetchSize())
                .statementType(ms.getStatementType())
                .keyGenerator(ms.getKeyGenerator())
                .timeout(ms.getTimeout())
                .parameterMap(ms.getParameterMap())
                .resultSetType(ms.getResultSetType())
                .cache(ms.getCache())
                .flushCacheRequired(ms.isFlushCacheRequired())
                .useCache(ms.isUseCache());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            builder.keyProperty(String.join(StringPool.COMMA, ms.getKeyProperties()));
        }
        List<ResultMap> resultMaps = buildResultMap(ms, resultType, ew);
        builder.resultMaps(resultMaps);
        return builder.build();
    }

    /**
     * 构建resultMap TODO 可以用lambda简化代码
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private List<ResultMap> buildResultMap(MappedStatement ms, Class<?> resultType, Object obj) {
        List<ResultMap> result = new ArrayList<>();
        TableInfo tableInfo = TableInfoHelper.getTableInfo(resultType);
        String id = ms.getId() + StringPool.DOT + Constants.MYBATIS_PLUS + StringPool.UNDERSCORE + resultType.getName();
        //基本数据类型
        if (MPJReflectionKit.isPrimitiveOrWrapper(resultType)) {
            return Collections.singletonList(new ResultMap.Builder(ms.getConfiguration(), id, resultType, EMPTY_RESULT_MAPPING).build());
        }
        if (!(obj instanceof MPJLambdaWrapper) || Map.class.isAssignableFrom(resultType) ||
                Collection.class.isAssignableFrom(resultType)) {
            result.add(getDefaultResultMap(tableInfo, ms, resultType, id));
            return result;
        }
        MPJLambdaWrapper wrapper = (MPJLambdaWrapper) obj;
        Map<String, Field> fieldMap = ReflectionKit.getFieldMap(resultType);
        List<SelectColumn> columnList = wrapper.getSelectColumns();
        //移除对多查询列，为了可重复使用wrapper
        columnList.removeIf(SelectColumn::isLabel);
        List<ResultMapping> resultMappings = new ArrayList<>(columnList.size());
        for (SelectColumn i : columnList) {
            TableFieldInfo info = i.getTableFieldInfo();
            if (StringUtils.isNotBlank(i.getAlias())) {
                //优先别名查询 selectFunc selectAs
                Class<?> aliasField = fieldMap.containsKey(i.getAlias()) ? fieldMap.get(i.getAlias()).getType() : null;
                if (aliasField == null) {
                    continue;
                }
                ResultMapping.Builder builder = new ResultMapping.Builder(ms.getConfiguration(), i.getAlias(),
                        i.getAlias(), aliasField);
                if (i.getFuncEnum() == null || StringUtils.isBlank(i.getFuncEnum().getSql())) {
                    Field f = fieldMap.get(i.getAlias());
                    if (f == null) {
                        continue;
                    }
                    if (info != null && info.getTypeHandler() != null && info.getTypeHandler() != UnknownTypeHandler.class) {
                        if (f.getType() == info.getField().getType()) {
                            builder.typeHandler(getTypeHandler(ms, info));
                        }
                    }
                }
                resultMappings.add(builder.build());
            } else if (info != null) {
                // select selectAll selectAsClass
                if (i.getFuncEnum() == null || StringUtils.isBlank(i.getFuncEnum().getSql())) {
                    ResultMapping.Builder builder = new ResultMapping.Builder(ms.getConfiguration(), info.getProperty(),
                            StringUtils.getTargetColumn(info.getColumn()), info.getPropertyType());
                    Field f = fieldMap.get(info.getProperty());
                    if (f == null) {
                        continue;
                    }
                    if (f.getType() == info.getField().getType()) {
                        if (info.getTypeHandler() != null && info.getTypeHandler() != UnknownTypeHandler.class) {
                            builder.typeHandler(getTypeHandler(ms, info));
                        }
                    }
                    builder.javaType(f.getType());
                    resultMappings.add(builder.build());
                } else {
                    resultMappings.add(new ResultMapping.Builder(ms.getConfiguration(), info.getProperty(),
                            StringUtils.getTargetColumn(info.getColumn()), info.getPropertyType()).build());
                }
            } else {
                // 主键列
                resultMappings.add(new ResultMapping.Builder(ms.getConfiguration(), i.getTagProperty(),
                        StringUtils.getTargetColumn(i.getColumnName()), i.getKeyType())
                        .flags(Collections.singletonList(ResultFlag.ID)).build());
            }
        }
        Set<String> columnSet = resultMappings.stream().map(ResultMapping::getColumn).collect(Collectors.toSet());
        //移除result中不存在的标签
        resultMappings.removeIf(i -> !fieldMap.containsKey(i.getProperty()));
        if (wrapper.isResultMap()) {
            for (Object o : wrapper.getResultMapMybatisLabel()) {
                MybatisLabel<?, ?> label = (MybatisLabel<?, ?>) o;
                resultMappings.add(buildResult(ms, label, columnSet, columnList));
            }
        }
        result.add(new ResultMap.Builder(ms.getConfiguration(), id, resultType, resultMappings).build());
        return result;
    }


    //fix 重上往下会有resultMap覆盖问题,应该从根节点开始,id向上传递

    /**
     * @return 返回节点id
     */
    private ResultMapping buildResult(MappedStatement ms, MybatisLabel<?, ?> mybatisLabel, Set<String> columnSet,
                                      List<SelectColumn> columnList) {
        List<Result> resultList = mybatisLabel.getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }
        List<ResultMapping> childMapping = new ArrayList<>(resultList.size());
        for (Result r : resultList) {
            String columnName = r.getColumn();
            //列名去重
            columnName = getColumn(columnSet, StringUtils.getTargetColumn(columnName));
            columnList.add(SelectColumn.of(mybatisLabel.getEntityClass(), r.getColumn(), null,
                    Objects.equals(columnName, r.getColumn()) ? null : columnName, null, null, true, null));
            ResultMapping.Builder builder = new ResultMapping.Builder(ms.getConfiguration(), r.getProperty(),
                    StringUtils.getTargetColumn(columnName), r.getJavaType());
            if (r.isId()) {//主键标记为id标签
                builder.flags(Collections.singletonList(ResultFlag.ID));
            }
            //TypeHandle
            TableFieldInfo info = r.getTableFieldInfo();
            if (info != null && info.getTypeHandler() != null && info.getTypeHandler() != UnknownTypeHandler.class) {
                builder.typeHandler(getTypeHandler(ms, info));
            }
            childMapping.add(builder.build());
        }

        String childId;
        if (CollectionUtils.isEmpty(mybatisLabel.getMybatisLabels())) {
            childId = "MPJ_" + mybatisLabel.getEntityClass().getName() + StringPool.UNDERSCORE + mybatisLabel.getOfType().getName() +
                    StringPool.UNDERSCORE + childMapping.stream().map(i -> "(" + (CollectionUtils.isEmpty(i.getFlags()) ?
                            ResultFlag.CONSTRUCTOR : i.getFlags().get(0)) + "-" + i.getProperty() + "-" + i.getColumn() + ")")
                    .collect(Collectors.joining(StringPool.DASH));
        } else {
            //递归调用
            StringBuilder sb = new StringBuilder("MPJ_[");
            for (MybatisLabel<?, ?> o : mybatisLabel.getMybatisLabels()) {
                if (Objects.isNull(o)) {
                    continue;
                }
                ResultMapping result = buildResult(ms, o, columnSet, columnList);
                if (Objects.isNull(result)) {
                    continue;
                }
                childMapping.add(result);
                sb.append(result.getNestedResultMapId());
                sb.append("@");
            }
            sb.append("]");
            sb.append("_MPJ_")
                    .append(mybatisLabel.getEntityClass().getName())
                    .append(StringPool.UNDERSCORE)
                    .append(mybatisLabel.getOfType().getName())
                    .append(StringPool.UNDERSCORE);
            childId = sb + childMapping.stream().map(i -> "(" + (CollectionUtils.isEmpty(i.getFlags()) ?
                            ResultFlag.CONSTRUCTOR : i.getFlags().get(0)) + "-" + i.getProperty() + "-" + i.getColumn() + ")")
                    .collect(Collectors.joining(StringPool.DASH));
        }
        //双检
        if (!ms.getConfiguration().hasResultMap(childId)) {
            ResultMap build = new ResultMap.Builder(ms.getConfiguration(), childId, mybatisLabel.getOfType(), childMapping).build();
            MPJInterceptor.addResultMap(ms, childId, build);
        }
        return new ResultMapping.Builder(ms.getConfiguration(), mybatisLabel.getProperty())
                .javaType(mybatisLabel.getJavaType())
                .nestedResultMapId(childId)
                .build();
    }

    /**
     * 获取字段typeHandle
     */
    private TypeHandler<?> getTypeHandler(MappedStatement ms, TableFieldInfo info) {
        TypeHandlerRegistry registry = ms.getConfiguration().getTypeHandlerRegistry();
        TypeHandler<?> typeHandler = registry.getMappingTypeHandler(info.getTypeHandler());
        if (typeHandler == null) {
            typeHandler = registry.getInstance(info.getPropertyType(), info.getTypeHandler());
        }
        return typeHandler;
    }

    /**
     * 列名去重 重复的添加 mpj 前缀 再重复走递归
     *
     * @param pool       查询列 集合
     * @param columnName 列明
     * @return 唯一列名
     */
    private String getColumn(Set<String> pool, String columnName) {
        if (!pool.contains(columnName)) {
            pool.add(columnName);
            return columnName;
        }
        columnName = "join_" + columnName;
        return getColumn(pool, columnName);
    }

    /**
     * 获取非lambda的resultMap
     */
    private ResultMap getDefaultResultMap(TableInfo tableInfo, MappedStatement ms, Class<?> resultType, String id) {
        if (tableInfo != null && tableInfo.isAutoInitResultMap()) {
            //补充不全的属性 并且是基础数据类型及其包装类
            ResultMap resultMap = ms.getConfiguration().getResultMap(tableInfo.getResultMap());
            List<ResultMapping> resultMappings = resultMap.getResultMappings();
            List<Field> fieldList = ReflectionKit.getFieldList(resultType);
            fieldList.removeIf(i -> resultMappings.stream().anyMatch(r -> i.getName().equals(r.getProperty())));
            if (CollectionUtils.isNotEmpty(fieldList)) {
                //复制已有的resultMapping
                List<ResultMapping> resultMappingList = new ArrayList<>(resultMappings);
                //复制不存在的resultMapping
                for (Field i : fieldList) {
                    if (MPJReflectionKit.isPrimitiveOrWrapper(i.getType())) {
                        resultMappingList.add(new ResultMapping.Builder(ms.getConfiguration(),
                                i.getName(), i.getName(), i.getType()).build());
                    }
                }
                return new ResultMap.Builder(ms.getConfiguration(), id, resultType, resultMappingList).build();
            }
        }
        return new ResultMap.Builder(ms.getConfiguration(), id, resultType, EMPTY_RESULT_MAPPING).build();
    }


    /**
     * 往 Configuration 添加resultMap
     */
    private synchronized static void addResultMap(MappedStatement ms, String key, ResultMap resultMap) {
        if (!ms.getConfiguration().hasResultMap(key)) {
            ms.getConfiguration().addResultMap(resultMap);
        }
    }

    private Class<?> getEntity(String id) {
        try {
            return Class.forName(id.substring(0, id.lastIndexOf(StringPool.DOT)));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}