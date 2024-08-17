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


    default <R> Children isNull(SFunction<R, ?> column) {
        return isNull(true, null, column);
    }

    default <R> Children isNull(String alias, SFunction<R, ?> column) {
        return isNull(true, alias, column);
    }

    default <R> Children isNull(boolean condition, SFunction<R, ?> column) {
        return isNull(condition, null, column);
    }

    /**
     * 字段 IS NULL
     * <p>例: isNull("name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    <R> Children isNull(boolean condition, String alias, SFunction<R, ?> column);


    default <R> Children isNotNull(SFunction<R, ?> column) {
        return isNotNull(true, null, column);
    }

    default <R> Children isNotNull(String alias, SFunction<R, ?> column) {
        return isNotNull(true, alias, column);
    }

    default <R> Children isNotNull(boolean condition, SFunction<R, ?> column) {
        return isNotNull(condition, null, column);
    }

    /**
     * 字段 IS NOT NULL
     * <p>例: isNotNull("name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    <R> Children isNotNull(boolean condition, String alias, SFunction<R, ?> column);


    default <R> Children in(SFunction<R, ?> column, Collection<?> coll) {
        return in(true, null, column, coll);
    }

    default <R> Children in(String alias, SFunction<R, ?> column, Collection<?> coll) {
        return in(true, alias, column, coll);
    }

    default <R> Children in(boolean condition, SFunction<R, ?> column, Collection<?> coll) {
        return in(condition, null, column, coll);
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
    <R> Children in(boolean condition, String alias, SFunction<R, ?> column, Collection<?> coll);


    default <R> Children in(SFunction<R, ?> column, Object... values) {
        return in(true, null, column, values);
    }

    default <R> Children in(String alias, SFunction<R, ?> column, Object... values) {
        return in(true, alias, column, values);
    }

    default <R> Children in(boolean condition, SFunction<R, ?> column, Object... values) {
        return in(condition, null, column, values);
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
    <R> Children in(boolean condition, String alias, SFunction<R, ?> column, Object... values);


    default <R> Children notIn(SFunction<R, ?> column, Collection<?> coll) {
        return notIn(true, null, column, coll);
    }

    default <R> Children notIn(String alias, SFunction<R, ?> column, Collection<?> coll) {
        return notIn(true, alias, column, coll);
    }

    default <R> Children notIn(boolean condition, SFunction<R, ?> column, Collection<?> coll) {
        return notIn(condition, null, column, coll);
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
    <R> Children notIn(boolean condition, String alias, SFunction<R, ?> column, Collection<?> coll);


    default <R> Children notIn(SFunction<R, ?> column, Object... values) {
        return notIn(true, null, column, values);
    }

    default <R> Children notIn(String alias, SFunction<R, ?> column, Object... values) {
        return notIn(true, alias, column, values);
    }

    default <R> Children notIn(boolean condition, SFunction<R, ?> column, Object... values) {
        return notIn(condition, null, column, values);
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
    <R> Children notIn(boolean condition, String alias, SFunction<R, ?> column, Object... values);


    default <R> Children inSql(SFunction<R, ?> column, String inValue) {
        return inSql(true, null, column, inValue);
    }

    default <R> Children inSql(String alias, SFunction<R, ?> column, String inValue) {
        return inSql(true, alias, column, inValue);
    }

    default <R> Children inSql(boolean condition, SFunction<R, ?> column, String inValue) {
        return inSql(condition, null, column, inValue);
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
    <R> Children inSql(boolean condition, String alias, SFunction<R, ?> column, String inValue);


    default <R> Children notInSql(SFunction<R, ?> column, String inValue) {
        return notInSql(true, null, column, inValue);
    }

    default <R> Children notInSql(String alias, SFunction<R, ?> column, String inValue) {
        return notInSql(true, alias, column, inValue);
    }

    default <R> Children notInSql(boolean condition, SFunction<R, ?> column, String inValue) {
        return notInSql(condition, null, column, inValue);
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
    <R> Children notInSql(boolean condition, String alias, SFunction<R, ?> column, String inValue);

    default <R> Children gtSql(SFunction<R, ?> column, String inValue) {
        return gtSql(true, null, column, inValue);
    }

    default <R> Children gtSql(String alias, SFunction<R, ?> column, String inValue) {
        return gtSql(true, alias, column, inValue);
    }

    default <R> Children gtSql(boolean condition, SFunction<R, ?> column, String inValue) {
        return gtSql(condition, null, column, inValue);
    }

    /**
     * 字段 &gt; ( sql语句 )
     * <p>例1: gtSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: gtSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句 ---&gt; 1,2,3,4,5,6 或者 select id from table where id &lt; 3
     * @return children
     */
    <R> Children gtSql(boolean condition, String alias, SFunction<R, ?> column, String inValue);

    default <R> Children geSql(SFunction<R, ?> column, String inValue) {
        return geSql(true, null, column, inValue);
    }

    default <R> Children geSql(String alias, SFunction<R, ?> column, String inValue) {
        return geSql(true, alias, column, inValue);
    }

    default <R> Children geSql(boolean condition, SFunction<R, ?> column, String inValue) {
        return geSql(condition, null, column, inValue);
    }

    /**
     * 字段 >= ( sql语句 )
     * <p>例1: geSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: geSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句 ---&gt; 1,2,3,4,5,6 或者 select id from table where id &lt; 3
     * @return children
     */
    <R> Children geSql(boolean condition, String alias, SFunction<R, ?> column, String inValue);

    default <R> Children ltSql(SFunction<R, ?> column, String inValue) {
        return ltSql(true, null, column, inValue);
    }

    default <R> Children ltSql(String alias, SFunction<R, ?> column, String inValue) {
        return ltSql(true, alias, column, inValue);
    }

    default <R> Children ltSql(boolean condition, SFunction<R, ?> column, String inValue) {
        return ltSql(condition, null, column, inValue);
    }

    /**
     * 字段 &lt; ( sql语句 )
     * <p>例1: ltSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: ltSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句 ---&gt; 1,2,3,4,5,6 或者 select id from table where id &lt; 3
     * @return children
     */
    <R> Children ltSql(boolean condition, String alias, SFunction<R, ?> column, String inValue);

    default <R> Children leSql(SFunction<R, ?> column, String inValue) {
        return leSql(true, null, column, inValue);
    }

    default <R> Children leSql(String alias, SFunction<R, ?> column, String inValue) {
        return leSql(true, alias, column, inValue);
    }

    default <R> Children leSql(boolean condition, SFunction<R, ?> column, String inValue) {
        return leSql(condition, null, column, inValue);
    }

    /**
     * 字段 <= ( sql语句 )
     * <p>例1: leSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: leSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句 ---&gt; 1,2,3,4,5,6 或者 select id from table where id &lt; 3
     * @return children
     */
    <R> Children leSql(boolean condition, String alias, SFunction<R, ?> column, String inValue);

    default <R> Children eqSql(SFunction<R, ?> column, String inValue) {
        return eqSql(true, null, column, inValue);
    }

    default <R> Children eqSql(String alias, SFunction<R, ?> column, String inValue) {
        return eqSql(true, alias, column, inValue);
    }

    default <R> Children eqSql(boolean condition, SFunction<R, ?> column, String inValue) {
        return eqSql(condition, null, column, inValue);
    }

    /**
     * 字段 <= ( sql语句 )
     * <p>例1: leSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: leSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句 ---&gt; 1,2,3,4,5,6 或者 select id from table where id &lt; 3
     * @return children
     */
    <R> Children eqSql(boolean condition, String alias, SFunction<R, ?> column, String inValue);

    default <R> Children groupBy(SFunction<R, ?> column) {
        return groupBy(true, (String) null, column);
    }

    default <R> Children groupBy(String alias, SFunction<R, ?> column) {
        return groupBy(true, alias, column);
    }

    default <R> Children groupBy(List<SFunction<R, ?>> column) {
        return groupBy(true, null, column);
    }

    default <R> Children groupBy(String alias, List<SFunction<R, ?>> column) {
        return groupBy(true, alias, column);
    }

    default <R> Children groupBy(boolean condition, List<SFunction<R, ?>> columns) {
        return groupBy(condition, null, columns);
    }


    <R> Children groupBy(boolean condition, String alias, List<SFunction<R, ?>> columns);


    default <R> Children groupBy(SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return groupBy(true, null, column, columns);
    }

    default <R> Children groupBy(String alias, SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return groupBy(true, alias, column, columns);
    }

    default <R> Children groupBy(boolean condition, SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return groupBy(condition, null, column, columns);
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
    <R> Children groupBy(boolean condition, String alias, SFunction<R, ?> column, SFunction<R, ?>... columns);


    default <R> Children orderByAsc(SFunction<R, ?> column) {
        return orderByAsc(true, (String) null, column);
    }

    default <R> Children orderByAsc(String alias, SFunction<R, ?> column) {
        return orderByAsc(true, alias, column);
    }

    default <R> Children orderByAsc(List<SFunction<R, ?>> columns) {
        return orderByAsc(true, null, columns);
    }

    default <R> Children orderByAsc(String alias, List<SFunction<R, ?>> columns) {
        return orderByAsc(true, alias, columns);
    }

    default <R> Children orderByAsc(boolean condition, List<SFunction<R, ?>> columns) {
        return orderByAsc(condition, null, columns);
    }

    <R> Children orderByAsc(boolean condition, String alias, List<SFunction<R, ?>> columns);


    default <R> Children orderByAsc(SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return orderByAsc(true, null, column, columns);
    }

    default <R> Children orderByAsc(String alias, SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return orderByAsc(true, alias, column, columns);
    }

    default <R> Children orderByAsc(boolean condition, String alias, SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return orderBy(condition, true, alias, column, columns);
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
        return orderBy(condition, true, null, column, columns);
    }


    default <R> Children orderByDesc(SFunction<R, ?> column) {
        return orderByDesc(true, (String) null, column);
    }

    default <R> Children orderByDesc(String alias, SFunction<R, ?> column) {
        return orderByDesc(true, alias, column);
    }


    default <R> Children orderByDesc(List<SFunction<R, ?>> columns) {
        return orderByDesc(true, null, columns);
    }

    default <R> Children orderByDesc(String alias, List<SFunction<R, ?>> columns) {
        return orderByDesc(true, alias, columns);
    }

    default <R> Children orderByDesc(boolean condition, List<SFunction<R, ?>> columns) {
        return orderByDesc(condition, null, columns);
    }

    <R> Children orderByDesc(boolean condition, String alias, List<SFunction<R, ?>> columns);


    default <R> Children orderByDesc(SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return orderByDesc(true, null, column, columns);
    }

    default <R> Children orderByDesc(String alias, SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return orderByDesc(true, alias, column, columns);
    }

    default <R> Children orderByDesc(boolean condition, String alias, SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return orderBy(condition, false, alias, column, columns);
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
        return orderBy(condition, false, null, column, columns);
    }

    default <R> Children orderBy(boolean condition, boolean isAsc, SFunction<R, ?> column, SFunction<R, ?>... columns) {
        return orderBy(condition, isAsc, null, column, columns);
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
    <R> Children orderBy(boolean condition, boolean isAsc, String alias, SFunction<R, ?> column, SFunction<R, ?>... columns);


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
    default Children func(boolean condition, Consumer<Children> consumer) {
        return func(condition, consumer, null);
    }

    Children func(boolean condition, Consumer<Children> consumer, Consumer<Children> consumerElse);
}
