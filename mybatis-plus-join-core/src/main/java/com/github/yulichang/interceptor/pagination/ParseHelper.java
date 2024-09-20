package com.github.yulichang.interceptor.pagination;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.github.yulichang.toolkit.MPJReflectionKit;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * sql 分页解析工具类
 *
 * @author yulichang
 * @since 1.5.0
 */
@SuppressWarnings("unused")
public final class ParseHelper {

    private static final Map<SqlSource, SqlSourceWrapper> SQL_SOURCE_CACHE = new ConcurrentHashMap<>();

    private static final String prefix = "MPJ_Param_i_";
    private static final String suffix = "_MPJ_Param_i";
    private static final char placeholder_char = '?';
    private static final String placeholder_str = String.valueOf(placeholder_char);

    public static final Function<Object, String> format = index -> prefix + index + suffix;

    public static int countChar(String str) {
        return countChar(str, placeholder_char);
    }

    public static int countChar(String str, String tag) {
        return countChar(str, tag.charAt(0));
    }

    public static int countChar(String str, char tag) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == tag) {
                count++;
            }
        }
        return count;
    }

    public static String decode(String str) {
        return decode(str, null);
    }

    public static String decode(String str, Function<Object, String> formatter) {
        return decode(str, formatter, placeholder_char);
    }

    public static String decode(String str, Function<Object, String> formatter, char placeholder) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == placeholder) {
                sb.append(format.apply(formatter == null ? count : formatter.apply(count)));
                count++;
            } else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    public static String decode(String str, Function<Object, String> formatter, String placeholder) {
        StringBuilder sb = new StringBuilder();
        String[] split = str.split(placeholder);
        for (int i = 0; i < split.length; i++) {
            sb.append(split[i]);
            if (i < split.length - 1) {
                sb.append(format.apply(formatter == null ? i : formatter.apply(i)));
            }
        }
        return sb.toString();
    }

    public static String encode(List<ParameterMapping> mappings, int count, String repSql, Map<Integer, ParameterMapping> sortMap) {
        return encode(mappings, count, repSql, sortMap, placeholder_str);
    }

    public static String encode(List<ParameterMapping> mappings, int count, String repSql, Map<Integer, ParameterMapping> sortMap, String placeholder) {
        for (int i = 0; i < count; i++) {
            String repStr = ParseHelper.format.apply(i);
            int i1 = repSql.indexOf(repStr);
            if (i1 != -1) {
                repSql = repSql.replace(repStr, placeholder);
                sortMap.put(i1, mappings.get(i));
            }
        }
        return repSql;
    }

    public static String getOriginalSql(Object parameter, DynamicSqlSource sqlSource) {
        Assert.notNull(sqlSource, "sqlSource must not be null");
        SqlSourceWrapper sqlSourceWrapper = SQL_SOURCE_CACHE.computeIfAbsent(sqlSource, key -> {
            Configuration configuration = MPJReflectionKit.getFieldValue(sqlSource, "configuration");
            SqlNode sqlNode = MPJReflectionKit.getFieldValue(sqlSource, "rootSqlNode");
            return new SqlSourceWrapper(configuration, sqlNode);
        });
        return ParseHelper.decode(sqlSourceWrapper.getSql(parameter), null, ParseHelper.format.apply(""));
    }
}
