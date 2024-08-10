package com.github.yulichang.extension.apt.interfaces;

import com.github.yulichang.apt.BaseColumn;
import com.github.yulichang.apt.Column;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.query.interfaces.StringJoin;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.extension.apt.AptAbstractWrapper;
import com.github.yulichang.wrapper.interfaces.MFunction;

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
    default <T> Children leftJoin(BaseColumn<T> clazz, Column left, Column right) {
        return join(Constant.LEFT_JOIN, clazz, left, right);
    }


    /**
     * left join 多条件
     * <p>
     * 例 leftJoin(UserDO.class, on -> on.eq(UserDO::getId,UserAddressDO::getUserId).le().gt()...)
     *
     * @param clazz    关联实体类
     * @param function 条件`
     */
    default <T> Children leftJoin(BaseColumn<T> clazz, MFunction<AptAbstractWrapper<Entity, ?>> function) {
        return join(Constant.LEFT_JOIN, clazz, function);
    }


    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(BaseColumn<T> clazz, Column left, Column right) {
        return join(Constant.RIGHT_JOIN, clazz, left, right);

    }

    /**
     * ignore 参考 left join
     */
    default <T> Children rightJoin(BaseColumn<T> clazz, MFunction<AptAbstractWrapper<Entity, ?>> function) {
        return join(Constant.RIGHT_JOIN, clazz, function);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(BaseColumn<T> clazz, Column left, Column right) {
        return join(Constant.INNER_JOIN, clazz, left, right);
    }

    /**
     * ignore 参考 left join
     */
    default <T> Children innerJoin(BaseColumn<T> clazz, MFunction<AptAbstractWrapper<Entity, ?>> function) {
        return join(Constant.INNER_JOIN, clazz, function);
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
    default <T> Children join(String keyWord, BaseColumn<T> clazz, Column left, Column right) {
        return join(keyWord, clazz, on -> on.eq(left, right));
    }


    /**
     * 内部使用, 不建议直接调用
     */
    <T> Children join(String keyWord, BaseColumn<T> clazz, MFunction<AptAbstractWrapper<Entity, ?>> function);
}
