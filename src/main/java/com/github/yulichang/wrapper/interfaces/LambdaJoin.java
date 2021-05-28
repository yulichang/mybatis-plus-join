package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.wrapper.interfaces.on.OnFunction;

/**
 * @author yulichang
 */
public interface LambdaJoin<Children> extends MPJBaseJoin {

    default <T, X> Children leftJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return leftJoin(true, clazz, left, right);
    }

    default <T> Children leftJoin(Class<T> clazz, OnFunction function) {
        return leftJoin(true, clazz, function);
    }

    default <T, X> Children leftJoin(boolean condition, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return leftJoin(condition, clazz, on -> on.eq(left, right));
    }

    default <T> Children leftJoin(boolean condition, Class<T> clazz, OnFunction function) {
        return join(Constant.LEFT_JOIN, condition, clazz, function);
    }

    default <T, X> Children rightJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return rightJoin(true, clazz, left, right);
    }

    default <T> Children rightJoin(Class<T> clazz, OnFunction function) {
        return rightJoin(true, clazz, function);
    }

    default <T, X> Children rightJoin(boolean condition, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return rightJoin(condition, clazz, on -> on.eq(left, right));
    }

    default <T> Children rightJoin(boolean condition, Class<T> clazz, OnFunction function) {
        return join(Constant.RIGHT_JOIN, condition, clazz, function);
    }

    default <T, X> Children innerJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return innerJoin(true, clazz, left, right);
    }

    default <T> Children innerJoin(Class<T> clazz, OnFunction function) {
        return innerJoin(true, clazz, function);
    }

    default <T, X> Children innerJoin(boolean condition, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return innerJoin(condition, clazz, on -> on.eq(left, right));
    }

    default <T> Children innerJoin(boolean condition, Class<T> clazz, OnFunction function) {
        return join(Constant.INNER_JOIN, condition, clazz, function);
    }

    <T> Children join(String keyWord, boolean condition, Class<T> clazz, OnFunction function);
}
