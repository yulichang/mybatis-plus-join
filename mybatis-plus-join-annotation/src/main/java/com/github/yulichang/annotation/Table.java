package com.github.yulichang.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * apt注解
 * <p>
 * 支持Ognl语法字段说明<br/>
 * Ognl上下文
 * <ul>
 *   <li>classInfo {@link com.github.yulichang.apt.OgnlRoot.ClassInfo}</li>
 *   <li>stringHelper {@link com.github.yulichang.apt.OgnlRoot.StringHelper}</li>
 * </ul>
 * <p>
 * 指定开头 Ognl# 这不是ognl语法，这是MPJ规定的 用于区分 ognl还是String.format
 * <p>
 * 举例：
 * <ul>
 *   <li>去掉3长度的后缀并且大写  Ognl#classInfo.getClassName().substring(0, classInfo.getClassName().length() - 3).toUpperCase() </li>
 *   <li>以“APT”结尾 Ognl#classInfo.getClassName() + 'APT'</li>
 *   <li>全大写并且以“APT”结尾 Ognl#classInfo.getClassName().toUpperCase() + 'APT' </li>
 *   <li>"PO"结尾替换为“APT”且全大写  Ognl#stringHelper.replaceSuffix(classInfo.getClassName(), 'PO', 'APT').toUpperCase() </li>
 * </ul>
 * <p>
 * 支持 三元运算 String所有方法 如lastIndexOf subString toUpperCase等 Ognl语法<p>
 * 若想扩展stringHelper可在github交流
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
     * Tables中的字段名 默认大写的类名
     * <p>
     * 支持Ognl 默认使用String.format()
     */
    String tablesName() default "%S";

}