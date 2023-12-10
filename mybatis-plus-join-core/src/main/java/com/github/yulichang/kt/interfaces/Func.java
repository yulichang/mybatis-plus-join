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
@SuppressWarnings("unused")
public interface Func<Children> extends Serializable {


    default Children isNull(KProperty<?> column) {
        return isNull(true, null, column);
    }

    default Children isNull(String alias, KProperty<?> column) {
        return isNull(true, alias, column);
    }

    default Children isNull(boolean condition, KProperty<?> column) {
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
    Children isNull(boolean condition, String alias, KProperty<?> column);


    default Children isNotNull(KProperty<?> column) {
        return isNotNull(true, null, column);
    }

    default Children isNotNull(String alias, KProperty<?> column) {
        return isNotNull(true, alias, column);
    }

    default Children isNotNull(boolean condition, KProperty<?> column) {
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
    Children isNotNull(boolean condition, String alias, KProperty<?> column);


    default Children in(KProperty<?> column, Collection<?> coll) {
        return in(true, null, column, coll);
    }

    default Children in(String alias, KProperty<?> column, Collection<?> coll) {
        return in(true, alias, column, coll);
    }

    default Children in(boolean condition, KProperty<?> column, Collection<?> coll) {
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
    Children in(boolean condition, String alias, KProperty<?> column, Collection<?> coll);


    default Children in(KProperty<?> column, Object... values) {
        return in(true, null, column, values);
    }

    default Children in(String alias, KProperty<?> column, Object... values) {
        return in(true, alias, column, values);
    }

    default Children in(boolean condition, KProperty<?> column, Object... values) {
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
    Children in(boolean condition, String alias, KProperty<?> column, Object... values);


    default Children notIn(KProperty<?> column, Collection<?> coll) {
        return notIn(true, null, column, coll);
    }

    default Children notIn(String alias, KProperty<?> column, Collection<?> coll) {
        return notIn(true, alias, column, coll);
    }

    default Children notIn(boolean condition, KProperty<?> column, Collection<?> coll) {
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
    Children notIn(boolean condition, String alias, KProperty<?> column, Collection<?> coll);


    default Children notIn(KProperty<?> column, Object... value) {
        return notIn(true, null, column, value);
    }

    default Children notIn(String alias, KProperty<?> column, Object... value) {
        return notIn(true, alias, column, value);
    }

    default Children notIn(boolean condition, KProperty<?> column, Object... values) {
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
    Children notIn(boolean condition, String alias, KProperty<?> column, Object... values);


    default Children inSql(KProperty<?> column, String inValue) {
        return inSql(true, null, column, inValue);
    }

    default Children inSql(String alias, KProperty<?> column, String inValue) {
        return inSql(true, alias, column, inValue);
    }

    default Children inSql(boolean condition, KProperty<?> column, String inValue) {
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
    Children inSql(boolean condition, String alias, KProperty<?> column, String inValue);


    default Children notInSql(KProperty<?> column, String inValue) {
        return notInSql(true, null, column, inValue);
    }

    default Children notInSql(String alias, KProperty<?> column, String inValue) {
        return notInSql(true, alias, column, inValue);
    }

    default Children notInSql(boolean condition, KProperty<?> column, String inValue) {
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
    Children notInSql(boolean condition, String alias, KProperty<?> column, String inValue);

    default Children gtSql(KProperty<?> column, String inValue) {
        return gtSql(true, null, column, inValue);
    }

    default Children gtSql(String alias, KProperty<?> column, String inValue) {
        return gtSql(true, alias, column, inValue);
    }

    default Children gtSql(boolean condition, KProperty<?> column, String inValue) {
        return gtSql(condition, null, column, inValue);
    }

    /**
     * 字段 &gt; ( sql语句 )
     * <p>例1: gtSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: gtSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    Children gtSql(boolean condition, String alias, KProperty<?> column, String inValue);

    default Children geSql(KProperty<?> column, String inValue) {
        return geSql(true, null, column, inValue);
    }

    default Children geSql(String alias, KProperty<?> column, String inValue) {
        return geSql(true, alias, column, inValue);
    }

    default Children geSql(boolean condition, KProperty<?> column, String inValue) {
        return geSql(condition, null, column, inValue);
    }

    /**
     * 字段 >= ( sql语句 )
     * <p>例1: geSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: geSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    Children geSql(boolean condition, String alias, KProperty<?> column, String inValue);


    default Children ltSql(KProperty<?> column, String inValue) {
        return ltSql(true, null, column, inValue);
    }

    default Children ltSql(String alias, KProperty<?> column, String inValue) {
        return ltSql(true, alias, column, inValue);
    }

    default Children ltSql(boolean condition, KProperty<?> column, String inValue) {
        return ltSql(condition, null, column, inValue);
    }

    /**
     * 字段 &lt; ( sql语句 )
     * <p>例1: ltSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: ltSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    Children ltSql(boolean condition, String alias, KProperty<?> column, String inValue);

    default Children leSql(KProperty<?> column, String inValue) {
        return leSql(true, null, column, inValue);
    }

    default Children leSql(String alias, KProperty<?> column, String inValue) {
        return leSql(true, alias, column, inValue);
    }

    default Children leSql(boolean condition, KProperty<?> column, String inValue) {
        return leSql(condition, null, column, inValue);
    }

    /**
     * 字段 <= ( sql语句 )
     * <p>例1: leSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: leSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    Children leSql(boolean condition, String alias, KProperty<?> column, String inValue);


    default Children groupBy(List<KProperty<?>> column) {
        return groupBy(true, null, column);
    }

    default Children groupBy(String alias, List<KProperty<?>> column) {
        return groupBy(true, alias, column);
    }

    default Children groupBy(boolean condition, List<KProperty<?>> columns) {
        return groupBy(condition, null, columns);
    }


    Children groupBy(boolean condition, String alias, List<KProperty<?>> columns);


    default Children groupBy(KProperty<?>... columns) {
        return groupBy(true, null, columns);
    }

    default Children groupBy(String alias, KProperty<?>... columns) {
        return groupBy(true, alias, columns);
    }

    default Children groupBy(boolean condition, KProperty<?>... columns) {
        return groupBy(condition, null, columns);
    }

    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: groupBy("id", "name")</p>
     *
     * @param condition 执行条件
     * @param columns   字段数组
     * @return children
     */
    Children groupBy(boolean condition, String alias, KProperty<?>... columns);


    default Children orderByAsc(KProperty<?> column) {
        return orderByAsc(true, (String) null, column);
    }

    default Children orderByAsc(String alias, KProperty<?> column) {
        return orderByAsc(true, alias, column);
    }


    default Children orderByAsc(List<KProperty<?>> columns) {
        return orderByAsc(true, null, columns);
    }

    default Children orderByAsc(String alias, List<KProperty<?>> columns) {
        return orderByAsc(true, alias, columns);
    }

    default Children orderByAsc(boolean condition, List<KProperty<?>> columns) {
        return orderByAsc(condition, null, columns);
    }

    Children orderByAsc(boolean condition, String alias, List<KProperty<?>> columns);


    default Children orderByAsc(KProperty<?>... columns) {
        return orderByAsc(true, null, columns);
    }

    default Children orderByAsc(String alias, KProperty<?>... columns) {
        return orderByAsc(true, alias, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: orderByAsc("id", "name")</p>
     *
     * @param condition 执行条件
     * @param columns   字段数组
     * @return children
     */
    default Children orderByAsc(boolean condition, KProperty<?>... columns) {
        return orderBy(condition, true, null, columns);
    }

    default Children orderByAsc(boolean condition, String alias, KProperty<?>... columns) {
        return orderBy(condition, true, alias, columns);
    }

    default Children orderByDesc(List<KProperty<?>> columns) {
        return orderByDesc(true, null, columns);
    }

    default Children orderByDesc(String alias, List<KProperty<?>> columns) {
        return orderByDesc(true, alias, columns);
    }

    default Children orderByDesc(boolean condition, List<KProperty<?>> columns) {
        return orderByDesc(condition, null, columns);
    }

    Children orderByDesc(boolean condition, String alias, List<KProperty<?>> columns);


    default Children orderByDesc(KProperty<?>... columns) {
        return orderByDesc(true, null, columns);
    }

    default Children orderByDesc(String alias, KProperty<?>... columns) {
        return orderByDesc(true, alias, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: orderByDesc("id", "name")</p>
     *
     * @param condition 执行条件
     * @param columns   字段数组
     * @return children
     */
    default Children orderByDesc(boolean condition, KProperty<?>... columns) {
        return orderBy(condition, false, null, columns);
    }

    default Children orderByDesc(boolean condition, String alias, KProperty<?>... columns) {
        return orderBy(condition, false, alias, columns);
    }

    default Children orderBy(boolean condition, boolean isAsc, KProperty<?>... columns) {
        return orderBy(condition, isAsc, null, columns);
    }

    /**
     * 排序：ORDER BY 字段, ...
     * <p>例: orderBy(true, "id", "name")</p>
     *
     * @param condition 执行条件
     * @param isAsc     是否是 ASC 排序
     * @param columns   字段数组
     * @return children
     */
    Children orderBy(boolean condition, boolean isAsc, String alias, KProperty<?>... columns);


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
    Children func(boolean condition, Consumer<Children> consumer);
}
