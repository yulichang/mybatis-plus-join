package com.github.yulichang.extension.kt.interfaces;

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

    default Children eq(KProperty<?> column, KProperty<?> val) {
        return eq(true, null, column, null, val);
    }

    default Children eq(String alias, KProperty<?> column, KProperty<?> val) {
        return eq(true, alias, column, null, val);
    }

    default Children eq(KProperty<?> column, String rightAlias, KProperty<?> val) {
        return eq(true, null, column, rightAlias, val);
    }

    default Children eq(String alias, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return eq(true, alias, column, rightAlias, val);
    }

    default Children eq(boolean condition, KProperty<?> column, KProperty<?> val) {
        return eq(condition, null, column, null, val);
    }

    default Children eq(boolean condition, String alias, KProperty<?> column, KProperty<?> val) {
        return eq(condition, alias, column, null, val);
    }

    default Children eq(boolean condition, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return eq(condition, null, column, rightAlias, val);
    }

    /**
     * 等于 =
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children eq(boolean condition, String alias, KProperty<?> column, String rightAlias, KProperty<?> val);


    default Children ne(KProperty<?> column, KProperty<?> val) {
        return ne(true, null, column, null, val);
    }

    default Children ne(String alias, KProperty<?> column, KProperty<?> val) {
        return ne(true, alias, column, null, val);
    }

    default Children ne(KProperty<?> column, String rightAlias, KProperty<?> val) {
        return ne(true, null, column, rightAlias, val);
    }

    default Children ne(String alias, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return ne(true, alias, column, rightAlias, val);
    }

    default Children ne(boolean condition, KProperty<?> column, KProperty<?> val) {
        return ne(condition, null, column, null, val);
    }

    default Children ne(boolean condition, String alias, KProperty<?> column, KProperty<?> val) {
        return ne(condition, alias, column, null, val);
    }

    default Children ne(boolean condition, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return ne(condition, null, column, rightAlias, val);
    }

    /**
     * 不等于 &lt;&gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children ne(boolean condition, String alias, KProperty<?> column, String rightAlias, KProperty<?> val);


    default Children gt(KProperty<?> column, KProperty<?> val) {
        return gt(true, null, column, null, val);
    }

    default Children gt(String alias, KProperty<?> column, KProperty<?> val) {
        return gt(true, alias, column, null, val);
    }

    default Children gt(KProperty<?> column, String rightAlias, KProperty<?> val) {
        return gt(true, null, column, rightAlias, val);
    }

    default Children gt(String alias, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return gt(true, alias, column, rightAlias, val);
    }

    default Children gt(boolean condition, KProperty<?> column, KProperty<?> val) {
        return gt(condition, null, column, null, val);
    }

    default Children gt(boolean condition, String alias, KProperty<?> column, KProperty<?> val) {
        return gt(condition, alias, column, null, val);
    }

    default Children gt(boolean condition, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return gt(condition, null, column, rightAlias, val);
    }

    /**
     * 大于 &gt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children gt(boolean condition, String alias, KProperty<?> column, String rightAlias, KProperty<?> val);


    default Children ge(KProperty<?> column, KProperty<?> val) {
        return ge(true, null, column, null, val);
    }

    default Children ge(String alias, KProperty<?> column, KProperty<?> val) {
        return ge(true, alias, column, null, val);
    }

    default Children ge(KProperty<?> column, String rightAlias, KProperty<?> val) {
        return ge(true, null, column, rightAlias, val);
    }

    default Children ge(String alias, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return ge(true, alias, column, rightAlias, val);
    }

    default Children ge(boolean condition, KProperty<?> column, KProperty<?> val) {
        return ge(condition, null, column, null, val);
    }

    default Children ge(boolean condition, String alias, KProperty<?> column, KProperty<?> val) {
        return ge(condition, alias, column, null, val);
    }

    default Children ge(boolean condition, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return ge(condition, null, column, rightAlias, val);
    }

    /**
     * 大于等于 &gt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children ge(boolean condition, String alias, KProperty<?> column, String rightAlias, KProperty<?> val);


    default Children lt(KProperty<?> column, KProperty<?> val) {
        return lt(true, null, column, null, val);
    }

    default Children lt(String alias, KProperty<?> column, KProperty<?> val) {
        return lt(true, alias, column, null, val);
    }

    default Children lt(KProperty<?> column, String rightAlias, KProperty<?> val) {
        return lt(true, null, column, rightAlias, val);
    }

    default Children lt(String alias, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return lt(true, alias, column, rightAlias, val);
    }

    default Children lt(boolean condition, KProperty<?> column, KProperty<?> val) {
        return lt(condition, null, column, null, val);
    }

    default Children lt(boolean condition, String alias, KProperty<?> column, KProperty<?> val) {
        return lt(condition, alias, column, null, val);
    }

    default Children lt(boolean condition, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return lt(condition, null, column, rightAlias, val);
    }

    /**
     * 小于 &lt;
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children lt(boolean condition, String alias, KProperty<?> column, String rightAlias, KProperty<?> val);


    default Children le(KProperty<?> column, KProperty<?> val) {
        return le(true, null, column, null, val);
    }

    default Children le(String alias, KProperty<?> column, KProperty<?> val) {
        return le(true, alias, column, null, val);
    }

    default Children le(KProperty<?> column, String rightAlias, KProperty<?> val) {
        return le(true, null, column, rightAlias, val);
    }

    default Children le(String alias, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return le(true, alias, column, rightAlias, val);
    }

    default Children le(boolean condition, KProperty<?> column, KProperty<?> val) {
        return le(condition, null, column, null, val);
    }

    default Children le(boolean condition, String alias, KProperty<?> column, KProperty<?> val) {
        return le(condition, alias, column, null, val);
    }

    default Children le(boolean condition, KProperty<?> column, String rightAlias, KProperty<?> val) {
        return le(condition, null, column, rightAlias, val);
    }

    /**
     * 小于等于 &lt;=
     *
     * @param condition 执行条件
     * @param column    字段
     * @param val       值
     * @return children
     */
    Children le(boolean condition, String alias, KProperty<?> column, String rightAlias, KProperty<?> val);
}
