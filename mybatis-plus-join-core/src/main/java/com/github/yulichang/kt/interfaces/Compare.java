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

    /**
     * ignore
     */
    default  Children allEq(Map<KProperty<?>,?> params) {
        return allEq(params, true);
    }

    /**
     * ignore
     */
    default  Children allEq(Map<KProperty<?>, ?> params, boolean null2IsNull) {
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


    /**
     * ignore
     */
    @SuppressWarnings("UnusedReturnValue")
    default  Children eq(KProperty<?> column, Object val) {
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
     Children eq(boolean condition, KProperty<?> column, Object val);

    /**
     * ignore
     */
    default  Children ne(KProperty<?> column, Object val) {
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
     Children ne(boolean condition, KProperty<?> column, Object val);

    /**
     * ignore
     */
    default  Children gt(KProperty<?> column, Object val) {
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
     Children gt(boolean condition, KProperty<?> column, Object val);

    /**
     * ignore
     */
    default  Children ge(KProperty<?> column, Object val) {
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
     Children ge(boolean condition, KProperty<?> column, Object val);

    /**
     * ignore
     */
    default  Children lt(KProperty<?> column, Object val) {
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
     Children lt(boolean condition, KProperty<?> column, Object val);

    /**
     * ignore
     */
    default Children le(KProperty<?> column, Object val) {
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
     Children le(boolean condition, KProperty<?> column, Object val);

    /**
     * ignore
     */
    default  Children between(KProperty<?> column, Object val1, Object val2) {
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
     Children between(boolean condition, KProperty<?> column, Object val1, Object val2);

    /**
     * ignore
     */
    default  Children notBetween(KProperty<?> column, Object val1, Object val2) {
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
     Children notBetween(boolean condition, KProperty<?> column, Object val1, Object val2);

    /**
     * ignore
     */
    default  Children like(KProperty<?> column, Object val) {
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
     Children like(boolean condition, KProperty<?> column, Object val);

    /**
     * ignore
     */
    default  Children notLike(KProperty<?> column, Object val) {
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
     Children notLike(boolean condition, KProperty<?> column, Object val);

    /**
     * ignore
     */
    default  Children likeLeft(KProperty<?> column, Object val) {
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
     Children likeLeft(boolean condition, KProperty<?> column, Object val);

    /**
     * ignore
     */
    default  Children likeRight(KProperty<?> column, Object val) {
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
     Children likeRight(boolean condition, KProperty<?> column, Object val);
}
