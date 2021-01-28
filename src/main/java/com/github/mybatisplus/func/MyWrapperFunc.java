package com.github.mybatisplus.func;

import com.github.mybatisplus.wrapper.MyLambdaQueryWrapper;
import com.github.mybatisplus.base.MyBaseEntity;

/**
 * @author yulichang
 */
@FunctionalInterface
public interface MyWrapperFunc<T extends MyBaseEntity> {

    MyLambdaQueryWrapper<T> apply(MyLambdaQueryWrapper<T> wrapper);
}
