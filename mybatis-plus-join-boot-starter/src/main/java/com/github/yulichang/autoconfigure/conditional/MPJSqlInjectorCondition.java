package com.github.yulichang.autoconfigure.conditional;


import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author yulichang
 * @since 1.4.5
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Conditional(OnSqlInjectorCondition.class)
public @interface MPJSqlInjectorCondition {
}
