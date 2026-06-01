package com.github.yulichang.wrapper.interfaces;

import com.github.yulichang.wrapper.MPJLambdaWrapper;

/**
 * @param <Children> wrapper
 * @auther yulichang
 * @since 1.5.2
 */
public interface IExt<Children extends MPJLambdaWrapper<?>> {

    Children getChildren();
}

