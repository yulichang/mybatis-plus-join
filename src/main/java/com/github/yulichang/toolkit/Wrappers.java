package com.github.yulichang.toolkit;

import com.github.yulichang.common.JoinLambdaWrapper;
import com.github.yulichang.common.support.alias.AliasLambdaQueryWrapper;
import com.github.yulichang.common.support.alias.AliasQueryWrapper;
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

    public static <T> JoinLambdaWrapper<T> commonJoin() {
        return new JoinLambdaWrapper<>();
    }

    public static <T> AliasQueryWrapper<T> aliasQueryJoin() {
        return new AliasQueryWrapper<>();
    }

    public static <T> AliasLambdaQueryWrapper<T> aliasLambdaJoin() {
        return new AliasLambdaQueryWrapper<>();
    }
}
