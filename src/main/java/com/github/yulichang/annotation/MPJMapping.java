package com.github.yulichang.annotation;


import java.lang.annotation.*;

/**
 * 关系映射注解
 *
 * @author yulichang
 * @since 1.2.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface MPJMapping {

    /**
     * 关联的数据库实体类
     */
    Class<?> tag();

    /**
     * 当前类的关联的字段名称 (是实体类字段名称而不是数据库字段名称)
     * 默认获取当前类上定义的主键 @TableId
     */
    String thisField() default "";

    /**
     * 关联类的字段名称 (是实体类字段名称而不是数据库字段名称)
     * 默认获取关联类上定义的主键 @TableId
     */
    String joinField() default "";

    /**
     * 当前类的属性数据结构 是否是Map 或 List<Map>
     * 如果是 true  关联查询会调用 getMap() / listMaps() 等Map相关方法进行匹配
     * 如果是 false 关联查询会调用 getOne() / getById() / list() 等实体类相关方法进行匹配
     */
    boolean isMap() default false;

    /**
     * 一对一查询时 如果查询到多条记录是否抛出异常
     * true  抛出异常
     * false 不抛异常，获取列表第一条数据
     */
    boolean isThrowExp() default true;

    /**
     * 针对查询结果map的时候使用
     * 默认为thisField对应的数据库列名
     * <p>
     * 如果此类是以map方式查询的
     * 并且查询出来的map的key不是thisField对应的数据库列名就需要设置
     */
    String thisMapKey() default "";

    /**
     * isMap为true时生效
     * 针对查询结果map的时候使用
     * 默认为joinField对应的数据库列名
     * <p>
     * 如果此类是以map方式查询的
     * 并且查询出来的map的key不是thisField对应的数据库列名就需要设置
     */
    String joinMapKsy() default "";

    /**
     * 映射表查询条件之 select
     * 等效于 Wrappers.<T>query().select(xxx);
     */
    String select() default "";

    /**
     * 映射表查询条件之 first
     * 等效于 Wrappers.<T>query().first(xxx);
     */
    String first() default "";

    /**
     * 映射表查询条件之 apply
     * 等效于 Wrappers.<T>query().apply(xxx);
     */
    MPJMappingApply[] apply() default {};

    /**
     * 映射表查询条件
     */
    MPJMappingCondition[] condition() default {};

    /**
     * 映射表查询条件之 last
     * 建议不要在这使用分页语句，会导致关联查的时候查询不全
     * 等效于 Wrappers.<T>query().last(xxx);
     */
    String last() default "";
}
