package com.github.yulichang.annotation;

import java.lang.annotation.*;

/**
 * 动态表名注解
 * <p>
 * 1.5.2及以后无需添加此注解就可实现动态表名
 *
 * @author yulichang
 * @since 1.4.4
 */
@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DynamicTableName {
}
