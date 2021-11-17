package com.github.yulichang.annotation;

import java.lang.annotation.*;

/**
 * 关联查询时的表别名<br/>
 * 框架默认会随机生成 一般是 t1 t2 t3 ...<br/>
 * 不要在程序中使用随机别名，运行期间是不变的，但是重启就不一定了<br/>
 * 如需使用请配合此注解一起使用<br/>
 * <p>
 * 这个注解定义的表别名或者随机生成的别名只对MPJLambdaWrapper生效<br/>
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

    String value();
}
