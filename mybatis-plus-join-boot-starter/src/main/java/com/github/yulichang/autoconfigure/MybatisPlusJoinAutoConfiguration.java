package com.github.yulichang.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.github.yulichang.autoconfigure.conditional.MPJSqlInjectorCondition;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.config.MPJInterceptorConfig;
import com.github.yulichang.config.enums.LogicDelTypeEnum;
import com.github.yulichang.injector.MPJSqlInjector;
import com.github.yulichang.interceptor.MPJInterceptor;
import com.github.yulichang.toolkit.SpringContentUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.context.annotation.Primary;
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
        ConfigProperties.tableAlias = properties.getTableAlias();
        ConfigProperties.joinPrefix = properties.getJoinPrefix();
        ConfigProperties.logicDelType = "where".equalsIgnoreCase(properties.getLogicDelType()) ?
                LogicDelTypeEnum.WHERE : LogicDelTypeEnum.ON;
    }

    /**
     * mybatis plus join 拦截器
     */
    @Bean
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public MPJInterceptor mpjInterceptor() {
        return new MPJInterceptor();
    }

    /**
     * mybatis plus 拦截器配置
     */
    @Bean
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public MPJInterceptorConfig mpjInterceptorConfig(@Autowired(required = false) List<SqlSessionFactory> sqlSessionFactoryList) {
        return new MPJInterceptorConfig(sqlSessionFactoryList, properties.getBanner());
    }

    /**
     * mybatis plus join 自定义方法
     */
    @Bean
    @Primary
    @MPJSqlInjectorCondition
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnBean(ISqlInjector.class)
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public MPJSqlInjector mpjSqlInjector(ISqlInjector sqlInjector) {
        logger.info("MPJSqlInjector init");
        return new MPJSqlInjector(sqlInjector);
    }

    /**
     * mybatis plus join 自定义方法
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnMissingBean(ISqlInjector.class)
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public MPJSqlInjector mpjSqlInjectorOnMiss() {
        logger.info("MPJSqlInjector init");
        return new MPJSqlInjector();
    }

    /**
     * springboot context 工具类
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public SpringContentUtils mpjSpringContent(@Autowired(required = false) MPJSpringContext springContext) {
        return new SpringContentUtils(springContext);
    }

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public static class MPJMappingConfig implements ApplicationListener<ApplicationReadyEvent> {
        @Override
        @SuppressWarnings("NullableProblems")
        public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
            new com.github.yulichang.config.MappingConfig();
        }
    }

    @Configuration
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public static class MPJSpringContext implements SpringContentUtils.SpringContext, ApplicationContextAware {

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
