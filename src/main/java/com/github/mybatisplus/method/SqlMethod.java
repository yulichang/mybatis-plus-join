package com.github.mybatisplus.method;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yulichang
 * @see com.baomidou.mybatisplus.core.enums.SqlMethod
 * @since 2021/1/15
 */
public enum SqlMethod {

    /**
     * 连表查询
     */
    SELECT_JOIN_ONE("selectJoinOne", "返回一条记录",
            "<script>\nSELECT %s FROM %s <if test=\"ew.alias != null and ew.alias != ''\">${ew.alias}</if> <if test=\"ew.from != null and ew.from != ''\">${ew.from}</if> %s %s\n</script>"),

    SELECT_JOIN_LIST("selectJoinList", "返回List集合",
            "<script>\nSELECT %s FROM %s <if test=\"ew.alias != null and ew.alias != ''\">${ew.alias}</if> <if test=\"ew.from != null and ew.from != ''\">${ew.from}</if> %s %s\n</script>"),

    SELECT_JOIN_MAP("selectJoinMap", "返回一个Map",
            "<script>\nSELECT %s FROM %s <if test=\"ew.alias != null and ew.alias != ''\">${ew.alias}</if> <if test=\"ew.from != null and ew.from != ''\">${ew.from}</if> %s %s\n</script>"),

    SELECT_JOIN_MAPS("selectJoinMaps", "返回Map集合",
            "<script>\nSELECT %s FROM %s <if test=\"ew.alias != null and ew.alias != ''\">${ew.alias}</if> <if test=\"ew.from != null and ew.from != ''\">${ew.from}</if> %s %s\n</script>"),

    SELECT_JOIN_PAGE("selectJoinPage", "连表查询并分页",
            "<script>\nSELECT %s FROM %s <if test=\"ew.alias != null and ew.alias != ''\">${ew.alias}</if> <if test=\"ew.from != null and ew.from != ''\">${ew.from}</if> %s %s\n</script>"),

    SELECT_JOIN_MAPS_PAGE("selectJoinMapsPage", "返回Map集合并分页",
            "<script>\nSELECT %s FROM %s <if test=\"ew.alias != null and ew.alias != ''\">${ew.alias}</if> <if test=\"ew.from != null and ew.from != ''\">${ew.from}</if> %s %s\n</script>");

    private final String method;
    private final String desc;
    private final String sql;

    SqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.desc = desc;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }

    public String getDesc() {
        return desc;
    }

    public String getSql() {
        return sql;
    }


    public static final List<String> collect = Arrays.stream(SqlMethod.values()).map(SqlMethod::getMethod).collect(Collectors.toList());
}
