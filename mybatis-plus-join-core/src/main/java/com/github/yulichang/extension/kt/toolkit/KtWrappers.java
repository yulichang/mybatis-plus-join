package com.github.yulichang.extension.kt.toolkit;

import com.github.yulichang.extension.kt.KtLambdaWrapper;

/**
 * @author yulichang
 * @since 1.4.6
 */
@SuppressWarnings("unused")
public final class KtWrappers {

    /**
     * KtWrappers.kt(User.class)
     */
    public static <T> KtLambdaWrapper<T> query(Class<T> clazz) {
        return new KtLambdaWrapper<>(clazz);
    }

    /**
     * KtWrappers.kt("t", User.class)
     */
    public static <T> KtLambdaWrapper<T> query(String alias, Class<T> clazz) {
        return new KtLambdaWrapper<>(clazz, alias);
    }

    /**
     * KtWrappers.kt(user)
     */
    public static <T> KtLambdaWrapper<T> query(T entity) {
        return new KtLambdaWrapper<>(entity);
    }

    /**
     * KtWrappers.kt("t", user)
     */
    public static <T> KtLambdaWrapper<T> query(String alias, T entity) {
        return new KtLambdaWrapper<>(entity, alias);
    }
}
