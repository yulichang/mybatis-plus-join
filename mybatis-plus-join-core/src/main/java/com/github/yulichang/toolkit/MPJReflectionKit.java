package com.github.yulichang.toolkit;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类
 *
 * @author yulichang
 * @since 1.3.7
 */
@SuppressWarnings("unused")
public final class MPJReflectionKit {

    private static final Map<Class<?>, Map<String, Field>> CLASS_FIELD_CACHE = new ConcurrentHashMap<>();

    private static final Map<String, Field> EMPTY_MAP = new HashMap<>();

    /**
     * Collection字段的泛型
     */
    public static Class<?> getGenericType(Field field) {
        Type type = field.getGenericType();
        //没有写泛型
        if (!(type instanceof ParameterizedType)) {
            return Object.class;
        }
        ParameterizedType pt = (ParameterizedType) type;
        Type[] actualTypeArguments = pt.getActualTypeArguments();
        Type argument = actualTypeArguments[0];
        //通配符泛型 ? , ? extends XXX , ? super XXX
        if (argument instanceof WildcardType) {
            //获取上界
            Type[] types = ((WildcardType) argument).getUpperBounds();
            return (Class<?>) types[0];
        }
        return (Class<?>) argument;
    }

    /**
     * 获取该类的所有属性列表
     *
     * @param clazz 反射类
     */
    public static Map<String, Field> getFieldMap(Class<?> clazz) {
        if (clazz == null) {
            return EMPTY_MAP;
        }
        Map<String, Field> fieldMap = CLASS_FIELD_CACHE.get(clazz);
        if (fieldMap != null) {
            return fieldMap;
        }
        Map<String, Field> map = ReflectionKit.getFieldMap(clazz);
        CLASS_FIELD_CACHE.put(clazz, map);
        return map;
    }
}
