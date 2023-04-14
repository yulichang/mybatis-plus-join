//package com.github.yulichang.test.config;
//
//import com.baomidou.mybatisplus.core.injector.AbstractMethod;
//import com.baomidou.mybatisplus.core.injector.ISqlInjector;
//import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
//import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
//import com.github.yulichang.injector.MPJSqlInjector;
//import com.github.yulichang.test.util.ThreadLocalUtils;
//import org.apache.ibatis.cache.CacheKey;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.executor.statement.StatementHandler;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.plugin.*;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.sql.Connection;
//import java.util.List;
//import java.util.Objects;
//import java.util.StringTokenizer;
//
///**
// * mp 3.3.x
// */
//@Configuration
//public class MPConfig {
//
//    @Bean
//    public PaginationInterceptor mybatisPlusInterceptor() {
//        return new PaginationInterceptor();
//    }
//
//    @Bean
//    @Primary
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public ISqlInjector sqlInjector() {
//        return new MPJSqlInjector() {
//            @Override
//            public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
//                List<AbstractMethod> list = super.getMethodList(mapperClass);
//                //添加你的方法
//                list.add(new InsertBatchSomeColumn());
//                return list;
//            }
//        };
//    }
//
//    @Component
//    @Intercepts({
//            @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
//            @Signature(type = StatementHandler.class, method = "getBoundSql", args = {}),
//            @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
//            @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
//            @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
//    })
//    public static class SqlInterceptor implements Interceptor {
//
//        @Override
//        public Object intercept(Invocation invocation) throws Throwable {
//            Object target = invocation.getTarget();
//            BoundSql boundSql = null;
//            if (target instanceof StatementHandler) {
//                boundSql = ((StatementHandler) target).getBoundSql();
//                String sql = boundSql.getSql();
//                List<String> strings = ThreadLocalUtils.get();
//                if (CollectionUtils.isNotEmpty(strings)) {
//                    boolean flag = false;
//                    String ss = null;
//                    for (String s : strings) {
//                        if (sql != null && s != null) {
//                            String s1 = formatSql(sql);
//                            String s2 = formatSql(s);
//                            if (StringUtils.isNotBlank(s)) {
//                                if (!Objects.equals(s1.toLowerCase(), s2.toLowerCase())) {
//                                    ss = s;
//                                } else {
//                                    flag = true;
//                                    break;
//
//                                }
//                            }
//                        }
//                    }
//                    if (flag) {
//                        System.out.println("===============================================");
//                        System.out.println();
//                        System.out.println("pass");
//                        System.out.println();
//                        System.out.println("===============================================");
//                    } else {
//                        System.err.println("执行sql: " + removeExtraWhitespaces(sql));
//                        System.err.println("预期sql: " + removeExtraWhitespaces(ss));
//                        throw new RuntimeException("sql error");
//                    }
//                }
//            }
//            System.out.println(boundSql);
//            return invocation.proceed();
//        }
//
//        @Override
//        public Object plugin(Object target) {
//            if (target instanceof Executor || target instanceof StatementHandler) {
//                return Plugin.wrap(target, this);
//            }
//            return target;
//        }
//
//        private String formatSql(String sql) {
//            if (StringUtils.isBlank(sql)) {
//                return sql;
//            }
//            sql = sql.replaceAll("\n", "");
//            sql = sql.replaceAll("\r", "");
//            sql = sql.replaceAll("\t", "");
//            return dg(sql);
//        }
//
//        private String dg(String str) {
//            if (str.contains(" ")) {
//                str = str.replaceAll(" ", "");
//                return dg(str);
//            }
//            return str;
//        }
//
//        private String removeExtraWhitespaces(String original) {
//            StringTokenizer tokenizer = new StringTokenizer(original);
//            StringBuilder builder = new StringBuilder();
//            boolean hasMoreTokens = tokenizer.hasMoreTokens();
//            while (hasMoreTokens) {
//                builder.append(tokenizer.nextToken());
//                hasMoreTokens = tokenizer.hasMoreTokens();
//                if (hasMoreTokens) {
//                    builder.append(' ');
//                }
//            }
//            return builder.toString();
//        }
//    }
//}
