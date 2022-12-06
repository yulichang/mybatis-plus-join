package com.github.yulichang.wrapper.segments;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.Objects;

/**
 * 缓存列, 普通列
 *
 * @author yulichang
 * @since 1.3.10
 */
@Getter
public class SelectNormal implements Select {

    private final Class<?> clazz;

    private final boolean isPk;

    private final String column;

    private final Class<?> columnType;

    private final String tagColumn;

    private final String columProperty;

    private final TableFieldInfo tableFieldInfo;

    private final boolean hasTypeHandle;

    private final TypeHandler<?> typeHandler;

    public SelectNormal(Class<?> clazz, boolean isPk, String column, Class<?> columnType, String columProperty, TableFieldInfo tableFieldInfo) {
        this.clazz = clazz;
        this.isPk = isPk;
        this.column = column;
        this.columnType = columnType;
        this.columProperty = columProperty;
        this.tagColumn = StringUtils.getTargetColumn(column);
        this.tableFieldInfo = tableFieldInfo;
        if (Objects.isNull(tableFieldInfo)) {
            this.hasTypeHandle = false;
            this.typeHandler = null;
        } else {
            this.hasTypeHandle = this.tableFieldInfo.getTypeHandler() != null && tableFieldInfo.getTypeHandler() != UnknownTypeHandler.class;
            if (this.hasTypeHandle) {
                TableInfo info = TableInfoHelper.getTableInfo(clazz);
                Assert.notNull(info, "table not find by class <%s>", clazz.getSimpleName());
                this.typeHandler = getTypeHandler(info.getConfiguration(), tableFieldInfo);
            } else {
                this.typeHandler = null;
            }
        }
    }

    @Override
    public boolean hasTypeHandle() {
        return hasTypeHandle;
    }

    @Override
    public TypeHandler<?> getTypeHandle() {
        return typeHandler;
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


    private TypeHandler<?> getTypeHandler(Configuration configuration, TableFieldInfo info) {
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        TypeHandler<?> typeHandler = registry.getMappingTypeHandler(info.getTypeHandler());
        if (typeHandler == null) {
            typeHandler = registry.getInstance(info.getPropertyType(), info.getTypeHandler());
        }
        return typeHandler;
    }
}
