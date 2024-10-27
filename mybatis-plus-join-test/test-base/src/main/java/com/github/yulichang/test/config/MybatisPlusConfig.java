package com.github.yulichang.test.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.github.yulichang.injector.MPJSqlInjector;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.StrUtils;
import lombok.SneakyThrows;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
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
import java.util.Optional;
import java.util.function.Predicate;

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
        interceptor.addInnerInterceptor(new TenantLineInnerInterceptor(new TenantLineHandler() {
            @Override
            public Expression getTenantId() {
                return new LongValue(1);
            }

            @Override
            public boolean ignoreTable(String tableName) {
                return !tableName.startsWith("user_tenant");
            }
        }));

        PaginationInnerInterceptor page = new PaginationInnerInterceptor();
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
            @SuppressWarnings("deprecation")
            public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
                List<AbstractMethod> list = super.getMethodList(mapperClass);
                //添加你的方法
                list.add(new InsertBatchSomeColumn());
                return list;
            }

            @Override
            @SuppressWarnings("deprecation")
            public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> list = super.getMethodList(mapperClass, tableInfo);
                //添加你的方法
                list.add(new InsertBatchSomeColumn());
                return list;
            }

            @Override
            public List<AbstractMethod> getMethodList(org.apache.ibatis.session.Configuration configuration, Class<?> mapperClass, TableInfo tableInfo) {
                List<AbstractMethod> list = super.getMethodList(configuration, mapperClass, tableInfo);
                //添加你的方法
                list.add(new InsertBatchSomeColumn());
                return list;
            }
        };
    }

    /**
     * 校验sql
     */
    public static class SqlInterceptor implements InnerInterceptor {

        private DbType dbType;

        private static final Predicate<DbType> P = type -> type == DbType.POSTGRE_SQL || type == DbType.ORACLE;

        @Override
        @SneakyThrows

        public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
            String sql = boundSql.getSql();
            this.dbType = Optional.ofNullable(this.dbType).orElse(JdbcUtils.getDbType(executor));
            if (P.test(this.dbType)) {
                PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
                List<ParameterMapping> mappings = mpBoundSql.parameterMappings();
                mpBoundSql.sql(sql.replaceAll("`", "\""));
                mpBoundSql.parameterMappings(mappings);
            }
            check(sql);
        }

        private void check(String sql) {
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
            if (boundSql != null && StrUtils.isNotBlank(boundSql.getSql())) {
                String sql = boundSql.getSql();
                this.dbType = Optional.ofNullable(this.dbType).orElse(JdbcUtils.getDbType(connection.getMetaData().getURL()));
                if (P.test(this.dbType)) {
                    PluginUtils.MPBoundSql mpBoundSql = PluginUtils.mpBoundSql(boundSql);
                    List<ParameterMapping> mappings = mpBoundSql.parameterMappings();
                    mpBoundSql.sql(sql.replaceAll("`", "\""));
                    mpBoundSql.parameterMappings(mappings);
                }
                if (sql.toUpperCase().startsWith("SELECT")) {
                    return;
                }
                check(sql);
            }
        }

        private String formatSql(String sql) {
            if (StrUtils.isBlank(sql)) {
                return sql;
            }
            sql = sql.replaceAll("\n", "");
            sql = sql.replaceAll("\r", "");
            sql = sql.replaceAll("\t", "");
            if (P.test(this.dbType)) {
                sql = sql.replaceAll("\"", "`");
                sql = sql.replaceAll("`", "");
            }
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
