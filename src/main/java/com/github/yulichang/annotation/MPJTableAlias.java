package com.github.yulichang.annotation;

import java.lang.annotation.*;

/**
 * 关联查询时的表别名
 * 框架默认会随机生成 一般是 t1 t2 t3 ...
 * 这个注解定义的表别名或者随机生成的别名只对MPJLambdaWrapper生效
 * 对MPJQueryWrapper不生效,
 *
 * @author yulichang
 * @see com.github.yulichang.wrapper.MPJLambdaWrapper
 * @see com.github.yulichang.query.MPJQueryWrapper
 * @since 1.2.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
public @interface MPJTableAlias {

    String value() default "";
}
