package com.github.yulichang.wrapper.interfaces.on;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;

/**
 * 将原来的泛型R改成SFunction<R, ?>, Object改为SFunction<S, ?>
 * 以及移除不会在ON语句中出现的条件 比如like相关 保留原来的like 只是不太可能会出现 on a.id like b.id 所以不会支持这种写法
 * <p>
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 *
 * @since 1.1.8
 */
@SuppressWarnings("unused")
public interface OnCompare<Children> extends Serializable {
    /**
     * ignore
     */
    default <R, S> Children eq(SFunction<R, ?> column, SFunction<S, ?> val) {
        return eq(true, column, val);
    }

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, S> Children eq(boolean condition, SFunction<R, ?> column, SFunction<S, ?> val);

    /**
     * ignore
     */
    default <R, S> Children ne(SFunction<R, ?> column, SFunction<S, ?> val) {
        return ne(true, column, val);
    }

    /**
     * 不等于 &lt;&gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, S> Children ne(boolean condition, SFunction<R, ?> column, SFunction<S, ?> val);

    /**
     * ignore
     */
    default <R, S> Children gt(SFunction<R, ?> column, SFunction<S, ?> val) {
        return gt(true, column, val);
    }

    /**
     * 大于 &gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, S> Children gt(boolean condition, SFunction<R, ?> column, SFunction<S, ?> val);

    /**
     * ignore
     */
    default <R, S> Children ge(SFunction<R, ?> column, SFunction<S, ?> val) {
        return ge(true, column, val);
    }

    /**
     * 大于等于 &gt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, S> Children ge(boolean condition, SFunction<R, ?> column, SFunction<S, ?> val);

    /**
     * ignore
     */
    default <R, S> Children lt(SFunction<R, ?> column, SFunction<S, ?> val) {
        return lt(true, column, val);
    }

    /**
     * 小于 &lt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, S> Children lt(boolean condition, SFunction<R, ?> column, SFunction<S, ?> val);

    /**
     * ignore
     */
    default <R, S> Children le(SFunction<R, ?> column, SFunction<S, ?> val) {
        return le(true, column, val);
    }

    /**
     * 小于等于 &lt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, S> Children le(boolean condition, SFunction<R, ?> column, SFunction<S, ?> val);

    /**
     * ignore
     */
    default <R, S, T> Children between(SFunction<R, ?> column, SFunction<S, ?> val1, SFunction<T, ?> val2) {
        return between(true, column, val1, val2);
    }

    /**
     * BETWEEN 值1 AND 值2
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val1      值1
     * @param val2      值2
     * @return children
     */
    <R, S, T> Children between(boolean condition, SFunction<R, ?> column, SFunction<S, ?> val1, SFunction<T, ?> val2);

    /**
     * ignore
     */
    default <R, S, T> Children notBetween(SFunction<R, ?> column, SFunction<S, ?> val1, SFunction<T, ?> val2) {
        return notBetween(true, column, val1, val2);
    }

    /**
     * NOT BETWEEN 值1 AND 值2
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val1      值1
     * @param val2      值2
     * @return children
     */
    <R, S, T> Children notBetween(boolean condition, SFunction<R, ?> column, SFunction<S, ?> val1, SFunction<T, ?> val2);
}
