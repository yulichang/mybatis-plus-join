package com.github.yulichang.wrapper.segments;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import org.apache.ibatis.type.TypeHandler;

/**
 * 自定义字符串列
 *
 * @author yulichang
 * @since 1.3.12
 */
public class SelectString implements Select {
    private final String column;

    public SelectString(String column) {
        this.column = column;
    }

    @Override
    public Class<?> getClazz() {
        return null;
    }

    @Override
    public String getIndex() {
        return null;
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
        return null;
    }

    @Override
    public String getColumProperty() {
        return null;
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
    public TableFieldInfo getTableFieldInfo() {
        return null;
    }

    @Override
    public boolean isFunc() {
        return false;
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
