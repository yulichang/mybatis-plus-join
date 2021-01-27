package com.example.mp.mybatis.plus.wrapper;

public enum SelectFunc {

    SUM("SUM( %s )", "求和"),
    COUNT("COUNT( %s )", "数量"),
    AVG("AVG( %s )", "平均值"),
    MAX("MAX( %s )", "最大值"),
    MIN("MIN( %s )", "最小值"),
    DATE_FORMAT("DATE_FORMAT( %s ,'%Y-%m-%d %H:%i:%s')", "日期格式化");

    private final String sql;
    private final String desc;

    SelectFunc(String sql, String desc) {
        this.sql = sql;
        this.desc = desc;
    }

    public String getSql() {
        return sql;
    }

    public String getDesc() {
        return desc;
    }
}
