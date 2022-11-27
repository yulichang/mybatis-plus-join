package com.github.yulichang.wrapper.interfaces.on;

import com.github.yulichang.wrapper.MPJLambdaWrapper;

/**
 * on function
 *
 * @author yulichang
 * @since 1.1.8
 */
@FunctionalInterface
public interface OnFunction {

    MPJLambdaWrapper<?> apply(MPJLambdaWrapper<?> wrapper);
}
