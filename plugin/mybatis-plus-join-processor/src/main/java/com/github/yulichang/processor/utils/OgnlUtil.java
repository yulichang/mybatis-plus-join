package com.github.yulichang.processor.utils;

import java.util.Objects;

@SuppressWarnings("unused")
public final class OgnlUtil {
    /**
     * 移除后缀
     *
     * @param str    原字符串
     * @param suffix 指定后缀
     */
    public String removeSuffix(String str, String suffix) {
        if (isBlank(str) || isBlank(suffix)) {
            return str;
        }
        if (str.endsWith(suffix)) {
            return str.substring(0, str.length() - suffix.length());
        }
        return str;
    }

    /**
     * 替换后缀
     *
     * @param str         原字符串
     * @param suffix      指定后缀
     * @param replacement 新后缀
     */
    public String replaceSuffix(String str, String suffix, String replacement) {
        if (isBlank(str)) {
            return str;
        }
        String rep = Objects.isNull(replacement) ? "" : replacement;
        if (isBlank(suffix)) {
            return str + rep;
        }
        if (str.endsWith(suffix)) {
            return str.substring(0, str.length() - suffix.length()) + rep;
        }
        return str;
    }

    /**
     * 获取上级包名
     *
     * @param pk 报名
     * @return 上级报名
     */
    public String getParentPackage(String pk) {
        if (pk.lastIndexOf(".") > -1) {
            return pk;
        }
        return pk.substring(0, pk.lastIndexOf('.'));
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
