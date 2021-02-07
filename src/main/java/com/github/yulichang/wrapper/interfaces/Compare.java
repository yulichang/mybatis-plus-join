package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiPredicate;

/**
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 */
public interface Compare<Children> extends Serializable {

    /**
     * ignore
     */
    default <R, V> Children allEq(Map<SFunction<R, ?>, V> params) {
        return allEq(params, true);
    }

    /**
     * ignore
     */
    default <R, V> Children allEq(Map<SFunction<R, ?>, V> params, boolean null2IsNull) {
        return allEq(true, params, null2IsNull);
    }

    /**
     * map 所有非空属性等于 =
     *
     * @param condition   执行条件
     * @param params      map 类型的参数, key 是字段名, value 是字段值
     * @param null2IsNull 是否参数为 null 自动执行 isNull 方法, false 则忽略这个字段\
     * @return children
     */
    <R, V> Children allEq(boolean condition, Map<SFunction<R, ?>, V> params, boolean null2IsNull);

    /**
     * ignore
     */
    default <R, V> Children allEq(BiPredicate<SFunction<R, ?>, V> filter, Map<SFunction<R, ?>, V> params) {
        return allEq(filter, params, true);
    }

    /**
     * ignore
     */
    default <R, V> Children allEq(BiPredicate<SFunction<R, ?>, V> filter, Map<SFunction<R, ?>, V> params, boolean null2IsNull) {
        return allEq(true, filter, params, null2IsNull);
    }

    /**
     * 字段过滤接口，传入多参数时允许对参数进行过滤
     *
     * @param condition   执行条件
     * @param filter      返回 true 来允许字段传入比对条件中
     * @param params      map 类型的参数, key 是字段名, value 是字段值
     * @param null2IsNull 是否参数为 null 自动执行 isNull 方法, false 则忽略这个字段
     * @return children
     */
    <R, V> Children allEq(boolean condition, BiPredicate<SFunction<R, ?>, V> filter, Map<SFunction<R, ?>, V> params, boolean null2IsNull);

    /**
     * ignore
     */
    default <R> Children eq(SFunction<R, ?> column, Object val) {
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
    <R> Children eq(boolean condition, SFunction<R, ?> column, Object val);

    /**
     * ignore
     */
    default <R> Children ne(SFunction<R, ?> column, Object val) {
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
    <R> Children ne(boolean condition, SFunction<R, ?> column, Object val);

    /**
     * ignore
     */
    default <R> Children gt(SFunction<R, ?> column, Object val) {
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
    <R> Children gt(boolean condition, SFunction<R, ?> column, Object val);

    /**
     * ignore
     */
    default <R> Children ge(SFunction<R, ?> column, Object val) {
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
    <R> Children ge(boolean condition, SFunction<R, ?> column, Object val);

    /**
     * ignore
     */
    default <R> Children lt(SFunction<R, ?> column, Object val) {
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
    <R> Children lt(boolean condition, SFunction<R, ?> column, Object val);

    /**
     * ignore
     */
    default <R> Children le(SFunction<R, ?> column, Object val) {
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
    <R> Children le(boolean condition, SFunction<R, ?> column, Object val);

    /**
     * ignore
     */
    default <R> Children between(SFunction<R, ?> column, Object val1, Object val2) {
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
    <R> Children between(boolean condition, SFunction<R, ?> column, Object val1, Object val2);

    /**
     * ignore
     */
    default <R> Children notBetween(SFunction<R, ?> column, Object val1, Object val2) {
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
    <R> Children notBetween(boolean condition, SFunction<R, ?> column, Object val1, Object val2);

    /**
     * ignore
     */
    default <R> Children like(SFunction<R, ?> column, Object val) {
        return like(true, column, val);
    }

    /**
     * LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children like(boolean condition, SFunction<R, ?> column, Object val);

    /**
     * ignore
     */
    default <R> Children notLike(SFunction<R, ?> column, Object val) {
        return notLike(true, column, val);
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children notLike(boolean condition, SFunction<R, ?> column, Object val);

    /**
     * ignore
     */
    default <R> Children likeLeft(SFunction<R, ?> column, Object val) {
        return likeLeft(true, column, val);
    }

    /**
     * LIKE '%值'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children likeLeft(boolean condition, SFunction<R, ?> column, Object val);

    /**
     * ignore
     */
    default <R> Children likeRight(SFunction<R, ?> column, Object val) {
        return likeRight(true, column, val);
    }

    /**
     * LIKE '值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    <R> Children likeRight(boolean condition, SFunction<R, ?> column, Object val);
}
