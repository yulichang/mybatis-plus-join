package com.github.yulichang.autoconfigure;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.github.yulichang.config.InterceptorConfig;
import com.github.yulichang.config.MappingConfig;
import com.github.yulichang.injector.MPJSqlInjector;
import com.github.yulichang.interceptor.MPJInterceptor;
import com.github.yulichang.toolkit.SpringContentUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author yulichang
 * @since 1.3.2
 */
@SuppressWarnings("unused")
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
        return new MPJSqlInjector();
    }

    /**
     * 多表查询注解
     *
     * @deprecated
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public MappingConfig mappingConfig() {
        return new MappingConfig();
    }

    /**
     * springboot content 工具类
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SpringContentUtils springContentUtils() {
        return new SpringContentUtils();
    }

}
