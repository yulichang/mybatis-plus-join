package com.github.yulichang.test.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.yulichang.injector.MPJSqlInjector;
import com.github.yulichang.test.util.ThreadLocalUtils;
import lombok.SneakyThrows;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

/**
 * mybatis-plus配置
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor page = new PaginationInnerInterceptor(DbType.H2);
        page.setOptimizeJoin(false);
        interceptor.addInnerInterceptor(page);
        interceptor.addInnerInterceptor(new SqlInterceptor());
        return interceptor;
    }

    @Bean
    @Primary
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ISqlInjector sqlInjector() {
        return new MPJSqlInjector() {
            @Override
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> list = super.getMethodList(mapperClass, tableInfo);
                //添加你的方法
                list.add(new InsertBatchSomeColumn());
                return list;
            }
        };
    }

//    @Bean
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    public SqlSessionFactory sqlSessionFactory(DataSource dataSource,
//                                               MybatisPlusInterceptor interceptor) throws Exception {
//        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
//        factory.setDataSource(dataSource);
//        GlobalConfig.DbConfig config = new GlobalConfig.DbConfig();
//        config.setLogicDeleteField("del");
//        config.setLogicDeleteValue("true");
//        config.setLogicNotDeleteValue("false");
//        factory.setGlobalConfig(new GlobalConfig().setSqlInjector(new MPJSqlInjector())
//                .setDbConfig(config));
//        factory.setPlugins(interceptor);
//        return factory.getObject();
//    }

    /**
     * 校验sql
     */
    public static class SqlInterceptor implements InnerInterceptor {

        @Override
        @SneakyThrows
        public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
            String sql = boundSql.getSql();
            List<String> sqlList = ThreadLocalUtils.get();
            if (CollectionUtils.isNotEmpty(sqlList)) {
                if (sqlList.stream().anyMatch(e -> Objects.equals(formatSql(sql), formatSql(e)))) {
                    System.out.println("===============================================");
                    System.out.println();
                    System.out.println("pass");
                    System.out.println();
                    System.out.println("===============================================");
                } else {
                    System.err.println("执行sql: " + SqlSourceBuilder.removeExtraWhitespaces(sql));
                    sqlList.forEach(i -> System.err.println("预期sql: " + SqlSourceBuilder.removeExtraWhitespaces(i)));
                    throw new RuntimeException("sql error");
                }
            }
        }

        @Override
        @SneakyThrows
        public void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {
            BoundSql boundSql = sh.getBoundSql();
            if (boundSql != null && StringUtils.isNotBlank(boundSql.getSql())) {
                String sql = boundSql.getSql();
                if (sql.toUpperCase().startsWith("SELECT")) {
                    return;
                }
                List<String> sqlList = ThreadLocalUtils.get();
                if (CollectionUtils.isNotEmpty(sqlList)) {
                    if (sqlList.stream().anyMatch(e -> Objects.equals(formatSql(sql), formatSql(e)))) {
                        System.out.println("===============================================");
                        System.out.println();
                        System.out.println("pass");
                        System.out.println();
                        System.out.println("===============================================");
                    } else {
                        System.err.println("执行sql: " + SqlSourceBuilder.removeExtraWhitespaces(sql));
                        sqlList.forEach(i -> System.err.println("预期sql: " + SqlSourceBuilder.removeExtraWhitespaces(i)));
                        throw new RuntimeException("sql error");
                    }
                }
            }
        }

        private String formatSql(String sql) {
            if (StringUtils.isBlank(sql)) {
                return sql;
            }
            sql = sql.replaceAll("\n", "");
            sql = sql.replaceAll("\r", "");
            sql = sql.replaceAll("\t", "");
            return dg(sql);
        }

        private String dg(String str) {
            if (str.contains(" ")) {
                str = str.replaceAll(" ", "");
                return dg(str);
            }
            return str;
        }
    }
}
