package com.github.yulichang.extension.mapping.mapper;


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
import java.util.Objects;

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
                conditionList.add(new Condition(convert(condition.keyWord()), condition.column(), condition.value()));
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

    private SqlKeyword convert(com.github.yulichang.annotation.enums.SqlKeyword sqlKeyword) {
        if (Objects.isNull(sqlKeyword)) {
            return null;
        }
        switch (sqlKeyword) {
            case NOT:
                return SqlKeyword.NOT;
            case IN:
                return SqlKeyword.IN;
            case NOT_IN:
                return SqlKeyword.NOT_IN;
            case LIKE:
                return SqlKeyword.LIKE;
            case NOT_LIKE:
                return SqlKeyword.NOT_LIKE;
            case EQ:
                return SqlKeyword.EQ;
            case NE:
                return SqlKeyword.NE;
            case GT:
                return SqlKeyword.GT;
            case GE:
                return SqlKeyword.GE;
            case LT:
                return SqlKeyword.LT;
            case LE:
                return SqlKeyword.LE;
            case IS_NULL:
                return SqlKeyword.IS_NULL;
            case IS_NOT_NULL:
                return SqlKeyword.IS_NOT_NULL;
            case BETWEEN:
                return SqlKeyword.BETWEEN;
            case NOT_BETWEEN:
                return SqlKeyword.NOT_BETWEEN;
            default:
                return SqlKeyword.EQ;
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
