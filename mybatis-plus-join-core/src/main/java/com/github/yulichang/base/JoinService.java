package com.github.yulichang.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
public interface JoinService<T> {

    BaseMapper<T> getBaseMapper();

    /**
     * 连接查询返回一条记录
     */
    default <DTO> DTO selectOne(Class<DTO> clazz, Wrapper<T> wrapper) {
        return ((JoinMapper<T>) getBaseMapper()).selectOne(clazz, wrapper);
    }

    /**
     * 连接查询返回集合
     */
    default <DTO> List<DTO> selectList(Class<DTO> clazz, Wrapper<T> wrapper) {
        return ((JoinMapper<T>) getBaseMapper()).selectList(clazz, wrapper);
    }

    /**
     * 连接查询返回集合并分页
     */
    default <DTO, P extends IPage<DTO>> P selectPage(P page, Class<DTO> clazz, Wrapper<T> wrapper) {
        return ((JoinMapper<T>) getBaseMapper()).selectPage(page, clazz, wrapper);
    }
}
