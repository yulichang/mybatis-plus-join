package com.github.yulichang.toolkit;

import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

/**
 * Wrapper 条件构造
 *
 * @author yulichang
 */
public class Wrappers {

    /**
     * Wrappers.<UserDO>queryJoin()
     */
    public static <T> MPJQueryWrapper<T> queryJoin() {
        return new MPJQueryWrapper<>();
    }

    /**
     * Wrappers.<UserDO>lambdaJoin()
     */
    public static <T> MPJLambdaWrapper<T> lambdaJoin() {
        return new MPJLambdaWrapper<>();
    }
}
