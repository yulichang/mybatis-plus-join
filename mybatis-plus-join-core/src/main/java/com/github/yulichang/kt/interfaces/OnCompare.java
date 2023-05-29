package com.github.yulichang.kt.interfaces;

import kotlin.reflect.KProperty;

import java.io.Serializable;

/**
 * 将原来的泛型R改成KProperty<?>, Object改为KProperty<?>
 * 以及移除不会在ON语句中出现的条件 比如like相关 保留原来的like 只是不太可能会出现 on a.id like b.id 所以不会支持这种写法
 * <p>
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 *
 * @since 1.4.6
 */
@SuppressWarnings("unused")
public interface OnCompare<Children> extends Serializable {
    /**
     * ignore
     */
    default Children eq(KProperty<?> column, KProperty<?> val) {
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
    Children eq(boolean condition, KProperty<?> column, KProperty<?> val);

    /**
     * ignore
     */
    default Children ne(KProperty<?> column, KProperty<?> val) {
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
    Children ne(boolean condition, KProperty<?> column, KProperty<?> val);

    /**
     * ignore
     */
    default Children gt(KProperty<?> column, KProperty<?> val) {
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
    Children gt(boolean condition, KProperty<?> column, KProperty<?> val);

    /**
     * ignore
     */
    default Children ge(KProperty<?> column, KProperty<?> val) {
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
    Children ge(boolean condition, KProperty<?> column, KProperty<?> val);

    /**
     * ignore
     */
    default Children lt(KProperty<?> column, KProperty<?> val) {
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
    Children lt(boolean condition, KProperty<?> column, KProperty<?> val);

    /**
     * ignore
     */
    default Children le(KProperty<?> column, KProperty<?> val) {
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
    Children le(boolean condition, KProperty<?> column, KProperty<?> val);
}
