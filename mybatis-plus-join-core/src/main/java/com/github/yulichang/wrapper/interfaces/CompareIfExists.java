package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.enums.IfExistsSqlKeyWordEnum;

import java.util.function.BiPredicate;

/**
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareIfExists<Children> extends Compare<Children> {

    BiPredicate<Object, IfExistsSqlKeyWordEnum> getIfExists();

    default <R> Children eqIfExists(SFunction<R, ?> column, Object val) {
        return eq(getIfExists().test(val, IfExistsSqlKeyWordEnum.EQ), null, column, val);
    }

    default <R> Children eqIfExists(String alias, SFunction<R, ?> column, Object val) {
        return eq(getIfExists().test(val, IfExistsSqlKeyWordEnum.EQ), alias, column, val);
    }

    default <R> Children neIfExists(SFunction<R, ?> column, Object val) {
        return ne(getIfExists().test(val, IfExistsSqlKeyWordEnum.NE), null, column, val);
    }

    default <R> Children neIfExists(String alias, SFunction<R, ?> column, Object val) {
        return ne(getIfExists().test(val, IfExistsSqlKeyWordEnum.NE), alias, column, val);
    }

    default <R> Children gtIfExists(SFunction<R, ?> column, Object val) {
        return gt(getIfExists().test(val, IfExistsSqlKeyWordEnum.GT), null, column, val);
    }

    default <R> Children gtIfExists(String alias, SFunction<R, ?> column, Object val) {
        return gt(getIfExists().test(val, IfExistsSqlKeyWordEnum.GT), alias, column, val);
    }

    default <R> Children geIfExists(SFunction<R, ?> column, Object val) {
        return ge(getIfExists().test(val, IfExistsSqlKeyWordEnum.GE), null, column, val);
    }

    default <R> Children geIfExists(String alias, SFunction<R, ?> column, Object val) {
        return ge(getIfExists().test(val, IfExistsSqlKeyWordEnum.GE), alias, column, val);
    }

    default <R> Children ltIfExists(SFunction<R, ?> column, Object val) {
        return lt(getIfExists().test(val, IfExistsSqlKeyWordEnum.LT), null, column, val);
    }

    default <R> Children ltIfExists(String alias, SFunction<R, ?> column, Object val) {
        return lt(getIfExists().test(val, IfExistsSqlKeyWordEnum.LT), alias, column, val);
    }

    default <R> Children leIfExists(SFunction<R, ?> column, Object val) {
        return le(getIfExists().test(val, IfExistsSqlKeyWordEnum.LE), null, column, val);
    }

    default <R> Children leIfExists(String alias, SFunction<R, ?> column, Object val) {
        return le(getIfExists().test(val, IfExistsSqlKeyWordEnum.LE), alias, column, val);
    }

    default <R> Children likeIfExists(SFunction<R, ?> column, Object val) {
        return like(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE), null, column, val);
    }

    default <R> Children likeIfExists(String alias, SFunction<R, ?> column, Object val) {
        return like(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE), alias, column, val);
    }

    default <R> Children notLikeIfExists(SFunction<R, ?> column, Object val) {
        return notLike(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE), null, column, val);
    }

    default <R> Children notLikeIfExists(String alias, SFunction<R, ?> column, Object val) {
        return notLike(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE), alias, column, val);
    }

    default <R> Children likeLeftIfExists(SFunction<R, ?> column, Object val) {
        return likeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_LEFT), null, column, val);
    }

    default <R> Children likeLeftIfExists(String alias, SFunction<R, ?> column, Object val) {
        return likeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_LEFT), alias, column, val);
    }

    default <R> Children notLikeLeftIfExists(SFunction<R, ?> column, Object val) {
        return notLikeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_LEFT), null, column, val);
    }

    default <R> Children notLikeLeftIfExists(String alias, SFunction<R, ?> column, Object val) {
        return notLikeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_LEFT), alias, column, val);
    }

    default <R> Children likeRightIfExists(SFunction<R, ?> column, Object val) {
        return likeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_RIGHT), null, column, val);
    }

    default <R> Children likeRightIfExists(String alias, SFunction<R, ?> column, Object val) {
        return likeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_RIGHT), alias, column, val);
    }

    default <R> Children notLikeRightIfExists(SFunction<R, ?> column, Object val) {
        return notLikeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_RIGHT), null, column, val);
    }

    default <R> Children notLikeRightIfExists(String alias, SFunction<R, ?> column, Object val) {
        return notLikeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_RIGHT), alias, column, val);
    }
}
