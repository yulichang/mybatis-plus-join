package com.github.yulichang.wrapper.segments;


import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
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

    private final String index;

    private final SelectCache cache;

    private final Class<?> tagClass;

    private final Field tagField;

    private final boolean hasAlias;

    private final String alias;

    public SelectLabel(SelectCache cache, String index, Class<?> tagClass, Field tagField) {
        this.cache = cache;
        this.index = index;
        this.tagClass = tagClass;
        this.tagField = tagField;
        this.hasAlias = false;
        this.alias = null;
    }

    public SelectLabel(SelectCache cache, String index, Class<?> tagClass, Field tagField, String column) {
        this.cache = cache;
        this.index = index;
        this.tagClass = tagClass;
        this.tagField = tagField;
        this.hasAlias = true;
        this.alias = column;
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
    public TableFieldInfo getTableFieldInfo() {
        return cache.getTableFieldInfo();
    }

    @Override
    public boolean isFunc() {
        return false;
    }

    @Override
    public SFunction<?, ?>[] getArgs() {
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
