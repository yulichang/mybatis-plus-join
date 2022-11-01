package com.github.yulichang.interceptor;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class MPJInterceptor implements Interceptor {
    private static final Log logger = LogFactory.getLog(MPJInterceptor.class);

    private static final List<ResultMapping> EMPTY_RESULT_MAPPING = new ArrayList<>(0);

    /**
     * 缓存MappedStatement,不需要每次都去重新构建MappedStatement
     */
    private static final Map<String, Map<Configuration, MappedStatement>> MS_CACHE = new ConcurrentHashMap<>();

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
        resultMaps.add(buildResultMap(ms, resultType, ew));
        builder.resultMaps(resultMaps);
        return builder.build();
    }

    /**
     * 构建resultMap
     */
    @SuppressWarnings({"rawtypes", "unchecked", "ConstantConditions"})
    private ResultMap buildResultMap(MappedStatement ms, Class<?> resultType, Object obj) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(resultType);
        if (tableInfo == null || !(obj instanceof MPJLambdaWrapper)) {
            return getDefaultResultMap(tableInfo, ms, resultType);
        }
        MPJLambdaWrapper wrapper = (MPJLambdaWrapper) obj;
        String currentNamespace = ms.getResource().split(StringPool.SPACE)[0];
        String id = currentNamespace + StringPool.DOT + Constants.MYBATIS_PLUS + StringPool.UNDERSCORE + resultType.getSimpleName();
        if (wrapper.isResultMap()) {
            //TODO
            //添加 collection 标签
            return new ResultMap.Builder(ms.getConfiguration(), ms.getId(), resultType, EMPTY_RESULT_MAPPING).build();
        } else {
            List<SelectColumn> columnList = wrapper.getSelectColumns();
            List<ResultMapping> resultMappings = new ArrayList<>();
            columnList.forEach(i -> {
                //别名优先
                if (StringUtils.isNotBlank(i.getAlias())) {
                    resultMappings.add(new ResultMapping.Builder(ms.getConfiguration(), i.getAlias())
                            .column(i.getColumnName()).build());
                } else if (i.getTableFieldInfo() != null) {
                    //其次field info
                    TableFieldInfo info = i.getTableFieldInfo();
                    if (info.getTypeHandler() != null && info.getTypeHandler() != UnknownTypeHandler.class) {
                        TypeHandlerRegistry registry = ms.getConfiguration().getTypeHandlerRegistry();
                        TypeHandler<?> typeHandler = registry.getMappingTypeHandler(info.getTypeHandler());
                        if (typeHandler == null) {
                            typeHandler = registry.getInstance(info.getPropertyType(), info.getTypeHandler());
                        }
                        resultMappings.add(new ResultMapping.Builder(ms.getConfiguration(), info.getProperty(),
                                info.getColumn(), info.getPropertyType())
                                .typeHandler(typeHandler).build());
                    } else {
                        resultMappings.add(new ResultMapping.Builder(ms.getConfiguration(), info.getProperty(),
                                info.getColumn(), info.getPropertyType()).build());
                    }
                } else {
                    //最后取值
                    TableFieldInfo info = tableInfo.getFieldList().stream().filter(t -> t.getColumn().equals(i.getColumnName()))
                            .findFirst().orElseGet(null);
                    if (info != null && Objects.equals(tableInfo.getKeyColumn(), i.getColumnName())) {
                        resultMappings.add(new ResultMapping.Builder(ms.getConfiguration(), tableInfo.getKeyProperty(),
                                tableInfo.getKeyColumn(), tableInfo.getKeyType()).build());
                    } else if (info != null) {
                        resultMappings.add(new ResultMapping.Builder(ms.getConfiguration(), info.getProperty(),
                                info.getColumn(), info.getPropertyType()).build());
                    } else {
                        resultMappings.add(new ResultMapping.Builder(ms.getConfiguration(), i.getColumnName())
                                .column(i.getColumnName()).build());
                    }
                }
            });
            return new ResultMap.Builder(ms.getConfiguration(), id, resultType, resultMappings).build();
        }
    }


    //TODO 可以加缓存
    private ResultMap getDefaultResultMap(TableInfo tableInfo, MappedStatement ms, Class<?> resultType) {
        if (tableInfo != null && tableInfo.isAutoInitResultMap()) {
            //补充不全的属性
            ResultMap resultMap = ms.getConfiguration().getResultMap(tableInfo.getResultMap());
            List<ResultMapping> resultMappings = resultMap.getResultMappings();
            List<Field> notExistField = ReflectionKit.getFieldList(resultType).stream().filter(i ->
                    !i.getAnnotation(TableField.class).exist()).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(notExistField)) {
                //复制已有的resultMapping
                List<ResultMapping> resultMappingList = new ArrayList<>(resultMappings);
                //复制不存在的resultMapping
                for (Field i : notExistField) {
                    resultMappingList.add(new ResultMapping.Builder(ms.getConfiguration(),
                            i.getName(), i.getName(), i.getType()).build());
                }
                return new ResultMap.Builder(ms.getConfiguration(), ms.getId(), resultType, resultMappingList).build();
            }
        }
        return new ResultMap.Builder(ms.getConfiguration(), ms.getId(), resultType, EMPTY_RESULT_MAPPING).build();
    }
}
