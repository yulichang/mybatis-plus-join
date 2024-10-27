package com.github.yulichang.extension.mapping.mapper;


import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.yulichang.toolkit.StrUtils;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.segments.SelectCache;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;
import java.util.stream.Collectors;

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

    public MPJMappingWrapper(Class<?> joinClass, String first, String select, com.github.yulichang.annotation.Apply[] applyArr,
                             com.github.yulichang.annotation.Condition[] conditions, String last, String[] orderByAsc, String[] orderByDesc) {
        this.hasFirst = StrUtils.isNotBlank(first);
        if (this.hasFirst) {
            this.first = first;
        }

        this.hasSelect = StrUtils.isNotBlank(select);
        if (this.hasSelect) {
            this.select = select;
        }

        this.hasApply = applyArr.length > 0;
        if (this.hasApply) {
            this.applyList = new ArrayList<>();
            for (com.github.yulichang.annotation.Apply apply : applyArr) {
                this.applyList.add(new Apply(apply.value(), apply.args()));
            }
        }

        this.hasCondition = conditions.length > 0;
        if (this.hasCondition) {
            this.conditionList = new ArrayList<>();
            for (com.github.yulichang.annotation.Condition condition : conditions) {
                List<SelectCache> listField = ColumnCache.getListField(joinClass);
                if (listField.stream().anyMatch(f -> f.getColumn().equals(condition.column().trim()))) {
                    conditionList.add(new Condition(convert(condition.keyWord()), condition.column(), condition.value()));
                } else {
                    //通过属性获取
                    String col = listField.stream().filter(f -> f.getColumProperty().equals(condition.column())).findFirst()
                            .map(SelectCache::getColumn).orElse(condition.column());
                    conditionList.add(new Condition(convert(condition.keyWord()), col, condition.value()));
                }
            }
        }

        this.hasLast = StrUtils.isNotBlank(last);
        if (this.hasLast) {
            this.last = last;
        }

        this.hasOrderByAsc = orderByAsc.length > 0;
        if (this.hasOrderByAsc) {
            List<SelectCache> listField = ColumnCache.getListField(joinClass);
            Set<String> colSet = listField.stream().map(SelectCache::getColumn).collect(Collectors.toSet());
            List<String> allColumns = new ArrayList<>();
            for (String orderBy : orderByAsc) {
                allColumns.addAll(Arrays.asList(orderBy.split(StringPool.COMMA)));
            }
            this.orderByAsc = allColumns.stream().filter(StrUtils::isNotBlank).map(String::trim).map(f ->
                    colSet.contains(f) ? f : listField.stream().filter(s -> s.getColumProperty().equals(f))
                            .findFirst().map(SelectCache::getColumn).orElse(f)).collect(Collectors.toList());
        }

        this.hasOrderByDesc = orderByDesc.length > 0;
        if (this.hasOrderByDesc) {
            List<SelectCache> listField = ColumnCache.getListField(joinClass);
            Set<String> colSet = listField.stream().map(SelectCache::getColumn).collect(Collectors.toSet());
            List<String> allColumns = new ArrayList<>();
            for (String orderBy : orderByDesc) {
                allColumns.addAll(Arrays.asList(orderBy.split(StringPool.COMMA)));
            }
            this.orderByDesc = allColumns.stream().filter(StrUtils::isNotBlank).map(String::trim).map(f ->
                    colSet.contains(f) ? f : listField.stream().filter(s -> s.getColumProperty().equals(f))
                            .findFirst().map(SelectCache::getColumn).orElse(f)).collect(Collectors.toList());
        }
    }

    public static SqlKeyword convert(com.github.yulichang.annotation.enums.SqlKeyword sqlKeyword) {
        if (Objects.isNull(sqlKeyword)) {
            return null;
        }
        return SqlKeyword.valueOf(sqlKeyword.name());
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
