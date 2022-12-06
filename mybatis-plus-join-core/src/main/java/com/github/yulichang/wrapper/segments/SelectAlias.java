package com.github.yulichang.wrapper.segments;


import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import lombok.Getter;
import org.apache.ibatis.type.TypeHandler;

/**
 * 别名列
 *
 * @author yulichang
 * @since 1.3.10
 */
@Getter
public class SelectAlias implements Select {

    private final SelectNormal selectNormal;

    private final boolean hasAlias;

    private final String alias;

    public SelectAlias(SelectNormal selectNormal, String alias) {
        this.selectNormal = selectNormal;
        this.hasAlias = true;
        this.alias = alias;
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
        return selectNormal.hasTypeHandle();
    }

    @Override
    public TypeHandler<?> getTypeHandle() {
        return selectNormal.getTypeHandle();
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
        return false;
    }
}
