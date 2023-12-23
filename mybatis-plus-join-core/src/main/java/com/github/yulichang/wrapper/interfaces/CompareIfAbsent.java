package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.config.MybatisPlusJoinIfAbsent;

/**
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareIfAbsent<Children> extends Compare<Children> {

    MybatisPlusJoinIfAbsent getIfAbsent();

    default <R> Children eqIfAbsent(SFunction<R, ?> column, Object val) {
        return eq(getIfAbsent().test(val), null, column, val);
    }

    default <R> Children eqIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return eq(getIfAbsent().test(val), alias, column, val);
    }

    default <R> Children neIfAbsent(SFunction<R, ?> column, Object val) {
        return ne(getIfAbsent().test(val), null, column, val);
    }

    default <R> Children neIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return ne(getIfAbsent().test(val), alias, column, val);
    }

    default <R> Children gtIfAbsent(SFunction<R, ?> column, Object val) {
        return gt(getIfAbsent().test(val), null, column, val);
    }

    default <R> Children gtIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return gt(getIfAbsent().test(val), alias, column, val);
    }

    default <R> Children geIfAbsent(SFunction<R, ?> column, Object val) {
        return ge(getIfAbsent().test(val), null, column, val);
    }

    default <R> Children geIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return ge(getIfAbsent().test(val), alias, column, val);
    }

    default <R> Children ltIfAbsent(SFunction<R, ?> column, Object val) {
        return lt(getIfAbsent().test(val), null, column, val);
    }

    default <R> Children ltIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return lt(getIfAbsent().test(val), alias, column, val);
    }

    default <R> Children leIfAbsent(SFunction<R, ?> column, Object val) {
        return le(getIfAbsent().test(val), null, column, val);
    }

    default <R> Children leIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return le(getIfAbsent().test(val), alias, column, val);
    }

    default <R> Children likeIfAbsent(SFunction<R, ?> column, Object val) {
        return like(getIfAbsent().test(val), null, column, val);
    }

    default <R> Children likeIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return like(getIfAbsent().test(val), alias, column, val);
    }

    default <R> Children notLikeIfAbsent(SFunction<R, ?> column, Object val) {
        return notLike(getIfAbsent().test(val), null, column, val);
    }

    default <R> Children notLikeIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return notLike(getIfAbsent().test(val), alias, column, val);
    }

    default <R> Children likeLeftIfAbsent(SFunction<R, ?> column, Object val) {
        return likeLeft(getIfAbsent().test(val), null, column, val);
    }

    default <R> Children likeLeftIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return likeLeft(getIfAbsent().test(val), alias, column, val);
    }

    default <R> Children likeRightIfAbsent(SFunction<R, ?> column, Object val) {
        return likeRight(getIfAbsent().test(val), null, column, val);
    }

    default <R> Children likeRightIfAbsent(String alias, SFunction<R, ?> column, Object val) {
        return likeRight(getIfAbsent().test(val), alias, column, val);
    }
}
