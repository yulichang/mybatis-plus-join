package com.github.yulichang.kt.interfaces;

import com.github.yulichang.wrapper.enums.IfPresentSqlKeyWordEnum;
import kotlin.reflect.KProperty;

import java.util.function.BiPredicate;

/**
 * ifPresent
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareIfPresent<Children> extends Compare<Children> {

    BiPredicate<Object, IfPresentSqlKeyWordEnum> getIfPresent();

    default Children eqIfPresent(KProperty<?> column, Object val) {
        return eq(getIfPresent().test(val, IfPresentSqlKeyWordEnum.EQ), null, column, val);
    }

    default Children eqIfPresent(String alias, KProperty<?> column, Object val) {
        return eq(getIfPresent().test(val, IfPresentSqlKeyWordEnum.EQ), alias, column, val);
    }

    default Children neIfPresent(KProperty<?> column, Object val) {
        return ne(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NE), null, column, val);
    }

    default Children neIfPresent(String alias, KProperty<?> column, Object val) {
        return ne(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NE), alias, column, val);
    }

    default Children gtIfPresent(KProperty<?> column, Object val) {
        return gt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GT), null, column, val);
    }

    default Children gtIfPresent(String alias, KProperty<?> column, Object val) {
        return gt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GT), alias, column, val);
    }


    default Children geIfPresent(KProperty<?> column, Object val) {
        return ge(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GE), null, column, val);
    }

    default Children geIfPresent(String alias, KProperty<?> column, Object val) {
        return ge(getIfPresent().test(val, IfPresentSqlKeyWordEnum.GE), alias, column, val);
    }

    default Children ltIfPresent(KProperty<?> column, Object val) {
        return lt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LT), null, column, val);
    }

    default Children ltIfPresent(String alias, KProperty<?> column, Object val) {
        return lt(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LT), alias, column, val);
    }

    default Children leIfPresent(KProperty<?> column, Object val) {
        return le(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LE), null, column, val);
    }

    default Children leIfPresent(String alias, KProperty<?> column, Object val) {
        return le(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LE), alias, column, val);
    }

    default Children likeIfPresent(KProperty<?> column, Object val) {
        return like(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE), null, column, val);
    }

    default Children likeIfPresent(String alisa, KProperty<?> column, Object val) {
        return like(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE), alisa, column, val);
    }

    default Children notLikeIfPresent(KProperty<?> column, Object val) {
        return notLike(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE), null, column, val);
    }

    default Children notLikeIfPresent(String alias, KProperty<?> column, Object val) {
        return notLike(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE), alias, column, val);
    }

    default Children likeLeftIfPresent(KProperty<?> column, Object val) {
        return likeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_LEFT), null, column, val);
    }

    default Children likeLeftIfPresent(String alias, KProperty<?> column, Object val) {
        return likeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_LEFT), alias, column, val);
    }

    default Children notLikeLeftIfPresent(KProperty<?> column, Object val) {
        return notLikeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_LEFT), null, column, val);
    }

    default Children notLikeLeftIfPresent(String alias, KProperty<?> column, Object val) {
        return notLikeLeft(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_LEFT), alias, column, val);
    }

    default Children likeRightIfPresent(KProperty<?> column, Object val) {
        return likeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_RIGHT), null, column, val);
    }

    default Children likeRightIfPresent(String alias, KProperty<?> column, Object val) {
        return likeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.LIKE_RIGHT), alias, column, val);
    }

    default Children notLikeRightIfPresent(KProperty<?> column, Object val) {
        return notLikeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_RIGHT), null, column, val);
    }

    default Children notLikeRightIfPresent(String alias, KProperty<?> column, Object val) {
        return notLikeRight(getIfPresent().test(val, IfPresentSqlKeyWordEnum.NOT_LIKE_RIGHT), alias, column, val);
    }
}
