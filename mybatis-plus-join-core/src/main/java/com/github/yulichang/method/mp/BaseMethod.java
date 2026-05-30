package com.github.yulichang.method.mp;

/**
 * 兼容原生方法
 *
 * @author yulichang
 * @since 2.0.0
 */
public interface BaseMethod {

    default String removeScript(String format) {
        return format.substring(8, format.length() - 9);
    }

    default String addScript(String sql) {
        return "<script>" + sql + "</script>";
    }
}
