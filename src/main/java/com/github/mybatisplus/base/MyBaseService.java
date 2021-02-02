package com.github.mybatisplus.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.mybatisplus.interfaces.BaseJoin;

import java.util.List;
import java.util.Map;

/**
 * @author yulichang
 * @see IService
 */
public interface MyBaseService<T> extends IService<T> {

    /**
     * 连接查询返回一条记录
     */
    <DTO> DTO selectJoinOne(Class<DTO> clazz, BaseJoin wrapper);

    /**
     * 连接查询返回集合
     */
    <DTO> List<DTO> selectJoinList(Class<DTO> clazz, BaseJoin wrapper);

    /**
     * 连接查询返回集合并分页
     */
    <DTO, P extends IPage<?>> IPage<DTO> selectJoinListPage(P page, Class<DTO> clazz, BaseJoin wrapper);

    /**
     * 连接查询返回Map
     */
    Map<String, Object> selectJoinMap(BaseJoin wrapper);

    /**
     * 连接查询返回Map集合
     */
    List<Map<String, Object>> selectJoinMaps(BaseJoin wrapper);


    /**
     * 连接查询返回Map集合并分页
     */
    IPage<Map<String, Object>> selectJoinMapsPage(IPage<Map<String, Object>> page, BaseJoin wrapper);
}
