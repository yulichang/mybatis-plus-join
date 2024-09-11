package com.github.yulichang.extension.apt.interfaces;

import com.github.yulichang.extension.apt.matedata.Column;

import java.io.Serializable;

/**
 * 将原来的泛型R改成Column
 * <p>
 * {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 */
@SuppressWarnings("unused")
public interface Compare<Children> extends Serializable {

    default Children eq(Column column, Object val) {
        return eq(true, column, val);
    }

    Children eq(boolean condition, Column column, Object val);

    default Children ne(Column column, Object val) {
        return ne(true, column, val);
    }

    Children ne(boolean condition, Column column, Object val);

    default Children gt(Column column, Object val) {
        return gt(true, column, val);
    }

    Children gt(boolean condition, Column column, Object val);

    default Children ge(Column column, Object val) {
        return ge(true, column, val);
    }

    Children ge(boolean condition, Column column, Object val);

    default Children lt(Column column, Object val) {
        return lt(true, column, val);
    }

    Children lt(boolean condition, Column column, Object val);

    default Children le(Column column, Object val) {
        return le(true, column, val);
    }

    Children le(boolean condition, Column column, Object val);

    default Children between(Column column, Object val1, Object val2) {
        return between(true, column, val1, val2);
    }

    Children between(boolean condition, Column column, Object val1, Object val2);

    default Children notBetween(Column column, Object val1, Object val2) {
        return notBetween(true, column, val1, val2);
    }

    Children notBetween(boolean condition, Column column, Object val1, Object val2);

    default Children like(Column column, Object val) {
        return like(true, column, val);
    }

    Children like(boolean condition, Column column, Object val);

    default Children notLike(Column column, Object val) {
        return notLike(true, column, val);
    }

    Children notLike(boolean condition, Column column, Object val);

    default Children likeLeft(Column column, Object val) {
        return likeLeft(true, column, val);
    }

    Children likeLeft(boolean condition, Column column, Object val);

    default Children notLikeLeft(Column column, Object val) {
        return notLikeLeft(true, column, val);
    }

    Children notLikeLeft(boolean condition, Column column, Object val);

    default Children likeRight(Column column, Object val) {
        return likeRight(true, column, val);
    }

    Children likeRight(boolean condition, Column column, Object val);

    default Children notLikeRight(Column column, Object val) {
        return notLikeRight(true, column, val);
    }

    Children notLikeRight(boolean condition, Column column, Object val);
}
