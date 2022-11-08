package com.github.yulichang.wrapper.enums;

/**
 * 常用的sql函数枚举 默认实现
 * 可以自己实现接口 {@link BaseFuncEnum} 自定义函数
 * 目前支持一个占位符,不支持多个%s
 * <p>
 * 只例举几个通用的,其他函数 first() last() len() ucase() lcase() 等 或者数据库自定义函数请自行扩展
 * <p> mpj的初衷是只针对简单的连表查询,而非相对复杂的查询,
 * <p> 建议慎用,复杂的查询还是推荐写sql
 *
 * @author yulichang
 */
public enum DefaultFuncEnum implements BaseFuncEnum {

    SUM("SUM(%s)"),
    COUNT("COUNT(%s)"),
    MAX("MAX(%s)"),
    MIN("MIN(%s)"),
    AVG("AVG(%s)"),
    LEN("LEN(%s)");

    private final String sql;

    DefaultFuncEnum(String sql) {
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return this.sql;
    }

}
