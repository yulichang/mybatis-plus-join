package com.github.yulichang.extension.apt.interfaces;

import com.github.yulichang.extension.apt.matedata.Column;

import java.io.Serializable;

/**
 * 将原来的泛型R改成Column, Object改为Column
 * 以及移除不会在ON语句中出现的条件 比如like相关 保留原来的like 只是不太可能会出现 on a.id like b.id 所以不会支持这种写法
 * <p>
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 *
 * @since 1.1.8
 */
@SuppressWarnings("unused")
public interface OnCompare<Children> extends Serializable {

    default Children eq(Column column, Column val) {
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
    Children eq(boolean condition, Column column, Column val);

    default Children ne(Column column, Column val) {
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
    Children ne(boolean condition, Column column, Column val);

    default Children gt(Column column, Column val) {
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
    Children gt(boolean condition, Column column, Column val);

    default Children ge(Column column, Column val) {
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
    Children ge(boolean condition, Column column, Column val);

    default Children lt(Column column, Column val) {
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
    Children lt(boolean condition, Column column, Column val);

    default Children le(Column column, Column val) {
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
    Children le(boolean condition, Column column, Column val);
}
