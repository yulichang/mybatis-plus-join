package com.example.mp.mybatis.plus.func;

import com.example.mp.mybatis.plus.base.MyBaseEntity;
import com.example.mp.mybatis.plus.wrapper.MyLambdaQueryWrapper;

/**
 * @author yulichang
 */
@FunctionalInterface
public interface MyWrapperFunc<T extends MyBaseEntity> {

    MyLambdaQueryWrapper<T> apply(MyLambdaQueryWrapper<T> wrapper);
}
