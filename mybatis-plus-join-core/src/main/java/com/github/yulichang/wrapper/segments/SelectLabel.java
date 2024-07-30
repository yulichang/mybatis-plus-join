package com.github.yulichang.wrapper.segments;


import com.github.yulichang.apt.BaseColumn;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import lombok.Getter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * 对多或对一查询列
 *
 * @author yulichang
 * @since 1.3.10
 */
@Getter
public class SelectLabel implements Select {

    private final Integer index;

    private final SelectCache cache;

    private final Class<?> tagClass;

    private final boolean hasAlias;

    private final String alias;

    private final boolean hasTableAlias;

    private final String tableAlias;

    private final BaseColumn<?> baseColumn;

    public SelectLabel(SelectCache cache, Integer index, Class<?> tagClass, boolean hasTableAlias, String tableAlias, BaseColumn<?> baseColumn) {
        this.cache = cache;
        this.index = index;
        this.tagClass = tagClass;
        this.hasAlias = false;
        this.alias = null;
        this.hasTableAlias = hasTableAlias;
        this.tableAlias = hasTableAlias ? tableAlias : null;
        this.baseColumn = baseColumn;
    }

    public SelectLabel(SelectCache cache, Integer index, Class<?> tagClass, String column, boolean hasTableAlias, String tableAlias, BaseColumn<?> baseColumn) {
        this.cache = cache;
        this.index = index;
        this.tagClass = tagClass;
        this.hasAlias = true;
        this.alias = column;
        this.hasTableAlias = hasTableAlias;
        this.tableAlias = hasTableAlias ? tableAlias : null;
        this.baseColumn = baseColumn;
    }

    @Override
    public Class<?> getClazz() {
        return cache.getClazz();
    }

    @Override
    public boolean isPk() {
        return cache.isPk();
    }

    @Override
    public String getColumn() {
        return cache.getColumn();
    }

    @Override
    public Class<?> getColumnType() {
        return cache.getColumnType();
    }

    @Override
    public String getTagColumn() {
        return cache.getTagColumn();
    }

    @Override
    public String getColumProperty() {
        return cache.getColumProperty();
    }

    @Override
    public boolean hasTypeHandle() {
        return cache.isHasTypeHandle();
    }

    @Override
    public TypeHandler<?> getTypeHandle() {
        return cache.getTypeHandler();
    }

    @Override
    public boolean isHasAlias() {
        return hasAlias;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public Class<?> getPropertyType() {
        return cache.getPropertyType();
    }

    @Override
    public JdbcType getJdbcType() {
        return cache.getJdbcType();
    }

    @Override
    public boolean isFunc() {
        return false;
    }

    @Override
    public SelectFunc.Arg[] getArgs() {
        return null;
    }

    @Override
    public BaseFuncEnum getFunc() {
        return null;
    }

    @Override
    public boolean isLabel() {
        return true;
    }

    @Override
    public boolean isStr() {
        return false;
    }
}
