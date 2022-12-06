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


    private final SelectNormal selectNormal;

    private final String coloum;

    private final boolean hasAlias;

    private final String alias;

    private final boolean isFunc;

    private final BaseFuncEnum func;


    public SelectFunc(SelectNormal selectNormal, String alias, BaseFuncEnum func) {
        this.selectNormal = selectNormal;
        this.coloum = selectNormal.getColumn();
        this.hasAlias = true;
        this.alias = alias;
        this.isFunc = true;
        this.func = func;
    }

    public SelectFunc(String alias, BaseFuncEnum func, String column) {
        this.coloum = column;
        this.selectNormal = null;
        this.hasAlias = true;
        this.alias = alias;
        this.isFunc = true;
        this.func = func;
    }

    @Override
    public Class<?> getClazz() {
        return selectNormal.getClazz();
    }

    @Override
    public boolean isPk() {
        return false;
    }

    @Override
    public String getColumn() {
        return coloum;
    }

    @Override
    public Class<?> getColumnType() {
        return Objects.isNull(selectNormal) ? null : selectNormal.getColumnType();
    }

    @Override
    public String getTagColumn() {
        return Objects.isNull(selectNormal) ? null : selectNormal.getTagColumn();
    }

    @Override
    public String getColumProperty() {
        return Objects.isNull(selectNormal) ? null : selectNormal.getColumProperty();
    }

    @Override
    public boolean hasTypeHandle() {
        return !Objects.isNull(selectNormal) && selectNormal.isHasTypeHandle();
    }

    @Override
    public TypeHandler<?> getTypeHandle() {
        return Objects.isNull(selectNormal) ? null : selectNormal.getTypeHandle();
    }

    @Override
    public TableFieldInfo getTableFieldInfo() {
        return Objects.isNull(selectNormal) ? null : selectNormal.getTableFieldInfo();
    }

    @Override
    public boolean isLabel() {
        return false;
    }
}
