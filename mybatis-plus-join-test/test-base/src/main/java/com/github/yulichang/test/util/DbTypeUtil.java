package com.github.yulichang.test.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.toolkit.JdbcUtils;
import com.github.yulichang.toolkit.SpringContentUtils;
import org.springframework.core.env.Environment;

import java.util.Optional;

public class DbTypeUtil {

    private static DbType dbType;

    public static DbType getDbType() {
        return Optional.ofNullable(dbType).orElseGet(() -> {
            String jdbcUrl = SpringContentUtils.getBean(Environment.class).getProperty("spring.datasource.url");
            assert jdbcUrl != null;
            DbType type = JdbcUtils.getDbType(jdbcUrl);
            dbType = type;
            return type;
        });
    }
}
