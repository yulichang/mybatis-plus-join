package com.github.yulichang.base.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.annotation.EntityMapping;
import com.github.yulichang.annotation.FieldMapping;
import com.github.yulichang.base.mapper.MPJRelationMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 深度查询
 * <p>
 * 对配置了映射注解的字段进行查询
 * 目前查询深度只支持2级(只解析当前实体类的映射注解,不会对查询结果再次解析注解)
 * 多级查询可能存在循环引用的问题，也可能会导致全量查询
 * 用于替换deep
 *
 * @author yulichang
 * @see EntityMapping
 * @see FieldMapping
 * @since 1.4.4
 */
@SuppressWarnings({"unused"})
public interface MPJRelationService<T> extends IService<T> {

    default <R, M extends BaseMapper<T>> R selectRelation(Function<M, R> function) {
        return selectRelation(function, new ArrayList<>());
    }

    /**
     * 通过注解实现单表多次查询
     *
     * @param function BaseMapper调用方法
     * @param list     属性过滤, 可以只查询需要映射的属性
     * @see com.github.yulichang.annotation.EntityMapping
     * @see com.github.yulichang.annotation.FieldMapping
     */
    default <R, M extends BaseMapper<T>> R selectRelation(Function<M, R> function, List<SFunction<T, ?>> list) {
        return ((MPJRelationMapper<T>) getBaseMapper()).selectRelation(function, list);
    }

}
