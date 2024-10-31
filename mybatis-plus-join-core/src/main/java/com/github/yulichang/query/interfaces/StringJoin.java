package com.github.yulichang.query.interfaces;

import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.Constant;

/**
 * @author yulichang
 */
@SuppressWarnings("unused")
public interface StringJoin<Children, T> extends MPJBaseJoin<T> {

    /**
     * left join
     */
    default Children leftJoin(String joinSql) {
        return leftJoin(true, joinSql);
    }

    default Children leftJoin(String joinSql, Object... args) {
        return leftJoin(true, joinSql, args);
    }


    default Children leftJoin(boolean condition, String joinSql) {
        return join(Constant.LEFT_JOIN, condition, joinSql);
    }

    default Children leftJoin(boolean condition, String joinSql, Object... args) {
        return join(Constant.LEFT_JOIN, condition, joinSql, args);
    }


    /**
     * right join
     */
    default Children rightJoin(String joinSql) {
        return rightJoin(true, joinSql);
    }

    default Children rightJoin(String joinSql, Object... args) {
        return rightJoin(true, joinSql, args);
    }

    default Children rightJoin(boolean condition, String joinSql) {
        return join(Constant.RIGHT_JOIN, condition, joinSql);
    }

    default Children rightJoin(boolean condition, String joinSql, Object... args) {
        return join(Constant.RIGHT_JOIN, condition, joinSql, args);
    }

    /**
     * inner join
     */
    default Children innerJoin(String joinSql) {
        return innerJoin(true, joinSql);
    }

    default Children innerJoin(String joinSql, Object... args) {
        return innerJoin(true, joinSql, args);
    }

    default Children innerJoin(boolean condition, String joinSql) {
        return join(Constant.INNER_JOIN, condition, joinSql);
    }

    default Children innerJoin(boolean condition, String joinSql, Object... args) {
        return join(Constant.INNER_JOIN, condition, joinSql, args);
    }

    /**
     * full join
     */
    default Children fullJoin(String joinSql) {
        return fullJoin(true, joinSql);
    }

    default Children fullJoin(String joinSql, Object... args) {
        return fullJoin(true, joinSql, args);
    }

    default Children fullJoin(boolean condition, String joinSql) {
        return join(Constant.FULL_JOIN, condition, joinSql);
    }

    default Children fullJoin(boolean condition, String joinSql, Object... args) {
        return join(Constant.FULL_JOIN, condition, joinSql, args);
    }

    default Children join(String keyWord, boolean condition, String joinSql) {
        return join(keyWord, condition, joinSql, new Object[0]);
    }

    default Children join(String keyWord, boolean condition, String joinSql, Object... args){
        return null;
    }
}
