package com.github.mybatisplus.func;

import com.github.mybatisplus.wrapper.MyLambdaQueryWrapper;

/**
 * @author yulichang
 */
@FunctionalInterface
public interface MyWrapperFunc<T> {

    MyLambdaQueryWrapper<T> apply(MyLambdaQueryWrapper<T> wrapper);
}
