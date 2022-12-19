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

    default Children leftJoin(boolean condition, String joinSql) {
        return join(Constant.LEFT_JOIN, condition, joinSql);
    }


    /**
     * right join
     */
    default Children rightJoin(String joinSql) {
        return rightJoin(true, joinSql);
    }

    default Children rightJoin(boolean condition, String joinSql) {
        return join(Constant.RIGHT_JOIN, condition, joinSql);
    }


    /**
     * inner join
     */
    default Children innerJoin(String joinSql) {
        return innerJoin(true, joinSql);
    }

    default Children innerJoin(boolean condition, String joinSql) {
        return join(Constant.INNER_JOIN, condition, joinSql);
    }

    /**
     * full join
     */
    default Children fullJoin(String joinSql) {
        return fullJoin(true, joinSql);
    }

    default Children fullJoin(boolean condition, String joinSql) {
        return join(Constant.FULL_JOIN, condition, joinSql);
    }

    Children join(String keyWord, boolean condition, String joinSql);
}
