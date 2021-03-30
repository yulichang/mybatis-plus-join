package com.github.yulichang.interceptor;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
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
    private static final Map<String, MappedStatement> MS_CACHE = new ConcurrentHashMap<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (args[0] instanceof MappedStatement) {
            MappedStatement ms = (MappedStatement) args[0];
            Object parameter = args[1];
            if (parameter instanceof Map) {
                Map<String, ?> map = (Map<String, ?>) parameter;
                if (CollectionUtils.isNotEmpty(map)) {
                    try {
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
                    } catch (Exception e) {
                        //通常是MapperMethod内部类HashMap的子类ParamMap重写了了get方法抛出的BindingException
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
        String id = ms.getId() + "_" + resultType.getName();
        MappedStatement statement = MS_CACHE.get(id);
        if (Objects.nonNull(statement)) {
            return statement;
        }
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), id, ms.getSqlSource(), ms.getSqlCommandType());
        builder.resource(ms.getResource())
                .fetchSize(ms.getFetchSize())
                .statementType(ms.getStatementType())
                .keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout())
                .parameterMap(ms.getParameterMap());
        //count查询返回值int
        List<ResultMap> resultMaps = new ArrayList<>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), resultType, EMPTY_RESULT_MAPPING).build();
        resultMaps.add(resultMap);
        builder.resultMaps(resultMaps)
                .resultSetType(ms.getResultSetType())
                .cache(ms.getCache())
                .flushCacheRequired(ms.isFlushCacheRequired())
                .useCache(ms.isUseCache());
        MappedStatement mappedStatement = builder.build();
        MS_CACHE.put(id, mappedStatement);
        return mappedStatement;
    }
}
