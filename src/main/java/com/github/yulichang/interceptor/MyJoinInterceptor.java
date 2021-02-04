package com.github.yulichang.interceptor;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.github.yulichang.toolkit.Constant;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 结果封装拦截器
 *
 * @author yulichang
 */
public class MyJoinInterceptor implements InnerInterceptor {

    private static Field type = null;

    static {
        try {
            type = ResultMap.class.getDeclaredField("type");
            type.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        if (parameter instanceof Map) {
            Map<String, ?> map = (Map<String, ?>) parameter;
            if (CollectionUtils.isNotEmpty(map)) {
                try {
                    Class<?> clazz = (Class<?>) map.get(Constant.CLAZZ);
                    List<ResultMap> list = ms.getResultMaps();
                    if (CollectionUtils.isNotEmpty(list)) {
                        ResultMap resultMap = list.get(0);
                        type.set(resultMap, clazz);
                    }
                } catch (Exception ignored) {
                    //通常是MapperMethod内部类HashMap的子类ParamMap重写了了get方法抛出的BindingException
                }
            }
        }
    }
}
