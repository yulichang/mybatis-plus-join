package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.io.Serializable;

/**
 * 将原来的泛型R改成SFunction<R, ?>
 * <p>
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 */
@SuppressWarnings("unused")
public interface CompareWrapper<Children> extends Serializable {

    default <R, T> Children eq(SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return eq(true, null, column, clazz, val);
    }

    default <R, T> Children eq(String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return eq(true, alias, column, clazz, val);
    }

    default <R, T> Children eq(boolean condition, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return eq(condition, null, column, clazz, val);
    }

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, T> Children eq(boolean condition, String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val);

    default <R, T> Children ne(SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return ne(true, null, column, clazz, val);
    }

    default <R, T> Children ne(String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return ne(true, alias, column, clazz, val);
    }

    default <R, T> Children ne(boolean condition, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return ne(condition, null, column, clazz, val);
    }

    /**
     * 不等于 &lt;&gt;
     *
     * @param condition 执行条件
     * @param alias     字段别名
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, T> Children ne(boolean condition, String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val);


    default <R, T> Children gt(SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return gt(true, null, column, clazz, val);
    }

    default <R, T> Children gt(String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return gt(true, alias, column, clazz, val);
    }

    default <R, T> Children gt(boolean condition, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return gt(condition, null, column, clazz, val);
    }

    /**
     * 大于 &gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, T> Children gt(boolean condition, String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val);

    default <R, T> Children ge(SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return ge(true, null, column, clazz, val);
    }

    default <R, T> Children ge(String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return ge(true, alias, column, clazz, val);
    }

    default <R, T> Children ge(boolean condition, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return ge(condition, null, column, clazz, val);
    }

    /**
     * 大于等于 &gt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, T> Children ge(boolean condition, String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val);


    default <R, T> Children lt(SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return lt(true, null, column, clazz, val);
    }

    default <R, T> Children lt(String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return lt(true, alias, column, clazz, val);
    }

    default <R, T> Children lt(boolean condition, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return lt(condition, null, column, clazz, val);
    }

    /**
     * 小于 &lt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, T> Children lt(boolean condition, String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val);


    default <R, T> Children le(SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return le(true, null, column, clazz, val);
    }

    default <R, T> Children le(String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return le(true, alias, column, clazz, val);
    }

    default <R, T> Children le(boolean condition, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val) {
        return le(condition, null, column, clazz, val);
    }

    /**
     * 小于等于 &lt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R, T> Children le(boolean condition, String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> val);
}
