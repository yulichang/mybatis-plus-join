package com.github.yulichang.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.MPJTableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.MPJTableInfo;
import com.baomidou.mybatisplus.core.metadata.MPJTableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.annotation.MPJMapping;
import com.github.yulichang.base.MPJBaseMapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
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
@SuppressWarnings("unused")
public interface MPJDeepService<T> extends IService<T> {

    Class<T> currentModelClass();

    /**
     * 根据 ID 深度查询
     *
     * @param id 主键ID列表
     */
    default <R> T getByIdDeep(Serializable id) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectByIdDeep(id);
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
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    default List<T> listByMapDeep(Map<String, Object> columnMap) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectByMapDeep(columnMap);
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
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     * @param throwEx      有多个 result 是否抛出异常
     */
    default T getOneDeep(Wrapper<T> queryWrapper, boolean throwEx) {
        return ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(getOne(queryWrapper, throwEx));
    }

    /**
     * 根据 Wrapper，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    default Map<String, Object> getMapDeep(Wrapper<T> queryWrapper) {
        Map<String, Object> map = getMap(queryWrapper);
        if (CollectionUtils.isNotEmpty(map)) {
            MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(currentModelClass());
            if (tableInfo.isHasMapping()) {
                for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                    Object o = map.get(fieldInfo.getThisMapKey());
                    if (o != null) {
                        List<?> data = (List<?>) fieldInfo.getJoinMapper().mappingWrapperConstructor(fieldInfo.isFieldIsMap(),
                                SqlKeyword.EQ, fieldInfo.getJoinColumn(), o, fieldInfo);
                        MPJTableFieldInfo.bindMap(fieldInfo, map, data);
                    }
                }
            }
        }
        return map;
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
     * 查询所有
     *
     * @see Wrappers#emptyWrapper()
     */
    default List<T> listDeep() {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectListDeep(Wrappers.emptyWrapper());
    }

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    default <E extends IPage<T>> E pageDeep(E page, Wrapper<T> queryWrapper) {
        E e = page(page, queryWrapper);
        ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(e.getRecords());
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
        ((MPJBaseMapper<T>) getBaseMapper()).queryMapping(e.getRecords());
        return e;
    }

    /**
     * 查询列表
     *
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    default List<Map<String, Object>> listMapsDeep(Wrapper<T> queryWrapper) {
        return queryMapMapping(listMaps(queryWrapper));
    }

    /**
     * 查询所有列表
     *
     * @see Wrappers#emptyWrapper()
     */
    default List<Map<String, Object>> listMapsDeep() {
        return queryMapMapping(listMaps());
    }


    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param queryWrapper 实体对象封装操作类 {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
     */
    default <E extends IPage<Map<String, Object>>> E pageMapsDeep(E page, Wrapper<T> queryWrapper) {
        E e = pageMaps(page, queryWrapper);
        queryMapMapping(e.getRecords());
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
        queryMapMapping(e.getRecords());
        return e;
    }


    /**
     * 查询映射关系
     * 对结果进行二次查询
     * 可以自行查询然后在通过此方法进行二次查询
     *
     * @param list 第一次查询结果
     */
    @SuppressWarnings("unchecked")
    default List<Map<String, Object>> queryMapMapping(List<Map<String, Object>> list) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(currentModelClass());
        if (tableInfo.isHasMapping()) {
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                List<Object> itemList = list.stream().map(m -> m.get(fieldInfo.getThisMapKey())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(itemList)) {
                    if (fieldInfo.isFieldIsMap()) {
                        List<Map<String, Object>> joinList = (List<Map<String, Object>>) fieldInfo.getJoinMapper()
                                .mappingWrapperConstructor(fieldInfo.isFieldIsMap(), SqlKeyword.IN,
                                        fieldInfo.getJoinColumn(), itemList, fieldInfo);
                        list.forEach(i -> {
                            List<Map<String, Object>> data = joinList.stream().filter(j -> j.containsKey(fieldInfo.getJoinMapKey())
                                    && j.get(fieldInfo.getJoinMapKey()).equals(i.get(fieldInfo.getThisMapKey()))).collect(Collectors.toList());
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
        return list;
    }


}
