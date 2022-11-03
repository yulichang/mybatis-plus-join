package com.github.yulichang.interceptor;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.method.MPJResultType;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.ReflectionKit;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.SelectColumn;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.MappedStatement;
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

/**
 * 连表拦截器
 * 用于实现动态resultType
 * 之前的实现方式是mybatis-plus的Interceptor,无法修改args,存在并发问题
 * 所以将这个拦截器独立出来
 *
 * @author yulichang
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class MPJInterceptor implements Interceptor {
    private static final Log logger = LogFactory.getLog(MPJInterceptor.class);

    private static final List<ResultMapping> EMPTY_RESULT_MAPPING = new ArrayList<>(0);

    /**
     * 缓存MappedStatement,不需要每次都去重新构建MappedStatement
     */
    private static final Map<String, Map<Configuration, MappedStatement>> MS_CACHE = new ConcurrentHashMap<>();

    /**
     * 打印 MPJ resultMap
     */
    private static final boolean printResultMap = false;

    @Override
    @SuppressWarnings({"Java8MapApi", "unchecked"})
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
    public MappedStatement getMappedStatement(MappedStatement ms, Class<?> resultType, Object ew) {
        String id = ms.getId() + StringPool.UNDERSCORE + resultType.getName();

        if (ew instanceof MPJLambdaWrapper) {
            //不走缓存
            return buildMappedStatement(ms, resultType, ew, id);
        }
        //走缓存
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
        List<ResultMap> resultMaps = new ArrayList<>();
        ResultMap resultMap = buildResultMap(ms, resultType, ew);
        resultMaps.add(resultMap);
        printResultMap(resultMap);
        builder.resultMaps(resultMaps);
        return builder.build();
    }

    /**
     * 构建resultMap TODO 可以用lambda简化代码
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private ResultMap buildResultMap(MappedStatement ms, Class<?> resultType, Object obj) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(resultType);
        String currentNamespace = ms.getResource().split(StringPool.SPACE)[0];
        String id = currentNamespace + StringPool.DOT + Constants.MYBATIS_PLUS + StringPool.UNDERSCORE + resultType.getSimpleName();

        if (!(obj instanceof MPJLambdaWrapper) || Map.class.isAssignableFrom(resultType) ||
                ReflectionKit.isPrimitiveOrWrapper(resultType) ||
                Collection.class.isAssignableFrom(resultType)) {
            return getDefaultResultMap(tableInfo, ms, resultType, id);
        }
        MPJLambdaWrapper wrapper = (MPJLambdaWrapper) obj;
        Map<String, Field> fieldMap = ReflectionKit.getFieldMap(resultType);

        if (wrapper.isResultMap()) {
            //TODO
            return new ResultMap.Builder(ms.getConfiguration(), id, resultType, EMPTY_RESULT_MAPPING).build();
        } else {
            List<SelectColumn> columnList = wrapper.getSelectColumns();
            List<ResultMapping> resultMappings = new ArrayList<>(columnList.size());
            columnList.forEach(i -> {
                TableFieldInfo info = i.getTableFieldInfo();
                if (StringUtils.isNotBlank(i.getAlias())) {
                    //优先别名查询 selectFunc selectAs
                    ResultMapping.Builder builder = new ResultMapping.Builder(ms.getConfiguration(), i.getAlias(),
                            i.getAlias(), getAliasField(resultType, fieldMap, i.getAlias()));
                    if (i.getFuncEnum() == null || StringUtils.isBlank(i.getFuncEnum().getSql())) {
                        if (info != null && info.getTypeHandler() != null && info.getTypeHandler() != UnknownTypeHandler.class) {
                            Field f = fieldMap.get(i.getAlias());
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
                                info.getColumn(), info.getPropertyType());
                        if (info.getTypeHandler() != null && info.getTypeHandler() != UnknownTypeHandler.class) {
                            Field f = fieldMap.get(info.getProperty());
                            if (f != null && f.getType() == info.getField().getType()) {
                                builder.typeHandler(getTypeHandler(ms, info));
                            }
                        }
                        resultMappings.add(builder.build());
                    } else {
                        resultMappings.add(new ResultMapping.Builder(ms.getConfiguration(), info.getProperty(), info.getColumn(), info.getPropertyType()).build());
                    }
                } else {
                    // 主键列
                    resultMappings.add(new ResultMapping.Builder(ms.getConfiguration(), i.getTagProperty(), i.getColumnName(),
                            getAliasField(resultType, fieldMap, i.getTagProperty())).build());
                }
            });
            //移除result中不存在的标签
            resultMappings.removeIf(i -> !fieldMap.containsKey(i.getProperty()));
            return new ResultMap.Builder(ms.getConfiguration(), id, resultType, resultMappings).build();
        }
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
                    resultMappingList.add(new ResultMapping.Builder(ms.getConfiguration(),
                            i.getName(), i.getName(), i.getType()).build());
                }
                return new ResultMap.Builder(ms.getConfiguration(), id, resultType, resultMappingList).build();
            }
        }
        return new ResultMap.Builder(ms.getConfiguration(), id, resultType, EMPTY_RESULT_MAPPING).build();
    }

    /**
     * 获取result指定名称的字段
     */
    private Class<?> getAliasField(Class<?> resultType, Map<String, Field> fieldMap, String alias) {
        Field field = fieldMap.get(alias);
        Assert.notNull(field, "Result Class <%s> not find Field <%s>", resultType.getSimpleName(), alias);
        return field.getType();
    }

    /**
     * 打印resultMap
     * 先打开 mybatis plus 日志， 在设置 printResultMap = true 才会打印
     */
    private void printResultMap(ResultMap resultMap) {
        if (resultMap == null || !printResultMap)
            return;
        logger.debug("===================== MPJ resultMap =========================");
        List<ResultMapping> mappings = resultMap.getResultMappings();
        logger.debug(String.format("    <resultMap id=\"%s\" type=\"%s\">", resultMap.getId(), resultMap.getType().getName()));
        mappings.forEach(i -> logger.debug(String.format("        <result property=\"%s\" column=\"%s\" javaType=\"%s\" typeHandler=\"%s\"/>", i.getProperty(), i.getColumn(), i.getJavaType(), i.getTypeHandler())));
        logger.debug("    </resultMap>");
        logger.debug("=====================      end      =========================");
    }
}
