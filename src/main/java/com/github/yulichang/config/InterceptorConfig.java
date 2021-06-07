package com.github.yulichang.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.yulichang.exception.MPJException;
import com.github.yulichang.interceptor.MPJInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.InterceptorChain;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 拦截器配置类 如果配置了分页插件,可能会使拦截器失效
 * 此类的作用就是校验拦截器顺序,保证连表插件在其他拦截器之前执行
 *
 * @author yulichang
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class InterceptorConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;
    @Autowired
    private MPJInterceptor mpjInterceptor;

    @Override
    @SuppressWarnings("unchecked")
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (CollectionUtils.isNotEmpty(sqlSessionFactoryList)) {
            try {
                for (SqlSessionFactory factory : sqlSessionFactoryList) {
                    Field interceptorChain = Configuration.class.getDeclaredField("interceptorChain");
                    interceptorChain.setAccessible(true);
                    InterceptorChain chain = (InterceptorChain) interceptorChain.get(factory.getConfiguration());
                    Field interceptors = InterceptorChain.class.getDeclaredField("interceptors");
                    interceptors.setAccessible(true);
                    List<Interceptor> list = (List<Interceptor>) interceptors.get(chain);
                    if (CollectionUtils.isNotEmpty(list) && list.get(list.size() - 1) != mpjInterceptor) {
                        list.removeIf(i -> i == mpjInterceptor);
                        list.add(mpjInterceptor);
                    }
                }
            } catch (Exception ignored) {
                throw new MPJException("mpjInterceptor exception");
            }
        }
    }
}
