package com.github.yulichang.annotation;

/**
 * 映射表条件
 * 用法参考 mybatis plus wrapper 的 .apply()方法
 *
 * @author yulichang
 * @since 1.2.0
 */
public @interface MPJMappingApply {

    /**
     * sql片段
     */
    String value();

    /**
     * .apply() 对应的可变参数
     */
    String[] args() default {};
}
