package com.github.yulichang.repository;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.repository.IRepository;
import com.github.yulichang.base.MPJBaseMapper;

import java.util.List;

/**
 * {@link IRepository}
 *
 * @author yulichang
 * @since 1.5.2
 */
@SuppressWarnings({"unused"})
public interface JoinRepository<T> extends IRepository<T> {

    /**
     * 连接查询返回一条记录
     */
    default <DTO> DTO getOne(Class<DTO> clazz, Wrapper<T> wrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectOne(clazz, wrapper);
    }

    /**
     * 连接查询返回集合
     */
    default <DTO> List<DTO> list(Class<DTO> clazz, Wrapper<T> wrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectList(clazz, wrapper);
    }

    /**
     * 连接查询返回集合并分页
     */
    default <DTO, P extends IPage<DTO>> P page(P page, Class<DTO> clazz, Wrapper<T> wrapper) {
        return ((MPJBaseMapper<T>) getBaseMapper()).selectPage(page, clazz, wrapper);
    }
}
