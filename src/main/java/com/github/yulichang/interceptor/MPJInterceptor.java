package com.github.yulichang.interceptor;

import com.baomidou.mybatisplus.core.metadata.MPJTableInfoHelper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.yulichang.method.MPJResultType;
import com.github.yulichang.toolkit.Constant;
import org.apache.ibatis.executor.Executor;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private static final Map<String, Map<Configuration, MappedStatement>> MS_CACHE = new ConcurrentHashMap<>();

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
        Map<Configuration, MappedStatement> statementMap = MS_CACHE.get(id);
        if (CollectionUtils.isNotEmpty(statementMap)) {
            MappedStatement statement = statementMap.get(ms.getConfiguration());
            if (Objects.nonNull(statement)) {
                return statement;
            }
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

        if (statementMap == null) {
            statementMap = new ConcurrentHashMap<>();
            MS_CACHE.put(id, statementMap);
        }
        statementMap.put(ms.getConfiguration(), mappedStatement);
        return mappedStatement;
    }

    /**
     * 构建resultMap
     */
    private ResultMap newResultMap(MappedStatement ms, Class<?> resultType) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(resultType);
        if (tableInfo != null && tableInfo.isAutoInitResultMap() && tableInfo.getEntityType() == resultType) {
            return ms.getConfiguration().getResultMap(tableInfo.getResultMap());
        }
        TableInfo infoDTO = MPJTableInfoHelper.getTableInfo(resultType);
        if (infoDTO == null) {
            infoDTO = MPJTableInfoHelper.initTableInfo(ms.getConfiguration(),
                    ms.getId().substring(0, ms.getId().lastIndexOf(".")), resultType);
        }
        if (infoDTO.isAutoInitResultMap()) {
            return ms.getConfiguration().getResultMap(infoDTO.getResultMap());
        }
        return new ResultMap.Builder(ms.getConfiguration(), ms.getId(), resultType, EMPTY_RESULT_MAPPING).build();
    }
}
