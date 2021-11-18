package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * 将原来的泛型R改成SFunction<R, ?>
 * <p>
 * copy {@link com.baomidou.mybatisplus.core.conditions.interfaces.Func}
 */
@SuppressWarnings({"unchecked", "unused"})
public interface Func<Children> extends Serializable {

    /**
     * ignore
     */
    @SuppressWarnings("UnusedReturnValue")
    default <R> Children isNull(SFunction<R, ?> column) {
        return isNull(true, column);
    }

    /**
     * 字段 IS NULL
     * <p>例: isNull("name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    <R> Children isNull(boolean condition, SFunction<R, ?> column);

    /**
     * ignore
     */
    default <R> Children isNotNull(SFunction<R, ?> column) {
        return isNotNull(true, column);
    }

    /**
     * 字段 IS NOT NULL
     * <p>例: isNotNull("name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    <R> Children isNotNull(boolean condition, SFunction<R, ?> column);

    /**
     * ignore
     */
    default <R> Children in(SFunction<R, ?> column, Collection<?> coll) {
        return in(true, column, coll);
    }

    /**
     * 字段 IN (value.get(0), value.get(1), ...)
     * <p>例: in("id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * <li> 注意！集合为空若存在逻辑错误，请在 condition 条件中判断 </li>
     * <li> 如果集合为 empty 则不会进行 sql 拼接 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    <R> Children in(boolean condition, SFunction<R, ?> column, Collection<?> coll);

    /**
     * ignore
     */
    default <R> Children in(SFunction<R, ?> column, Object... values) {
        return in(true, column, values);
    }

    /**
     * 字段 IN (v0, v1, ...)
     * <p>例: in("id", 1, 2, 3, 4, 5)</p>
     *
     * <li> 注意！数组为空若存在逻辑错误，请在 condition 条件中判断 </li>
     * <li> 如果动态数组为 empty 则不会进行 sql 拼接 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    <R> Children in(boolean condition, SFunction<R, ?> column, Object... values);

    /**
     * ignore
     */
    default <R> Children notIn(SFunction<R, ?> column, Collection<?> coll) {
        return notIn(true, column, coll);
    }

    /**
     * 字段 NOT IN (value.get(0), value.get(1), ...)
     * <p>例: notIn("id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    <R> Children notIn(boolean condition, SFunction<R, ?> column, Collection<?> coll);

    /**
     * ignore
     */
    default <R> Children notIn(SFunction<R, ?> column, Object... value) {
        return notIn(true, column, value);
    }

    /**
     * 字段 NOT IN (v0, v1, ...)
     * <p>例: notIn("id", 1, 2, 3, 4, 5)</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    <R> Children notIn(boolean condition, SFunction<R, ?> column, Object... values);

    /**
     * ignore
     */
    default <R> Children inSql(SFunction<R, ?> column, String inValue) {
        return inSql(true, column, inValue);
    }

    /**
     * 字段 IN ( sql语句 )
     * <p>!! sql 注入方式的 in 方法 !!</p>
     * <p>例1: inSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例2: inSql("id", "select id from table where id &lt; 3")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    <R> Children inSql(boolean condition, SFunction<R, ?> column, String inValue);

    /**
     * ignore
     */
    default <R> Children notInSql(SFunction<R, ?> column, String inValue) {
        return notInSql(true, column, inValue);
    }

    /**
     * 字段 NOT IN ( sql语句 )
     * <p>!! sql 注入方式的 not in 方法 !!</p>
     * <p>例1: notInSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例2: notInSql("id", "select id from table where id &lt; 3")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句 ---&gt; 1,2,3,4,5,6 或者 select id from table where id &lt; 3
     * @return children
     */
    <R> Children notInSql(boolean condition, SFunction<R, ?> column, String inValue);

    /**
     * ignore
     */
    default <R> Children groupBy(SFunction<R, ?> column) {
        return groupBy(true, column);
    }

    /**
     * ignore
     */
    default <R> Children groupBy(List<SFunction<R, ?>> column) {
        return groupBy(true, column);
    }

    /**
     * ignore
     */
    <R> Children groupBy(boolean condition, List<SFunction<R, ?>> columns);

    /**
     * ignore
     */
    default <R> Children groupBy(SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return groupBy(true, column, columns);
    }

    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: groupBy("id", "name")</p>
     *
     * @param condition 执行条件
     * @param column    单个字段
     * @param columns   字段数组
     * @return children
     */
    <R> Children groupBy(boolean condition, SFunction<R, ?> column, SFunction<R, ?>... columns);

    /**
     * ignore
     */
    default <R> Children orderByAsc(SFunction<R, ?> column) {
        return orderByAsc(true, column);
    }

    /**
     * ignore
     */
    default <R> Children orderByAsc(List<SFunction<R, ?>> columns) {
        return orderByAsc(true, columns);
    }

    /**
     * ignore
     */
    <R> Children orderByAsc(boolean condition, List<SFunction<R, ?>> columns);

    /**
     * ignore
     */
    default <R> Children orderByAsc(SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return orderByAsc(true, column, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: orderByAsc("id", "name")</p>
     *
     * @param condition 执行条件
     * @param column    单个字段
     * @param columns   字段数组
     * @return children
     */
    default <R> Children orderByAsc(boolean condition, SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return orderBy(condition, true, column, columns);
    }

    /**
     * ignore
     */
    default <R> Children orderByDesc(SFunction<R, ?> column) {
        return orderByDesc(true, column);
    }

    /**
     * ignore
     */
    default <R> Children orderByDesc(List<SFunction<R, ?>> columns) {
        return orderByDesc(true, columns);
    }

    /**
     * ignore
     */
    <R> Children orderByDesc(boolean condition, List<SFunction<R, ?>> columns);

    /**
     * ignore
     */
    default <R> Children orderByDesc(SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return orderByDesc(true, column, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: orderByDesc("id", "name")</p>
     *
     * @param condition 执行条件
     * @param column    单个字段
     * @param columns   字段数组
     * @return children
     */
    default <R> Children orderByDesc(boolean condition, SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return orderBy(condition, false, column, columns);
    }

    /**
     * 排序：ORDER BY 字段, ...
     * <p>例: orderBy(true, "id", "name")</p>
     *
     * @param condition 执行条件
     * @param isAsc     是否是 ASC 排序
     * @param column    单个字段
     * @param columns   字段数组
     * @return children
     */
    <R> Children orderBy(boolean condition, boolean isAsc, SFunction<R, ?> column, SFunction<R, ?>... columns);

    /**
     * ignore
     */
    default Children having(String sqlHaving, Object... params) {
        return having(true, sqlHaving, params);
    }

    /**
     * HAVING ( sql语句 )
     * <p>例1: having("sum(age) &gt; 10")</p>
     * <p>例2: having("sum(age) &gt; {0}", 10)</p>
     *
     * @param condition 执行条件
     * @param sqlHaving sql 语句
     * @param params    参数数组
     * @return children
     */
    Children having(boolean condition, String sqlHaving, Object... params);

    /**
     * ignore
     */
    default Children func(Consumer<Children> consumer) {
        return func(true, consumer);
    }

    /**
     * 消费函数
     *
     * @param consumer 消费函数
     * @return children
     * @since 3.3.1
     */
    Children func(boolean condition, Consumer<Children> consumer);
}
