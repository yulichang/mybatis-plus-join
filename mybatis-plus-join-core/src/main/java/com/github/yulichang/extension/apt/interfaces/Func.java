package com.github.yulichang.extension.apt.interfaces;

import com.github.yulichang.apt.Column;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * 将原来的泛型R改成Column
 * <p>
 * copy {@link com.baomidou.mybatisplus.core.conditions.interfaces.Func}
 */
@SuppressWarnings("unused")
public interface Func<Children> extends FuncLambda<Children> {


    default Children isNull(Column column) {
        return isNull(true, column);
    }

    Children isNull(boolean condition, Column column);


    default Children isNotNull(Column column) {
        return isNotNull(true, column);
    }

    Children isNotNull(boolean condition, Column column);


    default Children in(Column column, Collection<?> coll) {
        return in(true, column, coll);
    }

    Children in(boolean condition, Column column, Collection<?> coll);


    default Children in(Column column, Object... values) {
        return in(true, column, values);
    }

    Children in(boolean condition, Column column, Object... values);


    default Children notIn(Column column, Collection<?> coll) {
        return notIn(true, column, coll);
    }

    Children notIn(boolean condition, Column column, Collection<?> coll);


    default Children notIn(Column column, Object... values) {
        return notIn(true, column, values);
    }

    Children notIn(boolean condition, Column column, Object... values);


    default Children inSql(Column column, String inValue) {
        return inSql(true, column, inValue);
    }

    Children inSql(boolean condition, Column column, String inValue);

    default Children notInSql(Column column, String inValue) {
        return notInSql(true, column, inValue);
    }

    Children notInSql(boolean condition, Column column, String inValue);

    default Children gtSql(Column column, String inValue) {
        return gtSql(true, column, inValue);
    }

    Children gtSql(boolean condition, Column column, String inValue);

    default Children geSql(Column column, String inValue) {
        return geSql(true, column, inValue);
    }

    Children geSql(boolean condition, Column column, String inValue);

    default Children ltSql(Column column, String inValue) {
        return ltSql(true, column, inValue);
    }

    Children ltSql(boolean condition, Column column, String inValue);

    default Children leSql(Column column, String inValue) {
        return leSql(true, column, inValue);
    }

    Children leSql(boolean condition, Column column, String inValue);

    default Children eqSql(Column column, String inValue) {
        return eqSql(true, column, inValue);
    }

    Children eqSql(boolean condition, Column column, String inValue);

    default Children groupBy(Column column) {
        return groupBy(true, column);
    }

    default Children groupBy(List<Column> column) {
        return groupBy(true, column);
    }

    Children groupBy(boolean condition, List<Column> columns);

    default Children groupBy(Column column, Column... columns) {
        return groupBy(true, column, columns);
    }

    Children groupBy(boolean condition, Column column, Column... columns);


    default Children orderByAsc(Column column) {
        return orderByAsc(true, column);
    }

    default Children orderByAsc(List<Column> columns) {
        return orderByAsc(true, columns);
    }

    Children orderByAsc(boolean condition, List<Column> columns);


    default Children orderByAsc(Column column, Column... columns) {
        return orderByAsc(true, column, columns);
    }

    default Children orderByAsc(boolean condition, Column column, Column... columns) {
        return orderBy(condition, true, column, columns);
    }

    default Children orderByDesc(Column column) {
        return orderByDesc(true, column);
    }

    default Children orderByDesc(List<Column> columns) {
        return orderByDesc(true, columns);
    }

    Children orderByDesc(boolean condition, List<Column> columns);

    default Children orderByDesc(Column column, Column... columns) {
        return orderByDesc(true, column, columns);
    }

    default Children orderByDesc(boolean condition, Column column, Column... columns) {
        return orderBy(condition, false, column, columns);
    }

    Children orderBy(boolean condition, boolean isAsc, Column column, Column... columns);

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
