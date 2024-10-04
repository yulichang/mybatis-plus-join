package com.github.yulichang.processor.utils;

import java.util.Arrays;
import java.util.stream.Collectors;

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

    public static boolean matches(String packageName, String packageRegex) {
        if (packageRegex.lastIndexOf("*") != -1) {
            String regex = Arrays.stream(packageRegex.split("\\.")).map(r -> {
                if (r.equals("**")) {
                    return "[A-Za-z0-9_.]*";
                } else if (r.equals("*")) {
                    return "\\w*";
                } else {
                    return r;
                }
            }).collect(Collectors.joining("\\."));
            return packageName.matches(regex);
        }
        return packageRegex.equals(packageName);
    }
}
