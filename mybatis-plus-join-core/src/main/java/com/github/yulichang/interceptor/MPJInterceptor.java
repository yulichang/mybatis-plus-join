package com.github.yulichang.interceptor;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.adapter.base.tookit.VersionUtils;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.*;
import com.github.yulichang.toolkit.support.FieldCache;
import com.github.yulichang.wrapper.interfaces.SelectWrapper;
import com.github.yulichang.wrapper.resultmap.IResult;
import com.github.yulichang.wrapper.resultmap.Label;
import com.github.yulichang.wrapper.segments.Select;
import com.github.yulichang.wrapper.segments.SelectLabel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 连表拦截器
 * <p>
 * 用于实现动态resultType
 *
 * @author yulichang
 */
@SuppressWarnings("unchecked")
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class MPJInterceptor implements Interceptor {

    private static final boolean v = VersionUtils.compare(MybatisPlusVersion.getVersion(), "3.4.3.1") > 0;

    private static final List<ResultMapping> EMPTY_RESULT_MAPPING = new ArrayList<>(0);

    private static final Map<String, Val> MS_MAPPER_CACHE = new ConcurrentHashMap<>();

    private static final Map<String, Val> RES_MAPPER_CACHE = new ConcurrentHashMap<>();
    private static final Log log = LogFactory.getLog(MPJInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (args[0] instanceof MappedStatement) {
            MappedStatement ms = (MappedStatement) args[0];
            if (args[1] instanceof Map) {
                Map<String, Object> map = (Map<String, Object>) args[1];
                Object ew = map.getOrDefault(Constants.WRAPPER, null);
                if (Objects.nonNull(ew) && ew instanceof MPJBaseJoin) {
                    if (CollectionUtils.isNotEmpty(map)) {
                        Class<?> rt = null;
                        if (map.containsKey(Constant.CLAZZ)) {
                            rt = (Class<?>) map.get(Constant.CLAZZ);
                        } else {
                            if (CollectionUtils.isNotEmpty(ms.getResultMaps())) {
                                Class<?> entity = MPJTableMapperHelper.getEntity(getMapper(ms.getId(), ms.getResource()));
                                Class<?> type = ms.getResultMaps().get(0).getType();
                                if (Objects.nonNull(entity) && Objects.nonNull(type)
                                        && !MPJReflectionKit.isPrimitiveOrWrapper(type) && entity == type) {
                                    rt = type;
                                }
                            }
                        }
                        if (Objects.nonNull(rt)) {
                            args[0] = getMappedStatement(ms, rt, ew, map);
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
    public <E> MappedStatement getMappedStatement(MappedStatement ms, Class<?> resultType, Object ew, Map<String, Object> map) {
        if (ew instanceof SelectWrapper) {
            SelectWrapper<E, ?> wrapper = (SelectWrapper<E, ?>) ew;
            if (wrapper.getEntityClass() == null) {
                wrapper.setEntityClass((Class<E>) MPJTableMapperHelper.getEntity(getMapper(ms.getId(), ms.getResource())));
            }
            if (wrapper.getSelectColumns().isEmpty() && wrapper.getEntityClass() != null) {
                wrapper.selectAll();
            }
            if (wrapper.isResultMapCollection()) {
                if (map.values().stream().anyMatch(a -> a instanceof IPage) && !wrapper.isPageByMain()) {
                    // 一对多分页问题警告
                    log.warn("select one to many and page query will result in errors in the total count statistics, please use xml.");
                }
            }
        }
        return buildMappedStatement(ms, resultType, ew);
    }


    /**
     * 构建新的MappedStatement
     */
    private MappedStatement buildMappedStatement(MappedStatement ms, Class<?> resultType, Object ew) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), ms.getSqlSource(), ms.getSqlCommandType())
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
    private List<ResultMap> buildResultMap(MappedStatement ms, Class<?> resultType, Object obj) {
        List<ResultMap> result = new ArrayList<>();
        TableInfo tableInfo = TableHelper.get(resultType);
        String id = ms.getId() + StringPool.DOT + Constants.MYBATIS_PLUS + StringPool.UNDERSCORE + resultType.getName();
        //基本数据类型
        if (MPJReflectionKit.isPrimitiveOrWrapper(resultType)) {
            return Collections.singletonList(new ResultMap.Builder(ms.getConfiguration(), id, resultType, EMPTY_RESULT_MAPPING).build());
        }
        if (!(obj instanceof SelectWrapper) || Map.class.isAssignableFrom(resultType) ||
                Collection.class.isAssignableFrom(resultType)) {
            result.add(getDefaultResultMap(tableInfo, ms, resultType, id));
            return result;
        }
        SelectWrapper<?, ?> wrapper = (SelectWrapper<?, ?>) obj;
        Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(resultType);
        List<Select> columnList = wrapper.getSelectColumns();
        //移除对多查询列，为了可重复使用wrapper
        columnList.removeIf(Select::isLabel);
        List<ResultMapping> resultMappings = new ArrayList<>();
        Set<String> columnSet = new HashSet<>();
        for (Select i : columnList) {
            if (i.isHasAlias()) {
                FieldCache field = fieldMap.get(i.getAlias());
                columnSet.add(i.getAlias());
                if (Objects.nonNull(field)) {
                    ResultMapping.Builder builder = new ResultMapping.Builder(ms.getConfiguration(), i.getAlias(),
                            i.getAlias(), field.getType());
                    resultMappings.add(selectToResult(wrapper.getEntityClass(), i, field.getType(), builder));
                }
            } else {
                FieldCache field = fieldMap.get(i.getColumProperty());
                if (StringUtils.isNotBlank(i.getTagColumn())) {
                    columnSet.add(i.getTagColumn());
                    if (Objects.nonNull(field)) {
                        ResultMapping.Builder builder = new ResultMapping.Builder(ms.getConfiguration(), i.getColumProperty(),
                                i.getTagColumn(), field.getType());
                        resultMappings.add(selectToResult(wrapper.getEntityClass(), i, field.getType(), builder));
                    }
                } else if (wrapper.isResultMap()) {
                    AdapterHelper.getAdapter().parserColum(wrapper.getAlias(), wrapper.getFrom(), i.getColumn(), col -> {
                        String tagCol = MPJStringUtils.getTargetColumn(col);
                        FieldCache strField = fieldMap.get(tagCol);
                        columnSet.add(tagCol);
                        if (Objects.nonNull(strField)) {
                            ResultMapping.Builder builder = new ResultMapping.Builder(ms.getConfiguration(), tagCol,
                                    tagCol, strField.getType());
                            resultMappings.add(selectToResult(wrapper.getEntityClass(), i, strField.getType(), builder));
                        }
                    });
                }
            }
        }
        if (wrapper.isResultMap()) {
            for (Label<?> o : wrapper.getResultMapMybatisLabel()) {
                resultMappings.add(buildResult(ms, o, columnSet, columnList));
            }
        }
        result.add(new ResultMap.Builder(ms.getConfiguration(), id, resultType, resultMappings).build());
        return result;
    }

    private ResultMapping selectToResult(Class<?> entity, Select select, Class<?> type, ResultMapping.Builder builder) {
        if (select.hasTypeHandle()) {
            if (select.getPropertyType().isAssignableFrom(type)) {
                builder.typeHandler(select.getTypeHandle());
            } else {
                throw new ClassCastException(String.format("%s not cast to %s [%s]",
                        select.getPropertyType().getSimpleName(), type.getSimpleName(), entity.getName() + "." + select.getColumProperty()));
            }
        }
        if (select.isPk() && entity == select.getClazz()) {
            builder.flags(Collections.singletonList(ResultFlag.ID));
        }
        return builder.build();
    }


    //fix 重上往下会有resultMap覆盖问题,应该从根节点开始,id向上传递

    /**
     * @return 返回节点id
     */
    private ResultMapping buildResult(MappedStatement ms, Label<?> mybatisLabel, Set<String> columnSet,
                                      List<Select> columnList) {
        List<IResult> resultList = mybatisLabel.getResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return null;
        }
        StringBuilder childId = new StringBuilder("MPJ_")
                .append(mybatisLabel.getOfType().getName())
                .append(StringPool.UNDERSCORE)
                .append(mybatisLabel.getProperty())
                .append(StringPool.UNDERSCORE);
        List<ResultMapping> childMapping = new ArrayList<>(resultList.size());
        for (IResult r : resultList) {
            childId.append("(");
            Map<String, FieldCache> ofTypeField = MPJReflectionKit.getFieldMap(mybatisLabel.getOfType());
            //列名去重
            String columnName = r.getSelectNormal().getTagColumn();
            SelectLabel label;
            FieldCache field = ofTypeField.get(r.getProperty());
            String index = r.getIndex();
            if (columnSet.contains(columnName)) {
                columnName = getColumn(columnSet, columnName, 0);
                label = new SelectLabel(r.getSelectNormal(), null, mybatisLabel.getOfType(), columnName, StringUtils.isNotBlank(index), index, r.getBaseColumn());
            } else {
                columnSet.add(columnName);
                label = new SelectLabel(r.getSelectNormal(), null, mybatisLabel.getOfType(), StringUtils.isNotBlank(index), index, r.getBaseColumn());
            }
            columnList.add(label);
            ResultMapping.Builder builder = new ResultMapping.Builder(ms.getConfiguration(), r.getProperty(), columnName, r.getJavaType());
            if (r.isId()) {//主键标记为id标签
                builder.flags(Collections.singletonList(ResultFlag.ID));
                childId.append("i");
            } else {
                childId.append("c");
            }
            //TypeHandle
            if (label.hasTypeHandle() && label.getColumnType().isAssignableFrom(field.getType())) {
                builder.typeHandler(label.getTypeHandle());
            }
            if (Objects.nonNull(r.getJdbcType())) {
                builder.jdbcType(r.getJdbcType());
            }
            childMapping.add(builder.build());
            childId.append(StringPool.DASH)
                    .append(columnName)
                    .append(StringPool.DASH)
                    .append(r.getProperty())
                    .append(")");
        }

        if (CollectionUtils.isNotEmpty(mybatisLabel.getMybatisLabels())) {
            //递归调用
            childId.append("[");
            for (Object it : mybatisLabel.getMybatisLabels()) {
                Label<?> o = (Label<?>) it;
                if (Objects.isNull(o)) {
                    continue;
                }
                ResultMapping result = buildResult(ms, o, columnSet, columnList);
                if (Objects.isNull(result)) {
                    continue;
                }
                childMapping.add(result);
                childId.append(result.getNestedResultMapId());
                childId.append("@");
            }
            childId.append("]");
        }
        //双检
        String id = v ? childId.toString() : childId.toString().replaceAll("\\.", "~");
        if (!ms.getConfiguration().hasResultMap(id)) {
            ResultMap build = new ResultMap.Builder(ms.getConfiguration(), id, mybatisLabel.getOfType(), childMapping).build();
            MPJInterceptor.addResultMap(ms, id, build);
        }
        return new ResultMapping.Builder(ms.getConfiguration(), mybatisLabel.getProperty())
                .javaType(mybatisLabel.getJavaType())
                .nestedResultMapId(id)
                .build();
    }

    /**
     * 列名去重 重复的添加 mpj 前缀 再重复走递归
     *
     * @param pool       查询列 集合
     * @param columnName 列明
     * @return 唯一列名
     */
    private String getColumn(Set<String> pool, String columnName, int num) {
        String newName = ConfigProperties.joinPrefix + getPrefix(num) + StringPool.UNDERSCORE + columnName;
        if (!pool.contains(newName)) {
            pool.add(newName);
            return newName;
        }
        return getColumn(pool, columnName, ++num);
    }

    private String getPrefix(int num) {
        String s = Integer.toString(num, 25);
        char[] array = s.toCharArray();
        char[] chars = new char[s.length()];
        for (int i = 0; i < array.length; i++) {
            chars[i] = (char) (array[i] < 58 ? (array[i] + 49) : (array[i] + 10));
        }
        return new String(chars);
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

    private Class<?> getMapper(String id, String resource) {
        Class<?> clazz = MS_MAPPER_CACHE.computeIfAbsent(id, key -> {
            try {
                String className = key.substring(0, key.lastIndexOf(StringPool.DOT));
                try {
                    return new Val(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    return new Val(MPJTableMapperHelper.getMapperForName(className));
                }
            } catch (Exception ignored) {
                return new Val(null);
            }
        }).getVal();

        if (Objects.nonNull(clazz)) {
            return clazz;
        }

        clazz = RES_MAPPER_CACHE.computeIfAbsent(resource, key -> {
            try {
                String className = key.substring(0, key.lastIndexOf(StringPool.DOT)).replaceAll("/", StringPool.DOT);
                try {
                    return new Val(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    return new Val(MPJTableMapperHelper.getMapperForName(className));
                }
            } catch (Exception ignored) {
                return new Val(null);
            }
        }).getVal();

        return clazz;
    }

    @Override
    public Object plugin(Object target) {
        try {
            return Interceptor.super.plugin(target);
        } catch (Throwable e) {
            return Plugin.wrap(target, this);
        }
    }

    @Override
    public void setProperties(Properties properties) {
        try {
            Interceptor.super.setProperties(properties);
        } catch (Throwable ignored) {
        }
    }

    @Data
    @AllArgsConstructor
    public static class Val {
        private Class<?> val;
    }
}
