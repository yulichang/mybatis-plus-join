package com.github.yulichang.test.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;

public class ThreadLocalUtils {

    private final static ThreadLocal<String> userThreadLocal = new ThreadLocal<>();

    /**
     * 设置数据到当前线程
     */
    public static void set(String... sql) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            userThreadLocal.set(mapper.writeValueAsString(Arrays.asList(sql)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取线程中的数据
     */
    public static String get() {
        String s = userThreadLocal.get();
        set("");
        return s;
    }
}
