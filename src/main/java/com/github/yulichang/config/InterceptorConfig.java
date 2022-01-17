package com.github.yulichang.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.yulichang.exception.MPJException;
import com.github.yulichang.injector.MPJSqlInjector;
import com.github.yulichang.interceptor.MPJInterceptor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.InterceptorChain;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

/**
 * 拦截器配置类 如果配置了分页插件,可能会使拦截器失效
 * 此类的作用就是校验拦截器顺序,保证连表插件在其他拦截器之前执行
 *
 * @author yulichang
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class InterceptorConfig implements ApplicationListener<ApplicationReadyEvent> {

    private static final Log logger = LogFactory.getLog(InterceptorConfig.class);

    @Autowired(required = false)
    private List<SqlSessionFactory> sqlSessionFactoryList;
    @Autowired(required = false)
    private MPJInterceptor mpjInterceptor;
    @Autowired(required = false)
    private ISqlInjector iSqlInjector;

    @Override
    @SuppressWarnings("unchecked")
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (CollectionUtils.isNotEmpty(sqlSessionFactoryList) && Objects.nonNull(mpjInterceptor)) {
            try {
                for (SqlSessionFactory factory : sqlSessionFactoryList) {
                    Field interceptorChain = Configuration.class.getDeclaredField("interceptorChain");
                    interceptorChain.setAccessible(true);
                    InterceptorChain chain = (InterceptorChain) interceptorChain.get(factory.getConfiguration());
                    Field interceptors = InterceptorChain.class.getDeclaredField("interceptors");
                    interceptors.setAccessible(true);
                    List<Interceptor> list = (List<Interceptor>) interceptors.get(chain);
                    if (CollectionUtils.isNotEmpty(list)) {
                        if (list.get(list.size() - 1) != mpjInterceptor) {
                            list.removeIf(i -> i == mpjInterceptor);
                            list.add(mpjInterceptor);
                        }
                    } else {
                        list.add(mpjInterceptor);
                    }
                }
            } catch (Exception ignored) {
                throw new MPJException("mpjInterceptor exception");
            }
        }
        if (iSqlInjector != null && !(iSqlInjector instanceof MPJSqlInjector)) {
            logger.error("sql注入器未继承 MPJSqlInjector -> " + iSqlInjector.getClass());
        }
    }
}
