package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

/**
 * @author yulichang
 */
public interface Constant {

    String AS = " AS ";

    String ON = " ON ";

    String JOIN = "JOIN";

    String LEFT = "LEFT";

    String RIGHT = "RIGHT";

    String INNER = "INNER";

    String FULL = "FULL";

    String CLAZZ = "resultTypeClass_Eg1sG";

    /**
     * "LEFT JOIN"
     */
    String LEFT_JOIN = LEFT + StringPool.SPACE + JOIN;

    /**
     * "RIGHT JOIN"
     */
    String RIGHT_JOIN = RIGHT + StringPool.SPACE + JOIN;

    /**
     * "INNER JOIN"
     */
    String INNER_JOIN = INNER + StringPool.SPACE + JOIN;

    /**
     * "FULL JOIN"
     */
    String FULL_JOIN = FULL + StringPool.SPACE + JOIN;
}
