package com.github.yulichang.wrapper.segments;


import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import lombok.Getter;
import org.apache.ibatis.type.TypeHandler;

import java.util.Objects;

/**
 * 自定义函数列
 *
 * @author yulichang
 * @since 1.3.10
 */
@Getter
public class SelectFunc implements Select {

    private final String index;

    private final SelectCache cache;

    private final String column;

    private final boolean hasAlias;

    private final String alias;

    private final boolean isFunc;

    private final BaseFuncEnum func;


    public SelectFunc(SelectCache cache, String index, String alias, BaseFuncEnum func) {
        this.index = index;
        this.cache = cache;
        this.column = cache.getColumn();
        this.hasAlias = true;
        this.alias = alias;
        this.isFunc = true;
        this.func = func;
    }

    public SelectFunc(String alias, String index, BaseFuncEnum func, String column) {
        this.index = index;
        this.column = column;
        this.cache = null;
        this.hasAlias = true;
        this.alias = alias;
        this.isFunc = true;
        this.func = func;
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
    public TableFieldInfo getTableFieldInfo() {
        return Objects.isNull(cache) ? null : cache.getTableFieldInfo();
    }

    @Override
    public boolean isLabel() {
        return false;
    }
}
