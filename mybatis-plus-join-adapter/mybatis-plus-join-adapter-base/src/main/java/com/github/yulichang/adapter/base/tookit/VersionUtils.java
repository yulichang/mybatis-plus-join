package com.github.yulichang.adapter.base.tookit;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;

/**
 * 版本工具类
 *
 * @author yulichang
 * @since 1.4.4
 */
public class VersionUtils {

    public static String version = MybatisPlusVersion.getVersion();

    @SuppressWarnings("unused")
    public static void setMybatisPlusVersion(String version) {
        VersionUtils.version = version;
    }

    public static String getVersion() {
        if (version != null) {
            return version;
        } else {
            throw ExceptionUtils.mpe("mybatis-plus version is blank, " +
                    "please add VersionUtils.setMybatisPlusVersion(?) code before running application");
        }
    }

    public static int compare(String v1, String v2) {
        String[] v1s = v1.split("-")[0].split("\\.");
        String[] v2s = v2.split("\\.");

        String[] vs = v1s.length > v2s.length ? v2s : v1s;
        for (int i = 0; i < vs.length; i++) {
            int compareV = compareV(v1s[i], v2s[i]);
            if (compareV != 0) {
                return compareV;
            }
        }
        if (v1s.length == v2s.length) {
            return 0;
        }
        return v1s.length > v2s.length ? 1 : -1;
    }

    private static int compareV(String v1, String v2) {
        if (isNumber(v1)) {
            if (isNumber(v2)) {
                return Integer.valueOf(v1).compareTo(Integer.valueOf(v2));
            } else {
                return 1;
            }
        } else {
            if (isNumber(v2)) {
                return -1;
            } else {
                return v1.compareTo(v2);
            }
        }
    }

    private static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e2) {
            return false;
        }
    }
}
