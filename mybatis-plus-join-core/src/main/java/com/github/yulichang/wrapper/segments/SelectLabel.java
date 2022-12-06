package com.github.yulichang.wrapper.segments;


import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import lombok.Getter;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;

/**
 * 对多或对一查询列
 *
 * @author yulichang
 * @since 1.3.10
 */
@Getter
public class SelectLabel implements Select {

    private final SelectNormal selectNormal;

    private final Class<?> tagClass;

    private final Field tagField;

    private final boolean hasAlias;

    private final String alias;

    public SelectLabel(SelectNormal selectNormal, Class<?> tagClass, Field tagField) {
        this.selectNormal = selectNormal;
        this.tagClass = tagClass;
        this.tagField = tagField;
        this.hasAlias = false;
        this.alias = null;
    }

    public SelectLabel(SelectNormal selectNormal, Class<?> tagClass, Field tagField, String column) {
        this.selectNormal = selectNormal;
        this.tagClass = tagClass;
        this.tagField = tagField;
        this.hasAlias = true;
        this.alias = column;
    }

    @Override
    public Class<?> getClazz() {
        return selectNormal.getClazz();
    }

    @Override
    public boolean isPk() {
        return selectNormal.isPk();
    }

    @Override
    public String getColumn() {
        return selectNormal.getColumn();
    }

    @Override
    public Class<?> getColumnType() {
        return selectNormal.getColumnType();
    }

    @Override
    public String getTagColumn() {
        return selectNormal.getTagColumn();
    }

    @Override
    public String getColumProperty() {
        return selectNormal.getColumProperty();
    }

    @Override
    public boolean hasTypeHandle() {
        return selectNormal.isHasTypeHandle();
    }

    @Override
    public TypeHandler<?> getTypeHandle() {
        return selectNormal.getTypeHandle();
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
    public TableFieldInfo getTableFieldInfo() {
        return selectNormal.getTableFieldInfo();
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
        return true;
    }
}
