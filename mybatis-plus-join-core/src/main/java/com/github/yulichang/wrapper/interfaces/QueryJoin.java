package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.query.interfaces.StringJoin;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.wrapper.JoinAbstractLambdaWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;

import java.util.function.BiConsumer;

/**
 * @author yulichang
 */
public interface QueryJoin<Children, Entity> extends MPJBaseJoin<Entity>, StringJoin<Children, Entity> {

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
     * @param clazz      关联的实体类
     * @param left       条件
     * @param rightAlias 条件字段别名
     * @param right      条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.LEFT_JOIN, clazz, left, rightAlias, right);
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件`
     */
    default <T> Children leftJoin(Class<T> clazz, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.LEFT_JOIN, clazz, function);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.LEFT_JOIN, clazz, left, right, ext);
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param consumer 条件
     */
    default <T> Children leftJoin(Class<T> clazz, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.LEFT_JOIN, clazz, consumer);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, String alias, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(Constant.LEFT_JOIN, clazz, alias, left, right);
    }

    /**
     * left join
     *
     * @param clazz      关联的实体类
     * @param rightAlias 条件字段别名
     * @param left       条件
     * @param right      条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, String alias, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.LEFT_JOIN, clazz, alias, left, rightAlias, right);
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default <T> Children leftJoin(Class<T> clazz, String alias, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.LEFT_JOIN, clazz, alias, function);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, String alias, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.LEFT_JOIN, clazz, alias, left, right, ext);
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param consumer 条件
     */
    default <T> Children leftJoin(Class<T> clazz, String alias, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.LEFT_JOIN, clazz, alias, consumer);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(Constant.LEFT_JOIN, clazz, table, left, right);
    }

    /**
     * left join
     *
     * @param clazz      关联的实体类
     * @param left       条件
     * @param rightAlias 条件字段别名
     * @param right      条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.LEFT_JOIN, clazz, table, left, rightAlias, right);
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件`
     */
    default <T> Children leftJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.LEFT_JOIN, clazz, table, function);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.LEFT_JOIN, clazz, table, left, right, ext);
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param consumer 条件
     */
    default <T> Children leftJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.LEFT_JOIN, clazz, table, consumer);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(Constant.LEFT_JOIN, clazz, table, alias, left, right);
    }

    /**
     * left join
     *
     * @param clazz      关联的实体类
     * @param rightAlias 条件字段别名
     * @param left       条件
     * @param right      条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.LEFT_JOIN, clazz, table, alias, left, rightAlias, right);
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default <T> Children leftJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.LEFT_JOIN, clazz, table, alias, function);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.LEFT_JOIN, clazz, table, alias, left, right, ext);
    }

    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param consumer 条件
     */
    default <T> Children leftJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.LEFT_JOIN, clazz, table, alias, consumer);
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
    default <T, X> Children rightJoin(Class<T> clazz, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.RIGHT_JOIN, clazz, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.RIGHT_JOIN, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.RIGHT_JOIN, clazz, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.RIGHT_JOIN, clazz, consumer);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, String alias, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(Constant.RIGHT_JOIN, clazz, alias, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, String alias, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.RIGHT_JOIN, clazz, alias, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, String alias, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.RIGHT_JOIN, clazz, alias, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, String alias, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.RIGHT_JOIN, clazz, alias, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, String alias, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.RIGHT_JOIN, clazz, alias, consumer);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(Constant.RIGHT_JOIN, clazz, table, left, right);

    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.RIGHT_JOIN, clazz, table, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.RIGHT_JOIN, clazz, table, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.RIGHT_JOIN, clazz, table, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.RIGHT_JOIN, clazz, table, consumer);
    }


    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(Constant.RIGHT_JOIN, clazz, table, alias, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.RIGHT_JOIN, clazz, table, alias, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.RIGHT_JOIN, clazz, table, alias, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.RIGHT_JOIN, clazz, table, alias, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.RIGHT_JOIN, clazz, table, alias, consumer);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(Constant.INNER_JOIN, clazz, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.INNER_JOIN, clazz, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.INNER_JOIN, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.INNER_JOIN, clazz, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.INNER_JOIN, clazz, consumer);
    }


    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, String alias, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(Constant.INNER_JOIN, clazz, alias, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, String alias, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.INNER_JOIN, clazz, alias, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, String alias, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.INNER_JOIN, clazz, alias, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, String alias, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.INNER_JOIN, clazz, alias, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, String alias, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.INNER_JOIN, clazz, alias, consumer);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(Constant.INNER_JOIN, clazz, table, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.INNER_JOIN, clazz, table, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.INNER_JOIN, clazz, table, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.INNER_JOIN, clazz, table, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.INNER_JOIN, clazz, table, consumer);
    }


    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(Constant.INNER_JOIN, clazz, table, alias, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(Constant.INNER_JOIN, clazz, table, alias, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(Constant.INNER_JOIN, clazz, table, alias, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(Constant.INNER_JOIN, clazz, table, alias, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(Constant.INNER_JOIN, clazz, table, alias, consumer);
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
    default <T, X> Children fullJoin(Class<T> clazz, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {
        return join(Constant.FULL_JOIN, clazz, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children fullJoin(Class<T> clazz, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {
        return join(Constant.FULL_JOIN, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {
        return join(Constant.FULL_JOIN, clazz, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children fullJoin(Class<T> clazz, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {
        return join(Constant.FULL_JOIN, clazz, consumer);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(Class<T> clazz, String alias, SFunction<T, ?> left, SFunction<X, ?> right) {
        return join(Constant.FULL_JOIN, clazz, alias, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(Class<T> clazz, String alias, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {
        return join(Constant.FULL_JOIN, clazz, alias, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children fullJoin(Class<T> clazz, String alias, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {
        return join(Constant.FULL_JOIN, clazz, alias, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children fullJoin(Class<T> clazz, String alias, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {
        return join(Constant.FULL_JOIN, clazz, alias, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children fullJoin(Class<T> clazz, String alias, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {
        return join(Constant.FULL_JOIN, clazz, alias, consumer);
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

        return join(keyWord, clazz, null, null, (on, e) -> on.eq(left, right));
    }

    /**
     * 自定义连表关键词
     * <p>
     * 查询基类 可以直接调用此方法实现以上所有功能
     *
     * @param keyWord 连表关键字
     * @param clazz   连表实体类
     * @param left    关联条件
     * @param right   扩展 用于关联表的 select 和 where
     */
    default <T, X> Children join(String keyWord, Class<T> clazz, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(keyWord, clazz, null, null, (on, e) -> on.eq(left, rightAlias, right));
    }

    /**
     * 自定义连表关键词
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default <T> Children join(String keyWord, Class<T> clazz, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(keyWord, clazz, null, null, (on, e) -> function.apply(on));
    }

    /**
     * 自定义连表关键词
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children join(String keyWord, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(keyWord, clazz, null, null, (on, e) -> {
            on.eq(left, right);
            ext.apply(e);
        });
    }

    /**
     * 内部使用, 不建议直接调用
     */
    default <T> Children join(String keyWord, Class<T> clazz, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(keyWord, clazz, null, null, consumer);
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
    default <T, X> Children join(String keyWord, Class<T> clazz, String alias, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(keyWord, clazz, null, alias, (on, e) -> on.eq(left, right));
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
    default <T, X> Children join(String keyWord, Class<T> clazz, String alias, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(keyWord, clazz, null, alias, (on, e) -> on.eq(left, rightAlias, right));
    }

    /**
     * 自定义连表关键词
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default <T> Children join(String keyWord, Class<T> clazz, String alias, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(keyWord, clazz, null, alias, (on, e) -> function.apply(on));
    }

    /**
     * 自定义连表关键词
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children join(String keyWord, Class<T> clazz, String alias, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(keyWord, clazz, null, alias, (on, e) -> {
            on.eq(left, right);
            ext.apply(e);
        });
    }

    /**
     * 内部使用, 不建议直接调用
     */
    default <T> Children join(String keyWord, Class<T> clazz, String alias, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(keyWord, clazz, null, alias, consumer);
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
    default <T, X> Children join(String keyWord, Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(keyWord, clazz, table, null, (on, e) -> on.eq(left, right));
    }

    /**
     * 自定义连表关键词
     * <p>
     * 查询基类 可以直接调用此方法实现以上所有功能
     *
     * @param keyWord 连表关键字
     * @param clazz   连表实体类
     * @param left    关联条件
     * @param right   扩展 用于关联表的 select 和 where
     */
    default <T, X> Children join(String keyWord, Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(keyWord, clazz, table, null, (on, e) -> on.eq(left, rightAlias, right));
    }

    /**
     * 自定义连表关键词
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default <T> Children join(String keyWord, Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(keyWord, clazz, table, null, (on, e) -> function.apply(on));
    }

    /**
     * 自定义连表关键词
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children join(String keyWord, Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(keyWord, clazz, table, null, (on, e) -> {
            on.eq(left, right);
            ext.apply(e);
        });
    }

    /**
     * 内部使用, 不建议直接调用
     */
    default <T> Children join(String keyWord, Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer) {

        return join(keyWord, clazz, table, null, consumer);
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
    default <T, X> Children join(String keyWord, Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, SFunction<X, ?> right) {

        return join(keyWord, clazz, table, alias, (on, e) -> on.eq(left, right));
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
    default <T, X> Children join(String keyWord, Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, String rightAlias, SFunction<X, ?> right) {

        return join(keyWord, clazz, table, alias, (on, e) -> on.eq(left, rightAlias, right));
    }

    /**
     * 自定义连表关键词
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default <T> Children join(String keyWord, Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, MFunction<JoinAbstractLambdaWrapper<Entity, ?>> function) {

        return join(keyWord, clazz, table, alias, (on, e) -> function.apply(on));
    }

    /**
     * 自定义连表关键词
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children join(String keyWord, Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, SFunction<T, ?> left, SFunction<X, ?> right, MFunction<Children> ext) {

        return join(keyWord, clazz, table, alias, (on, e) -> {
            on.eq(left, right);
            ext.apply(e);
        });
    }

    /**
     * 内部使用, 不建议直接调用
     */
    <T> Children join(String keyWord, Class<T> clazz, MConsumer<MPJLambdaWrapper<T>> table, String alias, BiConsumer<JoinAbstractLambdaWrapper<Entity, ?>, Children> consumer);
}
