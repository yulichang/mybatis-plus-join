package com.github.yulichang.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {

    /**
     * 生成类的类名前缀
     */
    String prefix() default "";

    /**
     * 生成类的类名后缀
     */
    String suffix() default "";

    /**
     * format 优先级低，如果配置了prefix或suffix则不会生效
     */
    String format() default "%sCol";

    /**
     * 生成类的包名
     */
    String packageName() default "apt";

    /**
     * Tables中的字段名 默认大写的类名
     */
    String tablesName() default "%S";


}

