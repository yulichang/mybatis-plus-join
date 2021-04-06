package com.github.yulichang.toolkit;

import com.github.yulichang.query.MPJLambdaQueryWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJJoinLambdaQueryWrapper;

/**
 * Wrapper 条件构造
 *
 * @author yulichang
 */
public class Wrappers {

    public static <T> MPJQueryWrapper<T> queryJoin() {
        return new MPJQueryWrapper<>();
    }

    @Deprecated
    public static <T> MPJLambdaQueryWrapper<T> lambdaJoin() {
        return new MPJLambdaQueryWrapper<>();
    }

    public static <T> MPJJoinLambdaQueryWrapper<T> lambdaJoinWrapper() {
        return new MPJJoinLambdaQueryWrapper<>();
    }
}
