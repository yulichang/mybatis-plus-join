package com.github.yulichang.base.mapper.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.MPJMappingWrapper;
import com.baomidou.mybatisplus.core.metadata.MPJTableFieldInfo;

import java.util.List;

public class MappingQuery<T> extends QueryWrapper<T> {

    /**
     * 公开 addCondition 方法
     */
    @Override
    public QueryWrapper<T> addCondition(boolean condition, String column, SqlKeyword sqlKeyword, Object val) {
        return super.addCondition(condition, column, sqlKeyword, val);
    }

    /**
     * 映射 wrapper 构造器
     * 仅对使用映射注解时使用
     */
    public static <T> List<?> mpjQueryList(BaseMapper<T> baseMapper, boolean selectMap, SqlKeyword keyword,
                                           String column, Object val, MPJTableFieldInfo fieldInfo) {
        MPJMappingWrapper infoWrapper = fieldInfo.getWrapper();
        MappingQuery<T> wrapper = new MappingQuery<>();
        if (infoWrapper.isHasCondition()) {
            infoWrapper.getConditionList().forEach(c -> {
                if (c.getKeyword() == SqlKeyword.BETWEEN) {
                    wrapper.between(c.getColumn(), c.getVal()[0], c.getVal()[1]);
                } else if (c.getKeyword() == SqlKeyword.IN) {
                    wrapper.in(c.getColumn(), (Object[]) c.getVal());
                } else {
                    wrapper.addCondition(true, c.getColumn(),
                            c.getKeyword(), c.getVal()[0]);
                }
            });
        }
        wrapper.eq(SqlKeyword.EQ == keyword, column, val);
        //此处不用链式调用，提高效率
        if (infoWrapper.isHasFirst()) {
            wrapper.first(infoWrapper.getFirst());
        }
        if (infoWrapper.isHasOrderByAsc()) {
            //mybatis plus 3.4.3 之后支持数组，但之前版本仅支持可变参数，为了兼容，多个循环处理
            infoWrapper.getOrderByAsc().forEach(wrapper::orderByAsc);
        }
        if (infoWrapper.isHasOrderByDesc()) {
            //mybatis plus 3.4.3 之后支持数组，但之前版本仅支持可变参数，为了兼容，多个循环处理
            infoWrapper.getOrderByDesc().forEach(wrapper::orderByDesc);
        }
        if (infoWrapper.isHasLast()) {
            wrapper.last(infoWrapper.getLast());
        }
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
            return baseMapper.selectMaps(wrapper);
        }
        return baseMapper.selectList(wrapper);
    }
}
