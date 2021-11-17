package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.wrapper.interfaces.on.OnFunction;

/**
 * @author yulichang
 */
@SuppressWarnings("unused")
public interface LambdaJoin<Children> extends MPJBaseJoin {

    /**
     * left join
     *
     * @param clazz 关联的实体类
     * @param left  条件
     * @param right 条件
     */
    default <T, X> Children leftJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return leftJoin(true, clazz, left, right);
    }

    /**
     * left join
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联的实体类
     * @param function 条件
     */
    default <T> Children leftJoin(Class<T> clazz, OnFunction function) {
        return leftJoin(true, clazz, function);
    }

    /**
     * left join
     *
     * @param condition 是否执行
     * @param clazz     关联的实体类
     * @param left      条件
     * @param right     条件
     */
    default <T, X> Children leftJoin(boolean condition, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return leftJoin(condition, clazz, on -> on.eq(left, right));
    }

    /**
     * left join
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param condition 是否执行
     * @param clazz     关联实体类
     * @param function  条件
     */
    default <T> Children leftJoin(boolean condition, Class<T> clazz, OnFunction function) {
        return join(Constant.LEFT_JOIN, condition, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return rightJoin(true, clazz, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(Class<T> clazz, OnFunction function) {
        return rightJoin(true, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children rightJoin(boolean condition, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return rightJoin(condition, clazz, on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(boolean condition, Class<T> clazz, OnFunction function) {
        return join(Constant.RIGHT_JOIN, condition, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return innerJoin(true, clazz, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(Class<T> clazz, OnFunction function) {
        return innerJoin(true, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T, X> Children innerJoin(boolean condition, Class<T> clazz, SFunction<T, ?> left, SFunction<X, ?> right) {
        return innerJoin(condition, clazz, on -> on.eq(left, right));
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(boolean condition, Class<T> clazz, OnFunction function) {
        return join(Constant.INNER_JOIN, condition, clazz, function);
    }

    /**
     * 查询基类 可以直接调用此方法实现以上所有功能
     *
     * @param keyWord   连表关键字
     * @param condition 是否执行
     * @param clazz     连表实体类
     * @param function  关联条件
     */
    <T> Children join(String keyWord, boolean condition, Class<T> clazz, OnFunction function);
}
