package com.github.mybatisplus.query.interfaces;

public interface MyJoin<Children> {

    default Children leftJoin(String joinSql) {
        return leftJoin(true, joinSql);
    }

    Children leftJoin(boolean condition, String joinSql);

    default Children rightJoin(String joinSql) {
        return rightJoin(true, joinSql);
    }

    Children rightJoin(boolean condition, String joinSql);

    default Children innerJoin(String joinSql) {
        return innerJoin(true, joinSql);
    }

    Children innerJoin(boolean condition, String joinSql);
}
