package com.github.yulichang.toolkit;

import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

/**
 * Wrapper 条件构造
 * 改名 JoinWrappers
 *
 * @author yulichang
 * @see JoinWrappers
 * @deprecated
 */
@Deprecated
@SuppressWarnings("DeprecatedIsStillUsed")
public class MPJWrappers {

    /**
     * MPJWrappers.<UserDO>queryJoin()
     * 请使用 JoinWrappers
     *
     * @see JoinWrappers
     */
    @Deprecated
    public static <T> MPJQueryWrapper<T> queryJoin() {
        return new MPJQueryWrapper<>();
    }

    /**
     * MPJWrappers.<UserDO>lambdaJoin()
     * 请使用 JoinWrappers
     *
     * @see JoinWrappers
     */
    @Deprecated
    public static <T> MPJLambdaWrapper<T> lambdaJoin() {
        return new MPJLambdaWrapper<>();
    }
}
