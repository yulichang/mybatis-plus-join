package com.github.yulichang.processor.utils;

public final class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String getSimpleName(String fullName) {
        if (isEmpty(fullName) && fullName.lastIndexOf(".") == -1) {
            return fullName;
        }
        return fullName.substring(fullName.lastIndexOf(".") + 1);
    }
}
