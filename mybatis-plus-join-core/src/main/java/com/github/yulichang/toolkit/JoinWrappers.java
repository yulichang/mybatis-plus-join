package com.github.yulichang.toolkit;

import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

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
     * JoinWrappers.<UserDO>query(User.class)
     */
    public static <T> MPJQueryWrapper<T> query(Class<T> clazz) {
        return new MPJQueryWrapper<>(clazz);
    }

    /**
     * JoinWrappers.<UserDO>query(user)
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
     * JoinWrappers.<UserDO>lambda(User.class)
     */
    public static <T> MPJLambdaWrapper<T> lambda(Class<T> clazz) {
        return new MPJLambdaWrapper<>(clazz);
    }

    /**
     * JoinWrappers.<UserDO>lambda(user)
     */
    public static <T> MPJLambdaWrapper<T> lambda(T entity) {
        return new MPJLambdaWrapper<>(entity);
    }
}
