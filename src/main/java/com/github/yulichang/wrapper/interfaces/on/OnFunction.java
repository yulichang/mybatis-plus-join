package com.github.yulichang.wrapper.interfaces.on;

import com.github.yulichang.wrapper.MPJLambdaWrapper;

@FunctionalInterface
public interface OnFunction {

    MPJLambdaWrapper<?> apply(MPJLambdaWrapper<?> wrapper);
}
