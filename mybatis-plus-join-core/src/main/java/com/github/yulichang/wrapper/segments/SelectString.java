package com.github.yulichang.wrapper.segments;

import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * 自定义字符串列
 *
 * @author yulichang
 * @since 1.3.12
 */
public class SelectString implements Select {
    private final String column;

    private final String tagProperty;

    public SelectString(String column, String tagProperty) {
        this.column = column;
        this.tagProperty = tagProperty;
    }

    @Override
    public Class<?> getClazz() {
        return null;
    }

    @Override
    public Integer getIndex() {
        return null;
    }

    @Override
    public boolean isHasTableAlias() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getTableAlias() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPk() {
        return false;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public Class<?> getColumnType() {
        return null;
    }

    @Override
    public String getTagColumn() {
        return tagProperty;
    }

    @Override
    public String getColumProperty() {
        return tagProperty;
    }

    @Override
    public boolean hasTypeHandle() {
        return false;
    }

    @Override
    public TypeHandler<?> getTypeHandle() {
        return null;
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
        return null;
    }

    @Override
    public JdbcType getJdbcType() {
        return null;
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
        return true;
    }
}
