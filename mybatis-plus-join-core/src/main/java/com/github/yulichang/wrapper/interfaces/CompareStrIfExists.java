package com.github.yulichang.wrapper.interfaces;

import com.github.yulichang.wrapper.enums.IfExistsSqlKeyWordEnum;

import java.util.function.BiPredicate;

/**
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareStrIfExists<Children, R> extends CompareStr<Children, R> {

    BiPredicate<Object, IfExistsSqlKeyWordEnum> getIfExists();

    default Children eqIfExists(R column, Object val) {
        return eq(getIfExists().test(val, IfExistsSqlKeyWordEnum.EQ), column, val);
    }

    default Children neIfExists(R column, Object val) {
        return ne(getIfExists().test(val, IfExistsSqlKeyWordEnum.NE), column, val);
    }

    default Children gtIfExists(R column, Object val) {
        return gt(getIfExists().test(val, IfExistsSqlKeyWordEnum.GT), column, val);
    }

    default Children geIfExists(R column, Object val) {
        return ge(getIfExists().test(val, IfExistsSqlKeyWordEnum.GE), column, val);
    }

    default Children ltIfExists(R column, Object val) {
        return lt(getIfExists().test(val, IfExistsSqlKeyWordEnum.LT), column, val);
    }

    default Children leIfExists(R column, Object val) {
        return le(getIfExists().test(val, IfExistsSqlKeyWordEnum.LE), column, val);
    }

    default Children likeIfExists(R column, Object val) {
        return like(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE), column, val);
    }

    default Children notLikeIfExists(R column, Object val) {
        return notLike(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE), column, val);
    }

    default Children likeLeftIfExists(R column, Object val) {
        return likeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_LEFT), column, val);
    }

    default Children likeRightIfExists(R column, Object val) {
        return likeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_RIGHT), column, val);
    }

    default Children notLikeLeftIfExists(R column, Object val) {
        return notLikeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_LEFT), column, val);
    }

    default Children notLikeRightIfExists(R column, Object val) {
        return notLikeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_RIGHT), column, val);
    }
}
