package com.github.yulichang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * apt注解
 * <p>
 * 默认为String.format
 * <p>
 * 举例：
 * <ul>
 *   <li>全大写 %S</li>
 *   <li>加APT后缀 %sAPT</li>
 *   <li>加APT前缀 APT%s</li>
 *   <li>加APT后缀并且大写 %SAPT</li>
 * </ul>
 * <p>
 * 支持Ognl语法字段说明<br/>
 * Ognl上下文
 * <ul>
 *   <li>classInfo {@link com.github.yulichang.apt.OgnlRoot.ClassInfo}</li>
 *   <li>stringHelper {@link com.github.yulichang.apt.OgnlRoot.StringHelper}</li>
 * </ul>
 * 指定开头 Ognl# 这不是ognl语法，这是MPJ规定的 用于区分 ognl还是String.format
 * <p>
 * 举例：
 * <ul>
 *   <li>去掉3长度的后缀并且大写  Ognl#classInfo.className.substring(0, classInfo.className.length() - 3).toUpperCase() </li>
 *   <li>以“APT”结尾 Ognl#classInfo.className + 'APT'</li>
 *   <li>全大写并且以“APT”结尾 Ognl#classInfo.className.toUpperCase() + 'APT' </li>
 *   <li>"PO"结尾替换为“APT”且全大写  Ognl#stringHelper.replaceSuffix(classInfo.className, 'PO', 'APT').toUpperCase() </li>
 * </ul>
 * <p>
 * 支持 三元运算 String所有方法 如lastIndexOf subString toUpperCase等 Ognl语法<p>
 *
 * @author yulichang
 * @since 1.5.0
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * APT类名
     * <p>
     * 支持Ognl 默认使用String.format()
     */
    String value() default "%sCol";

    /**
     * 生成类的包名
     * <p>
     * 支持Ognl 默认使用String.format()
     */
    String packageName() default "%s.apt";

    /**
     * 是否在Tables中生成对应的类字段
     */
    boolean genTables() default true;

    /**
     * Tables中的字段名 默认大写的类名
     * <p>
     * 支持Ognl 默认使用String.format()
     */
    String tablesPackageName() default "%s.tables";

    /**
     * Tables中的字段名 默认大写的类名
     * <p>
     * 支持Ognl 默认使用String.format()
     */
    String tablesName() default "%S";

}