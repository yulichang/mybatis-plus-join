package com.github.yulichang.wrapper.interfaces;

import com.github.yulichang.wrapper.enums.IfAbsentSqlKeyWordEnum;

import java.util.function.BiPredicate;

/**
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareStrIfAbsent<Children, R> extends CompareStr<Children, R> {

    BiPredicate<Object, IfAbsentSqlKeyWordEnum> getIfAbsent();

    default Children eqIfAbsent(R column, Object val) {
        return eq(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.EQ), column, val);
    }

    default Children neIfAbsent(R column, Object val) {
        return ne(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NE), column, val);
    }

    default Children gtIfAbsent(R column, Object val) {
        return gt(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.GT), column, val);
    }

    default Children geIfAbsent(R column, Object val) {
        return ge(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.GE), column, val);
    }

    default Children ltIfAbsent(R column, Object val) {
        return lt(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LT), column, val);
    }

    default Children leIfAbsent(R column, Object val) {
        return le(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LE), column, val);
    }

    default Children likeIfAbsent(R column, Object val) {
        return like(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE), column, val);
    }

    default Children notLikeIfAbsent(R column, Object val) {
        return notLike(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NOT_LIKE), column, val);
    }

    default Children likeLeftIfAbsent(R column, Object val) {
        return likeLeft(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE_LEFT), column, val);
    }

    default Children likeRightIfAbsent(R column, Object val) {
        return likeRight(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE_RIGHT), column, val);
    }

    default Children notLikeLeftIfAbsent(R column, Object val) {
        return notLikeLeft(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NOT_LIKE_LEFT), column, val);
    }

    default Children notLikeRightIfAbsent(R column, Object val) {
        return notLikeRight(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NOT_LIKE_RIGHT), column, val);
    }
}
