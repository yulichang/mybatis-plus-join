package com.github.yulichang.apt;

import lombok.Getter;

import java.util.Objects;

/**
 * apt ognl表达式上下文
 *
 * @author yulichang
 * @since 1.5.0
 */
@Getter
public class OgnlRoot {

    private final String className;

    private final String classPackage;

    private final StringHelper util = new StringHelper();

    public OgnlRoot(String className, String classPackage) {
        this.className = className;
        this.classPackage = classPackage;
    }

    @SuppressWarnings("unused")
    public static final class StringHelper {
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
}
