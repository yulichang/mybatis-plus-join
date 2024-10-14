package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.segments.FuncConsumer;

import java.io.Serializable;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.update.Update}
 *
 * @author yulichang
 * @since 1.4.5
 */
@SuppressWarnings("unused")
public interface Update<Children> extends Serializable {

    /**
     * ignore
     */
    default <R> Children set(SFunction<R, ?> column, Object val) {
        return set(true, column, val);
    }

    /**
     * ignore
     */
    default <R, V> Children set(SFunction<R, ?> column, SFunction<V, ?> val) {
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
    default <R> Children set(boolean condition, SFunction<R, ?> column, Object val) {
        return set(condition, column, val, null);
    }

    default <R, V> Children set(boolean condition, SFunction<R, ?> column, SFunction<V, ?> val) {
        return set(condition, column, val, null);
    }

    /**
     * ignore
     */
    default <R> Children set(SFunction<R, ?> column, Object val, String mapping) {
        return set(true, column, val, mapping);
    }

    default <R, V> Children set(SFunction<R, ?> column, SFunction<V, ?> val, String mapping) {
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
    <R> Children set(boolean condition, SFunction<R, ?> column, Object val, String mapping);

    <R, V> Children set(boolean condition, SFunction<R, ?> column, SFunction<V, ?> val, String mapping);

    default <R> Children setIncrBy(SFunction<R, ?> column, Number val) {
        return setIncrBy(true, column, val);
    }

    <R> Children setIncrBy(boolean condition, SFunction<R, ?> column, Number val);

    default <R> Children setDecrBy(SFunction<R, ?> column, Number val) {
        return setDecrBy(true, column, val);
    }

    <R> Children setDecrBy(boolean condition, SFunction<R, ?> column, Number val);

    default Children setApply(String applySql, MFunction<FuncConsumer> consumerFunction, Object... values) {
        return setApply(true, applySql, consumerFunction, values);
    }

    Children setApply(boolean condition, String applySql, MFunction<FuncConsumer> consumerFunction, Object... values);

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
