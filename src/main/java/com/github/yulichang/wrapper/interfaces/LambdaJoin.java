package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.Constant;

/**
 * @author yulichang
 */
public interface LambdaJoin<Children> extends MPJBaseJoin {

    default <T, X> Children leftJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return leftJoin(true, clazz, left, right);
    }

    default <T, X> Children leftJoin(boolean condition, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.LEFT_JOIN, condition, clazz, left, right);
    }

    default <T, X> Children rightJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return rightJoin(true, clazz, left, right);
    }

    default <T, X> Children rightJoin(boolean condition, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.RIGHT_JOIN, condition, clazz, left, right);
    }

    default <T, X> Children innerJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return innerJoin(true, clazz, left, right);
    }

    default <T, X> Children innerJoin(boolean condition, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.INNER_JOIN, condition, clazz, left, right);
    }

    <T, X> Children join(String keyWord, boolean condition, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right);


}
