package com.github.yulichang.wrapper.interfaces;

import com.github.yulichang.config.MybatisPlusJoinIfAbsent;

/**
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 *
 * @author yulichang
 * @since 1.4.9
 */
@SuppressWarnings("unused")
public interface CompareStrIfAbsent<Children, R> extends CompareStr<Children, R> {

    MybatisPlusJoinIfAbsent getIfAbsent();

    default Children eqIfAbsent(R column, Object val) {
        return eq(getIfAbsent().test(val), column, val);
    }

    default Children neIfAbsent(R column, Object val) {
        return ne(getIfAbsent().test(val), column, val);
    }

    default Children gtIfAbsent(R column, Object val) {
        return gt(getIfAbsent().test(val), column, val);
    }

    default Children geIfAbsent(R column, Object val) {
        return ge(getIfAbsent().test(val), column, val);
    }

    default Children ltIfAbsent(R column, Object val) {
        return lt(getIfAbsent().test(val), column, val);
    }

    default Children leIfAbsent(R column, Object val) {
        return le(getIfAbsent().test(val), column, val);
    }

    default Children likeIfAbsent(R column, Object val) {
        return like(getIfAbsent().test(val), column, val);
    }

    default Children notLikeIfAbsent(R column, Object val) {
        return notLike(getIfAbsent().test(val), column, val);
    }

    default Children likeLeftIfAbsent(R column, Object val) {
        return likeLeft(getIfAbsent().test(val), column, val);
    }

    default Children likeRightIfAbsent(R column, Object val) {
        return likeRight(getIfAbsent().test(val), column, val);
    }
}
