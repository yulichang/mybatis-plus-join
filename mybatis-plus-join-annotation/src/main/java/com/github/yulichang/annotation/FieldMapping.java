package com.github.yulichang.annotation;


import java.lang.annotation.*;

/**
 * 字段关系映射注解
 *
 * @author yulichang
 * @since 1.2.0
 */
@Documented
@SuppressWarnings({"unused"})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
public @interface FieldMapping {

    /**
     * 关联的数据库实体类<br/>
     * 默认获取此注解所对应的类
     */
    Class<?> tag();

    /**
     * 当前类的关联的字段名称 (是实体类字段名称而不是数据库字段名称)<br/>
     * 默认获取当前类上定义的主键 @TableId
     */
    String thisField() default "";

    /**
     * 关联类的字段名称 (是实体类字段名称而不是数据库字段名称)<br/>
     * 默认获取关联类上定义的主键 @TableId
     */
    String joinField() default "";

    /**
     * 一对一查询时 如果查询到多条记录是否抛出异常<br/>
     * true  抛出异常<br/>
     * false 不抛异常，获取列表第一条数据<br/>
     */
    boolean isThrowExp() default true;

    /**
     * 字段映射对应的属性名
     */
    String select();

    /**
     * 映射表查询条件之 first<br/>
     * 等效于 Wrappers.<T>query().first(xxx);
     */
    String first() default "";

    /**
     * 映射表查询条件之 apply<br/>
     * 等效于 Wrappers.<T>query().apply(xxx);
     */
    MPJMappingApply[] apply() default {};

    /**
     * 映射表查询条件
     */
    MPJMappingCondition[] condition() default {};

    /**
     * 映射表查询条件之 orderBy<br/>
     * 等效于 Wrappers.<T>query().orderByAsc(xxx);
     */
    String orderByAsc() default "";

    /**
     * 映射表查询条件之 orderByDesc<br/>
     * 等效于 Wrappers.<T>query().orderByDesc(xxx);
     */
    String orderByDesc() default "";

    /**
     * 映射表查询条件之 last<br/>
     * 建议不要在这使用分页语句，会导致关联查的时候查询不全<br/>
     * 等效于 Wrappers.<T>query().last(xxx);
     */
    String last() default "";
}
