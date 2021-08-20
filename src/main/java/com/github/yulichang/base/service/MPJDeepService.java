package com.github.yulichang.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.MPJTableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.MPJTableInfo;
import com.baomidou.mybatisplus.core.metadata.MPJTableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.annotation.MPJMapping;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.toolkit.LambdaUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 深度查询
 * <p>
 * 对配置了@MPJMapping注解的字段进行查询
 * 目前查询深度只支持2级(只解析当前实体类的MPJMapping注解,不会对查询结果再次解析注解)
 * 多级查询可能存在循环引用的问题，也可能会导致全量查询
 *
 * @author yulichang
 * @see MPJMapping
 * @since 1.2.0
 */
@SuppressWarnings({"unused", "unchecked"})
public interface MPJDeepService<T> extends IService<T> {

    Class<T> currentModelClass();

    /**
     * 根据 ID 深度查询
     *
     * @param id 主键ID列表
     */
    default T getByIdDeep(Serializable id) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectByIdDeep(id);
    }

    /**
     * 根据 ID 深度查询
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param id       主键ID列表
     * @param property 需要关联的字段
     */
    default <R> T getByIdDeep(Serializable id, SFunction<T, R>... property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectByIdDeep(id, property);
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： getByIdDeep(id, Arrays.asList(User::getId, ... ))
     *
     * @param id       主键ID列表
     * @param property 需要关联的字段
     */
    default <R> T getByIdDeep(Serializable id, List<SFunction<T, R>> property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectByIdDeep(id, property);
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表
     */
    default List<T> listByIdsDeep(Collection<? extends Serializable> idList) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectBatchIdsDeep(idList);
    }

    /**
     * 查询（根据ID 批量查询）
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param idList   主键ID列表
     * @param property 需要关联的字段
     */
    default <R> List<T> listByIdsDeep(Collection<? extends Serializable> idList, SFunction<T, R>... property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectBatchIdsDeep(idList, Arrays.asList(property));
    }

    /**
     * 查询（根据ID 批量查询）
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： listByIdsDeep(idList, Arrays.asList(User::getId, ... ))
     *
     * @param idList   主键ID列表
     * @param property 需要关联的字段
     */
    default <R> List<T> listByIdsDeep(Collection<? extends Serializable> idList, List<SFunction<T, R>> property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectBatchIdsDeep(idList, property);
    }

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    default List<T> listByMapDeep(Map<String, Object> columnMap) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectByMapDeep(columnMap);
    }

    /**
     * 查询（根据 columnMap 条件）
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param columnMap 表字段 map 对象
     * @param property  需要关联的字段
     */
    default <R> List<T> listByMapDeep(Map<String, Object> columnMap, SFunction<T, R>... property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectByMapDeep(columnMap, Arrays.asList(property));
    }

    /**
     * 查询（根据 columnMap 条件）
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： listByMapDeep(columnMap, Arrays.asList(User::getId, ... ))
     *
     * @param columnMap 表字段 map 对象
     * @param property  需要关联的字段
     */
    default <R> List<T> listByMapDeep(Map<String, Object> columnMap, List<SFunction<T, R>> property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectByMapDeep(columnMap, property);
    }


    /**
     * 根据 Wrapper，查询一条记录 <br/>
     * <p>结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")</p>
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    default T getOneDeep(Wrapper<T> queryWrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectOneDeep(queryWrapper);
    }

    /**
     * 根据 Wrapper，查询一条记录 <br/>
     * <p>结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")</p>
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R> T getOneDeep(Wrapper<T> queryWrapper, SFunction<T, R>... property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectOneDeep(queryWrapper, Arrays.asList(property));
    }

    /**
     * 根据 Wrapper，查询一条记录 <br/>
     * list为null或空，会查询全部映射关系<br/>
     * <p>结果集，如果是多个会抛出异常，随机取一条加上限制条件 wrapper.last("LIMIT 1")</p>
     * <p>
     * 例： getOneDeep(queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R> T getOneDeep(Wrapper<T> queryWrapper, List<SFunction<T, R>> property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectOneDeep(queryWrapper, property);
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param throwEx      有多个 result 是否抛出异常
     */
    default T getOneDeep(Wrapper<T> queryWrapper, boolean throwEx) {
        return ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(getOne(queryWrapper, throwEx), null);
    }

    /**
     * 根据 Wrapper，查询一条记录
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param throwEx      有多个 result 是否抛出异常
     * @param property     需要关联的字段
     */
    default <R> T getOneDeep(Wrapper<T> queryWrapper, boolean throwEx, SFunction<T, R>... property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(getOne(queryWrapper, throwEx), Arrays.asList(property));
    }

    /**
     * 根据 Wrapper，查询一条记录
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： getOneDeep(queryWrapper, throwEx, Arrays.asList(User::getId, ... ))
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param throwEx      有多个 result 是否抛出异常
     * @param property     需要关联的字段
     */
    default <R> T getOneDeep(Wrapper<T> queryWrapper, boolean throwEx, List<SFunction<T, R>> property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(getOne(queryWrapper, throwEx), property);
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    default Map<String, Object> getMapDeep(Wrapper<T> queryWrapper) {
        return queryMapMapping(getMap(queryWrapper), null);
    }

    /**
     * 根据 Wrapper，查询一条记录
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R> Map<String, Object> getMapDeep(Wrapper<T> queryWrapper, SFunction<T, R>... property) {
        return queryMapMapping(getMap(queryWrapper), Arrays.asList(property));
    }

    /**
     * 根据 Wrapper，查询一条记录
     * list为null或空，会查询全部映射关系
     * <p>
     * 例：getMapDeep(queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R> Map<String, Object> getMapDeep(Wrapper<T> queryWrapper, List<SFunction<T, R>> property) {
        return queryMapMapping(getMap(queryWrapper), property);
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    default List<T> listDeep(Wrapper<T> queryWrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectListDeep(queryWrapper);
    }

    /**
     * 查询列表
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R> List<T> listDeep(Wrapper<T> queryWrapper, SFunction<T, R>... property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectListDeep(queryWrapper, Arrays.asList(property));
    }

    /**
     * 查询列表
     * list为null或空，会查询全部映射关系
     * <p>
     * 例：listDeep(queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R> List<T> listDeep(Wrapper<T> queryWrapper, List<SFunction<T, R>> property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectListDeep(queryWrapper, property);
    }

    /**
     * 查询所有
     *
     * @see Wrappers#emptyWrapper()
     */
    default List<T> listDeep() {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectListDeep(Wrappers.emptyWrapper());
    }

    /**
     * 查询所有
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param property 需要关联的字段
     * @see Wrappers#emptyWrapper()
     */
    default <R> List<T> listDeep(SFunction<T, R>... property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectListDeep(Wrappers.emptyWrapper(), Arrays.asList(property));
    }

    /**
     * 查询所有
     * list为null或空，会查询全部映射关系
     * <p>
     * 例：listDeep(Arrays.asList(User::getId, ... ))
     *
     * @param property 需要关联的字段
     * @see Wrappers#emptyWrapper()
     */
    default <R> List<T> listDeep(List<SFunction<T, R>> property) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectListDeep(Wrappers.emptyWrapper(), property);
    }

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    default <E extends IPage<T>> E pageDeep(E page, Wrapper<T> queryWrapper) {
        E e = page(page, queryWrapper);
        ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(e.getRecords(), null);
        return e;
    }

    /**
     * 翻页查询
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R, E extends IPage<T>> E pageDeep(E page, Wrapper<T> queryWrapper, SFunction<T, R>... property) {
        E e = page(page, queryWrapper);
        ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(e.getRecords(), Arrays.asList(property));
        return e;
    }

    /**
     * 翻页查询
     * list为null或空，会查询全部映射关系
     * <p>
     * 例：pageDeep(page, queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R, E extends IPage<T>> E pageDeep(E page, Wrapper<T> queryWrapper, List<SFunction<T, R>> property) {
        E e = page(page, queryWrapper);
        ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(e.getRecords(), property);
        return e;
    }

    /**
     * 无条件翻页查询
     *
     * @param page 翻页对象
     * @see Wrappers#emptyWrapper()
     */
    default <E extends IPage<T>> E pageDeep(E page) {
        E e = page(page);
        ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(e.getRecords(), null);
        return e;
    }

    /**
     * 无条件翻页查询
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param page     翻页对象
     * @param property 需要关联的字段
     * @see Wrappers#emptyWrapper()
     */
    default <R, E extends IPage<T>> E pageDeep(E page, SFunction<T, R>... property) {
        E e = page(page);
        ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(e.getRecords(), Arrays.asList(property));
        return e;
    }

    /**
     * 无条件翻页查询
     * list为null或空，会查询全部映射关系
     * <p>
     * 例：pageDeep(page, Arrays.asList(User::getId, ... ))
     *
     * @param page     翻页对象
     * @param property 需要关联的字段
     * @see Wrappers#emptyWrapper()
     */
    default <R, E extends IPage<T>> E pageDeep(E page, List<SFunction<T, R>> property) {
        E e = page(page);
        ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(e.getRecords(), property);
        return e;
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    default List<Map<String, Object>> listMapsDeep(Wrapper<T> queryWrapper) {
        return queryMapMapping(listMaps(queryWrapper), null);
    }

    /**
     * 查询列表
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R> List<Map<String, Object>> listMapsDeep(Wrapper<T> queryWrapper, SFunction<T, R>... property) {
        return queryMapMapping(listMaps(queryWrapper), Arrays.asList(property));
    }

    /**
     * 查询列表
     * list为null或空，会查询全部映射关系
     * <p>
     * 例：listMapsDeep(queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R> List<Map<String, Object>> listMapsDeep(Wrapper<T> queryWrapper, List<SFunction<T, R>> property) {
        return queryMapMapping(listMaps(queryWrapper), property);
    }

    /**
     * 查询所有列表
     *
     * @see Wrappers#emptyWrapper()
     */
    default List<Map<String, Object>> listMapsDeep() {
        return queryMapMapping(listMaps(), null);
    }

    /**
     * 查询所有列表
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param property 需要关联的字段
     * @see Wrappers#emptyWrapper()
     */
    default <R> List<Map<String, Object>> listMapsDeep(SFunction<T, R>... property) {
        return queryMapMapping(listMaps(), Arrays.asList(property));
    }

    /**
     * 查询所有列表
     * list为null或空，会查询全部映射关系
     * <p>
     * 例：listMapsDeep(Arrays.asList(User::getId, ... ))
     *
     * @param property 需要关联的字段
     * @see Wrappers#emptyWrapper()
     */
    default <R> List<Map<String, Object>> listMapsDeep(List<SFunction<T, R>> property) {
        return queryMapMapping(listMaps(), property);
    }

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    default <E extends IPage<Map<String, Object>>> E pageMapsDeep(E page, Wrapper<T> queryWrapper) {
        E e = pageMaps(page, queryWrapper);
        queryMapMapping(e.getRecords(), null);
        return e;
    }

    /**
     * 翻页查询
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R, E extends IPage<Map<String, Object>>> E pageMapsDeep(E page, Wrapper<T> queryWrapper, SFunction<T, R>... property) {
        E e = pageMaps(page, queryWrapper);
        queryMapMapping(e.getRecords(), Arrays.asList(property));
        return e;
    }

    /**
     * 翻页查询
     * list为null或空，会查询全部映射关系
     * <p>
     * 例：pageMapsDeep(page, queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param property     需要关联的字段
     */
    default <R, E extends IPage<Map<String, Object>>> E pageMapsDeep(E page, Wrapper<T> queryWrapper, List<SFunction<T, R>> property) {
        E e = pageMaps(page, queryWrapper);
        queryMapMapping(e.getRecords(), property);
        return e;
    }

    /**
     * 无条件翻页查询
     *
     * @param page 翻页对象
     * @see Wrappers#emptyWrapper()
     */
    default <E extends IPage<Map<String, Object>>> E pageMapsDeep(E page) {
        E e = pageMaps(page);
        queryMapMapping(e.getRecords(), null);
        return e;
    }

    /**
     * 无条件翻页查询
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param page     翻页对象
     * @param property 需要关联的字段
     * @see Wrappers#emptyWrapper()
     */
    default <R, E extends IPage<Map<String, Object>>> E pageMapsDeep(E page, SFunction<T, R>... property) {
        E e = pageMaps(page);
        queryMapMapping(e.getRecords(), Arrays.asList(property));
        return e;
    }

    /**
     * 无条件翻页查询
     * list为null或空，会查询全部映射关系
     * <p>
     * 例：pageMapsDeep(page, Arrays.asList(User::getId, ... ))
     *
     * @param page     翻页对象
     * @param property 需要关联的字段
     * @see Wrappers#emptyWrapper()
     */
    default <R, E extends IPage<Map<String, Object>>> E pageMapsDeep(E page, List<SFunction<T, R>> property) {
        E e = pageMaps(page);
        queryMapMapping(e.getRecords(), property);
        return e;
    }

    /**
     * 查询映射关系<br/>
     * 对结果进行二次查询<br/>
     * 可以自行查询然后在通过此方法进行二次查询<br/>
     * list为null或空，会查询全部映射关系<br/>
     *
     * @param map 第一次查询结果
     */
    default <R> Map<String, Object> queryMapMapping(Map<String, Object> map, List<SFunction<T, R>> property) {
        if (CollectionUtils.isNotEmpty(map)) {
            MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(currentModelClass());
            if (tableInfo.isHasMapping()) {
                boolean hasProperty = CollectionUtils.isNotEmpty(property);
                List<String> list = hasProperty ? property.stream().map(LambdaUtils::getName).collect(
                        Collectors.toList()) : null;
                for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                    if (!hasProperty || list.contains(fieldInfo.getProperty())) {
                        Object o = map.get(fieldInfo.getThisMapKey());
                        if (o != null) {
                            List<?> data = (List<?>) fieldInfo.getJoinMapper().mappingWrapperConstructor(
                                    fieldInfo.isFieldIsMap(), SqlKeyword.EQ, fieldInfo.getJoinColumn(), o, fieldInfo);
                            MPJTableFieldInfo.bindMap(fieldInfo, map, data);
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 查询映射关系<br/>
     * 对结果进行二次查询<br/>
     * 可以自行查询然后在通过此方法进行二次查询<br/>
     * list为null或空，会查询全部映射关系<br/>
     *
     * @param list 第一次查询结果
     */
    @SuppressWarnings("unchecked")
    default <R> List<Map<String, Object>> queryMapMapping(List<Map<String, Object>> list, List<SFunction<T, R>> property) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(currentModelClass());
        if (tableInfo.isHasMapping()) {
            boolean hasProperty = CollectionUtils.isNotEmpty(property);
            List<String> listProperty = hasProperty ? property.stream().map(LambdaUtils::getName).collect(
                    Collectors.toList()) : null;
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!hasProperty || listProperty.contains(fieldInfo.getProperty())) {
                    List<Object> itemList = list.stream().map(m -> m.get(fieldInfo.getThisMapKey()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(itemList)) {
                        if (fieldInfo.isFieldIsMap()) {
                            List<Map<String, Object>> joinList = (List<Map<String, Object>>) fieldInfo.getJoinMapper()
                                    .mappingWrapperConstructor(fieldInfo.isFieldIsMap(), SqlKeyword.IN,
                                            fieldInfo.getJoinColumn(), itemList, fieldInfo);
                            list.forEach(i -> {
                                List<Map<String, Object>> data = joinList.stream().filter(j -> j.containsKey(
                                        fieldInfo.getJoinMapKey()) && j.get(fieldInfo.getJoinMapKey()).equals(
                                        i.get(fieldInfo.getThisMapKey()))).collect(Collectors.toList());
                                MPJTableFieldInfo.bindMap(fieldInfo, i, data);
                            });
                        } else {
                            List<?> joinList = (List<?>) fieldInfo.getJoinMapper().mappingWrapperConstructor(
                                    fieldInfo.isFieldIsMap(), SqlKeyword.IN, fieldInfo.getJoinColumn(), itemList, fieldInfo);
                            list.forEach(i -> {
                                List<?> data = joinList.stream().filter(j -> {
                                    Object o = fieldInfo.joinFieldGet(j);
                                    return o != null && o.equals(i.get(fieldInfo.getThisMapKey()));
                                }).collect(Collectors.toList());
                                MPJTableFieldInfo.bindMap(fieldInfo, i, data);
                            });
                        }
                    } else {
                        list.forEach(i -> i.put(fieldInfo.getField().getName(), new ArrayList<>()));
                    }
                }
            }
        }
        return list;
    }
}
