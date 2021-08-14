package com.github.yulichang.annotation;

import java.lang.annotation.*;

/**
 * 关联查询时的表别名
 * 框架默认会随机生成 一般是 t1 t2 t3 ...
 * 不要在程序中使用随机别名，运行期间是不变的，但是重启就不一定了
 * 如需使用请配合此注解一起使用
 * <p>
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
