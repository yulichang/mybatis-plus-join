package com.github.yulichang.test.util;

import com.baomidou.mybatisplus.annotation.DbType;

@SuppressWarnings("unused")
public class EnabledIfConfig {

    public static final String runWithMysql = "#{T(com.github.yulichang.test.util.EnabledIfConfig).runWithMysql()}";

    public static boolean runWithMysql() {
        return DbTypeUtil.getDbType() == DbType.MYSQL;
    }


    public static final String runWithPgsql = "#{T(com.github.yulichang.test.util.EnabledIfConfig).runWithPgsql()}";

    public static boolean runWithPgsql() {
        return DbTypeUtil.getDbType() == DbType.POSTGRE_SQL;
    }


    public static final String runWithExcludingOracle = "#{T(com.github.yulichang.test.util.EnabledIfConfig).runWithExcludingOracle()}";

    public static boolean runWithExcludingOracle() {
        return DbTypeUtil.getDbType() != DbType.ORACLE;
    }
}
