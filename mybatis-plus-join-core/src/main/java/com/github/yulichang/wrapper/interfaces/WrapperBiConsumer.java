package com.github.yulichang.wrapper.interfaces;

import com.github.yulichang.wrapper.MPJLambdaWrapper;

@FunctionalInterface
public interface WrapperBiConsumer<T> {

    void accept(MPJLambdaWrapper<T> on, MPJLambdaWrapper<T> ext);
}
