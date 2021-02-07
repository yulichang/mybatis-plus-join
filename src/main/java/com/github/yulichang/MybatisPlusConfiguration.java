package com.github.yulichang;

import com.github.yulichang.injector.MPJSqlInjector;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yulichang
 */
@Configuration
public class MybatisPlusConfiguration {

    @Bean
    @ConditionalOnMissingBean(MPJSqlInjector.class)
    public MPJSqlInjector mySqlInjector() {
        return new MPJSqlInjector();
    }

}
