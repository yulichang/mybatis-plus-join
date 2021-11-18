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

    String JOIN = "JOIN";

    String LEFT = "LEFT";

    String RIGHT = "RIGHT";

    String INNER = "INNER";

    String CLAZZ = "resultTypeClass_Eg1sG";

    String PARAM_TYPE = "paramType_Gr8re1Ee";

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
}
