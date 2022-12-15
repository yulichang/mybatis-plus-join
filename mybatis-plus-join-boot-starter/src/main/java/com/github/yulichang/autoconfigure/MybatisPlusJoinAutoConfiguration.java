package com.github.yulichang.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.config.InterceptorConfig;
import com.github.yulichang.injector.MPJSqlInjector;
import com.github.yulichang.interceptor.MPJInterceptor;
import com.github.yulichang.toolkit.SpringContentUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.util.List;

/**
 * springboot 自动配置类
 *
 * @author yulichang
 * @since 1.3.7
 */
@SuppressWarnings("ALL")
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(MybatisPlusJoinProperties.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class, MybatisPlusLanguageDriverAutoConfiguration.class})
public class MybatisPlusJoinAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MybatisPlusJoinAutoConfiguration.class);


    @SuppressWarnings("FieldCanBeLocal")
    private final MybatisPlusJoinProperties properties;


    public MybatisPlusJoinAutoConfiguration(MybatisPlusJoinProperties properties) {
        this.properties = properties;
        ConfigProperties.subTableLogic = properties.getSubTableLogic();
        ConfigProperties.msCache = properties.isMsCache();
    }

    /**
     * mybatis plus join 拦截器
     */
    @Bean
    public MPJInterceptor mpjInterceptor() {
        return new MPJInterceptor();
    }

    /**
     * mybatis plus 拦截器配置
     */
    @Bean
    @ConditionalOnBean(SqlSessionFactory.class)
    public InterceptorConfig interceptorConfig(List<SqlSessionFactory> sqlSessionFactoryList) {
        return new InterceptorConfig(sqlSessionFactoryList, properties.getBanner());
    }

    /**
     * mybatis plus join 自定义方法
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnMissingBean({DefaultSqlInjector.class, AbstractSqlInjector.class, ISqlInjector.class})
    public MPJSqlInjector mpjSqlInjector() {
        logger.info("MPJSqlInjector init");
        return new MPJSqlInjector();
    }

    /**
     * springboot content 工具类
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SpringContentUtils springContentUtils(SpringContext springContext) {
        return new SpringContentUtils(springContext);
    }

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnBean(SqlSessionFactory.class)
    public static class MappingConfig implements ApplicationListener<ApplicationReadyEvent> {
        @Override
        @SuppressWarnings("NullableProblems")
        public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
            new com.github.yulichang.config.MappingConfig();
        }
    }

    @Configuration
    @ConditionalOnBean(SqlSessionFactory.class)
    public static class SpringContext implements SpringContentUtils.SpringContext, ApplicationContextAware {

        private ApplicationContext applicationContext;

        @Override
        public <T> T getBean(Class<T> clazz) {
            return this.applicationContext.getBean(clazz);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }
    }

}
