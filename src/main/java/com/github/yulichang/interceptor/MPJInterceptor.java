package com.github.yulichang.interceptor;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.method.MPJResultType;
import com.github.yulichang.toolkit.Constant;
import org.apache.ibatis.executor.Executor;
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
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.UnknownTypeHandler;

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
@SuppressWarnings("unchecked")
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class MPJInterceptor implements Interceptor {
    private static final List<ResultMapping> EMPTY_RESULT_MAPPING = new ArrayList<>(0);

    /**
     * 缓存MappedStatement,不需要每次都去重写构建MappedStatement
     */
    private static final Map<String, MappedStatement> MS_CACHE = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (args[0] instanceof MappedStatement) {
            MappedStatement ms = (MappedStatement) args[0];
            if (args[1] instanceof Map) {
                Map<String, ?> map = (Map<String, ?>) args[1];
                if (CollectionUtils.isNotEmpty(map)) {
                    if (map.containsKey(Constant.CLAZZ)) {
                        Class<?> clazz = (Class<?>) map.get(Constant.CLAZZ);
                        if (Objects.nonNull(clazz)) {
                            List<ResultMap> list = ms.getResultMaps();
                            if (CollectionUtils.isNotEmpty(list)) {
                                ResultMap resultMap = list.get(0);
                                if (resultMap.getType() == MPJResultType.class) {
                                    args[0] = newMappedStatement(ms, clazz);
                                }
                            }
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }

    /**
     * 构建新的MappedStatement
     */
    public MappedStatement newMappedStatement(MappedStatement ms, Class<?> resultType) {
        String id = ms.getId() + StringPool.UNDERSCORE + resultType.getName();
        MappedStatement statement = MS_CACHE.get(id);
        if (Objects.nonNull(statement)) {
            return statement;
        }
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
        resultMaps.add(newResultMap(ms, resultType));
        builder.resultMaps(resultMaps);
        MappedStatement mappedStatement = builder.build();
        MS_CACHE.put(id, mappedStatement);
        return mappedStatement;
    }

    /**
     * 构建resultMap
     */
    private ResultMap newResultMap(MappedStatement ms, Class<?> resultType) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(resultType);
        if (tableInfo != null && tableInfo.isAutoInitResultMap() && tableInfo.getEntityType() == resultType) {
            return initResultMapIfNeed(tableInfo, resultType);
        }
        return new ResultMap.Builder(ms.getConfiguration(), ms.getId(), resultType, EMPTY_RESULT_MAPPING).build();
    }

    /**
     * 构建resultMap
     */
    private ResultMap initResultMapIfNeed(TableInfo info, Class<?> resultType) {
        String id = info.getCurrentNamespace() + ".mybatis-plus-join_" + resultType.getSimpleName();
        List<ResultMapping> resultMappings = new ArrayList<>();
        if (info.havePK()) {
            ResultMapping idMapping = new ResultMapping.Builder(info.getConfiguration(), info.getKeyProperty(), info.getKeyColumn(), info.getKeyType())
                    .flags(Collections.singletonList(ResultFlag.ID)).build();
            resultMappings.add(idMapping);
        }
        if (CollectionUtils.isNotEmpty(info.getFieldList())) {
            info.getFieldList().forEach(i -> resultMappings.add(getResultMapping(i, info.getConfiguration())));
        }
        return new ResultMap.Builder(info.getConfiguration(), id, resultType, resultMappings).build();
    }


    /**
     * 获取 ResultMapping
     *
     * @param configuration MybatisConfiguration
     * @return ResultMapping
     */
    private ResultMapping getResultMapping(TableFieldInfo info, final Configuration configuration) {
        ResultMapping.Builder builder = new ResultMapping.Builder(configuration, info.getProperty(),
                StringUtils.getTargetColumn(info.getColumn()), info.getPropertyType());
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        if (info.getJdbcType() != null && info.getJdbcType() != JdbcType.UNDEFINED) {
            builder.jdbcType(info.getJdbcType());
        }
        if (info.getTypeHandler() != null && info.getTypeHandler() != UnknownTypeHandler.class) {
            TypeHandler<?> typeHandler = registry.getMappingTypeHandler(info.getTypeHandler());
            if (typeHandler == null) {
                typeHandler = registry.getInstance(info.getPropertyType(), info.getTypeHandler());
                // todo 这会有影响 registry.register(typeHandler);
            }
            builder.typeHandler(typeHandler);
        }
        return builder.build();
    }
}
