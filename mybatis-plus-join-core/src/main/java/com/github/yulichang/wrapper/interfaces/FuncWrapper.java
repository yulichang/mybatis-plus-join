package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.io.Serializable;

/**
 * 将原来的泛型R改成SFunction<R, ?>
 * <p>
 * copy {@link com.baomidou.mybatisplus.core.conditions.interfaces.Func}
 */
@SuppressWarnings({"unused"})
public interface FuncWrapper<Children> extends Serializable {


    default <T> Children isNull(Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return isNull(true, null, clazz, wrapper);
    }

    default <T> Children isNull(String alias, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return isNull(true, alias, clazz, wrapper);
    }

    default <T> Children isNull(boolean condition, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return isNull(condition, null, clazz, wrapper);
    }

    <T> Children isNull(boolean condition, String alias, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper);


    default <T> Children isNotNull(Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return isNotNull(true, null, clazz, wrapper);
    }

    default <T> Children isNotNull(String alias, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return isNotNull(true, alias, clazz, wrapper);
    }

    default <T> Children isNotNull(boolean condition, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return isNotNull(condition, null, clazz, wrapper);
    }

    <T> Children isNotNull(boolean condition, String alias, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper);

    default <R, T> Children in(SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return in(true, null, column, clazz, wrapper);
    }

    default <R, T> Children in(String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return in(true, alias, column, clazz, wrapper);
    }

    default <R, T> Children in(boolean condition, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return in(condition, null, column, clazz, wrapper);
    }

    <R, T> Children in(boolean condition, String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper);


    default <R, T> Children notIn(SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return notIn(true, null, column, clazz, wrapper);
    }

    default <R, T> Children notIn(String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return notIn(true, alias, column, clazz, wrapper);
    }

    default <R, T> Children notIn(boolean condition, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return notIn(condition, null, column, clazz, wrapper);
    }

    <R, T> Children notIn(boolean condition, String alias, SFunction<R, ?> column, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper);

    default <T> Children exists(Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return exists(true, clazz, wrapper);
    }

    <T> Children exists(boolean condition, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper);

    default <T> Children notExists(Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper) {
        return notExists(true, clazz, wrapper);
    }

    <T> Children notExists(boolean condition, Class<T> clazz, MFunction<MPJLambdaWrapper<T>> wrapper);
}
