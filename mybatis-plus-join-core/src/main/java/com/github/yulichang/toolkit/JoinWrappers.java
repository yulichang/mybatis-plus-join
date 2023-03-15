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
     * JoinWrappers.<UserDO>queryJoin()
     */
    public static <T> MPJQueryWrapper<T> queryJoin() {
        return new MPJQueryWrapper<>();
    }


    /**
     * JoinWrappers.<UserDO>queryJoin()
     */
    public static <T> MPJQueryWrapper<T> queryJoin(Class<T> clazz) {
        return new MPJQueryWrapper<>(clazz);
    }

    /**
     * JoinWrappers.<UserDO>lambdaJoin()
     */
    public static <T> MPJLambdaWrapper<T> lambdaJoin() {
        return new MPJLambdaWrapper<>();
    }

    /**
     * JoinWrappers.<UserDO>lambdaJoin()
     */
    public static <T> MPJLambdaWrapper<T> lambdaJoin(Class<T> clazz) {
        return new MPJLambdaWrapper<>(clazz);
    }
}
