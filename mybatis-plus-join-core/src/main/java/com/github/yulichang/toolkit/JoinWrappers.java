package com.github.yulichang.toolkit;

import com.github.yulichang.kt.KtDeleteJoinWrapper;
import com.github.yulichang.kt.KtLambdaWrapper;
import com.github.yulichang.kt.KtUpdateJoinWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.DeleteJoinWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.UpdateJoinWrapper;

/**
 * @author yulichang
 * @since 1.4.4
 */
@SuppressWarnings("unused")
public class JoinWrappers {

    /**
     * JoinWrappers.<UserDO>query()
     */
    public static <T> MPJQueryWrapper<T> query() {
        return new MPJQueryWrapper<>();
    }

    /**
     * JoinWrappers.query(User.class)
     */
    public static <T> MPJQueryWrapper<T> query(Class<T> clazz) {
        return new MPJQueryWrapper<>(clazz);
    }

    /**
     * JoinWrappers.query(user)
     */
    public static <T> MPJQueryWrapper<T> query(T entity) {
        return new MPJQueryWrapper<>(entity);
    }

    /**
     * JoinWrappers.<UserDO>lambda()
     */
    public static <T> MPJLambdaWrapper<T> lambda() {
        return new MPJLambdaWrapper<>();
    }

    /**
     * JoinWrappers.<UserDO>lambda("t")
     */
    public static <T> MPJLambdaWrapper<T> lambda(String alias) {
        return new MPJLambdaWrapper<>(alias);
    }

    /**
     * JoinWrappers.lambda(User.class)
     */
    public static <T> MPJLambdaWrapper<T> lambda(Class<T> clazz) {
        return new MPJLambdaWrapper<>(clazz);
    }

    /**
     * JoinWrappers.lambda("t", User.class)
     */
    public static <T> MPJLambdaWrapper<T> lambda(String alias, Class<T> clazz) {
        return new MPJLambdaWrapper<>(clazz, alias);
    }

    /**
     * JoinWrappers.lambda(user)
     */
    public static <T> MPJLambdaWrapper<T> lambda(T entity) {
        return new MPJLambdaWrapper<>(entity);
    }

    /**
     * JoinWrappers.lambda("t", user)
     */
    public static <T> MPJLambdaWrapper<T> lambda(String alias, T entity) {
        return new MPJLambdaWrapper<>(entity, alias);
    }

    /**
     * JoinWrappers.delete(User.class)
     */
    public static <T> DeleteJoinWrapper<T> delete(Class<T> clazz) {
        return new DeleteJoinWrapper<>(clazz);
    }

    /**
     * JoinWrappers.delete("t", User.class)
     */
    public static <T> DeleteJoinWrapper<T> delete(String alias, Class<T> clazz) {
        return new DeleteJoinWrapper<>(clazz, alias);
    }

    /**
     * JoinWrappers.update(User.class)
     */
    public static <T> UpdateJoinWrapper<T> update(Class<T> clazz) {
        return new UpdateJoinWrapper<>(clazz);
    }

    /**
     * JoinWrappers.update("t", User.class)
     */
    public static <T> UpdateJoinWrapper<T> update(String alias, Class<T> clazz) {
        return new UpdateJoinWrapper<>(clazz, alias);
    }

    /**
     * JoinWrappers.kt(User.class)
     */
    public static <T> KtLambdaWrapper<T> kt(Class<T> clazz) {
        return new KtLambdaWrapper<>(clazz);
    }

    /**
     * JoinWrappers.kt("t", User.class)
     */
    public static <T> KtLambdaWrapper<T> kt(String alias, Class<T> clazz) {
        return new KtLambdaWrapper<>(clazz, alias);
    }

    /**
     * JoinWrappers.kt(user)
     */
    public static <T> KtLambdaWrapper<T> kt(T entity) {
        return new KtLambdaWrapper<>(entity);
    }

    /**
     * JoinWrappers.kt("t", user)
     */
    public static <T> KtLambdaWrapper<T> kt(String alias, T entity) {
        return new KtLambdaWrapper<>(entity, alias);
    }

    /**
     * JoinWrappers.ktUpdate(User.class)
     */
    public static <T> KtUpdateJoinWrapper<T> ktUpdate(Class<T> clazz) {
        return new KtUpdateJoinWrapper<>(clazz);
    }

    /**
     * JoinWrappers.ktUpdate("t", User.class)
     */
    public static <T> KtUpdateJoinWrapper<T> ktUpdate(String alias, Class<T> clazz) {
        return new KtUpdateJoinWrapper<>(clazz, alias);
    }

    /**
     * JoinWrappers.ktUpdate(user)
     */
    public static <T> KtUpdateJoinWrapper<T> ktUpdate(T entity) {
        return new KtUpdateJoinWrapper<>(entity);
    }

    /**
     * JoinWrappers.ktUpdate("t", user)
     */
    public static <T> KtUpdateJoinWrapper<T> ktUpdate(String alias, T entity) {
        return new KtUpdateJoinWrapper<>(entity, alias);
    }

    /**
     * JoinWrappers.ktDelete(User.class)
     */
    public static <T> KtDeleteJoinWrapper<T> ktDelete(Class<T> clazz) {
        return new KtDeleteJoinWrapper<>(clazz);
    }

    /**
     * JoinWrappers.ktUpdate("t", User.class)
     */
    public static <T> KtDeleteJoinWrapper<T> ktDelete(String alias, Class<T> clazz) {
        return new KtDeleteJoinWrapper<>(clazz, alias);
    }
}
