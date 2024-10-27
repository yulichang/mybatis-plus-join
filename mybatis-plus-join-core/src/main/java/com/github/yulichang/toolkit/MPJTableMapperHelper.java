package com.github.yulichang.toolkit;

import com.github.yulichang.base.JoinMapper;

import java.util.Map;
import java.util.Optional;
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
    private static final Map<String, Class<?>> CACHE_MAPPER = new ConcurrentHashMap<>();


    public static void init(Class<?> clazz, Class<?> mapper) {
        if (clazz != null && mapper != null) {
            if (!CACHE.containsKey(clazz) || JoinMapper.class.isAssignableFrom(mapper)) {
                CACHE.put(clazz, mapper);
                CACHE_REVERSE.put(mapper, clazz);
                CACHE_MAPPER.put(mapper.getName(), mapper);
            }
        }
    }

    public static Class<?> getMapper(Class<?> clazz) {
        return Optional.ofNullable(clazz).map(m -> CACHE.get(clazz)).orElse(null);
    }

    public static Class<?> getEntity(Class<?> clazz) {
        return Optional.ofNullable(clazz).map(m -> CACHE_REVERSE.get(clazz)).orElse(null);
    }

    public static Class<?> getMapperForName(String name) {
        if (StrUtils.isBlank(name)) {
            return null;
        }
        return CACHE_MAPPER.get(name);
    }
}
