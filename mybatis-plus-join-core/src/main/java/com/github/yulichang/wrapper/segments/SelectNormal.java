package com.github.yulichang.wrapper.segments;

import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import lombok.Getter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * 缓存列
 *
 * @author yulichang
 * @since 1.3.10
 */
@Getter
public class SelectNormal implements Select {

    private final Integer index;

    private final SelectCache cache;

    private final boolean hasTableAlias;

    private final String tableAlias;

    public SelectNormal(SelectCache cache, Integer index, boolean hasTableAlias, String tableAlias) {
        this.cache = cache;
        this.index = index;
        this.hasTableAlias = hasTableAlias;
        this.tableAlias = tableAlias;
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
        return false;
    }

    @Override
    public String getAlias() {
        return null;
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
        return false;
    }

    @Override
    public boolean isStr() {
        return false;
    }
}
