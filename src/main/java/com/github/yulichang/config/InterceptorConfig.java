package com.github.yulichang.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.yulichang.toolkit.InterceptorList;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.InterceptorChain;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 兼容 page helper 插件类
 * <p>
 * 可以自定义Conditional注解简化代码 todo
 *
 * @author yulichang
 */
@Configuration
@SuppressWarnings("unused")
public class InterceptorConfig {

    private static final Log logger = LogFactory.getLog(InterceptorConfig.class);

    @Configuration
    @ConditionalOnBean(type = "com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration")
    @AutoConfigureBefore(name = {"com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration"})
    public static class PhSpringBoot {
        public PhSpringBoot(List<SqlSessionFactory> sqlSessionFactoryList) {
            replaceInterceptorChain(sqlSessionFactoryList);
        }
    }

    @Configuration
    @ConditionalOnBean(type = "com.github.pagehelper.PageInterceptor")
    @AutoConfigureBefore(name = {"com.github.pagehelper.PageInterceptor"})
    public static class PhSpring {
        public PhSpring(List<SqlSessionFactory> sqlSessionFactoryList) {
            replaceInterceptorChain(sqlSessionFactoryList);
        }
    }

    @SuppressWarnings("unchecked")
    public static void replaceInterceptorChain(List<SqlSessionFactory> sqlSessionFactoryList) {
        if (CollectionUtils.isEmpty(sqlSessionFactoryList)) {
            return;
        }
        for (SqlSessionFactory factory : sqlSessionFactoryList) {
            try {
                Field interceptorChain = org.apache.ibatis.session.Configuration.class.getDeclaredField("interceptorChain");
                interceptorChain.setAccessible(true);
                InterceptorChain chain = (InterceptorChain) interceptorChain.get(factory.getConfiguration());
                Field interceptors = InterceptorChain.class.getDeclaredField("interceptors");
                interceptors.setAccessible(true);
                List<Interceptor> list = (List<Interceptor>) interceptors.get(chain);
                if (CollectionUtils.isEmpty(list)) {
                    interceptors.set(chain, new InterceptorList<>());
                } else {
                    interceptors.set(chain, new InterceptorList<>(list));
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                logger.error("初始化 MPJ 拦截器失败");
                e.printStackTrace();
            }
        }
    }
}
