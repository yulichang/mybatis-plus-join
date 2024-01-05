package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.enums.IfPresentSqlKeyWordEnum;

import java.util.function.BiPredicate;

/**
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareIfPresent<Children> extends Compare<Children> {

    BiPredicate<Object, IfPresentSqlKeyWordEnum> getIfPresent();

    default <R> Children eqIfPresent(SFunction<R, ?> column, Object val) {
        return eq(getIfPresent().test(val, IfPresentSqlKeyWordEnum.EQ), null, column, val);
    }

    default <R> Children eqIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return eq(getIfPresent().test(val, IfPresentSqlKeyWordEnum.EQ), alias, column, val);
    }

    default <R> Children neIfPresent(SFunction<R, ?> column, Object val) {
        return ne(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NE), null, column, val);
    }

    default <R> Children neIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return ne(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NE), alias, column, val);
    }

    default <R> Children gtIfPresent(SFunction<R, ?> column, Object val) {
        return gt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GT), null, column, val);
    }

    default <R> Children gtIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return gt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GT), alias, column, val);
    }

    default <R> Children geIfPresent(SFunction<R, ?> column, Object val) {
        return ge(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GE), null, column, val);
    }

    default <R> Children geIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return ge(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GE), alias, column, val);
    }

    default <R> Children ltIfPresent(SFunction<R, ?> column, Object val) {
        return lt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LT), null, column, val);
    }

    default <R> Children ltIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return lt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LT), alias, column, val);
    }

    default <R> Children leIfPresent(SFunction<R, ?> column, Object val) {
        return le(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LE), null, column, val);
    }

    default <R> Children leIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return le(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LE), alias, column, val);
    }

    default <R> Children likeIfPresent(SFunction<R, ?> column, Object val) {
        return like(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE), null, column, val);
    }

    default <R> Children likeIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return like(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE), alias, column, val);
    }

    default <R> Children notLikeIfPresent(SFunction<R, ?> column, Object val) {
        return notLike(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE), null, column, val);
    }

    default <R> Children notLikeIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return notLike(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE), alias, column, val);
    }

    default <R> Children likeLeftIfPresent(SFunction<R, ?> column, Object val) {
        return likeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_LEFT), null, column, val);
    }

    default <R> Children likeLeftIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return likeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_LEFT), alias, column, val);
    }

    default <R> Children notLikeLeftIfPresent(SFunction<R, ?> column, Object val) {
        return notLikeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_LEFT), null, column, val);
    }

    default <R> Children notLikeLeftIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return notLikeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_LEFT), alias, column, val);
    }

    default <R> Children likeRightIfPresent(SFunction<R, ?> column, Object val) {
        return likeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_RIGHT), null, column, val);
    }

    default <R> Children likeRightIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return likeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_RIGHT), alias, column, val);
    }

    default <R> Children notLikeRightIfPresent(SFunction<R, ?> column, Object val) {
        return notLikeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_RIGHT), null, column, val);
    }

    default <R> Children notLikeRightIfPresent(String alias, SFunction<R, ?> column, Object val) {
        return notLikeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_RIGHT), alias, column, val);
    }
}
