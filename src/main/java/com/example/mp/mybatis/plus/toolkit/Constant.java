package com.example.mp.mybatis.plus.toolkit;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

/**
 * @author yulichang
 */
public interface Constant {
    /**
     * 表别名
     */
    String TABLE_ALIAS = "t";

    String AS = " AS ";

    String ON = " ON ";

    String EQUALS = " = ";

    String JOIN = "JOIN";

    String LEFT = "LEFT";

    String RIGHT = "RIGHT";

    String INNER = "INNER";

    /**
     * " LEFT JOIN "
     */
    String LEFT_JOIN = StringPool.SPACE + LEFT + StringPool.SPACE + JOIN + StringPool.SPACE;

    /**
     * " RIGHT JOIN "
     */
    String RIGHT_JOIN = StringPool.SPACE + RIGHT + StringPool.SPACE + JOIN + StringPool.SPACE;

    /**
     * " INNER JOIN "
     */
    String INNER_JOIN = StringPool.SPACE + INNER + StringPool.SPACE + JOIN + StringPool.SPACE;
}
