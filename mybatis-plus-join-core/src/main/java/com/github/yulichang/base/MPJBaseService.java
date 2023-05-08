package com.github.yulichang.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.yulichang.interfaces.MPJBaseJoin;

import java.util.List;
import java.util.Map;

/**
 * 基础service
 * 目前包含两个模块 连表查询 和 关系映射
 *
 * @author yulichang
 */
@SuppressWarnings({"unused", "unchecked"})
public interface MPJBaseService<T> extends IService<T> {


    /**
     * 根据 Wrapper 条件，连表删除
     *
     * @param wrapper joinWrapper
     */
    default boolean deleteJoin(MPJBaseJoin<T> wrapper) {
        return SqlHelper.retBool(((MPJBaseMapper<T>) getBaseMapper()).deleteJoin(wrapper));
    }

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param entity  实体对象 (set 条件值,可以为 null)
     * @param wrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
     */
    default boolean updateJoin(T entity, MPJBaseJoin<T> wrapper) {
        return SqlHelper.retBool(((MPJBaseMapper<T>) getBaseMapper()).updateJoin(entity, wrapper));
    }

    /**
     * 根据 whereEntity 条件，更新记录 (null字段也会更新 !!!)
     *
     * @param entity  实体对象 (set 条件值,可以为 null)
     * @param wrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
     */
    default boolean updateJoinAndNull(T entity, MPJBaseJoin<T> wrapper) {
        return SqlHelper.retBool(((MPJBaseMapper<T>) getBaseMapper()).updateJoinAndNull(entity, wrapper));
    }

    /**
     * 根据 Wrapper 条件，查询总记录数
     */
    default Long selectJoinCount(MPJBaseJoin<T> wrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectJoinCount(wrapper);
    }

    /**
     * 连接查询返回一条记录
     */
    default <DTO> DTO selectJoinOne(Class<DTO> clazz, MPJBaseJoin<T> wrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectJoinOne(clazz, wrapper);
    }

    /**
     * 连接查询返回集合
     */
    default <DTO> List<DTO> selectJoinList(Class<DTO> clazz, MPJBaseJoin<T> wrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectJoinList(clazz, wrapper);
    }

    /**
     * 连接查询返回集合并分页
     */
    default <DTO, P extends IPage<DTO>> P selectJoinListPage(P page, Class<DTO> clazz, MPJBaseJoin<T> wrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectJoinPage(page, clazz, wrapper);
    }

    /**
     * 连接查询返回Map
     */
    default Map<String, Object> selectJoinMap(MPJBaseJoin<T> wrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectJoinMap(wrapper);
    }

    /**
     * 连接查询返回Map集合
     */
    default List<Map<String, Object>> selectJoinMaps(MPJBaseJoin<T> wrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectJoinMaps(wrapper);
    }

    /**
     * 连接查询返回Map集合并分页
     */
    default <P extends IPage<Map<String, Object>>> P selectJoinMapsPage(P page, MPJBaseJoin<T> wrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectJoinMapsPage(page, wrapper);
    }
}
