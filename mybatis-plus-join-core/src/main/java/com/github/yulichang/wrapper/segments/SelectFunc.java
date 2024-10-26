package com.github.yulichang.wrapper.segments;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import com.github.yulichang.wrapper.interfaces.MFunction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.util.Arrays;
import java.util.Objects;

/**
 * 自定义函数列
 *
 * @author yulichang
 * @since 1.3.10
 */
@Getter
public class SelectFunc implements Select {

    private final Integer index;

    private final SelectCache cache;

    private final String column;

    private final Arg[] args;

    private final boolean hasAlias;

    private final String alias;

    private final boolean isFunc;

    private final BaseFuncEnum func;

    private final boolean hasTableAlias;

    private final String tableAlias;


    public SelectFunc(SelectCache cache, Integer index, String alias, BaseFuncEnum func, boolean hasTableAlias, String tableAlias) {
        this.index = index;
        this.cache = cache;
        this.column = cache.getColumn();
        this.hasAlias = true;
        this.args = null;
        this.alias = alias;
        this.isFunc = true;
        this.func = func;
        this.hasTableAlias = hasTableAlias;
        this.tableAlias = tableAlias;
    }

    public SelectFunc(String alias, Integer index, BaseFuncEnum func, String column, boolean hasTableAlias, String tableAlias) {
        this.index = index;
        this.column = column;
        this.args = null;
        this.cache = null;
        this.hasAlias = true;
        this.alias = alias;
        this.isFunc = true;
        this.func = func;
        this.hasTableAlias = hasTableAlias;
        this.tableAlias = tableAlias;
    }

    public SelectFunc(String alias, Integer index, BaseFuncEnum func, SFunction<?, ?>[] args, boolean hasTableAlias, String tableAlias) {
        this.index = index;
        this.column = null;
        this.args = Arrays.stream(args).map(i -> {
            if (i instanceof Fun) {
                Fun<?, ?> f = (Fun<?, ?>) i;
                if (f.isSub()) {
                    //noinspection unchecked
                    return new Arg(f.getClazz(), null, false, null, null, true, (MFunction<Object>) ((Object) f.getSub()));
                } else {
                    return new Arg(LambdaUtils.getEntityClass(f.getFunc()), LambdaUtils.getName(f.getFunc()),
                            true, f.getAlias(), null, false, null);
                }
            } else {
                return new Arg(LambdaUtils.getEntityClass(i), LambdaUtils.getName(i), false,
                        null, null, false, null);
            }
        }).toArray(Arg[]::new);
        this.cache = null;
        this.hasAlias = true;
        this.alias = alias;
        this.isFunc = true;
        this.func = func;
        this.hasTableAlias = hasTableAlias;
        this.tableAlias = tableAlias;
    }

    /**
     * kt
     */
    public SelectFunc(String alias, Integer index, BaseFuncEnum func, Arg[] args, boolean hasTableAlias, String tableAlias) {
        this.index = index;
        this.column = null;
        this.args = args;
        this.cache = null;
        this.hasAlias = true;
        this.alias = alias;
        this.isFunc = true;
        this.func = func;
        this.hasTableAlias = hasTableAlias;
        this.tableAlias = tableAlias;
    }

    @Override
    public Class<?> getClazz() {
        return Objects.isNull(cache) ? null : cache.getClazz();
    }


    @Override
    public boolean isPk() {
        return false;
    }


    @Override
    public Class<?> getColumnType() {
        return Objects.isNull(cache) ? null : cache.getColumnType();
    }

    @Override
    public String getTagColumn() {
        return Objects.isNull(cache) ? null : cache.getTagColumn();
    }

    @Override
    public String getColumProperty() {
        return Objects.isNull(cache) ? null : cache.getColumProperty();
    }

    @Override
    public boolean hasTypeHandle() {
        return !Objects.isNull(cache) && cache.isHasTypeHandle();
    }

    @Override
    public TypeHandler<?> getTypeHandle() {
        return Objects.isNull(cache) ? null : cache.getTypeHandler();
    }

    @Override
    public Class<?> getPropertyType() {
        return Objects.isNull(cache) ? null : cache.getPropertyType();
    }

    @Override
    public JdbcType getJdbcType() {
        return Objects.isNull(cache) ? null : cache.getJdbcType();
    }

    @Override
    public boolean isLabel() {
        return false;
    }

    @Override
    public boolean isStr() {
        return false;
    }

    @Getter
    @AllArgsConstructor
    public static class Arg {
        private final Class<?> clazz;
        private final String prop;
        private final boolean hasTableAlias;
        private final String tableAlias;
        private final Object property;
        private final boolean isSub;
        private final MFunction<Object> subFunc;
    }
}
