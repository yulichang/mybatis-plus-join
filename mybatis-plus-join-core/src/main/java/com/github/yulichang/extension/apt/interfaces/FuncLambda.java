package com.github.yulichang.extension.apt.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.util.List;

/**
 * 将原来的泛型R改成SFunction<X,?>
 * <p>
 * copy {@link com.baomidou.mybatisplus.core.conditions.interfaces.Func}
 */
@SuppressWarnings({"unused", "unchecked"})
public interface FuncLambda<Children> extends Serializable {

    default <X> Children groupBy(SFunction<X, ?> column) {
        return groupBy(true, column);
    }

    default <X> Children groupByLambda(List<SFunction<X, ?>> column) {
        return groupByLambda(true, column);
    }

    <X> Children groupByLambda(boolean condition, List<SFunction<X, ?>> columns);

    <X> Children groupBy(SFunction<X, ?> column, SFunction<X, ?>... columns);

    <X> Children groupBy(boolean condition, SFunction<X, ?> column, SFunction<X, ?>... columns);


    default <X> Children orderByAsc(SFunction<X, ?> column) {
        return orderByAsc(true, column);
    }

    default <X> Children orderByAscLambda(List<SFunction<X, ?>> columns) {
        return orderByAscLambda(true, columns);
    }

    <X> Children orderByAscLambda(boolean condition, List<SFunction<X, ?>> columns);


    <X> Children orderByAsc(SFunction<X, ?> column, SFunction<X, ?>... columns);

    <X> Children orderByAsc(boolean condition, SFunction<X, ?> column, SFunction<X, ?>... columns);

    default <X> Children orderByDesc(SFunction<X, ?> column) {
        return orderByDesc(true, column);
    }

    default <X> Children orderByDescLambda(List<SFunction<X, ?>> columns) {
        return orderByDescLambda(true, columns);
    }

    <X> Children orderByDescLambda(boolean condition, List<SFunction<X, ?>> columns);

    <X> Children orderByDesc(SFunction<X, ?> column, SFunction<X, ?>... columns);

    <X> Children orderByDesc(boolean condition, SFunction<X, ?> column, SFunction<X, ?>... columns);

    <X> Children orderBy(boolean condition, boolean isAsc, SFunction<X, ?> column, SFunction<X, ?>... columns);
}
