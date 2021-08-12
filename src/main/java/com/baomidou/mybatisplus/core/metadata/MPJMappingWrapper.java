package com.baomidou.mybatisplus.core.metadata;


import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.annotation.MPJMapping;
import com.github.yulichang.annotation.MPJMappingApply;
import com.github.yulichang.annotation.MPJMappingCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 关联查询条件
 *
 * @author yulichang
 * @since 1.2.0
 */
@Getter
public class MPJMappingWrapper {

    private final boolean hasFirst;
    private String first;

    private final boolean hasSelect;
    private String select;

    private final boolean hasApply;
    private List<Apply> applyList;

    private final boolean hasCondition;
    private List<Condition> conditionList;

    private final boolean hasLast;
    private String last;

    public MPJMappingWrapper(MPJMapping mapping) {
        this.hasFirst = StringUtils.isNotBlank(mapping.first());
        if (this.hasFirst) {
            this.first = mapping.first();
        }

        this.hasSelect = StringUtils.isNotBlank(mapping.select());
        if (this.hasSelect) {
            this.select = mapping.select();
        }

        this.hasApply = mapping.apply().length > 0;
        if (this.hasApply) {
            this.applyList = new ArrayList<>();
            for (MPJMappingApply apply : mapping.apply()) {
                this.applyList.add(new Apply(apply.value(), apply.args()));
            }
        }

        this.hasCondition = mapping.condition().length > 0;
        if (this.hasCondition) {
            this.conditionList = new ArrayList<>();
            for (MPJMappingCondition condition : mapping.condition()) {
                conditionList.add(new Condition(condition.keyWord(), condition.column(), condition.value()));
            }
        }

        this.hasLast = StringUtils.isNotBlank(mapping.last());
        if (this.hasLast) {
            this.last = mapping.last();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class Apply {
        private final String sql;
        private final String[] val;
    }

    @Getter
    @AllArgsConstructor
    public static class Condition {
        private final SqlKeyword keyword;
        private final String column;
        private final String[] val;
    }
}
