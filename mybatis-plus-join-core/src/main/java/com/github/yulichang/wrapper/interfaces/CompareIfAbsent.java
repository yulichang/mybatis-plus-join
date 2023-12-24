package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.enums.IfAbsentSqlKeyWordEnum;

import java.util.function.BiPredicate;

/**
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareIfAbsent<Children> extends Compare<Children> {

    BiPredicate<Object, IfAbsentSqlKeyWordEnum> getIfAbsent();

    default <R> Children eqIfAbsent(SFunction<R, ?> column, Object val) {
        return eq(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.EQ), null, column, val);
    }

    default <R> Children eqIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return eq(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.EQ), alias, column, val);
    }

    default <R> Children neIfAbsent(SFunction<R, ?> column, Object val) {
        return ne(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NE), null, column, val);
    }

    default <R> Children neIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return ne(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NE), alias, column, val);
    }

    default <R> Children gtIfAbsent(SFunction<R, ?> column, Object val) {
        return gt(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.GT), null, column, val);
    }

    default <R> Children gtIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return gt(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.GT), alias, column, val);
    }

    default <R> Children geIfAbsent(SFunction<R, ?> column, Object val) {
        return ge(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.GE), null, column, val);
    }

    default <R> Children geIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return ge(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.GE), alias, column, val);
    }

    default <R> Children ltIfAbsent(SFunction<R, ?> column, Object val) {
        return lt(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LT), null, column, val);
    }

    default <R> Children ltIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return lt(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LT), alias, column, val);
    }

    default <R> Children leIfAbsent(SFunction<R, ?> column, Object val) {
        return le(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LE), null, column, val);
    }

    default <R> Children leIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return le(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LE), alias, column, val);
    }

    default <R> Children likeIfAbsent(SFunction<R, ?> column, Object val) {
        return like(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE), null, column, val);
    }

    default <R> Children likeIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return like(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE), alias, column, val);
    }

    default <R> Children notLikeIfAbsent(SFunction<R, ?> column, Object val) {
        return notLike(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NOT_LIKE), null, column, val);
    }

    default <R> Children notLikeIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return notLike(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NOT_LIKE), alias, column, val);
    }

    default <R> Children likeLeftIfAbsent(SFunction<R, ?> column, Object val) {
        return likeLeft(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE_LEFT), null, column, val);
    }

    default <R> Children likeLeftIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return likeLeft(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE_LEFT), alias, column, val);
    }

    default <R> Children likeRightIfAbsent(SFunction<R, ?> column, Object val) {
        return likeRight(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE_RIGHT), null, column, val);
    }

    default <R> Children likeRightIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return likeRight(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE_RIGHT), alias, column, val);
    }
}
