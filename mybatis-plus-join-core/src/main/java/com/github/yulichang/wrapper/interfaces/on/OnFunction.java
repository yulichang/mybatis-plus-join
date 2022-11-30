package com.github.yulichang.wrapper.interfaces.on;

import com.github.yulichang.wrapper.MPJLambdaWrapper;

/**
 * on function
 *
 * @author yulichang
 * @since 1.1.8
 */
@FunctionalInterface
public interface OnFunction<T> {

    MPJLambdaWrapper<T> apply(MPJLambdaWrapper<T> wrapper);
}
