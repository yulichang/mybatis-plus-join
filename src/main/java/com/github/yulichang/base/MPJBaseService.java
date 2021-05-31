package com.github.yulichang.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.yulichang.interfaces.MPJBaseJoin;

import java.util.List;
import java.util.Map;

/**
 * @author yulichang
 * @see IService
 */
public interface MPJBaseService<T> extends IService<T> {

    /**
     * 根据 Wrapper 条件，查询总记录数
     */
    Integer selectJoinCount(MPJBaseJoin wrapper);

    /**
     * 连接查询返回一条记录
     */
    <DTO> DTO selectJoinOne(Class<DTO> clazz, MPJBaseJoin wrapper);

    /**
     * 连接查询返回集合
     */
    <DTO> List<DTO> selectJoinList(Class<DTO> clazz, MPJBaseJoin wrapper);

    /**
     * 连接查询返回集合并分页
     */
    <DTO, P extends IPage<?>> IPage<DTO> selectJoinListPage(P page, Class<DTO> clazz, MPJBaseJoin wrapper);

    /**
     * 连接查询返回Map
     */
    Map<String, Object> selectJoinMap(MPJBaseJoin wrapper);

    /**
     * 连接查询返回Map集合
     */
    List<Map<String, Object>> selectJoinMaps(MPJBaseJoin wrapper);

    /**
     * 连接查询返回Map集合并分页
     */
    <P extends IPage<Map<String, Object>>> IPage<Map<String, Object>> selectJoinMapsPage(P page, MPJBaseJoin wrapper);
}
