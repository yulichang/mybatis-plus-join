package com.github.yulichang.method;

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
    SELECT_JOIN_COUNT("selectJoinCount", "查询满足条件总记录数",
            "<script>\n%s SELECT COUNT(%s) FROM %s %s %s %s %s\n</script>"),

    SELECT_JOIN_ONE("selectJoinOne", "返回一条记录",
            "<script>\n%s SELECT %s %s FROM %s %s %s %s %s\n</script>"),

    SELECT_JOIN_LIST("selectJoinList", "返回List集合",
            "<script>\n%s SELECT %s %s FROM %s %s %s %s %s\n</script>"),

    SELECT_JOIN_MAP("selectJoinMap", "返回一个Map",
            "<script>\n%s SELECT %s %s FROM %s %s %s %s %s\n</script>"),

    SELECT_JOIN_MAPS("selectJoinMaps", "返回Map集合",
            "<script>\n%s SELECT %s %s FROM %s %s %s %s %s\n</script>"),

    SELECT_JOIN_PAGE("selectJoinPage", "连表查询并分页",
            "<script>\n%s SELECT %s %s FROM %s %s %s %s %s\n</script>"),

    SELECT_JOIN_MAPS_PAGE("selectJoinMapsPage", "返回Map集合并分页",
            "<script>\n%s SELECT %s %s FROM %s %s %s %s %s\n</script>");

    private final String method;
    private final String sql;

    @SuppressWarnings("unused")
    SqlMethod(String method, String desc, String sql) {
        this.method = method;
        this.sql = sql;
    }

    public String getMethod() {
        return method;
    }


    public String getSql() {
        return sql;
    }


    public static final List<String> collect = Arrays.stream(SqlMethod.values()).map(SqlMethod::getMethod).collect(Collectors.toList());
}
