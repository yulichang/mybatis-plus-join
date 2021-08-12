package com.github.yulichang.base;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.MPJMappingWrapper;
import com.baomidou.mybatisplus.core.metadata.MPJTableFieldInfo;
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
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param wrapper joinWrapper
     */
    Integer selectJoinCount(@Param(Constants.WRAPPER) MPJBaseJoin wrapper);

    /**
     * 连表查询返回一条记录
     *
     * @param wrapper joinWrapper
     * @param clazz   resultType
     */
    <DTO> DTO selectJoinOne(@Param(Constant.CLAZZ) Class<DTO> clazz,
                            @Param(Constants.WRAPPER) MPJBaseJoin wrapper);

    /**
     * 连表查询返回Map
     *
     * @param wrapper joinWrapper
     */
    Map<String, Object> selectJoinMap(@Param(Constants.WRAPPER) MPJBaseJoin wrapper);

    /**
     * 连表查询返回记录集合
     *
     * @param wrapper joinWrapper
     * @param clazz   resultType
     */
    <DTO> List<DTO> selectJoinList(@Param(Constant.CLAZZ) Class<DTO> clazz,
                                   @Param(Constants.WRAPPER) MPJBaseJoin wrapper);

    /**
     * 连表查询返回Map集合
     *
     * @param wrapper joinWrapper
     */
    List<Map<String, Object>> selectJoinMaps(@Param(Constants.WRAPPER) MPJBaseJoin wrapper);

    /**
     * 连表查询返回记录集合并分页
     *
     * @param wrapper joinWrapper
     * @param clazz   resultType
     * @param <DTO>   分页返回对象
     */
    <DTO, P extends IPage<?>> IPage<DTO> selectJoinPage(P page,
                                                        @Param(Constant.CLAZZ) Class<DTO> clazz,
                                                        @Param(Constants.WRAPPER) MPJBaseJoin wrapper);

    /**
     * 连表查询返回Map集合并分页
     *
     * @param wrapper joinWrapper
     */
    <P extends IPage<?>> IPage<Map<String, Object>> selectJoinMapsPage(P page,
                                                                       @Param(Constants.WRAPPER) MPJBaseJoin wrapper);


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
