package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import kotlin.jvm.internal.CallableReference;
import kotlin.reflect.KProperty;

import java.lang.reflect.Field;

/**
 * @author yulichang
 * @since 1.4.6
 */
public class KtUtils {

    private static final Field owner;

    static {
        try {
            owner = CallableReference.class.getDeclaredField("owner");
            owner.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw ExceptionUtils.mpe("获取字段失败, kotlin 版本需要 1.4+");
        }
    }

    public static Class<?> ref(KProperty<?> kProperty) {
        try {
            return (Class<?>) owner.get(kProperty);
        } catch (IllegalAccessException e) {
            throw ExceptionUtils.mpe("获取属性失败 <%s>" + kProperty.getName());
        }
    }
}
