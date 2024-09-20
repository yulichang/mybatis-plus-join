package com.github.yulichang.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.Constant;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yulichang
 * @see BaseMapper
 */
public interface MPJBaseMapper<T> extends BaseMapper<T> {

    /**
     * 根据 Wrapper 条件，连表删除
     *
     * @param wrapper joinWrapper
     */
    int deleteJoin(@Param(Constants.WRAPPER) MPJBaseJoin<T> wrapper);

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param entity  实体对象 (set 条件值,可以为 null)
     * @param wrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
     */
    int updateJoin(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) MPJBaseJoin<T> wrapper);

    /**
     * 根据 whereEntity 条件，更新记录 (null字段也会更新 !!!)
     *
     * @param entity  实体对象 (set 条件值,可以为 null)
     * @param wrapper 实体对象封装操作类（可以为 null,里面的 entity 用于生成 where 语句）
     */
    int updateJoinAndNull(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) MPJBaseJoin<T> wrapper);

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param wrapper joinWrapper
     */
    Long selectJoinCount(@Param(Constants.WRAPPER) MPJBaseJoin<T> wrapper);

    /**
     * 连表查询返回一条记录
     *
     * @param wrapper joinWrapper
     * @param clazz   resultType
     */
    <DTO> DTO selectJoinOne(@Param(Constant.CLAZZ) Class<DTO> clazz,
                            @Param(Constants.WRAPPER) MPJBaseJoin<T> wrapper);

    /**
     * 连表查询返回Map
     *
     * @param wrapper joinWrapper
     */
    default Map<String, Object> selectJoinMap(@Param(Constants.WRAPPER) MPJBaseJoin<T> wrapper) {
        //noinspection unchecked
        return selectJoinOne(Map.class, wrapper);
    }

    /**
     * 连表查询返回记录集合
     *
     * @param wrapper joinWrapper
     * @param clazz   resultType
     */
    <DTO> List<DTO> selectJoinList(@Param(Constant.CLAZZ) Class<DTO> clazz,
                                   @Param(Constants.WRAPPER) MPJBaseJoin<T> wrapper);

    /**
     * 连表查询返回Map集合
     *
     * @param wrapper joinWrapper
     */
    default List<Map<String, Object>> selectJoinMaps(@Param(Constants.WRAPPER) MPJBaseJoin<T> wrapper) {
        //noinspection unchecked
        return (List<Map<String, Object>>) ((Object) selectJoinList(Map.class, wrapper));
    }

    /**
     * 连表查询返回记录集合并分页
     *
     * @param wrapper joinWrapper
     * @param clazz   resultType
     * @param <DTO>   分页返回对象
     */
    <DTO, P extends IPage<DTO>> P selectJoinPage(P page,
                                                 @Param(Constant.CLAZZ) Class<DTO> clazz,
                                                 @Param(Constants.WRAPPER) MPJBaseJoin<T> wrapper);

    /**
     * 连表查询返回Map集合并分页
     *
     * @param wrapper joinWrapper
     */
    default <P extends IPage<Map<String, Object>>> P selectJoinMapsPage(P page,
                                                                        @Param(Constants.WRAPPER) MPJBaseJoin<T> wrapper) {
        //noinspection unchecked,rawtypes
        return (P) selectJoinPage((IPage) page, Map.class, wrapper);
    }
}
