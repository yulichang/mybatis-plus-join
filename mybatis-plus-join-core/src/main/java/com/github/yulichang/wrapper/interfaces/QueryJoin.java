package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.wrapper.interfaces.on.WrapperFunction;

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
        return leftJoin(clazz, on -> on.eq(left, right));
    }

    /**
     * left join
     *
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(SFunction<T, ?> left, SFunction<X, ?> right) {
        return leftJoin(LambdaUtils.getEntityClass(left), on -> on.eq(left, right));
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default <T> Children leftJoin(Class<T> clazz, WrapperFunction<Entity> function) {
        return join(Constant.LEFT_JOIN, clazz, function, null);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<Entity> ext) {
        return leftJoin(clazz, on -> on.eq(left, right), ext);
    }

    /**
     * left join
     *
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<Entity> ext) {
        return leftJoin(LambdaUtils.getEntityClass(left), on -> on.eq(left, right), ext);
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default <T> Children leftJoin(Class<T> clazz, WrapperFunction<Entity> function, WrapperFunction<Entity> ext) {
        return join(Constant.LEFT_JOIN, clazz, function, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return rightJoin(clazz, on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(SFunction<T, ?> left, SFunction<X, ?> right) {
        return rightJoin(LambdaUtils.getEntityClass(left), on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, WrapperFunction<Entity> function) {
        return join(Constant.RIGHT_JOIN, clazz, function, null);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<Entity> ext) {
        return rightJoin(clazz, on -> on.eq(left, right), ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<Entity> ext) {
        return rightJoin(LambdaUtils.getEntityClass(left), on -> on.eq(left, right), ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, WrapperFunction<Entity> function, WrapperFunction<Entity> ext) {
        return join(Constant.RIGHT_JOIN, clazz, function, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return innerJoin(clazz, on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(SFunction<T, ?> left, SFunction<X, ?> right) {
        return innerJoin(LambdaUtils.getEntityClass(left), on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, WrapperFunction<Entity> function) {
        return join(Constant.INNER_JOIN, clazz, function, null);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<Entity> ext) {
        return innerJoin(clazz, on -> on.eq(left, right), ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<Entity> ext) {
        return innerJoin(LambdaUtils.getEntityClass(left), on -> on.eq(left, right), ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, WrapperFunction<Entity> function, WrapperFunction<Entity> ext) {
        return join(Constant.INNER_JOIN, clazz, function, ext);
    }


    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return fullJoin(clazz, on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(SFunction<T, ?> left, SFunction<X, ?> right) {
        return fullJoin(LambdaUtils.getEntityClass(left), on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children fullJoin(Class<T> clazz, WrapperFunction<Entity> function) {
        return join(Constant.FULL_JOIN, clazz, function, null);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<Entity> ext) {
        return fullJoin(clazz, on -> on.eq(left, right), ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<Entity> ext) {
        return fullJoin(LambdaUtils.getEntityClass(left), on -> on.eq(left, right), ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children fullJoin(Class<T> clazz, WrapperFunction<Entity> function, WrapperFunction<Entity> ext) {
        return join(Constant.FULL_JOIN, clazz, function, ext);
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
    default <T> Children join(String keyWord, Class<T> clazz, WrapperFunction<Entity> function) {
        return join(keyWord, clazz, function, null);
    }

    /**
     * 自定义连表关键词
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children join(String keyWord, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<Entity> ext) {
        return join(keyWord, clazz, on -> on.eq(left, right), ext);
    }

    /**
     * 自定义连表关键词
     *
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children join(String keyWord, SFunction<T, ?> left, SFunction<X, ?> right, WrapperFunction<Entity> ext) {
        return join(keyWord, LambdaUtils.getEntityClass(left), on -> on.eq(left, right), ext);
    }

    /**
     * 内部使用, 不建议直接调用
     */
    <T> Children join(String keyWord, Class<T> clazz, WrapperFunction<Entity> function, WrapperFunction<Entity> ext);
}
