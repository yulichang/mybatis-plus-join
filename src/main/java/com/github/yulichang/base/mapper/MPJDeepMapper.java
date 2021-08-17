package com.github.yulichang.base.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.*;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yulichang
 * @see BaseMapper
 */
@SuppressWarnings("unused")
public interface MPJDeepMapper<T> extends BaseMapper<T> {

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    default T selectByIdDeep(Serializable id) {
        return queryMapping(selectById(id));
    }

    /**
     * 查询（根据ID 批量查询）
     *
     * @param idList 主键ID列表(不能为 null 以及 empty)
     */
    default List<T> selectBatchIdsDeep(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList) {
        return queryMapping(selectBatchIds(idList));
    }

    /**
     * 查询（根据 columnMap 条件）
     *
     * @param columnMap 表字段 map 对象
     */
    default List<T> selectByMapDeep(@Param(Constants.COLUMN_MAP) Map<String, Object> columnMap) {
        return queryMapping(selectByMap(columnMap));
    }

    /**
     * 根据 entity 条件，查询一条记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default T selectOneDeep(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
        return queryMapping(selectOne(queryWrapper));
    }

    /**
     * 根据 entity 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default List<T> selectListDeep(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
        return queryMapping(selectList(queryWrapper));
    }


    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    default <E extends IPage<T>> E selectPageDeep(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper) {
        E e = selectPage(page, queryWrapper);
        queryMapping(e.getRecords());
        return e;
    }

    /**
     * 查询映射关系
     * 对结果进行二次查询
     * 可以自行查询然后在通过此方法进行二次查询
     *
     * @param t 第一次查询结果
     */
    default T queryMapping(T t) {
        if (t == null) {
            return null;
        }
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(t.getClass());
        if (tableInfo.isHasMapping()) {
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
                Object get = fieldInfo.thisFieldGet(t);
                if (get != null) {
                    List<?> o = (List<?>) fieldInfo.getJoinMapper().mappingWrapperConstructor(fieldInfo.isFieldIsMap(),
                            SqlKeyword.EQ, fieldInfo.getJoinColumn(), get, fieldInfo);
                    MPJTableFieldInfo.bind(fieldInfo, t, o);
                }
            }
        }
        return t;
    }

    /**
     * 查询映射关系
     * 对结果进行二次查询
     * 可以自行查询然后在通过此方法进行二次查询
     *
     * @param list 第一次查询结果
     */
    default List<T> queryMapping(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        MPJTableInfo tableInfo = MPJTableInfoHelper.getTableInfo(list.get(0).getClass());
        if (tableInfo.isHasMapping()) {
            for (MPJTableFieldInfo fieldInfo : tableInfo.getFieldList()) {
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
