package com.github.yulichang.test.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    /**
     * 校验sql
     */
    public static class SqlInterceptor implements InnerInterceptor {

        @Override
        @SneakyThrows
        public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
            String sql = boundSql.getSql();
            String s = ThreadLocalUtils.get();
            if (StringUtils.isNotBlank(s)) {
                ObjectMapper mapper = new ObjectMapper();
                List<String> sqlList = mapper.readValue(s, new TypeReference<List<String>>() {
                });
                if (sqlList.stream().anyMatch(e -> Objects.equals(formatSql(sql), formatSql(e)))) {
                    System.out.println("===============================================");
                    System.out.println();
                    System.out.println("pass");
                    System.out.println();
                    System.out.println("===============================================");
                } else {
                    System.err.println("执行sql: " + SqlSourceBuilder.removeExtraWhitespaces(sql));
                    sqlList.forEach(i -> System.err.println("预期sql: " + i));
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
                String s = ThreadLocalUtils.get();
                if (StringUtils.isNotBlank(s)) {
                    ObjectMapper mapper = new ObjectMapper();
                    List<String> sqlList = mapper.readValue(s, new TypeReference<List<String>>() {
                    });
                    if (sqlList.stream().anyMatch(e -> Objects.equals(formatSql(sql), formatSql(e)))) {
                        System.out.println("===============================================");
                        System.out.println();
                        System.out.println("pass");
                        System.out.println();
                        System.out.println("===============================================");
                    } else {
                        System.err.println("执行sql: " + SqlSourceBuilder.removeExtraWhitespaces(sql));
                        sqlList.forEach(i -> System.err.println("预期sql: " + i));
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
