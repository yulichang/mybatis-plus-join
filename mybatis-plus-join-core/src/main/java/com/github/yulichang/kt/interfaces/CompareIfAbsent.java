package com.github.yulichang.kt.interfaces;

import com.github.yulichang.config.MybatisPlusJoinIfAbsent;
import kotlin.reflect.KProperty;

/**
 * ifAbsent
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareIfAbsent<Children> extends Compare<Children> {

    MybatisPlusJoinIfAbsent getIfAbsent();

    default Children eqIfAbsent(KProperty<?> column, Object val) {
        return eq(getIfAbsent().test(val), null, column, val);
    }

    default Children eqIfAbsent(String alias, KProperty<?> column, Object val) {
        return eq(getIfAbsent().test(val), alias, column, val);
    }

    default Children neIfAbsent(KProperty<?> column, Object val) {
        return ne(getIfAbsent().test(val), null, column, val);
    }

    default Children neIfAbsent(String alias, KProperty<?> column, Object val) {
        return ne(getIfAbsent().test(val), alias, column, val);
    }

    default Children gtIfAbsent(KProperty<?> column, Object val) {
        return gt(getIfAbsent().test(val), null, column, val);
    }

    default Children gtIfAbsent(String alias, KProperty<?> column, Object val) {
        return gt(getIfAbsent().test(val), alias, column, val);
    }


    default Children geIfAbsent(KProperty<?> column, Object val) {
        return ge(getIfAbsent().test(val), null, column, val);
    }

    default Children geIfAbsent(String alias, KProperty<?> column, Object val) {
        return ge(getIfAbsent().test(val), alias, column, val);
    }

    default Children ltIfAbsent(KProperty<?> column, Object val) {
        return lt(getIfAbsent().test(val), null, column, val);
    }

    default Children ltIfAbsent(String alias, KProperty<?> column, Object val) {
        return lt(getIfAbsent().test(val), alias, column, val);
    }

    default Children leIfAbsent(KProperty<?> column, Object val) {
        return le(getIfAbsent().test(val), null, column, val);
    }

    default Children leIfAbsent(String alias, KProperty<?> column, Object val) {
        return le(getIfAbsent().test(val), alias, column, val);
    }

    default Children likeIfAbsent(KProperty<?> column, Object val) {
        return like(getIfAbsent().test(val), null, column, val);
    }

    default Children likeIfAbsent(String alisa, KProperty<?> column, Object val) {
        return like(getIfAbsent().test(val), alisa, column, val);
    }

    default Children notLikeIfAbsent(KProperty<?> column, Object val) {
        return notLike(getIfAbsent().test(val), null, column, val);
    }

    default Children notLikeIfAbsent(String alias, KProperty<?> column, Object val) {
        return notLike(getIfAbsent().test(val), alias, column, val);
    }

    default Children likeLeftIfAbsent(KProperty<?> column, Object val) {
        return likeLeft(getIfAbsent().test(val), null, column, val);
    }

    default Children likeLeftIfAbsent(String alias, KProperty<?> column, Object val) {
        return likeLeft(getIfAbsent().test(val), alias, column, val);
    }

    default Children likeRightIfAbsent(KProperty<?> column, Object val) {
        return likeRight(getIfAbsent().test(val), null, column, val);
    }

    default Children likeRightIfAbsent(String alias, KProperty<?> column, Object val) {
        return likeRight(getIfAbsent().test(val), alias, column, val);
    }
}
