package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import org.apache.ibatis.reflection.Reflector;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.HashMap;
import java.util.IdentityHashMap;
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

    //mybatis 缓存
    private static final Map<Class<?>, Reflector> CLASS_REFLECTOR_CACHE = new ConcurrentHashMap<>();

    private static final Map<String, Field> EMPTY_MAP = new HashMap<>();


    @Deprecated
    @SuppressWarnings("DeprecatedIsStillUsed")
    private static final Map<Class<?>, Class<?>> PRIMITIVE_WRAPPER_TYPE_MAP = new IdentityHashMap<>(8);

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_TO_WRAPPER_MAP = new IdentityHashMap<>(8);

    static {
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Boolean.class, boolean.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Byte.class, byte.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Character.class, char.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Double.class, double.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Float.class, float.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Integer.class, int.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Long.class, long.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(Short.class, short.class);
        PRIMITIVE_WRAPPER_TYPE_MAP.put(String.class, String.class);
        for (Map.Entry<Class<?>, Class<?>> entry : PRIMITIVE_WRAPPER_TYPE_MAP.entrySet()) {
            PRIMITIVE_TYPE_TO_WRAPPER_MAP.put(entry.getValue(), entry.getKey());
        }
    }

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

    public static Class<?> getFieldType(Class<?> clazz, String name) {
        if (clazz == null) {
            return null;
        }
        Reflector reflector = CLASS_REFLECTOR_CACHE.computeIfAbsent(clazz, Reflector::new);
        Class<?> getterType = reflector.getGetterType(name);
        if (getterType != null) {
            return getterType;
        }
        return getFieldMap(clazz).get(name).getType();
    }

    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return (clazz.isPrimitive() || PRIMITIVE_WRAPPER_TYPE_MAP.containsKey(clazz));
    }
}
