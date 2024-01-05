package com.github.yulichang.wrapper.interfaces;

import com.github.yulichang.wrapper.enums.IfPresentSqlKeyWordEnum;

import java.util.function.BiPredicate;

/**
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareStrIfPresent<Children, R> extends CompareStr<Children, R> {

    BiPredicate<Object, IfPresentSqlKeyWordEnum> getIfPresent();

    default Children eqIfPresent(R column, Object val) {
        return eq(getIfPresent().test(val, IfPresentSqlKeyWordEnum.EQ), column, val);
    }

    default Children neIfPresent(R column, Object val) {
        return ne(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NE), column, val);
    }

    default Children gtIfPresent(R column, Object val) {
        return gt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GT), column, val);
    }

    default Children geIfPresent(R column, Object val) {
        return ge(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GE), column, val);
    }

    default Children ltIfPresent(R column, Object val) {
        return lt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LT), column, val);
    }

    default Children leIfPresent(R column, Object val) {
        return le(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LE), column, val);
    }

    default Children likeIfPresent(R column, Object val) {
        return like(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE), column, val);
    }

    default Children notLikeIfPresent(R column, Object val) {
        return notLike(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE), column, val);
    }

    default Children likeLeftIfPresent(R column, Object val) {
        return likeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_LEFT), column, val);
    }

    default Children likeRightIfPresent(R column, Object val) {
        return likeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_RIGHT), column, val);
    }

    default Children notLikeLeftIfPresent(R column, Object val) {
        return notLikeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_LEFT), column, val);
    }

    default Children notLikeRightIfPresent(R column, Object val) {
        return notLikeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_RIGHT), column, val);
    }
}
