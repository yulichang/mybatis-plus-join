package com.github.mybatisplus.wrapper.interfaces;

import com.github.mybatisplus.func.MySFunction;

import java.io.Serializable;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.interfaces.Compare}
 */
public interface MyCompare<Children> extends Serializable {

    /* 遗弃allEq */

    /* ***************************************** eq start ********************************************* */

    <E, F> Children eq(boolean condition, String alias, MySFunction<E, F> column, Object val);

    default <E, F> Children eq(boolean condition, MySFunction<E, F> column, Object val) {
        return eq(condition, null, column, val);
    }

    default <E, F> Children eq(String alias, MySFunction<E, F> column, Object val) {
        return eq(true, alias, column, val);
    }

    default <E, F> Children eq(MySFunction<E, F> column, Object val) {
        return eq(true, null, column, val);
    }

    <E, F, X, Y> Children eq(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val);

    default <E, F, X, Y> Children eq(MySFunction<E, F> column, MySFunction<X, Y> val) {
        return eq(true, null, column, null, val);
    }

    default <E, F, X, Y> Children eq(Boolean condition, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return eq(condition, null, column, null, val);
    }

    default <E, F, X, Y> Children eq(String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return eq(true, alias, column, null, val);
    }

    default <E, F, X, Y> Children eq(MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return eq(true, null, column, as, val);
    }

    default <E, F, X, Y> Children eq(Boolean condition, String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return eq(condition, alias, column, null, val);
    }

    default <E, F, X, Y> Children eq(Boolean condition, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return eq(condition, null, column, as, val);
    }

    default <E, F, X, Y> Children eq(String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return eq(true, alias, column, as, val);
    }

    /* ***************************************** eq end ********************************************* */


    /* ***************************************** ne start ********************************************* */

    default <E, F> Children ne(MySFunction<E, F> column, Object val) {
        return ne(true, null, column, val);
    }

    default <E, F> Children ne(boolean condition, MySFunction<E, F> column, Object val) {
        return ne(condition, null, column, val);
    }

    default <E, F> Children ne(String alias, MySFunction<E, F> column, Object val) {
        return ne(true, alias, column, val);
    }

    <E, F> Children ne(boolean condition, String alias, MySFunction<E, F> column, Object val);

    default <E, F, X, Y> Children ne(MySFunction<E, F> column, MySFunction<X, Y> val) {
        return ne(true, null, column, null, val);
    }

    default <E, F, X, Y> Children ne(boolean condition, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return ne(condition, null, column, null, val);
    }

    default <E, F, X, Y> Children ne(String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return ne(true, alias, column, null, val);
    }

    default <E, F, X, Y> Children ne(MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return ne(true, null, column, as, val);
    }

    default <E, F, X, Y> Children ne(boolean condition, String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return ne(condition, alias, column, null, val);
    }

    default <E, F, X, Y> Children ne(String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return ne(true, alias, column, as, val);
    }

    default <E, F, X, Y> Children ne(boolean condition, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return ne(condition, null, column, as, val);
    }

    <E, F, X, Y> Children ne(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val);

    /* ***************************************** ne end ********************************************* */


    /* ***************************************** gt start ********************************************* */

    default <E, F> Children gt(MySFunction<E, F> column, Object val) {
        return gt(true, null, column, val);
    }

    default <E, F> Children gt(boolean condition, MySFunction<E, F> column, Object val) {
        return gt(condition, null, column, val);
    }

    default <E, F> Children gt(String alias, MySFunction<E, F> column, Object val) {
        return gt(true, alias, column, val);
    }

    <E, F> Children gt(boolean condition, String alias, MySFunction<E, F> column, Object val);

    default <E, F, X, Y> Children gt(MySFunction<E, F> column, MySFunction<X, Y> val) {
        return gt(true, null, column, null, val);
    }

    default <E, F, X, Y> Children gt(boolean condition, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return gt(condition, null, column, null, val);
    }

    default <E, F, X, Y> Children gt(String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return gt(true, alias, column, null, val);
    }

    default <E, F, X, Y> Children gt(MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return gt(true, null, column, as, val);
    }

    default <E, F, X, Y> Children gt(boolean condition, String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return gt(condition, alias, column, null, val);
    }

    default <E, F, X, Y> Children gt(String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return gt(true, alias, column, as, val);
    }

    default <E, F, X, Y> Children gt(boolean condition, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return gt(condition, null, column, as, val);
    }

    <E, F, X, Y> Children gt(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val);


    /* ***************************************** gt end ********************************************* */


    /* ***************************************** ge start ********************************************* */

    default <E, F> Children ge(MySFunction<E, F> column, Object val) {
        return ge(true, null, column, val);
    }

    default <E, F> Children ge(boolean condition, MySFunction<E, F> column, Object val) {
        return ge(condition, null, column, val);
    }

    default <E, F> Children ge(String alias, MySFunction<E, F> column, Object val) {
        return ge(true, alias, column, val);
    }

    <E, F> Children ge(boolean condition, String alias, MySFunction<E, F> column, Object val);

    default <E, F, X, Y> Children ge(MySFunction<E, F> column, MySFunction<X, Y> val) {
        return ge(true, null, column, null, val);
    }

    default <E, F, X, Y> Children ge(boolean condition, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return ge(condition, null, column, null, val);
    }

    default <E, F, X, Y> Children ge(String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return ge(true, alias, column, null, val);
    }

    default <E, F, X, Y> Children ge(MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return ge(true, null, column, as, val);
    }

    default <E, F, X, Y> Children ge(boolean condition, String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return ge(condition, alias, column, null, val);
    }

    default <E, F, X, Y> Children ge(String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return ge(true, alias, column, as, val);
    }

    default <E, F, X, Y> Children ge(boolean condition, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return ge(condition, null, column, as, val);
    }

    <E, F, X, Y> Children ge(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val);

    /* ***************************************** ge end ********************************************* */


    /* ***************************************** lt start ********************************************* */

    default <E, F> Children lt(MySFunction<E, F> column, Object val) {
        return lt(true, null, column, val);
    }

    default <E, F> Children lt(boolean condition, MySFunction<E, F> column, Object val) {
        return lt(condition, null, column, val);
    }

    default <E, F> Children lt(String alias, MySFunction<E, F> column, Object val) {
        return lt(true, alias, column, val);
    }

    <E, F> Children lt(boolean condition, String alias, MySFunction<E, F> column, Object val);

    default <E, F, X, Y> Children lt(MySFunction<E, F> column, MySFunction<X, Y> val) {
        return lt(true, null, column, null, val);
    }

    default <E, F, X, Y> Children lt(boolean condition, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return lt(condition, null, column, null, val);
    }

    default <E, F, X, Y> Children lt(String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return lt(true, alias, column, null, val);
    }

    default <E, F, X, Y> Children lt(MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return lt(true, null, column, as, val);
    }

    default <E, F, X, Y> Children lt(boolean condition, String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return lt(condition, alias, column, null, val);
    }

    default <E, F, X, Y> Children lt(String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return lt(true, alias, column, as, val);
    }

    default <E, F, X, Y> Children lt(boolean condition, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return lt(condition, null, column, as, val);
    }

    <E, F, X, Y> Children lt(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val);

    /* ***************************************** lt end ********************************************* */


    /* ***************************************** lt start ********************************************* */

    default <E, F> Children le(MySFunction<E, F> column, Object val) {
        return le(true, null, column, val);
    }

    default <E, F> Children le(boolean condition, MySFunction<E, F> column, Object val) {
        return le(condition, null, column, val);
    }

    default <E, F> Children le(String alias, MySFunction<E, F> column, Object val) {
        return le(true, alias, column, val);
    }

    <E, F> Children le(boolean condition, String alias, MySFunction<E, F> column, Object val);

    default <E, F, X, Y> Children le(MySFunction<E, F> column, MySFunction<X, Y> val) {
        return le(true, null, column, null, val);
    }

    default <E, F, X, Y> Children le(boolean condition, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return le(condition, null, column, null, val);
    }

    default <E, F, X, Y> Children le(String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return le(true, alias, column, null, val);
    }

    default <E, F, X, Y> Children le(MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return le(true, null, column, as, val);
    }

    default <E, F, X, Y> Children le(boolean condition, String alias, MySFunction<E, F> column, MySFunction<X, Y> val) {
        return le(condition, alias, column, null, val);
    }

    default <E, F, X, Y> Children le(String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return le(true, alias, column, as, val);
    }

    default <E, F, X, Y> Children le(boolean condition, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return le(condition, null, column, as, val);
    }

    <E, F, X, Y> Children le(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val);

    /* ***************************************** le end ********************************************* */

    /* ***************************************** between start ********************************************* */

    default <E, F> Children between(MySFunction<E, F> column, Object val1, Object val2) {
        return between(true, null, column, val1, val2);
    }

    default <E, F> Children between(boolean condition, MySFunction<E, F> column, Object val1, Object val2) {
        return between(condition, null, column, val1, val2);
    }

    default <E, F> Children between(String alias, MySFunction<E, F> column, Object val1, Object val2) {
        return between(true, alias, column, val1, val2);
    }

    <E, F> Children between(boolean condition, String alias, MySFunction<E, F> column, Object val1, Object val2);

    /* ***************************************** between end ********************************************* */


    /* ***************************************** notBetween start ********************************************* */

    default <E, F> Children notBetween(MySFunction<E, F> column, Object val1, Object val2) {
        return notBetween(true, null, column, val1, val2);
    }

    default <E, F> Children notBetween(boolean condition, MySFunction<E, F> column, Object val1, Object val2) {
        return notBetween(condition, null, column, val1, val2);
    }

    default <E, F> Children notBetween(String alias, MySFunction<E, F> column, Object val1, Object val2) {
        return notBetween(true, alias, column, val1, val2);
    }

    <E, F> Children notBetween(boolean condition, String alias, MySFunction<E, F> column, Object val1, Object val2);

    /* ***************************************** notBetween end ********************************************* */


    /* ***************************************** like start ********************************************* */

    default <E, F> Children like(MySFunction<E, F> column, Object val) {
        return like(true, null, column, val);
    }

    default <E, F> Children like(boolean condition, MySFunction<E, F> column, Object val) {
        return like(condition, null, column, val);
    }

    default <E, F> Children like(String alias, MySFunction<E, F> column, Object val) {
        return like(true, alias, column, val);
    }

    <E, F> Children like(boolean condition, String alias, MySFunction<E, F> column, Object val);

    /* ***************************************** like end ********************************************* */


    /* ***************************************** notLike start ********************************************* */

    default <E, F> Children notLike(MySFunction<E, F> column, Object val) {
        return notLike(true, null, column, val);
    }

    default <E, F> Children notLike(boolean condition, MySFunction<E, F> column, Object val) {
        return notLike(condition, null, column, val);
    }

    default <E, F> Children notLike(String alias, MySFunction<E, F> column, Object val) {
        return notLike(true, alias, column, val);
    }

    <E, F> Children notLike(boolean condition, String alias, MySFunction<E, F> column, Object val);

    /* ***************************************** notLike end ********************************************* */


    /* ***************************************** likeLeft start ********************************************* */

    default <E, F> Children likeLeft(MySFunction<E, F> column, Object val) {
        return likeLeft(true, null, column, val);
    }

    default <E, F> Children likeLeft(boolean condition, MySFunction<E, F> column, Object val) {
        return likeLeft(condition, null, column, val);
    }

    default <E, F> Children likeLeft(String alias, MySFunction<E, F> column, Object val) {
        return likeLeft(true, alias, column, val);
    }

    <E, F> Children likeLeft(boolean condition, String alias, MySFunction<E, F> column, Object val);

    /* ***************************************** likeLeft end ********************************************* */



    /* ***************************************** likeRight start ********************************************* */

    default <E, F> Children likeRight(MySFunction<E, F> column, Object val) {
        return likeRight(true, null, column, val);
    }

    default <E, F> Children likeRight(boolean condition, MySFunction<E, F> column, Object val) {
        return likeRight(condition, null, column, val);
    }

    default <E, F> Children likeRight(String alias, MySFunction<E, F> column, Object val) {
        return likeRight(true, alias, column, val);
    }

    <E, F> Children likeRight(boolean condition, String alias, MySFunction<E, F> column, Object val);

    /* ***************************************** likeRight end ********************************************* */

}
