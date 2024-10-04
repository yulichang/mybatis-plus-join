package com.github.yulichang.processor.matedata;

import com.github.yulichang.processor.utils.OgnlUtil;

/**
 * apt ognl表达式上下文
 *
 * @author yulichang
 * @since 1.5.0
 */
@SuppressWarnings("unused")
public class OgnlRoot {

    /**
     * 类名
     */
    private final String className;

    /**
     * 包名
     */
    private final String classPackage;

    private final static OgnlUtil util = new OgnlUtil();

    public OgnlRoot(String className, String classPackage) {
        this.className = className;
        this.classPackage = classPackage;
    }

    public String getClassName() {
        return className;
    }

    public String getClassPackage() {
        return classPackage;
    }

    public OgnlUtil getUtil() {
        return util;
    }
}
