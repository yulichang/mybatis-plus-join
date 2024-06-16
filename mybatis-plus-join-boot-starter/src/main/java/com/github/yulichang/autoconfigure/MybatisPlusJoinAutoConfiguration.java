package com.github.yulichang.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.github.yulichang.autoconfigure.conditional.JoinSqlInjectorCondition;
import com.github.yulichang.autoconfigure.consumer.MybatisPlusJoinIfExistsConsumer;
import com.github.yulichang.autoconfigure.consumer.MybatisPlusJoinPropertiesConsumer;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.config.MPJInterceptorConfig;
import com.github.yulichang.extension.mapping.config.MappingConfig;
import com.github.yulichang.injector.MPJSqlInjector;
import com.github.yulichang.interceptor.MPJInterceptor;
import com.github.yulichang.toolkit.SpringContentUtils;
import com.github.yulichang.wrapper.enums.IfExistsSqlKeyWordEnum;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
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
import java.util.Optional;
import java.util.function.BiPredicate;

/**
 * springboot 自动配置类
 *
 * @author yulichang
 * @since 1.3.7
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@ConditionalOnSingleCandidate(DataSource.class)
@EnableConfigurationProperties(MybatisPlusJoinProperties.class)
@AutoConfigureAfter({DataSourceAutoConfiguration.class, MybatisPlusLanguageDriverAutoConfiguration.class})
public class MybatisPlusJoinAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(MybatisPlusJoinAutoConfiguration.class);

    private final MybatisPlusJoinProperties properties;

    public MybatisPlusJoinAutoConfiguration(MybatisPlusJoinProperties properties,
                                            ObjectProvider<MybatisPlusJoinPropertiesConsumer> propertiesConsumers,
                                            ObjectProvider<MybatisPlusJoinIfExistsConsumer> IfExistsConsumers) {
        this.properties = Optional.ofNullable(propertiesConsumers.getIfAvailable()).map(c -> c.config(properties)).orElse(properties);
        ConfigProperties.banner = this.properties.getBanner();
        ConfigProperties.msCache = this.properties.isMsCache();
        ConfigProperties.tableAlias = this.properties.getTableAlias();
        ConfigProperties.joinPrefix = this.properties.getJoinPrefix();
        ConfigProperties.logicDelType = this.properties.getLogicDelType();
        ConfigProperties.subQueryAlias = this.properties.getSubQueryAlias();
        ConfigProperties.subTableLogic = this.properties.getSubTableLogic();
        ConfigProperties.mappingMaxCount = this.properties.getMappingMaxCount();
        ConfigProperties.ifExists = Optional.ofNullable(IfExistsConsumers.getIfAvailable())
                .map(m -> (BiPredicate<Object, IfExistsSqlKeyWordEnum>) m)
                .orElse((val, key) -> this.properties.getIfExists().test(val));
        info("mybatis plus join properties config complete");
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
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public MPJInterceptorConfig mpjInterceptorConfig(List<SqlSessionFactory> sqlSessionFactoryList) {
        return new MPJInterceptorConfig(sqlSessionFactoryList, properties.getBanner());
    }

    /**
     * mybatis plus join 自定义方法
     */
    @Bean
    @Primary
    @JoinSqlInjectorCondition
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnBean(ISqlInjector.class)
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public MPJSqlInjector mpjSqlInjector(ISqlInjector sqlInjector) {
        info("mybatis plus join SqlInjector init");
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
        info("mybatis plus join SqlInjector init");
        return new MPJSqlInjector();
    }

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public static class MPJMappingConfig implements ApplicationListener<ApplicationReadyEvent> {

        @Override
        @SuppressWarnings("NullableProblems")
        public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
            MappingConfig.init();
        }
    }

    private void info(String info) {
        if (properties.getBanner()) {
            logger.info(info);
        }
    }

    @Configuration
    @ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
    public static class MPJSpringContext implements SpringContentUtils.SpringContext, BeanFactoryPostProcessor, ApplicationContextAware {

        private ApplicationContext applicationContext;

        private ListableBeanFactory listableBeanFactory;

        @Override
        public <T> T getBean(Class<T> clazz) {
            return getBeanFactory().getBean(clazz);
        }

        @Override
        public <T> void getBeansOfType(Class<T> clazz) {
            getBeanFactory().getBeansOfType(clazz);
        }

        private ListableBeanFactory getBeanFactory() {
            return applicationContext == null ? listableBeanFactory : applicationContext;
        }

        @Override
        @SuppressWarnings("NullableProblems")
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
            SpringContentUtils.setSpringContext(this);
        }

        @Override
        @SuppressWarnings("NullableProblems")
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            this.listableBeanFactory = beanFactory;
            SpringContentUtils.setSpringContext(this);
        }
    }
}
