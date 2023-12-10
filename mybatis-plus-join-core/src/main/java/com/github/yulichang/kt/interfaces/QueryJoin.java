package com.github.yulichang.kt.interfaces;

import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.kt.KtAbstractLambdaWrapper;
import com.github.yulichang.query.interfaces.StringJoin;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.wrapper.interfaces.WrapperFunction;
import kotlin.reflect.KProperty;

import java.util.function.BiConsumer;

/**
 * @author yulichang
 */
@SuppressWarnings("unused")
public interface QueryJoin<Children, Entity> extends MPJBaseJoin<Entity>, StringJoin<Children, Entity> {

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default Children leftJoin(Class<?> clazz, KProperty<?> left, KProperty<?> right) {
        return join(Constant.LEFT_JOIN, clazz, left, right);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default Children leftJoin(Class<?> clazz, KProperty<?> left, String rightAlias, KProperty<?> right) {
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
    default Children leftJoin(Class<?> clazz, WrapperFunction<KtAbstractLambdaWrapper<?, ?>> function) {
        return join(Constant.LEFT_JOIN, clazz, function);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default Children leftJoin(Class<?> clazz, KProperty<?> left, KProperty<?> right, WrapperFunction<Children> ext) {
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
    default Children leftJoin(Class<?> clazz, BiConsumer<KtAbstractLambdaWrapper<?, ?>, Children> consumer) {
        return join(Constant.LEFT_JOIN, clazz, consumer);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default Children leftJoin(Class<?> clazz, String alias, KProperty<?> left, KProperty<?> right) {
        return join(Constant.LEFT_JOIN, clazz, alias, left, right);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default Children leftJoin(Class<?> clazz, String alias, KProperty<?> left, String rightAlias, KProperty<?> right) {
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
    default Children leftJoin(Class<?> clazz, String alias, WrapperFunction<KtAbstractLambdaWrapper<?, ?>> function) {
        return join(Constant.LEFT_JOIN, clazz, alias, function);
    }

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default Children leftJoin(Class<?> clazz, String alias, KProperty<?> left, KProperty<?> right, WrapperFunction<Children> ext) {
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
    default Children leftJoin(Class<?> clazz, String alias, BiConsumer<KtAbstractLambdaWrapper<?, ?>, Children> consumer) {
        return join(Constant.LEFT_JOIN, clazz, alias, consumer);
    }

    /**
     * ignore 参考 left join
     */
    default Children rightJoin(Class<?> clazz, KProperty<?> left, KProperty<?> right) {
        return join(Constant.RIGHT_JOIN, clazz, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default Children rightJoin(Class<?> clazz, KProperty<?> left, String rightAlias, KProperty<?> right) {
        return join(Constant.RIGHT_JOIN, clazz, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default Children rightJoin(Class<?> clazz, WrapperFunction<KtAbstractLambdaWrapper<?, ?>> function) {
        return join(Constant.RIGHT_JOIN, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default Children rightJoin(Class<?> clazz, KProperty<?> left, KProperty<?> right, WrapperFunction<Children> ext) {
        return join(Constant.RIGHT_JOIN, clazz, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default Children rightJoin(Class<?> clazz, BiConsumer<KtAbstractLambdaWrapper<?, ?>, Children> consumer) {
        return join(Constant.RIGHT_JOIN, clazz, consumer);
    }

    /**
     * ignore 参考 left join
     */
    default Children rightJoin(Class<?> clazz, String alias, KProperty<?> left, KProperty<?> right) {
        return join(Constant.RIGHT_JOIN, clazz, alias, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default Children rightJoin(Class<?> clazz, String alias, KProperty<?> left, String rightAlias, KProperty<?> right) {
        return join(Constant.RIGHT_JOIN, clazz, alias, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default Children rightJoin(Class<?> clazz, String alias, WrapperFunction<KtAbstractLambdaWrapper<?, ?>> function) {
        return join(Constant.RIGHT_JOIN, clazz, alias, function);
    }

    /**
     * ignore 参考 left join
     */
    default Children rightJoin(Class<?> clazz, String alias, KProperty<?> left, KProperty<?> right, WrapperFunction<Children> ext) {
        return join(Constant.RIGHT_JOIN, clazz, alias, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default Children rightJoin(Class<?> clazz, String alias, BiConsumer<KtAbstractLambdaWrapper<?, ?>, Children> consumer) {
        return join(Constant.RIGHT_JOIN, clazz, alias, consumer);
    }


    /**
     * ignore 参考 left join
     */
    default Children innerJoin(Class<?> clazz, KProperty<?> left, KProperty<?> right) {
        return join(Constant.INNER_JOIN, clazz, on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default Children innerJoin(Class<?> clazz, KProperty<?> left, String rightAlias, KProperty<?> right) {
        return join(Constant.INNER_JOIN, clazz, left, rightAlias, right);
    }


    /**
     * ignore 参考 left join
     */
    default Children innerJoin(Class<?> clazz, WrapperFunction<KtAbstractLambdaWrapper<?, ?>> function) {
        return join(Constant.INNER_JOIN, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default Children innerJoin(Class<?> clazz, KProperty<?> left, KProperty<?> right, WrapperFunction<Children> ext) {
        return join(Constant.INNER_JOIN, clazz, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default Children innerJoin(Class<?> clazz, BiConsumer<KtAbstractLambdaWrapper<?, ?>, Children> consumer) {
        return join(Constant.INNER_JOIN, clazz, consumer);
    }


    /**
     * ignore 参考 left join
     */
    default Children innerJoin(Class<?> clazz, String alias, KProperty<?> left, KProperty<?> right) {
        return join(Constant.INNER_JOIN, clazz, alias, on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default Children innerJoin(Class<?> clazz, String alias, KProperty<?> left, String rightAlias, KProperty<?> right) {
        return join(Constant.INNER_JOIN, clazz, alias, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default Children innerJoin(Class<?> clazz, String alias, WrapperFunction<KtAbstractLambdaWrapper<?, ?>> function) {
        return join(Constant.INNER_JOIN, clazz, alias, function);
    }

    /**
     * ignore 参考 left join
     */
    default Children innerJoin(Class<?> clazz, String alias, KProperty<?> left, KProperty<?> right, WrapperFunction<Children> ext) {
        return join(Constant.INNER_JOIN, clazz, alias, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default Children innerJoin(Class<?> clazz, String alias, BiConsumer<KtAbstractLambdaWrapper<?, ?>, Children> consumer) {
        return join(Constant.INNER_JOIN, clazz, alias, consumer);
    }

    /**
     * ignore 参考 left join
     */
    default Children fullJoin(Class<?> clazz, KProperty<?> left, KProperty<?> right) {
        return join(Constant.FULL_JOIN, clazz, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default Children fullJoin(Class<?> clazz, KProperty<?> left, String rightAlias, KProperty<?> right) {
        return join(Constant.FULL_JOIN, clazz, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default Children fullJoin(Class<?> clazz, WrapperFunction<KtAbstractLambdaWrapper<?, ?>> function) {
        return join(Constant.FULL_JOIN, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default Children fullJoin(Class<?> clazz, KProperty<?> left, KProperty<?> right, WrapperFunction<Children> ext) {
        return join(Constant.FULL_JOIN, clazz, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default Children fullJoin(Class<?> clazz, BiConsumer<KtAbstractLambdaWrapper<?, ?>, Children> consumer) {
        return join(Constant.FULL_JOIN, clazz, consumer);
    }

    /**
     * ignore 参考 left join
     */
    default Children fullJoin(Class<?> clazz, String alias, KProperty<?> left, KProperty<?> right) {
        return join(Constant.FULL_JOIN, clazz, alias, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default Children fullJoin(Class<?> clazz, String alias, KProperty<?> left, String rightAlias, KProperty<?> right) {
        return join(Constant.FULL_JOIN, clazz, alias, left, rightAlias, right);
    }

    /**
     * ignore 参考 left join
     */
    default Children fullJoin(Class<?> clazz, String alias, WrapperFunction<KtAbstractLambdaWrapper<?, ?>> function) {
        return join(Constant.FULL_JOIN, clazz, alias, function);
    }

    /**
     * ignore 参考 left join
     */
    default Children fullJoin(Class<?> clazz, String alias, KProperty<?> left, KProperty<?> right, WrapperFunction<Children> ext) {
        return join(Constant.FULL_JOIN, clazz, alias, left, right, ext);
    }

    /**
     * ignore 参考 left join
     */
    default Children fullJoin(Class<?> clazz, String alias, BiConsumer<KtAbstractLambdaWrapper<?, ?>, Children> consumer) {
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
    default Children join(String keyWord, Class<?> clazz, KProperty<?> left, KProperty<?> right) {
        return join(keyWord, clazz, on -> on.eq(left, right));
    }

    /**
     * ignore
     */
    default Children join(String keyWord, Class<?> clazz, KProperty<?> left, String rightAlias, KProperty<?> right) {
        return join(keyWord, clazz, on -> on.eq(left, rightAlias, right));
    }

    /**
     * 自定义连表关键词
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default Children join(String keyWord, Class<?> clazz, WrapperFunction<KtAbstractLambdaWrapper<?, ?>> function) {
        return join(keyWord, clazz, (on, e) -> function.apply(on));
    }

    /**
     * 自定义连表关键词
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default Children join(String keyWord, Class<?> clazz, KProperty<?> left, KProperty<?> right, WrapperFunction<Children> ext) {
        return join(keyWord, clazz, (on, e) -> {
            on.eq(left, right);
            ext.apply(e);
        });
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
    default <T, X> Children join(String keyWord, Class<?> clazz, String alias, KProperty<?> left, KProperty<?> right) {
        return join(keyWord, clazz, alias, on -> on.eq(left, right));
    }

    /**
     * ignore
     */
    default <T, X> Children join(String keyWord, Class<?> clazz, String alias, KProperty<?> left, String rightAlias, KProperty<?> right) {
        return join(keyWord, clazz, alias, on -> on.eq(left, rightAlias, right));
    }

    /**
     * 自定义连表关键词
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件
     */
    default Children join(String keyWord, Class<?> clazz, String alias, WrapperFunction<KtAbstractLambdaWrapper<?, ?>> function) {
        return join(keyWord, clazz, alias, (on, e) -> function.apply(on));
    }

    /**
     * 自定义连表关键词
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default Children join(String keyWord, Class<?> clazz, String alias, KProperty<?> left, KProperty<?> right, WrapperFunction<Children> ext) {
        return join(keyWord, clazz, alias, (on, e) -> {
            on.eq(left, right);
            ext.apply(e);
        });
    }

    /**
     * 内部使用, 不建议直接调用
     */
    default Children join(String keyWord, Class<?> clazz, BiConsumer<KtAbstractLambdaWrapper<?, ?>, Children> consumer) {
        return join(keyWord, clazz, null, consumer);
    }

    /**
     * 内部使用, 不建议直接调用
     */
    Children join(String keyWord, Class<?> clazz, String alias, BiConsumer<KtAbstractLambdaWrapper<?, ?>, Children> consumer);
}
