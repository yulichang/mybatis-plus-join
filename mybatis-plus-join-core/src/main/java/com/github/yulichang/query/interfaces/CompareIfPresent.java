package com.github.yulichang.query.interfaces;

import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.github.yulichang.wrapper.enums.IfPresentSqlKeyWordEnum;

import java.util.function.BiPredicate;

/**
 * 查询条件封装
 * <p>比较值</p>
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareIfPresent<Children, R> extends Compare<Children, R> {

    BiPredicate<Object, IfPresentSqlKeyWordEnum> getIfPresent();

    /**
     * 等于 =
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children eqIfPresent(R column, Object val) {
        return eq(getIfPresent().test(val, IfPresentSqlKeyWordEnum.EQ), column, val);
    }

    /**
     * 不等于 &lt;&gt;
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children neIfPresent(R column, Object val) {
        return ne(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NE), column, val);
    }

    /**
     * 大于 &gt;
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children gtIfPresent(R column, Object val) {
        return gt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GT), column, val);
    }

    /**
     * 大于等于 &gt;=
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children geIfPresent(R column, Object val) {
        return ge(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GE), column, val);
    }

    /**
     * 小于 &lt;
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children ltIfPresent(R column, Object val) {
        return lt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LT), column, val);
    }

    /**
     * 小于等于 &lt;=
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children leIfPresent(R column, Object val) {
        return le(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LE), column, val);
    }

    /**
     * LIKE '%值%'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children likeIfPresent(R column, Object val) {
        return like(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE), column, val);
    }

    /**
     * NOT LIKE '%值%'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children notLikeIfPresent(R column, Object val) {
        return notLike(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE), column, val);
    }

    /**
     * NOT LIKE '%值'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children notLikeLeftIfPresent(R column, Object val) {
        return notLikeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_LEFT), column, val);
    }

    /**
     * NOT LIKE '值%'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children notLikeRightIfPresent(R column, Object val) {
        return notLikeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_RIGHT), column, val);
    }

    /**
     * LIKE '%值'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children likeLeftIfPresent(R column, Object val) {
        return likeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_LEFT), column, val);
    }

    /**
     * LIKE '值%'
     *
     * @param column 字段
     * @param val    值
     * @return children
     */
    default Children likeRightIfPresent(R column, Object val) {
        return likeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_RIGHT), column, val);
    }
}
