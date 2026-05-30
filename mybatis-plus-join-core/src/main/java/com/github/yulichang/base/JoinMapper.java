package com.github.yulichang.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.toolkit.Constant;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * join相关方法
 *
 * @author yulichang
 */
public interface JoinMapper<T> {
    /**
     * 连表查询返回一条记录
     *
     * @param wrapper joinWrapper
     * @param clazz   resultType
     */
    <DTO> DTO selectOne(@Param(Constant.CLAZZ) Class<DTO> clazz,
                        @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 连表查询返回记录集合
     *
     * @param wrapper joinWrapper
     * @param clazz   resultType
     */
    <DTO> List<DTO> selectList(@Param(Constant.CLAZZ) Class<DTO> clazz,
                               @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 连表查询返回记录集合并分页
     *
     * @param wrapper joinWrapper
     * @param clazz   resultType
     * @param <DTO>   分页返回对象
     */
    <DTO, P extends IPage<DTO>> P selectPage(P page,
                                             @Param(Constant.CLAZZ) Class<DTO> clazz,
                                             @Param(Constants.WRAPPER) Wrapper<T> wrapper);
}
