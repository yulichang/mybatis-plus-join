package com.github.yulichang.kt.interfaces;

import kotlin.reflect.KProperty;

import java.io.Serializable;
import java.util.Map;

/**
 * 将原来的泛型R改成KProperty<?>
 * <p>
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 */
@SuppressWarnings("unused")
public interface Compare<Children> extends Serializable {


    default Children allEq(Map<KProperty<?>, ?> params) {
        return allEq(params, true);
    }


    default Children allEq(Map<KProperty<?>, ?> params, boolean null2IsNull) {
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
    Children allEq(boolean condition, Map<KProperty<?>, ?> params, boolean null2IsNull);


    default Children eq(KProperty<?> column, Object val) {
        return eq(true, null, column, val);
    }

    default Children eq(String alias, KProperty<?> column, Object val) {
        return eq(true, alias, column, val);
    }

    default Children eq(boolean condition, KProperty<?> column, Object val) {
        return eq(condition, null, column, val);
    }

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children eq(boolean condition, String alias, KProperty<?> column, Object val);


    default Children ne(KProperty<?> column, Object val) {
        return ne(true, null, column, val);
    }

    default Children ne(String alias, KProperty<?> column, Object val) {
        return ne(true, alias, column, val);
    }

    default Children ne(boolean condition, KProperty<?> column, Object val) {
        return ne(condition, null, column, val);
    }

    /**
     * 不等于 &lt;&gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children ne(boolean condition, String alias, KProperty<?> column, Object val);


    default Children gt(KProperty<?> column, Object val) {
        return gt(true, null, column, val);
    }

    default Children gt(String alias, KProperty<?> column, Object val) {
        return gt(true, alias, column, val);
    }

    default Children gt(boolean condition, KProperty<?> column, Object val) {
        return gt(condition, null, column, val);
    }

    /**
     * 大于 &gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children gt(boolean condition, String alias, KProperty<?> column, Object val);


    default Children ge(KProperty<?> column, Object val) {
        return ge(true, null, column, val);
    }

    default Children ge(String alias, KProperty<?> column, Object val) {
        return ge(true, alias, column, val);
    }

    default Children ge(boolean condition, KProperty<?> column, Object val) {
        return ge(condition, null, column, val);
    }

    /**
     * 大于等于 &gt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children ge(boolean condition, String alias, KProperty<?> column, Object val);


    default Children lt(KProperty<?> column, Object val) {
        return lt(true, null, column, val);
    }

    default Children lt(String alias, KProperty<?> column, Object val) {
        return lt(true, alias, column, val);
    }

    default Children lt(boolean condition, KProperty<?> column, Object val) {
        return lt(condition, null, column, val);
    }

    /**
     * 小于 &lt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children lt(boolean condition, String alias, KProperty<?> column, Object val);


    default Children le(KProperty<?> column, Object val) {
        return le(true, null, column, val);
    }

    default Children le(String alias, KProperty<?> column, Object val) {
        return le(true, alias, column, val);
    }

    default Children le(boolean condition, KProperty<?> column, Object val) {
        return le(condition, null, column, val);
    }

    /**
     * 小于等于 &lt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children le(boolean condition, String alias, KProperty<?> column, Object val);


    default Children between(KProperty<?> column, Object val1, Object val2) {
        return between(true, null, column, val1, val2);
    }

    default Children between(String alias, KProperty<?> column, Object val1, Object val2) {
        return between(true, alias, column, val1, val2);
    }

    default Children between(boolean condition, KProperty<?> column, Object val1, Object val2) {
        return between(condition, null, column, val1, val2);
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
    Children between(boolean condition, String alias, KProperty<?> column, Object val1, Object val2);

    default Children notBetween(KProperty<?> column, Object val1, Object val2) {
        return notBetween(true, null, column, val1, val2);
    }

    default Children notBetween(String alias, KProperty<?> column, Object val1, Object val2) {
        return notBetween(true, alias, column, val1, val2);
    }

    default Children notBetween(boolean condition, KProperty<?> column, Object val1, Object val2) {
        return notBetween(condition, null, column, val1, val2);
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
    Children notBetween(boolean condition, String alias, KProperty<?> column, Object val1, Object val2);


    default Children like(KProperty<?> column, Object val) {
        return like(true, null, column, val);
    }

    default Children like(String alisa, KProperty<?> column, Object val) {
        return like(true, alisa, column, val);
    }

    default Children like(boolean condition, KProperty<?> column, Object val) {
        return like(condition, null, column, val);
    }

    /**
     * LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children like(boolean condition, String alias, KProperty<?> column, Object val);


    default Children notLike(KProperty<?> column, Object val) {
        return notLike(true, null, column, val);
    }

    default Children notLike(String alias, KProperty<?> column, Object val) {
        return notLike(true, alias, column, val);
    }

    default Children notLike(boolean condition, KProperty<?> column, Object val) {
        return notLike(condition, null, column, val);
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children notLike(boolean condition, String alias, KProperty<?> column, Object val);


    default Children likeLeft(KProperty<?> column, Object val) {
        return likeLeft(true, null, column, val);
    }

    default Children likeLeft(String alias, KProperty<?> column, Object val) {
        return likeLeft(true, alias, column, val);
    }

    default Children likeLeft(boolean condition, KProperty<?> column, Object val) {
        return likeLeft(condition, null, column, val);
    }

    /**
     * LIKE '%值'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children likeLeft(boolean condition, String alias, KProperty<?> column, Object val);


    default Children likeRight(KProperty<?> column, Object val) {
        return likeRight(true, null, column, val);
    }

    default Children likeRight(String alias, KProperty<?> column, Object val) {
        return likeRight(true, alias, column, val);
    }

    default Children likeRight(boolean condition, KProperty<?> column, Object val) {
        return likeRight(condition, null, column, val);
    }


    /**
     * LIKE '值%'
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children likeRight(boolean condition, String alias, KProperty<?> column, Object val);
}
