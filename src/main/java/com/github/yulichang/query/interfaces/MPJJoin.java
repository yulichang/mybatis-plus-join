package com.github.yulichang.query.interfaces;

import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.Constant;

/**
 * @author yulichang
 */
@SuppressWarnings("unused")
public interface MPJJoin<Children> extends MPJBaseJoin {

    default Children leftJoin(String joinSql) {
        return leftJoin(true, joinSql);
    }

    default Children leftJoin(boolean condition, String joinSql) {
        return join(Constant.LEFT_JOIN, condition, joinSql);
    }

    default Children rightJoin(String joinSql) {
        return rightJoin(true, joinSql);
    }

    default Children rightJoin(boolean condition, String joinSql) {
        return join(Constant.RIGHT_JOIN, condition, joinSql);
    }

    default Children innerJoin(String joinSql) {
        return innerJoin(true, joinSql);
    }

    default Children innerJoin(boolean condition, String joinSql) {
        return join(Constant.INNER_JOIN, condition, joinSql);
    }

    Children join(String keyWord, boolean condition, String joinSql);
}
