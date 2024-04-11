package com.github.yulichang.kt.interfaces;

import com.github.yulichang.wrapper.enums.IfExistsSqlKeyWordEnum;
import kotlin.reflect.KProperty;

import java.util.function.BiPredicate;

/**
 * IfExists
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareIfExists<Children> extends Compare<Children> {

    BiPredicate<Object, IfExistsSqlKeyWordEnum> getIfExists();

    default Children eqIfExists(KProperty<?> column, Object val) {
        return eq(getIfExists().test(val, IfExistsSqlKeyWordEnum.EQ), null, column, val);
    }

    default Children eqIfExists(String alias, KProperty<?> column, Object val) {
        return eq(getIfExists().test(val, IfExistsSqlKeyWordEnum.EQ), alias, column, val);
    }

    default Children neIfExists(KProperty<?> column, Object val) {
        return ne(getIfExists().test(val, IfExistsSqlKeyWordEnum.NE), null, column, val);
    }

    default Children neIfExists(String alias, KProperty<?> column, Object val) {
        return ne(getIfExists().test(val, IfExistsSqlKeyWordEnum.NE), alias, column, val);
    }

    default Children gtIfExists(KProperty<?> column, Object val) {
        return gt(getIfExists().test(val, IfExistsSqlKeyWordEnum.GT), null, column, val);
    }

    default Children gtIfExists(String alias, KProperty<?> column, Object val) {
        return gt(getIfExists().test(val, IfExistsSqlKeyWordEnum.GT), alias, column, val);
    }


    default Children geIfExists(KProperty<?> column, Object val) {
        return ge(getIfExists().test(val, IfExistsSqlKeyWordEnum.GE), null, column, val);
    }

    default Children geIfExists(String alias, KProperty<?> column, Object val) {
        return ge(getIfExists().test(val, IfExistsSqlKeyWordEnum.GE), alias, column, val);
    }

    default Children ltIfExists(KProperty<?> column, Object val) {
        return lt(getIfExists().test(val, IfExistsSqlKeyWordEnum.LT), null, column, val);
    }

    default Children ltIfExists(String alias, KProperty<?> column, Object val) {
        return lt(getIfExists().test(val, IfExistsSqlKeyWordEnum.LT), alias, column, val);
    }

    default Children leIfExists(KProperty<?> column, Object val) {
        return le(getIfExists().test(val, IfExistsSqlKeyWordEnum.LE), null, column, val);
    }

    default Children leIfExists(String alias, KProperty<?> column, Object val) {
        return le(getIfExists().test(val, IfExistsSqlKeyWordEnum.LE), alias, column, val);
    }

    default Children likeIfExists(KProperty<?> column, Object val) {
        return like(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE), null, column, val);
    }

    default Children likeIfExists(String alisa, KProperty<?> column, Object val) {
        return like(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE), alisa, column, val);
    }

    default Children notLikeIfExists(KProperty<?> column, Object val) {
        return notLike(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE), null, column, val);
    }

    default Children notLikeIfExists(String alias, KProperty<?> column, Object val) {
        return notLike(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE), alias, column, val);
    }

    default Children likeLeftIfExists(KProperty<?> column, Object val) {
        return likeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_LEFT), null, column, val);
    }

    default Children likeLeftIfExists(String alias, KProperty<?> column, Object val) {
        return likeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_LEFT), alias, column, val);
    }

    default Children notLikeLeftIfExists(KProperty<?> column, Object val) {
        return notLikeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_LEFT), null, column, val);
    }

    default Children notLikeLeftIfExists(String alias, KProperty<?> column, Object val) {
        return notLikeLeft(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_LEFT), alias, column, val);
    }

    default Children likeRightIfExists(KProperty<?> column, Object val) {
        return likeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_RIGHT), null, column, val);
    }

    default Children likeRightIfExists(String alias, KProperty<?> column, Object val) {
        return likeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.LIKE_RIGHT), alias, column, val);
    }

    default Children notLikeRightIfExists(KProperty<?> column, Object val) {
        return notLikeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_RIGHT), null, column, val);
    }

    default Children notLikeRightIfExists(String alias, KProperty<?> column, Object val) {
        return notLikeRight(getIfExists().test(val, IfExistsSqlKeyWordEnum.NOT_LIKE_RIGHT), alias, column, val);
    }
}
