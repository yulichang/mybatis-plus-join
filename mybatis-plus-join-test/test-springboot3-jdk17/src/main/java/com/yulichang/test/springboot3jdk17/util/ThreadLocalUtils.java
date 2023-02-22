package com.yulichang.test.springboot3jdk17.util;

public class ThreadLocalUtils {

    private final static ThreadLocal<String> userThreadLocal = new ThreadLocal<>();

    /**
     * 设置数据到当前线程
     */
    public static void set(String sql) {
        userThreadLocal.set(sql);
    }

    /**
     * 获取线程中的数据
     */
    public static String get() {
        String s = userThreadLocal.get();
        set(null);
        return s;
    }
}
