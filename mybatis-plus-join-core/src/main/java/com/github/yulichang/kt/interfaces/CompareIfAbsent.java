package com.github.yulichang.kt.interfaces;

import com.github.yulichang.wrapper.enums.IfAbsentSqlKeyWordEnum;
import kotlin.reflect.KProperty;

import java.util.function.BiPredicate;

/**
 * ifAbsent
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareIfAbsent<Children> extends Compare<Children> {

    BiPredicate<Object, IfAbsentSqlKeyWordEnum> getIfAbsent();

    default Children eqIfAbsent(KProperty<?> column, Object val) {
        return eq(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.EQ), null, column, val);
    }

    default Children eqIfAbsent(String alias, KProperty<?> column, Object val) {
        return eq(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.EQ), alias, column, val);
    }

    default Children neIfAbsent(KProperty<?> column, Object val) {
        return ne(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NE), null, column, val);
    }

    default Children neIfAbsent(String alias, KProperty<?> column, Object val) {
        return ne(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NE), alias, column, val);
    }

    default Children gtIfAbsent(KProperty<?> column, Object val) {
        return gt(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.GT), null, column, val);
    }

    default Children gtIfAbsent(String alias, KProperty<?> column, Object val) {
        return gt(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.GT), alias, column, val);
    }


    default Children geIfAbsent(KProperty<?> column, Object val) {
        return ge(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.GE), null, column, val);
    }

    default Children geIfAbsent(String alias, KProperty<?> column, Object val) {
        return ge(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.GE), alias, column, val);
    }

    default Children ltIfAbsent(KProperty<?> column, Object val) {
        return lt(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LT), null, column, val);
    }

    default Children ltIfAbsent(String alias, KProperty<?> column, Object val) {
        return lt(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LT), alias, column, val);
    }

    default Children leIfAbsent(KProperty<?> column, Object val) {
        return le(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LE), null, column, val);
    }

    default Children leIfAbsent(String alias, KProperty<?> column, Object val) {
        return le(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LE), alias, column, val);
    }

    default Children likeIfAbsent(KProperty<?> column, Object val) {
        return like(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE), null, column, val);
    }

    default Children likeIfAbsent(String alisa, KProperty<?> column, Object val) {
        return like(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE), alisa, column, val);
    }

    default Children notLikeIfAbsent(KProperty<?> column, Object val) {
        return notLike(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NOT_LIKE), null, column, val);
    }

    default Children notLikeIfAbsent(String alias, KProperty<?> column, Object val) {
        return notLike(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NOT_LIKE), alias, column, val);
    }

    default Children likeLeftIfAbsent(KProperty<?> column, Object val) {
        return likeLeft(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE_LEFT), null, column, val);
    }

    default Children likeLeftIfAbsent(String alias, KProperty<?> column, Object val) {
        return likeLeft(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE_LEFT), alias, column, val);
    }

    default Children notLikeLeftIfAbsent(KProperty<?> column, Object val) {
        return notLikeLeft(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NOT_LIKE_LEFT), null, column, val);
    }

    default Children notLikeLeftIfAbsent(String alias, KProperty<?> column, Object val) {
        return notLikeLeft(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NOT_LIKE_LEFT), alias, column, val);
    }

    default Children likeRightIfAbsent(KProperty<?> column, Object val) {
        return likeRight(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE_RIGHT), null, column, val);
    }

    default Children likeRightIfAbsent(String alias, KProperty<?> column, Object val) {
        return likeRight(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.LIKE_RIGHT), alias, column, val);
    }

    default Children notLikeRightIfAbsent(KProperty<?> column, Object val) {
        return notLikeRight(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NOT_LIKE_RIGHT), null, column, val);
    }

    default Children notLikeRightIfAbsent(String alias, KProperty<?> column, Object val) {
        return notLikeRight(getIfAbsent().test(val, IfAbsentSqlKeyWordEnum.NOT_LIKE_RIGHT), alias, column, val);
    }
}
