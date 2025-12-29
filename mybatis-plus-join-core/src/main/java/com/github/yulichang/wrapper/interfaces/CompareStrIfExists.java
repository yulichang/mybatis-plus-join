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
public interface CompareStrIfExists<Children> extends CompareStr<Children> {

    BiPredicate<Object, IfExistsSqlKeyWordEnum> getIfExists();

    default Children eqIfExists(String column, Object val) {
        return eq(getIfExists().test(val, IfExistsSqlKeyWordEnum.EQ), column, val);
    }

    default Children neIfExists(String column, Object val) {
        return ne(getIfExists().test(val, IfExistsSqlKeyWordEnum.NE), column, val);
    }

    default Children gtIfExists(String column, Object val) {
        return gt(getIfExists().test(val, IfExistsSqlKeyWordEnum.GT), column, val);
    }

    default Children geIfExists(String column, Object val) {
        return ge(getIfExists().test(val, IfExistsSqlKeyWordEnum.GE), column, val);
    }

    default Children ltIfExists(String column, Object val) {
        return lt(getIfExists().test(val, IfExistsSqlKeyWordEnum.LT), column, val);
    }

    default Children leIfExists(String column, Object val) {
        return le(getIfExists().test(val, IfExistsSqlKeyWordEnum.LE), column, val);
    }

    default Children likeIfExists(String column, Object val) {
        return like(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE), column, val);
    }

    default Children notLikeIfExists(String column, Object val) {
        return notLike(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE), column, val);
    }

    default Children likeLeftIfExists(String column, Object val) {
        return likeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_LEFT), column, val);
    }

    default Children likeRightIfExists(String column, Object val) {
        return likeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_RIGHT), column, val);
    }

    default Children notLikeLeftIfExists(String column, Object val) {
        return notLikeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_LEFT), column, val);
    }

    default Children notLikeRightIfExists(String column, Object val) {
        return notLikeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_RIGHT), column, val);
    }

    default Children betweenIfExists(String column, Object val1, Object val2) {
        return between(getIfExists().test(val1, IfExistsSqlKeyWordEnum.BETWEEN_FIRST) &&
                getIfExists().test(val2, IfExistsSqlKeyWordEnum.BETWEEN_SECOND), column, val1, val2);
    }

    default Children notBetweenIfExists(String column, Object val1, Object val2) {
        return notBetween(getIfExists().test(val1, IfExistsSqlKeyWordEnum.NOT_BETWEEN_FIRST) &&
                getIfExists().test(val2, IfExistsSqlKeyWordEnum.NOT_BETWEEN_SECOND), column, val1, val2);
    }
}
