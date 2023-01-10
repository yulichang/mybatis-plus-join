package com.github.yulichang.test.join.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.yulichang.test.join.util.ThreadLocalUtils;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
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
        public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
            String sql = boundSql.getSql();
            String s = ThreadLocalUtils.get();
            if (StringUtils.isNotBlank(s) && !Objects.equals(formatSql(sql), formatSql(s))) {
                System.err.println("执行sql: " + SqlSourceBuilder.removeExtraWhitespaces(sql));
                System.err.println("预期sql: " + SqlSourceBuilder.removeExtraWhitespaces(s));
                throw new RuntimeException("sql error");
            }
            InnerInterceptor.super.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
        }

        private String formatSql(String sql) {
            if (StringUtils.isBlank(sql)) {
                return sql;
            }
            sql = sql.replaceAll("\n", "");
            sql = sql.replaceAll("\r", "");
            sql = sql.replaceAll("\t", "");
            sql = sql.replaceAll("\s", "");
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
