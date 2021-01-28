package com.github.mybatisplus.wrapper.interfaces;

import com.github.mybatisplus.base.MyBaseEntity;
import com.github.mybatisplus.func.MySFunction;
import com.github.mybatisplus.wrapper.MyJoinLambdaQueryWrapper;

import java.util.function.Function;

/**
 * @author yulichang
 */
public interface MyJoin<Children, T extends MyBaseEntity> {


    <R extends MyBaseEntity, TE, RE> Children leftJoin(boolean condition,
                                                       String alias,
                                                       MySFunction<T, TE> leftCondition,
                                                       MySFunction<R, RE> rightCondition,
                                                       Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper);


    default <R extends MyBaseEntity, TE, RE> Children leftJoin(MySFunction<T, TE> leftCondition,
                                                               MySFunction<R, RE> rightCondition,
                                                               Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return leftJoin(true, null, leftCondition, rightCondition, rightWrapper);
    }

    default <R extends MyBaseEntity, TE, RE> Children leftJoin(String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return leftJoin(true, alias, leftCondition, rightCondition, rightWrapper);
    }

    default <R extends MyBaseEntity, TE, RE> Children leftJoin(boolean condition,
                                                               MySFunction<T, TE> leftCondition,
                                                               MySFunction<R, RE> rightCondition,
                                                               Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return leftJoin(condition, null, leftCondition, rightCondition, rightWrapper);
    }


    <R extends MyBaseEntity, TE, RE> Children rightJoin(boolean condition,
                                                        String alias,
                                                        MySFunction<T, TE> leftCondition,
                                                        MySFunction<R, RE> rightCondition,
                                                        Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper);


    default <R extends MyBaseEntity, TE, RE> Children rightJoin(MySFunction<T, TE> leftCondition,
                                                                MySFunction<R, RE> rightCondition,
                                                                Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return rightJoin(true, null, leftCondition, rightCondition, rightWrapper);
    }

    default <R extends MyBaseEntity, TE, RE> Children rightJoin(String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return rightJoin(true, alias, leftCondition, rightCondition, rightWrapper);
    }

    default <R extends MyBaseEntity, TE, RE> Children rightJoin(boolean condition,
                                                                MySFunction<T, TE> leftCondition,
                                                                MySFunction<R, RE> rightCondition,
                                                                Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return rightJoin(condition, null, leftCondition, rightCondition, rightWrapper);
    }


    <R extends MyBaseEntity, TE, RE> Children innerJoin(boolean condition,
                                                        String alias,
                                                        MySFunction<T, TE> leftCondition,
                                                        MySFunction<R, RE> rightCondition,
                                                        Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper);


    default <R extends MyBaseEntity, TE, RE> Children innerJoin(MySFunction<T, TE> leftCondition,
                                                                MySFunction<R, RE> rightCondition,
                                                                Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return innerJoin(true, null, leftCondition, rightCondition, rightWrapper);
    }

    default <R extends MyBaseEntity, TE, RE> Children innerJoin(String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return innerJoin(true, alias, leftCondition, rightCondition, rightWrapper);
    }

    default <R extends MyBaseEntity, TE, RE> Children innerJoin(boolean condition,
                                                                MySFunction<T, TE> leftCondition,
                                                                MySFunction<R, RE> rightCondition,
                                                                Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return innerJoin(condition, null, leftCondition, rightCondition, rightWrapper);
    }


}
