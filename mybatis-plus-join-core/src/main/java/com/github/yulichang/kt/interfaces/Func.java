package com.github.yulichang.kt.interfaces;

import kotlin.reflect.KProperty;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * 将原来的泛型R改成KProperty<?>
 * <p>
 * copy {@link com.baomidou.mybatisplus.core.conditions.interfaces.Func}
 */
@SuppressWarnings({"unused", "JavadocDeclaration"})
public interface Func<Children> extends Serializable {

    /**
     * ignore
     */
    @SuppressWarnings("UnusedReturnValue")
    default Children isNull(KProperty<?> column) {
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
    Children isNull(boolean condition, KProperty<?> column);

    /**
     * ignore
     */
    default Children isNotNull(KProperty<?> column) {
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
    Children isNotNull(boolean condition, KProperty<?> column);

    /**
     * ignore
     */
    default Children in(KProperty<?> column, Collection<?> coll) {
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
    Children in(boolean condition, KProperty<?> column, Collection<?> coll);

    /**
     * ignore
     */
    default Children in(KProperty<?> column, Object... values) {
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
    Children in(boolean condition, KProperty<?> column, Object... values);

    /**
     * ignore
     */
    default Children notIn(KProperty<?> column, Collection<?> coll) {
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
    Children notIn(boolean condition, KProperty<?> column, Collection<?> coll);

    /**
     * ignore
     */
    default Children notIn(KProperty<?> column, Object... value) {
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
    Children notIn(boolean condition, KProperty<?> column, Object... values);

    /**
     * ignore
     */
    default Children inSql(KProperty<?> column, String inValue) {
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
    Children inSql(boolean condition, KProperty<?> column, String inValue);

    /**
     * ignore
     */
    default Children notInSql(KProperty<?> column, String inValue) {
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
    Children notInSql(boolean condition, KProperty<?> column, String inValue);


    /**
     * 字段 &gt; ( sql语句 )
     * <p>例1: gtSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: gtSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition
     * @param column
     * @param inValue
     * @return
     */
    Children gtSql(boolean condition, KProperty<?> column, String inValue);

    /**
     * ignore
     */
    default Children gtSql(KProperty<?> column, String inValue) {
        return gtSql(true, column, inValue);
    }

    /**
     * 字段 >= ( sql语句 )
     * <p>例1: geSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: geSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition
     * @param column
     * @param inValue
     * @return
     */
    Children geSql(boolean condition, KProperty<?> column, String inValue);

    /**
     * ignore
     */
    default Children geSql(KProperty<?> column, String inValue) {
        return geSql(true, column, inValue);
    }

    /**
     * 字段 &lt; ( sql语句 )
     * <p>例1: ltSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: ltSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition
     * @param column
     * @param inValue
     * @return
     */
    Children ltSql(boolean condition, KProperty<?> column, String inValue);

    /**
     * ignore
     */
    default Children ltSql(KProperty<?> column, String inValue) {
        return ltSql(true, column, inValue);
    }

    /**
     * 字段 <= ( sql语句 )
     * <p>例1: leSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: leSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition
     * @param column
     * @param inValue
     * @return
     */
    Children leSql(boolean condition, KProperty<?> column, String inValue);

    /**
     * ignore
     */
    default Children leSql(KProperty<?> column, String inValue) {
        return leSql(true, column, inValue);
    }

    /**
     * ignore
     */
    default Children groupBy(KProperty<?> column) {
        return groupBy(true, column);
    }

    /**
     * ignore
     */
    default Children groupBy(List<KProperty<?>> column) {
        return groupBy(true, column);
    }

    /**
     * ignore
     */
    Children groupBy(boolean condition, List<KProperty<?>> columns);

    /**
     * ignore
     */
    default Children groupBy(KProperty<?> column, KProperty<?>... columns) {
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
    Children groupBy(boolean condition, KProperty<?> column, KProperty<?>... columns);

    /**
     * ignore
     */
    default Children orderByAsc(KProperty<?> column) {
        return orderByAsc(true, column);
    }

    /**
     * ignore
     */
    default Children orderByAsc(List<KProperty<?>> columns) {
        return orderByAsc(true, columns);
    }

    /**
     * ignore
     */
    Children orderByAsc(boolean condition, List<KProperty<?>> columns);

    /**
     * ignore
     */
    default Children orderByAsc(KProperty<?> column, KProperty<?>... columns) {
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
    default Children orderByAsc(boolean condition, KProperty<?> column, KProperty<?>... columns) {
        return orderBy(condition, true, column, columns);
    }

    /**
     * ignore
     */
    default Children orderByDesc(KProperty<?> column) {
        return orderByDesc(true, column);
    }

    /**
     * ignore
     */
    default Children orderByDesc(List<KProperty<?>> columns) {
        return orderByDesc(true, columns);
    }

    /**
     * ignore
     */
    Children orderByDesc(boolean condition, List<KProperty<?>> columns);

    /**
     * ignore
     */
    default Children orderByDesc(KProperty<?> column, KProperty<?>... columns) {
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
    default Children orderByDesc(boolean condition, KProperty<?> column, KProperty<?>... columns) {
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
    Children orderBy(boolean condition, boolean isAsc, KProperty<?> column, KProperty<?>... columns);

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
