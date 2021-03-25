package com.github.yulichang.toolkit;

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

    String CLAZZ = "clazz";

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

    /**
     * " t"
     */
    String SPACE_TABLE_ALIAS = StringPool.SPACE + Constant.TABLE_ALIAS;

    /**
     * " ON t"
     */
    String ON_TABLE_ALIAS = Constant.ON + Constant.TABLE_ALIAS;

    /**
     * " = t"
     */
    String EQUALS_TABLE_ALIAS = Constant.EQUALS + Constant.TABLE_ALIAS;
}
