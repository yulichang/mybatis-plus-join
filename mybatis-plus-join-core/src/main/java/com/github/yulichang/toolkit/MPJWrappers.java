package com.github.yulichang.toolkit;

import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

/**
 * Wrapper 条件构造
 *
 * @author yulichang
 * @see JoinWrappers
 */
@SuppressWarnings("unused")
public class MPJWrappers {

    /**
     * MPJWrappers.<UserDO>queryJoin()
     */
    public static <T> MPJQueryWrapper<T> queryJoin() {
        return new MPJQueryWrapper<>();
    }

    public static <T> MPJQueryWrapper<T> queryJoin(T entity) {
        return new MPJQueryWrapper<>(entity);
    }

    public static <T> MPJQueryWrapper<T> queryJoin(Class<T> entityClass) {
        return new MPJQueryWrapper<>(entityClass);
    }

    /**
     * MPJWrappers.<UserDO>lambdaJoin()
     */
    public static <T> MPJLambdaWrapper<T> lambdaJoin() {
        return new MPJLambdaWrapper<>();
    }

    public static <T> MPJLambdaWrapper<T> lambdaJoin(T entity) {
        return new MPJLambdaWrapper<>(entity);
    }

    public static <T> MPJLambdaWrapper<T> lambdaJoin(Class<T> entityClass) {
        return new MPJLambdaWrapper<>(entityClass);
    }
}
