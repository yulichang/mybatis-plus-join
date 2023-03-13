package com.github.yulichang.annotation;

import java.lang.annotation.*;

/**
 * 动态表名注解
 *
 * @author yulichang
 * @since 1.4.4
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DynamicTableName {
}
