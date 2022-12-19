package com.github.yulichang.mapper;

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
    private static final Map<Class<?>, Class<?>> CACHE_REVERSE = new ConcurrentHashMap<>();


    public static void init(Class<?> clazz, Class<?> mapper) {
        if (clazz != null && mapper != null) {
            CACHE.put(clazz, mapper);
            CACHE_REVERSE.put(mapper, clazz);
        }
    }

    public static Class<?> getMapper(Class<?> clazz) {
        return CACHE.get(clazz);
    }

    public static Class<?> getEntity(Class<?> clazz) {
        return CACHE_REVERSE.get(clazz);
    }

}
