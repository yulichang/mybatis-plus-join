package com.github.yulichang.wrapper.segments;

import com.github.yulichang.apt.BaseColumn;
import com.github.yulichang.apt.Column;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * 缓存列
 *
 * @author yulichang
 * @since 1.5.0
 */

public class SelectApt implements Select {

    private final SelectCache cache;

    @Getter
    @Setter
    private BaseColumn<?> baseColumn;

    private final boolean isFunc;

    private final BaseFuncEnum func;

    private final boolean hasAlias;

    private final String alias;

    @Getter
    private final Column[] columns;

    /**
     * select(col)
     */
    public SelectApt(SelectCache cache, Column column) {
        this.cache = cache;
        this.baseColumn = column.getRoot();
        this.isFunc = false;
        this.func = null;
        this.columns = null;
        this.hasAlias = false;
        this.alias = null;
    }

    /**
     * select(col)
     */
    public SelectApt(SelectCache cache, BaseColumn<?> baseColumn) {
        this.cache = cache;
        this.baseColumn = baseColumn;
        this.isFunc = false;
        this.func = null;
        this.columns = null;
        this.hasAlias = false;
        this.alias = null;
    }

    /**
     * selectAs(col,alias)
     */
    public SelectApt(SelectCache cache, Column column, String alias) {
        this.cache = cache;
        this.baseColumn = column.getRoot();
        this.isFunc = false;
        this.func = null;
        this.columns = null;
        this.hasAlias = true;
        this.alias = alias;
    }

    /**
     * select(()->"", col)
     */
    public SelectApt(SelectCache cache, Column column, BaseFuncEnum baseFuncEnum, String alias) {
        this.cache = cache;
        this.baseColumn = column.getRoot();
        this.isFunc = true;
        this.func = baseFuncEnum;
        this.columns = new Column[]{column};
        this.hasAlias = true;
        this.alias = alias;
    }

    /**
     * select("", arg.accept(col...))
     */
    public SelectApt(Column[] columns, BaseFuncEnum baseFuncEnum, String alias) {
        this.cache = null;
        this.baseColumn = null;
        this.isFunc = true;
        this.func = baseFuncEnum;
        this.columns = columns;
        this.hasAlias = true;
        this.alias = alias;
    }


    @Override
    public boolean isHasTableAlias() {
        return true;
    }

    @Override
    public String getTableAlias() {
        if (baseColumn != null) {
            return baseColumn.getAlias();
        }
        return null;
    }


    @Override
    public Class<?> getClazz() {
        if (cache != null) {
            return cache.getClazz();
        }
        return null;
    }

    @Override
    public Integer getIndex() {
        return null;
    }

    @Override
    public boolean isPk() {
        if (cache != null) {
            return cache.isPk();
        }
        return false;
    }

    @Override
    public String getColumn() {
        if (cache != null) {
            return cache.getColumn();
        }
        return null;
    }

    @Override
    public Class<?> getColumnType() {
        if (cache != null) {
            return cache.getColumnType();
        }
        return null;
    }

    @Override
    public String getTagColumn() {
        if (cache != null) {
            return cache.getTagColumn();
        }
        return null;
    }

    @Override
    public String getColumProperty() {
        if (cache != null) {
            return cache.getColumProperty();
        }
        return null;
    }

    @Override
    public boolean hasTypeHandle() {
        if (cache != null) {
            return cache.isHasTypeHandle();
        }
        return false;
    }

    @Override
    public TypeHandler<?> getTypeHandle() {
        if (cache != null) {
            return cache.getTypeHandler();
        }
        return null;
    }

    @Override
    public boolean isHasAlias() {
        return this.hasAlias;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public Class<?> getPropertyType() {
        if (cache != null) {
            return cache.getPropertyType();
        }
        return null;
    }

    @Override
    public JdbcType getJdbcType() {
        if (cache != null) {
            return cache.getJdbcType();
        }
        return null;
    }

    @Override
    public boolean isFunc() {
        return this.isFunc;
    }

    @Override
    public SelectFunc.Arg[] getArgs() {
        return null;
    }

    @Override
    public BaseFuncEnum getFunc() {
        return func;
    }

    @Override
    public boolean isLabel() {
        return false;
    }

    @Override
    public boolean isStr() {
        return false;
    }
}
