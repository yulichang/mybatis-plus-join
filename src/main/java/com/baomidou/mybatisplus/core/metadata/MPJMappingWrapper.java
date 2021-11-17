package com.baomidou.mybatisplus.core.metadata;


import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.annotation.MPJMappingApply;
import com.github.yulichang.annotation.MPJMappingCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
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

    private final boolean hasOrderByAsc;
    private List<String> orderByAsc;

    private final boolean hasOrderByDesc;
    private List<String> orderByDesc;

    public MPJMappingWrapper(String first, String select, MPJMappingApply[] applys,
                             MPJMappingCondition[] conditions, String last, String orderByAsc, String orderByDesc) {
        this.hasFirst = StringUtils.isNotBlank(first);
        if (this.hasFirst) {
            this.first = first;
        }

        this.hasSelect = StringUtils.isNotBlank(select);
        if (this.hasSelect) {
            this.select = select;
        }

        this.hasApply = applys.length > 0;
        if (this.hasApply) {
            this.applyList = new ArrayList<>();
            for (MPJMappingApply apply : applys) {
                this.applyList.add(new Apply(apply.value(), apply.args()));
            }
        }

        this.hasCondition = conditions.length > 0;
        if (this.hasCondition) {
            this.conditionList = new ArrayList<>();
            for (MPJMappingCondition condition : conditions) {
                conditionList.add(new Condition(condition.keyWord(), condition.column(), condition.value()));
            }
        }

        this.hasLast = StringUtils.isNotBlank(last);
        if (this.hasLast) {
            this.last = last;
        }

        this.hasOrderByAsc = StringUtils.isNotBlank(orderByAsc);
        if (this.hasOrderByAsc) {
            this.orderByAsc = Arrays.asList(orderByAsc.split(StringPool.COMMA));
        }

        this.hasOrderByDesc = StringUtils.isNotBlank(orderByDesc);
        if (this.hasOrderByDesc) {
            this.orderByDesc = Arrays.asList(orderByDesc.split(StringPool.COMMA));
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
