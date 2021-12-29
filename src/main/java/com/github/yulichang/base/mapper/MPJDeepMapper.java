package com.github.yulichang.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.*;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.base.mapper.wrapper.MappingQuery;
import com.github.yulichang.toolkit.LambdaUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 关联查询
 *
 * @author yulichang
 * @see BaseMapper
 */
@SuppressWarnings({"unused", "unchecked"})
public interface MPJDeepMapper<T> extends BaseMapper<T> {

    /**
     * 根据 ID 查询 并关联全部映射
     *
     * @param id 主键ID
     */
    default T selectByIdDeep(Serializable id) {
        return mpjQueryMapping(selectById(id), null);
    }

    /**
     * 根据 ID 查询 并关联指定映射
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param id       主键ID
     * @param property 需要关联的字段
     */
    default <R> T selectByIdDeep(Serializable id, SFunction<T, R>... property) {
        return mpjQueryMapping(selectById(id), Arrays.asList(property));
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectByIdDeep(1, Arrays.asList(User::getId, ... ))
     *
     * @param id       主键ID
     * @param property 需要关联的字段
     */
    default <R> T selectByIdDeep(Serializable id, List<SFunction<T, R>> property) {
        return mpjQueryMapping(selectById(id), property);
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     */
    default List<T> selectBatchIdsDeep(Collection<? extends Serializable> idList) {
        return mpjQueryMapping(selectBatchIds(idList), null);
    }

    /**
     * 查询（根据ID 批量查询）
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param idList   主键ID列表(不能为 null 以及 empty)
     * @param property 需要关联的字段
     */
    default <R> List<T> selectBatchIdsDeep(Collection<? extends Serializable> idList, SFunction<T, R>... property) {
        return mpjQueryMapping(selectBatchIds(idList), Arrays.asList(property));
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectBatchIdsDeep(idList, Arrays.asList(User::getId, ... ))
     *
     * @param idList   主键ID列表(不能为 null 以及 empty)
     * @param property 需要关联的字段
     */
    default <R> List<T> selectBatchIdsDeep(Collection<? extends Serializable> idList, List<SFunction<T, R>> property) {
        return mpjQueryMapping(selectBatchIds(idList), property);
    }

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    default List<T> selectByMapDeep(Map<String, Object> columnMap) {
        return mpjQueryMapping(selectByMap(columnMap), null);
    }

    /**
     * 查询（根据 columnMap 条件）
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param columnMap 表字段 map 对象
     * @param property  需要关联的字段
     */
    default <R> List<T> selectByMapDeep(Map<String, Object> columnMap, SFunction<T, R>... property) {
        return mpjQueryMapping(selectByMap(columnMap), Arrays.asList(property));
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectByMapDeep(columnMap, Arrays.asList(User::getId, ... ))
     *
     * @param columnMap 表字段 map 对象
     * @param property  需要关联的字段
     */
    default <R> List<T> selectByMapDeep(Map<String, Object> columnMap, List<SFunction<T, R>> property) {
        return mpjQueryMapping(selectByMap(columnMap), property);
    }

    /**
     * 根据 entity 条件，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default T selectOneDeep(Wrapper<T> queryWrapper) {
        return mpjQueryMapping(selectOne(queryWrapper), null);
    }

    /**
     * 根据 entity 条件，查询一条记录
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param property     需要关联的字段
     */
    default <R> T selectOneDeep(Wrapper<T> queryWrapper, SFunction<T, R>... property) {
        return mpjQueryMapping(selectOne(queryWrapper), Arrays.asList(property));
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectOneDeep(queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param property     需要关联的字段
     */
    default <R> T selectOneDeep(Wrapper<T> queryWrapper, List<SFunction<T, R>> property) {
        return mpjQueryMapping(selectOne(queryWrapper), property);
    }

    /**
     * 根据 entity 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default List<T> selectListDeep(Wrapper<T> queryWrapper) {
        return mpjQueryMapping(selectList(queryWrapper), null);
    }

    /**
     * 根据 entity 条件，查询全部记录
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param property     需要关联的字段
     */
    default <R> List<T> selectListDeep(Wrapper<T> queryWrapper, SFunction<T, R>... property) {
        return mpjQueryMapping(selectList(queryWrapper), Arrays.asList(property));
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectListDeep(queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param property     需要关联的字段
     */
    default <R> List<T> selectListDeep(Wrapper<T> queryWrapper, List<SFunction<T, R>> property) {
        return mpjQueryMapping(selectList(queryWrapper), property);
    }

    /**
     * 根据 Wrapper 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default List<Map<String, Object>> selectMapsDeep(Class<T> clazz, Wrapper<T> queryWrapper) {
        return mpjQueryMapMapping(selectMaps(queryWrapper), clazz, null);
    }

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param clazz        实体类class
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param property     需要关联的字段
     */
    default <R> List<Map<String, Object>> selectMapsDeep(Class<T> clazz, Wrapper<T> queryWrapper, SFunction<T, R>... property) {
        return mpjQueryMapMapping(selectMaps(queryWrapper), clazz, Arrays.asList(property));
    }

    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectMapsDeep(UserDO.class, queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param property     需要关联的字段
     */
    default <R> List<Map<String, Object>> selectMapsDeep(Class<T> clazz, Wrapper<T> queryWrapper, List<SFunction<T, R>> property) {
        return mpjQueryMapMapping(selectMaps(queryWrapper), clazz, property);
    }

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default <E extends IPage<T>> E selectPageDeep(E page, Wrapper<T> queryWrapper) {
        E e = selectPage(page, queryWrapper);
        mpjQueryMapping(e.getRecords(), null);
        return e;
    }

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param property     需要关联的字段
     */
    default <R, E extends IPage<T>> E selectPageDeep(E page, Wrapper<T> queryWrapper, SFunction<T, R>... property) {
        E e = selectPage(page, queryWrapper);
        mpjQueryMapping(e.getRecords(), Arrays.asList(property));
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
     * @param property     需要关联的字段
     */
    default <R, E extends IPage<T>> E selectPageDeep(E page, Wrapper<T> queryWrapper, List<SFunction<T, R>> property) {
        E e = selectPage(page, queryWrapper);
        mpjQueryMapping(e.getRecords(), property);
        return e;
    }


    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default <R, E extends IPage<Map<String, Object>>> E selectMapsPageDeep(E page, Class<T> clazz, Wrapper<T> queryWrapper) {
        E e = selectMapsPage(page, queryWrapper);
        mpjQueryMapMapping(e.getRecords(), clazz, null);
        return e;
    }


    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     * <p>
     * JDK 默认不推荐泛型数组，会引起 Java堆污染(Heap Pollution)
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param property     需要关联的字段
     */
    default <R, E extends IPage<Map<String, Object>>> E selectMapsPageDeep(E page, Class<T> clazz, Wrapper<T> queryWrapper,
                                                                           SFunction<T, R>... property) {
        E e = selectMapsPage(page, queryWrapper);
        mpjQueryMapMapping(e.getRecords(), clazz, Arrays.asList(property));
        return e;
    }


    /**
     * 针对可变参数堆污染提供的重载
     * list为null或空，会查询全部映射关系
     * <p>
     * 例： selectMapsPage(page, UserDO.class, queryWrapper, Arrays.asList(User::getId, ... ))
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param property     需要关联的字段
     */
    default <R, E extends IPage<Map<String, Object>>> E selectMapsPageDeep(E page, Class<T> clazz, Wrapper<T> queryWrapper,
                                                                           List<SFunction<T, R>> property) {
        E e = selectMapsPage(page, queryWrapper);
        mpjQueryMapMapping(e.getRecords(), clazz, property);
        return e;
    }

    /**
     * 查询映射关系<br/>
     * 对结果进行二次查询<br/>
     * 可以自行查询然后在通过此方法进行二次查询<br/>
     * list为null或空，会查询全部映射关系<br/>
     *
     * @param t 第一次查询结果
     */
    default <R> T mpjQueryMapping(T t, List<SFunction<T, R>> property) {
        if (t == null) {
            return null;
        }
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(t.getClass());
        if (tableInfo.isHasMappingOrField()) {
            boolean hasProperty = CollectionUtils.isNotEmpty(property);
            List<String> list = hasProperty ? property.stream().map(LambdaUtils::getName).collect(
                    Collectors.toList()) : null;
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!hasProperty || list.contains(fieldInfo.getProperty())) {
                    Object obj = fieldInfo.thisFieldGet(t);
                    if (obj != null) {
                        List<?> joinList = MappingQuery.mpjQueryList(fieldInfo.getJoinMapper(),
                                fieldInfo.isFieldIsMap(), SqlKeyword.EQ, fieldInfo.getJoinColumn(), obj, fieldInfo);
                        mpjBindData(t, fieldInfo, joinList);
                        fieldInfo.removeJoinField(joinList);
                    }
                }
            }
        }
        return t;
    }

    /**
     * 查询映射关系<br/>
     * 对结果进行二次查询<br/>
     * 可以自行查询然后在通过此方法进行二次查询<br/>
     * list为null或空，会查询全部映射关系<br/>
     *
     * @param map 第一次查询结果
     */
    default <R> Map<String, Object> mpjQueryMapMapping(Map<String, Object> map, Class<T> clazz, List<SFunction<T, R>> property) {
        if (CollectionUtils.isEmpty(map)) {
            return map;
        }
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(clazz);
        if (tableInfo.isHasMappingOrField()) {
            boolean hasProperty = CollectionUtils.isNotEmpty(property);
            List<String> list = hasProperty ? property.stream().map(LambdaUtils::getName).collect(
                    Collectors.toList()) : null;
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!hasProperty || list.contains(fieldInfo.getProperty())) {
                    Object obj = map.get(fieldInfo.getThisMapKey());
                    if (obj != null) {
                        List<?> joinList = MappingQuery.mpjQueryList(fieldInfo.getJoinMapper(),
                                fieldInfo.isFieldIsMap(), SqlKeyword.EQ, fieldInfo.getJoinColumn(), obj, fieldInfo);
                        mpjBindMap(map, fieldInfo, joinList);
                        fieldInfo.removeJoinField(joinList);
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
    default <R> List<T> mpjQueryMapping(List<T> list, List<SFunction<T, R>> property) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(list.get(0).getClass());
        if (tableInfo.isHasMappingOrField()) {
            boolean hasProperty = CollectionUtils.isNotEmpty(property);
            List<String> listProperty = hasProperty ? property.stream().map(LambdaUtils::getName).collect(
                    Collectors.toList()) : null;
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!hasProperty || listProperty.contains(fieldInfo.getProperty())) {
                    List<Object> itemList = list.stream().map(fieldInfo::thisFieldGet).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(itemList)) {
                        List<?> joinList = MappingQuery.mpjQueryList(fieldInfo.getJoinMapper(),
                                fieldInfo.isMappingEntity() && fieldInfo.isFieldIsMap(), SqlKeyword.IN,
                                fieldInfo.getJoinColumn(), itemList, fieldInfo);
                        list.forEach(i -> mpjBindData(i, fieldInfo, joinList));
                        fieldInfo.removeJoinField(joinList);
                    } else {
                        list.forEach(i -> fieldInfo.fieldSet(i, new ArrayList<>()));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 查询映射关系<br/>
     * 对结果进行二次查询<br/>
     * 可以自行查询然后在通过此方法进行二次查询<br/>
     * list为null或空，会查询全部映射关系<br/>
     *
     * @param list 第一次查询结果
     */
    default <R> List<Map<String, Object>> mpjQueryMapMapping(List<Map<String, Object>> list, Class<T> clazz, List<SFunction<T, R>> property) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(clazz);
        if (tableInfo.isHasMappingOrField()) {
            boolean hasProperty = CollectionUtils.isNotEmpty(property);
            List<String> listProperty = hasProperty ? property.stream().map(LambdaUtils::getName).collect(
                    Collectors.toList()) : null;
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!hasProperty || listProperty.contains(fieldInfo.getProperty())) {
                    List<Object> itemList = list.stream().map(m -> m.get(fieldInfo.getThisMapKey()))
                            .collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(itemList)) {
                        List<?> joinList = MappingQuery.mpjQueryList(fieldInfo.getJoinMapper(),
                                fieldInfo.isMappingEntity() && fieldInfo.isFieldIsMap(), SqlKeyword.IN,
                                fieldInfo.getJoinColumn(), itemList, fieldInfo);
                        list.forEach(i -> mpjBindMap(i, fieldInfo, joinList));
                        fieldInfo.removeJoinField(joinList);
                    } else {
                        list.forEach(i -> i.put(fieldInfo.getField().getName(), new ArrayList<>()));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 绑定数据
     */
    default void mpjBindData(T t, MPJTableFieldInfo fieldInfo, List<?> joinList) {
        if (fieldInfo.isMappingEntity()) {
            List<?> list = joinList.stream().filter(j -> fieldInfo.joinFieldGet(j).equals(fieldInfo.thisFieldGet(t)))
                    .collect(Collectors.toList());
            MPJTableFieldInfo.bind(fieldInfo, t, list);
        }
        if (fieldInfo.isMappingField()) {
            MPJTableFieldInfo.bind(fieldInfo, t, joinList.stream().filter(j -> fieldInfo.joinFieldGet(j).equals(
                    fieldInfo.thisFieldGet(t))).map(fieldInfo::bindFieldGet).collect(Collectors.toList()));
        }
    }

    /**
     * 绑定数据
     */
    default void mpjBindMap(Map<String, Object> t, MPJTableFieldInfo fieldInfo, List<?> joinList) {
        List<?> list = null;
        if (fieldInfo.isMappingEntity()) {
            if (fieldInfo.isFieldIsMap()) {
                list = ((List<Map<String, Object>>) joinList).stream().filter(j ->
                                j.get(fieldInfo.getJoinMapKey()).equals(t.get(fieldInfo.getThisMapKey())))
                        .collect(Collectors.toList());
            } else {
                list = joinList.stream().filter(j ->
                                fieldInfo.joinFieldGet(j).equals(t.get(fieldInfo.getThisMapKey())))
                        .collect(Collectors.toList());
            }
        }
        if (fieldInfo.isMappingField()) {
            list = joinList.stream().filter(j -> fieldInfo.joinFieldGet(j).equals(
                    t.get(fieldInfo.getThisMapKey()))).map(fieldInfo::bindFieldGet).collect(Collectors.toList());
        }
        MPJTableFieldInfo.bindMap(fieldInfo, t, list);
    }
}