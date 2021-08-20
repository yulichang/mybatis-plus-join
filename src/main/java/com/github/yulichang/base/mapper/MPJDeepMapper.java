package com.github.yulichang.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.*;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.LambdaUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
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
        return queryMapping(selectById(id), null);
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
        return queryMapping(selectById(id), Arrays.asList(property));
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
        return queryMapping(selectById(id), property);
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     */
    default List<T> selectBatchIdsDeep(Collection<? extends Serializable> idList) {
        return queryMapping(selectBatchIds(idList), null);
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
        return queryMapping(selectBatchIds(idList), Arrays.asList(property));
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
        return queryMapping(selectBatchIds(idList), property);
    }

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    default List<T> selectByMapDeep(Map<String, Object> columnMap) {
        return queryMapping(selectByMap(columnMap), null);
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
        return queryMapping(selectByMap(columnMap), Arrays.asList(property));
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
        return queryMapping(selectByMap(columnMap), property);
    }

    /**
     * 根据 entity 条件，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default T selectOneDeep(Wrapper<T> queryWrapper) {
        return queryMapping(selectOne(queryWrapper), null);
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
        return queryMapping(selectOne(queryWrapper), Arrays.asList(property));
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
        return queryMapping(selectOne(queryWrapper), property);
    }

    /**
     * 根据 entity 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default List<T> selectListDeep(Wrapper<T> queryWrapper) {
        return queryMapping(selectList(queryWrapper), null);
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
        return queryMapping(selectList(queryWrapper), Arrays.asList(property));
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
        return queryMapping(selectList(queryWrapper), property);
    }


    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default <E extends IPage<T>> E selectPageDeep(E page, Wrapper<T> queryWrapper) {
        E e = selectPage(page, queryWrapper);
        queryMapping(e.getRecords(), null);
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
        queryMapping(e.getRecords(), Arrays.asList(property));
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
        queryMapping(e.getRecords(), property);
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
    default <R> T queryMapping(T t, List<SFunction<T, R>> property) {
        if (t == null) {
            return null;
        }
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(t.getClass());
        if (tableInfo.isHasMapping()) {
            boolean hasProperty = CollectionUtils.isNotEmpty(property);
            List<String> list = hasProperty ? property.stream().map(LambdaUtils::getName).collect(
                    Collectors.toList()) : null;
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!hasProperty || list.contains(fieldInfo.getProperty())) {
                    Object get = fieldInfo.thisFieldGet(t);
                    if (get != null) {
                        List<?> o = (List<?>) fieldInfo.getJoinMapper().mappingWrapperConstructor(fieldInfo.isFieldIsMap(),
                                SqlKeyword.EQ, fieldInfo.getJoinColumn(), get, fieldInfo);
                        MPJTableFieldInfo.bind(fieldInfo, t, o);
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
     * @param list 第一次查询结果
     */
    default <R> List<T> queryMapping(List<T> list, List<SFunction<T, R>> property) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(list.get(0).getClass());
        if (tableInfo.isHasMapping()) {
            boolean hasProperty = CollectionUtils.isNotEmpty(property);
            List<String> listProperty = hasProperty ? property.stream().map(LambdaUtils::getName).collect(
                    Collectors.toList()) : null;
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                if (!hasProperty || listProperty.contains(fieldInfo.getProperty())) {
                    List<Object> itemList = list.stream().map(fieldInfo::thisFieldGet).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(itemList)) {
                        List<?> joinList = (List<?>) fieldInfo.getJoinMapper().mappingWrapperConstructor(
                                fieldInfo.isFieldIsMap(), SqlKeyword.IN, fieldInfo.getJoinColumn(), itemList, fieldInfo);
                        list.forEach(i -> {
                            List<?> data = joinList.stream().filter(j -> fieldInfo.joinFieldGet(j)
                                    .equals(fieldInfo.thisFieldGet(i))).collect(Collectors.toList());
                            MPJTableFieldInfo.bind(fieldInfo, i, data);
                        });
                    } else {
                        list.forEach(i -> fieldInfo.fieldSet(i, new ArrayList<>()));
                    }
                }
            }
        }
        return list;
    }

    /**
     * 映射 wrapper 构造器
     * 仅对使用 @MPJMapping 时使用
     */
    default Object mappingWrapperConstructor(boolean selectMap, SqlKeyword keyword,
                                             String column, Object val, MPJTableFieldInfo fieldInfo) {
        MPJMappingWrapper infoWrapper = fieldInfo.getWrapper();
        MappingQuery<T> wrapper = new MappingQuery<>();
        if (infoWrapper.isHasCondition()) {
            infoWrapper.getConditionList().forEach(c -> wrapper.addCondition(true, c.getColumn(),
                    c.getKeyword(), c.getVal()));
        }
        wrapper.eq(SqlKeyword.EQ == keyword, column, val)
                .first(infoWrapper.isHasFirst(), infoWrapper.getFirst())
                .last(infoWrapper.isHasLast(), infoWrapper.getLast());
        if (SqlKeyword.IN == keyword) {
            wrapper.in(column, (List<?>) val);
        }
        if (infoWrapper.isHasSelect()) {
            wrapper.select(infoWrapper.getSelect());
        }
        if (infoWrapper.isHasApply()) {
            infoWrapper.getApplyList().forEach(a -> wrapper.apply(a.getSql(), (Object[]) a.getVal()));
        }
        if (selectMap) {
            return selectMaps(wrapper);
        }
        return selectList(wrapper);
    }

    /**
     * 公开 addCondition 方法
     */
    class MappingQuery<T> extends QueryWrapper<T> {
        @Override
        public QueryWrapper<T> addCondition(boolean condition, String column, SqlKeyword sqlKeyword, Object val) {
            return super.addCondition(condition, column, sqlKeyword, val);
        }
    }
}
