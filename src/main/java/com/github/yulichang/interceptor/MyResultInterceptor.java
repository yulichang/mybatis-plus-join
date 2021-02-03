package com.github.yulichang.interceptor;

import com.baomidou.mybatisplus.core.MybatisParameterHandler;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.yulichang.toolkit.Constant;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 结果封装
 *
 * @author yulichang
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
})
//@SuppressWarnings("all")
public class MyResultInterceptor implements Interceptor {

    /**
     * 缓存初始化反射字段
     * todo
     * 应该可以想mybatis-plus那样用自定义的MybatisParameterHandler直接获取mappedStatement和resultMap,而不是反射获取
     */
    private static Field mappedStatement = null;
    private static Field type = null;

    static {
        try {
            mappedStatement = MybatisParameterHandler.class.getDeclaredField("mappedStatement");
            mappedStatement.setAccessible(true);
            type = ResultMap.class.getDeclaredField("type");
            type.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (Objects.nonNull(invocation.getTarget()) && invocation.getTarget() instanceof MybatisParameterHandler) {
            MybatisParameterHandler mybatisParameterHandler = (MybatisParameterHandler) invocation.getTarget();
            Map<String, ?> map = (Map<String, ?>) mybatisParameterHandler.getParameterObject();
            if (CollectionUtils.isNotEmpty(map)) {
                try {
                    Class<?> resClazz = (Class<?>) map.get(Constant.CLAZZ);
                    MappedStatement statement = (MappedStatement) mappedStatement.get(mybatisParameterHandler);
                    List<ResultMap> list = statement.getResultMaps();
                    if (CollectionUtils.isNotEmpty(list)) {
                        ResultMap resultMap = list.get(0);
                        type.set(resultMap, resClazz);
                    }
                } catch (Exception e) {
                    return invocation.proceed();
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
