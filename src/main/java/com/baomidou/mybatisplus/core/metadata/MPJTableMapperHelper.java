package com.baomidou.mybatisplus.core.metadata;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实体类对应的mapper管理
 *
 * @author yulichang
 * @since 1.2.0
 */
public class MPJTableMapperHelper {

    private static final Map<Class<?>, Class<?>> CACHE = new ConcurrentHashMap<>();

    public static void init(Class<?> clazz, Class<?> mapper) {
        CACHE.put(clazz, mapper);
    }

    public static Class<?> get(Class<?> clazz) {
        return CACHE.get(clazz);
    }
}
