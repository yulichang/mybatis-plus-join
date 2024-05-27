package com.github.yulichang.test.util;

import com.baomidou.mybatisplus.annotation.DbType;

import java.util.Arrays;

public enum DbTypeEnum {

    H2(DbType.H2, ""),
    MYSQL(DbType.MYSQL, ""),
    POSTGRE_SQL(DbType.POSTGRE_SQL, "pgsql/"),
    ORACLE(DbType.ORACLE, "oracle/");

    private final DbType dbType;

    private final String path;

    DbTypeEnum(DbType dbType, String path) {
        this.dbType = dbType;
        this.path = path;
    }

    public static String getPath(DbType dbType) {
        return Arrays.stream(DbTypeEnum.values()).filter(e -> e.dbType == dbType).findFirst().map(e -> e.path).orElse("");
    }
}
