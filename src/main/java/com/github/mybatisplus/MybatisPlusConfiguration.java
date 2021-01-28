package com.github.mybatisplus;

import com.github.mybatisplus.injector.MySqlInjector;
import com.github.mybatisplus.interceptor.MyResultInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yulichang
 */
@Configuration
public class MybatisPlusConfiguration {

    @Bean
    @ConditionalOnMissingBean(MySqlInjector.class)
    public MySqlInjector mySqlInjector() {
        return new MySqlInjector();
    }

    @Bean
    @ConditionalOnMissingBean(MyResultInterceptor.class)
    public MyResultInterceptor myResultInterceptor() {
        return new MyResultInterceptor();
    }
}
