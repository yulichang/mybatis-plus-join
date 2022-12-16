package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.wrapper.MPJAbstractLambdaWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.function.BiConsumer;

/**
 * @author yulichang
 */
@SuppressWarnings("unused")
public interface QueryJoin<Children, Entity> extends MPJBaseJoin<Entity> {

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.LEFT_JOIN, clazz, left, right);
    }

    /**
     * left join
     *
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.LEFT_JOIN, left, right);
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default <T> Children leftJoin(Class<T> clazz, WrapperFunction<MPJAbstractLambdaWrapper<Entity, ?>> function) {
        return join(Constant.LEFT_JOIN, clazz, function);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<MPJLambdaWrapper<Entity>> ext) {
        return join(Constant.LEFT_JOIN, clazz, left, right, ext);
    }

    /**
     * left join
     *
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<MPJLambdaWrapper<Entity>> ext) {
        return join(Constant.LEFT_JOIN, left, right, ext);
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param consumer 条件
     */
    default <T> Children leftJoin(Class<T> clazz, BiConsumer<MPJAbstractLambdaWrapper<Entity, ?>, MPJLambdaWrapper<Entity>> consumer) {
        return join(Constant.LEFT_JOIN, clazz, consumer);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.RIGHT_JOIN, clazz, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.RIGHT_JOIN, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, WrapperFunction<MPJAbstractLambdaWrapper<Entity, ?>> function) {
        return join(Constant.RIGHT_JOIN, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<MPJLambdaWrapper<Entity>> ext) {
        return join(Constant.RIGHT_JOIN, clazz, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<MPJLambdaWrapper<Entity>> ext) {
        return join(Constant.RIGHT_JOIN, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, BiConsumer<MPJAbstractLambdaWrapper<Entity, ?>, MPJLambdaWrapper<Entity>> consumer) {
        return join(Constant.RIGHT_JOIN, clazz, consumer);
    }


    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.INNER_JOIN, clazz, on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.INNER_JOIN, LambdaUtils.getEntityClass(left), on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, WrapperFunction<MPJAbstractLambdaWrapper<Entity, ?>> function) {
        return join(Constant.INNER_JOIN, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<MPJLambdaWrapper<Entity>> ext) {
        return join(Constant.INNER_JOIN, clazz, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<MPJLambdaWrapper<Entity>> ext) {
        return join(Constant.INNER_JOIN, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, BiConsumer<MPJAbstractLambdaWrapper<Entity, ?>, MPJLambdaWrapper<Entity>> consumer) {
        return join(Constant.INNER_JOIN, clazz, consumer);
    }


    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.FULL_JOIN, clazz, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.FULL_JOIN, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children fullJoin(Class<T> clazz, WrapperFunction<MPJAbstractLambdaWrapper<Entity, ?>> function) {
        return join(Constant.FULL_JOIN, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<MPJLambdaWrapper<Entity>> ext) {
        return join(Constant.FULL_JOIN, clazz, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<MPJLambdaWrapper<Entity>> ext) {
        return join(Constant.FULL_JOIN, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children fullJoin(Class<T> clazz, BiConsumer<MPJAbstractLambdaWrapper<Entity, ?>, MPJLambdaWrapper<Entity>> consumer) {
        return join(Constant.FULL_JOIN, clazz, consumer);
    }

    /**
     * 自定义连表关键词
     * 调用此方法 keyword 前后需要带空格 比如 " LEFT JOIN "  " RIGHT JOIN "
     * <p>
     * 查询基类 可以直接调用此方法实现以上所有功能
     *
     * @param keyWord 连表关键字
     * @param clazz   连表实体类
     * @param left    关联条件
     * @param right   扩展 用于关联表的 select 和 where
     */
    default <T, X> Children join(String keyWord, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(keyWord, clazz, on -> on.eq(left, right));
    }

    /**
     * 自定义连表关键词
     *
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children join(String keyWord, SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(keyWord, LambdaUtils.getEntityClass(left), on -> on.eq(left, right));
    }

    /**
     * 自定义连表关键词
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default <T> Children join(String keyWord, Class<T> clazz, WrapperFunction<MPJAbstractLambdaWrapper<Entity, ?>> function) {
        return join(keyWord, clazz, (on, e) -> function.apply(on));
    }

    /**
     * 自定义连表关键词
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children join(String keyWord, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<MPJLambdaWrapper<Entity>> ext) {
        return join(keyWord, clazz, (on, e) -> {
            on.eq(left, right);
            ext.apply(e);
        });
    }

    /**
     * 自定义连表关键词
     *
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children join(String keyWord, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<MPJLambdaWrapper<Entity>> ext) {
        return join(keyWord, LambdaUtils.getEntityClass(left), left, right, ext);
    }

    /**
     * 内部使用, 不建议直接调用
     */
    <T> Children join(String keyWord, Class<T> clazz, BiConsumer<MPJAbstractLambdaWrapper<Entity, ?>, MPJLambdaWrapper<Entity>> consumer);
}
