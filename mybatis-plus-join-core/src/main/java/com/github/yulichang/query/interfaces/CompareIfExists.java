package com.github.yulichang.query.interfaces;

import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.github.yulichang.wrapper.enums.IfExistsSqlKeyWordEnum;

import java.util.function.BiPredicate;

/**
 * 查询条件封装
 * <p>比较值</p>
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareIfExists<Children, R> extends Compare<Children, R> {

    BiPredicate<Object, IfExistsSqlKeyWordEnum> getIfExists();

    /**
     * 等于 =
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children eqIfExists(R column, Object val) {
        return eq(getIfExists().test(val, IfExistsSqlKeyWordEnum.EQ), column, val);
    }

    /**
     * 不等于 &lt;&gt;
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children neIfExists(R column, Object val) {
        return ne(getIfExists().test(val, IfExistsSqlKeyWordEnum.NE), column, val);
    }

    /**
     * 大于 &gt;
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children gtIfExists(R column, Object val) {
        return gt(getIfExists().test(val, IfExistsSqlKeyWordEnum.GT), column, val);
    }

    /**
     * 大于等于 &gt;=
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children geIfExists(R column, Object val) {
        return ge(getIfExists().test(val, IfExistsSqlKeyWordEnum.GE), column, val);
    }

    /**
     * 小于 &lt;
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children ltIfExists(R column, Object val) {
        return lt(getIfExists().test(val, IfExistsSqlKeyWordEnum.LT), column, val);
    }

    /**
     * 小于等于 &lt;=
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children leIfExists(R column, Object val) {
        return le(getIfExists().test(val, IfExistsSqlKeyWordEnum.LE), column, val);
    }

    /**
     * LIKE '%值%'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children likeIfExists(R column, Object val) {
        return like(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE), column, val);
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children notLikeIfExists(R column, Object val) {
        return notLike(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE), column, val);
    }

    /**
     * NOT LIKE '%值'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children notLikeLeftIfExists(R column, Object val) {
        return notLikeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_LEFT), column, val);
    }

    /**
     * NOT LIKE '值%'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children notLikeRightIfExists(R column, Object val) {
        return notLikeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_RIGHT), column, val);
    }

    /**
     * LIKE '%值'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children likeLeftIfExists(R column, Object val) {
        return likeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_LEFT), column, val);
    }

    /**
     * LIKE '值%'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children likeRightIfExists(R column, Object val) {
        return likeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_RIGHT), column, val);
    }
}
