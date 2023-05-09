package com.github.yulichang.extension.mapping.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.annotation.EntityMapping;
import com.github.yulichang.annotation.FieldMapping;
import com.github.yulichang.extension.mapping.config.DeepConfig;
import com.github.yulichang.extension.mapping.relation.Relation;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 深度查询
 * <p>
 * 对配置了映射注解的字段进行查询
 * 目前查询深度只支持2级(只解析当前实体类的映射注解,不会对查询结果再次解析注解)
 * 多级查询可能存在循环引用的问题，也可能会导致全量查询
 *
 * @author yulichang
 * @see EntityMapping
 * @see FieldMapping
 * @since 1.2.0
 */
@SuppressWarnings({"unused"})
public interface MPJDeepService<T> extends IService<T> {

    /**
     * 根据 ID 查询 并关联全部映射
     *
     * @param id 主键ID
     */
    default T getByIdDeep(Serializable id) {
        return Relation.mpjGetRelation(getById(id), DeepConfig.defaultConfig());
    }

    /**
     * 根据 ID 查询 并关联指定映射
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param id     主键ID
     * @param config 映射配置
     */
    default <R> T getByIdDeep(Serializable id, Function<DeepConfig.Builder<T>, DeepConfig.Builder<T>> config) {
        return Relation.mpjGetRelation(getById(id), config.apply(DeepConfig.builder()).build());
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectByIdDeep(1, Arrays.asList(User::getId, ... ))
     *
     * @param id     主键ID
     * @param config 映射配置
     */
    default <R> T getByIdDeep(Serializable id, DeepConfig<T> config) {
        return Relation.mpjGetRelation(getById(id), config);
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     */
    default List<T> listByIdsDeep(Collection<? extends Serializable> idList) {
        return Relation.mpjGetRelation(listByIds(idList), DeepConfig.defaultConfig());
    }

    /**
     * 查询（根据ID 批量查询）
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     * @param config 映射配置
     */
    default <R> List<T> listByIdsDeep(Collection<? extends Serializable> idList, Function<DeepConfig.Builder<T>, DeepConfig.Builder<T>> config) {
        return Relation.mpjGetRelation(listByIds(idList), config.apply(DeepConfig.builder()).build());
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectBatchIdsDeep(idList, Arrays.asList(User::getId, ... ))
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     * @param config 映射配置
     */
    default <R> List<T> listByIdsDeep(Collection<? extends Serializable> idList, DeepConfig<T> config) {
        return Relation.mpjGetRelation(listByIds(idList), config);
    }

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    default List<T> listByMapDeep(Map<String, Object> columnMap) {
        return Relation.mpjGetRelation(listByMap(columnMap), DeepConfig.defaultConfig());
    }

    /**
     * 查询（根据 columnMap 条件）
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param columnMap 表字段 map 对象
     * @param config    映射配置
     */
    default <R> List<T> listByMapDeep(Map<String, Object> columnMap, Function<DeepConfig.Builder<T>, DeepConfig.Builder<T>> config) {
        return Relation.mpjGetRelation(listByMap(columnMap), config.apply(DeepConfig.builder()).build());
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectByMapDeep(columnMap, Arrays.asList(User::getId, ... ))
     *
     * @param columnMap 表字段 map 对象
     * @param config    映射配置
     */
    default <R> List<T> listByMapDeep(Map<String, Object> columnMap, DeepConfig<T> config) {
        return Relation.mpjGetRelation(listByMap(columnMap), config);
    }

    /**
     * 根据 entity 条件，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default T getOneDeep(Wrapper<T> queryWrapper) {
        return Relation.mpjGetRelation(getOne(queryWrapper), DeepConfig.defaultConfig());
    }

    /**
     * 根据 entity 条件，查询一条记录
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param config       映射配置
     */
    default <R> T getOneDeep(Wrapper<T> queryWrapper, Function<DeepConfig.Builder<T>, DeepConfig.Builder<T>> config) {
        return Relation.mpjGetRelation(getOne(queryWrapper), config.apply(DeepConfig.builder()).build());
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectOneDeep(queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param config       映射配置
     */
    default <R> T getOneDeep(Wrapper<T> queryWrapper, DeepConfig<T> config) {
        return Relation.mpjGetRelation(getOne(queryWrapper), config);
    }

    /**
     * 根据 entity 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default List<T> listDeep(Wrapper<T> queryWrapper) {
        return Relation.mpjGetRelation(list(queryWrapper), DeepConfig.defaultConfig());
    }

    /**
     * 根据 entity 条件，查询全部记录
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param config       映射配置
     */
    default <R> List<T> listDeep(Wrapper<T> queryWrapper, Function<DeepConfig.Builder<T>, DeepConfig.Builder<T>> config) {
        return Relation.mpjGetRelation(list(queryWrapper), config.apply(DeepConfig.builder()).build());
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectListDeep(queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param config       映射配置
     */
    default <R> List<T> listDeep(Wrapper<T> queryWrapper, DeepConfig<T> config) {
        return Relation.mpjGetRelation(list(queryWrapper), config);
    }

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default <E extends IPage<T>> E pageDeep(E page, Wrapper<T> queryWrapper) {
        E e = page(page, queryWrapper);
        Relation.mpjGetRelation(e.getRecords(), DeepConfig.defaultConfig());
        return e;
    }

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param config       映射配置
     */
    default <R, E extends IPage<T>> E pageDeep(E page, Wrapper<T> queryWrapper, Function<DeepConfig.Builder<T>, DeepConfig.Builder<T>> config) {
        E e = page(page, queryWrapper);
        Relation.mpjGetRelation(e.getRecords(), config.apply(DeepConfig.builder()).build());
        return e;
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectPageDeep(page, queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param config       映射配置
     */
    default <R, E extends IPage<T>> E pageDeep(E page, Wrapper<T> queryWrapper, DeepConfig<T> config) {
        E e = page(page, queryWrapper);
        Relation.mpjGetRelation(e.getRecords(), config);
        return e;
    }
}
