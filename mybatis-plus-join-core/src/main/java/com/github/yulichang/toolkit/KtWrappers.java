package com.github.yulichang.toolkit;

import com.github.yulichang.kt.KtDeleteJoinWrapper;
import com.github.yulichang.kt.KtLambdaWrapper;
import com.github.yulichang.kt.KtUpdateJoinWrapper;

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

    /**
     * KtWrappers.ktUpdate(User.class)
     */
    public static <T> KtUpdateJoinWrapper<T> update(Class<T> clazz) {
        return new KtUpdateJoinWrapper<>(clazz);
    }

    /**
     * KtWrappers.ktUpdate("t", User.class)
     */
    public static <T> KtUpdateJoinWrapper<T> update(String alias, Class<T> clazz) {
        return new KtUpdateJoinWrapper<>(clazz, alias);
    }

    /**
     * KtWrappers.ktUpdate(user)
     */
    public static <T> KtUpdateJoinWrapper<T> update(T entity) {
        return new KtUpdateJoinWrapper<>(entity);
    }

    /**
     * KtWrappers.ktUpdate("t", user)
     */
    public static <T> KtUpdateJoinWrapper<T> update(String alias, T entity) {
        return new KtUpdateJoinWrapper<>(entity, alias);
    }

    /**
     * KtWrappers.ktDelete(User.class)
     */
    public static <T> KtDeleteJoinWrapper<T> delete(Class<T> clazz) {
        return new KtDeleteJoinWrapper<>(clazz);
    }

    /**
     * KtWrappers.ktUpdate("t", User.class)
     */
    public static <T> KtDeleteJoinWrapper<T> delete(String alias, Class<T> clazz) {
        return new KtDeleteJoinWrapper<>(clazz, alias);
    }
}
