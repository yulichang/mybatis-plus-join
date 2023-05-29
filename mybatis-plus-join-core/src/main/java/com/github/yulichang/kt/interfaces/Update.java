package com.github.yulichang.kt.interfaces;

import kotlin.reflect.KProperty;

import java.io.Serializable;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.update.Update}
 *
 * @author yulichang
 * @since 1.4.6
 */
@SuppressWarnings("unused")
public interface Update<Children> extends Serializable {

    /**
     * ignore
     */
    default Children set(KProperty<?> column, Object val) {
        return set(true, column, val);
    }

    /**
     * 设置 更新 SQL 的 SET 片段
     *
     * @param condition 是否加入 set
     * @param column    字段
     * @param val       值
     * @return children
     */
    default Children set(boolean condition, KProperty<?> column, Object val) {
        return set(condition, column, val, null);
    }

    /**
     * ignore
     */
    default Children set(KProperty<?> column, Object val, String mapping) {
        return set(true, column, val, mapping);
    }

    /**
     * 设置 更新 SQL 的 SET 片段
     *
     * @param condition 是否加入 set
     * @param column    字段
     * @param val       值
     * @param mapping   例: javaType=int,jdbcType=NUMERIC,typeHandler=xxx.xxx.MyTypeHandler
     * @return children
     */
    Children set(boolean condition, KProperty<?> column, Object val, String mapping);

    /**
     * ignore
     */
    default Children setSql(String sql) {
        return setSql(true, sql);
    }

    /**
     * 设置 更新 SQL 的 SET 片段
     *
     * @param sql set sql
     * @return children
     */
    Children setSql(boolean condition, String sql);

    /**
     * 获取 更新 SQL 的 SET 片段
     */
    String getSqlSet();
}
