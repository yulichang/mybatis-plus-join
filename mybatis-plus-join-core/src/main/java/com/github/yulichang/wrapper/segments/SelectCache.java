package com.github.yulichang.wrapper.segments;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.toolkit.TableHelper;
import lombok.Getter;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.Objects;

/**
 * 缓存
 *
 * @author yulichang
 * @since 1.3.10
 */
@Getter
public class SelectCache {

    private final Class<?> clazz;

    private final boolean isPk;

    private final String column;

    private final Class<?> columnType;

    private final String tagColumn;

    private final String columProperty;

    private final TableFieldInfo tableFieldInfo;

    private final boolean hasTypeHandle;

    private final TypeHandler<?> typeHandler;

    public SelectCache(Class<?> clazz, boolean isPk, String column, Class<?> columnType, String columProperty, TableFieldInfo tableFieldInfo) {
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
                TableInfo info = TableHelper.get(clazz);
                Assert.notNull(info, "table not find by class <%s>", clazz.getSimpleName());
                this.typeHandler = getTypeHandler(ConfigProperties.tableInfoAdapter.mpjGetConfiguration(info), tableFieldInfo);
            } else {
                this.typeHandler = null;
            }
        }
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
