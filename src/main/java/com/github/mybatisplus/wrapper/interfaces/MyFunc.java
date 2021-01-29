package com.github.mybatisplus.wrapper.interfaces;

import com.github.mybatisplus.func.MySFunction;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.interfaces.Func}
 */
@SuppressWarnings("unchecked")
public interface MyFunc<Children> extends Serializable {

    default <E, F> Children isNull(MySFunction<E, F> column) {
        return isNull(true, null, column);
    }

    default <E, F> Children isNull(boolean condition, MySFunction<E, F> column) {
        return isNull(condition, null, column);
    }

    default <E, F> Children isNull(String alias, MySFunction<E, F> column) {
        return isNull(true, alias, column);
    }

    <E, F> Children isNull(boolean condition, String alias, MySFunction<E, F> column);


    default <E, F> Children isNotNull(MySFunction<E, F> column) {
        return isNotNull(true, column);
    }

    default <E, F> Children isNotNull(boolean condition, MySFunction<E, F> column) {
        return isNotNull(condition, null, column);
    }

    default <E, F> Children isNotNull(String alias, MySFunction<E, F> column) {
        return isNotNull(true, alias, column);
    }

    <E, F> Children isNotNull(boolean condition, String alias, MySFunction<E, F> column);

    default <E, F> Children in(MySFunction<E, F> column, Collection<?> coll) {
        return in(true, null, column, coll);
    }

    default <E, F> Children in(boolean condition, MySFunction<E, F> column, Collection<?> coll) {
        return in(condition, null, column, coll);
    }

    default <E, F> Children in(String alias, MySFunction<E, F> column, Collection<?> coll) {
        return in(true, alias, column, coll);
    }

    <E, F> Children in(boolean condition, String alias, MySFunction<E, F> column, Collection<?> coll);

    default <E, F> Children in(MySFunction<E, F> column, Object... values) {
        return in(true, null, column, values);
    }

    default <E, F> Children in(boolean condition, MySFunction<E, F> column, Object... values) {
        return in(condition, null, column, values);
    }

    default <E, F> Children in(String alias, MySFunction<E, F> column, Object... values) {
        return in(true, alias, column, values);
    }

    default <E, F> Children in(boolean condition, String alias, MySFunction<E, F> column, Object... values) {
        return in(condition, alias, column, Arrays.stream(Optional.ofNullable(values).orElseGet(() -> new Object[]{}))
                .collect(toList()));
    }

    default <E, F> Children notIn(MySFunction<E, F> column, Collection<?> coll) {
        return notIn(true, null, column, coll);
    }

    default <E, F> Children notIn(boolean condition, MySFunction<E, F> column, Collection<?> coll) {
        return notIn(condition, null, column, coll);
    }

    default <E, F> Children notIn(String alias, MySFunction<E, F> column, Collection<?> coll) {
        return notIn(true, alias, column, coll);
    }

    <E, F> Children notIn(boolean condition, String alias, MySFunction<E, F> column, Collection<?> coll);

    default <E, F> Children notIn(MySFunction<E, F> column, Object... value) {
        return notIn(true, null, column, value);
    }

    default <E, F> Children notIn(boolean condition, MySFunction<E, F> column, Object... value) {
        return notIn(condition, null, column, value);
    }

    default <E, F> Children notIn(String alias, MySFunction<E, F> column, Object... value) {
        return notIn(true, alias, column, value);
    }

    default <E, F> Children notIn(boolean condition, String alias, MySFunction<E, F> column, Object... values) {
        return notIn(condition, alias, column, Arrays.stream(Optional.ofNullable(values).orElseGet(() -> new Object[]{}))
                .collect(toList()));
    }

    default <E, F> Children inSql(MySFunction<E, F> column, String inValue) {
        return inSql(true, null, column, inValue);
    }

    default <E, F> Children inSql(boolean condition, MySFunction<E, F> column, String inValue) {
        return inSql(condition, null, column, inValue);
    }

    default <E, F> Children inSql(String alias, MySFunction<E, F> column, String inValue) {
        return inSql(true, alias, column, inValue);
    }

    <E, F> Children inSql(boolean condition, String alias, MySFunction<E, F> column, String inValue);

    default <E, F> Children notInSql(MySFunction<E, F> column, String inValue) {
        return notInSql(true, null, column, inValue);
    }

    default <E, F> Children notInSql(boolean condition, MySFunction<E, F> column, String inValue) {
        return notInSql(condition, null, column, inValue);
    }

    default <E, F> Children notInSql(String alias, MySFunction<E, F> column, String inValue) {
        return notInSql(true, alias, column, inValue);
    }

    <E, F> Children notInSql(boolean condition, String alias, MySFunction<E, F> column, String inValue);

    default <E, F> Children groupBy(MySFunction<E, F>... columns) {
        return groupBy(true, null, columns);
    }

    default <E, F> Children groupBy(boolean condition, MySFunction<E, F>... columns) {
        return groupBy(condition, null, columns);
    }

    default <E, F> Children groupBy(String alias, MySFunction<E, F>... columns) {
        return groupBy(true, alias, columns);
    }

    <E, F> Children groupBy(boolean condition, String alias, MySFunction<E, F>... columns);

    default <E, F> Children orderByAsc(MySFunction<E, F>... columns) {
        return orderByAsc(true, null, columns);
    }

    default <E, F> Children orderByAsc(boolean condition, MySFunction<E, F>... columns) {
        return orderByAsc(condition, null, columns);
    }

    default <E, F> Children orderByAsc(String alias, MySFunction<E, F>... columns) {
        return orderByAsc(true, alias, columns);
    }

    default <E, F> Children orderByAsc(boolean condition, String alias, MySFunction<E, F>... columns) {
        return orderBy(condition, alias, true, columns);
    }

    default <E, F> Children orderByDesc(MySFunction<E, F>... columns) {
        return orderByDesc(true, null, columns);
    }

    default <E, F> Children orderByDesc(boolean condition, MySFunction<E, F>... columns) {
        return orderByDesc(condition, null, columns);
    }

    default <E, F> Children orderByDesc(String alias, MySFunction<E, F>... columns) {
        return orderByDesc(true, alias, columns);
    }

    default <E, F> Children orderByDesc(boolean condition, String alias, MySFunction<E, F>... columns) {
        return orderBy(condition, alias, false, columns);
    }

    <E, F> Children orderBy(boolean condition, String alias, boolean isAsc, MySFunction<E, F>... columns);

    default <E, F> Children having(String sqlHaving, Object... params) {
        return having(true, null, sqlHaving, params);
    }

    default <E, F> Children having(boolean condition, String sqlHaving, Object... params) {
        return having(condition, null, sqlHaving, params);
    }

    default <E, F> Children having(String alias, String sqlHaving, Object... params) {
        return having(true, alias, sqlHaving, params);
    }

    <E, F> Children having(boolean condition, String alias, String sqlHaving, Object... params);

    default <E, F> Children func(Consumer<Children> consumer) {
        return func(true, null, consumer);
    }

    default <E, F> Children func(boolean condition, Consumer<Children> consumer) {
        return func(condition, null, consumer);
    }

    default <E, F> Children func(String alias, Consumer<Children> consumer) {
        return func(true, alias, consumer);
    }

    <E, F> Children func(boolean condition, String alias, Consumer<Children> consumer);
}
