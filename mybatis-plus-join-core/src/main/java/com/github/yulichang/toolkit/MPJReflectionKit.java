package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.github.yulichang.toolkit.support.FieldCache;
import org.apache.ibatis.reflection.Reflector;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 反射工具类
 *
 * @author yulichang
 * @since 1.3.7
 */
@SuppressWarnings("unused")
public final class MPJReflectionKit {

    private static final Map<Class<?>, Map<String, FieldCache>> CLASS_FIELD_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, List<FieldCache>> CLASS_FIELD_LIST_CACHE = new ConcurrentHashMap<>();

    private static final Map<String, FieldCache> EMPTY_MAP = new HashMap<>();


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
    public static Map<String, FieldCache> getFieldMap(Class<?> clazz) {
        return CLASS_FIELD_CACHE.computeIfAbsent(clazz, key -> getFieldList(key).stream().collect(Collectors.toMap(f ->
                f.getField().getName(), Function.identity())));
    }

    public static List<FieldCache> getFieldList(Class<?> clazz) {
        if (clazz == null) {
            return Collections.emptyList();
        }
        List<FieldCache> fieldList = CLASS_FIELD_LIST_CACHE.get(clazz);
        if (fieldList != null) {
            return fieldList;
        }
        List<Field> list = ReflectionKit.getFieldList(clazz);
        List<FieldCache> cache = list.stream().map(f -> {
            FieldCache fieldCache = new FieldCache();
            fieldCache.setField(f);
            try {
                Reflector reflector = new Reflector(clazz);
                Class<?> getterType = reflector.getGetterType(f.getName());
                fieldCache.setType(Objects.isNull(getterType) ? f.getType() : getterType);
            } catch (Throwable throwable) {
                fieldCache.setType(f.getType());
            }
            return fieldCache;
        }).collect(Collectors.toList());
        CLASS_FIELD_LIST_CACHE.put(clazz, cache);
        return cache;
    }

    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        Assert.notNull(clazz, "Class must not be null");
        return (clazz.isPrimitive() || PRIMITIVE_WRAPPER_TYPE_MAP.containsKey(clazz));
    }
}
