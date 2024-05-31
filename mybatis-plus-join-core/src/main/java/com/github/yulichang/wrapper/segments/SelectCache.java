package com.github.yulichang.wrapper.segments;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.toolkit.MPJStringUtils;
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

    /**
     * 实体类
     */
    private final Class<?> clazz;

    /**
     * 是否是主键
     */
    private final boolean isPk;

    /**
     * 查询字段
     */
    private final String column;

    /**
     * 字段类型
     */
    private final Class<?> columnType;

    /**
     * 查询字段 去除特殊符号 比如反引号,单引号,双引号等
     *
     * @see StringUtils#getTargetColumn(String)
     */
    private final String tagColumn;

    /**
     * 字段属性名
     */
    private final String columProperty;

    /**
     * mp 字段信息
     */
    private final TableFieldInfo tableFieldInfo;

    /**
     * 使用使用 hasTypeHandle
     */
    private final boolean hasTypeHandle;

    /**
     * hasTypeHandle 类型
     */
    private final TypeHandler<?> typeHandler;

    public SelectCache(Class<?> clazz, boolean isPk, String column, Class<?> columnType, String columProperty, TableFieldInfo tableFieldInfo) {
        this.clazz = clazz;
        this.isPk = isPk;
        this.column = column;
        this.columnType = columnType;
        this.columProperty = columProperty;
        this.tagColumn = MPJStringUtils.getTargetColumn(column);
        this.tableFieldInfo = tableFieldInfo;
        if (Objects.isNull(tableFieldInfo)) {
            this.hasTypeHandle = false;
            this.typeHandler = null;
        } else {
            this.hasTypeHandle = this.tableFieldInfo.getTypeHandler() != null && tableFieldInfo.getTypeHandler() != UnknownTypeHandler.class;
            if (this.hasTypeHandle) {
                TableInfo info = TableHelper.getAssert(clazz);
                this.typeHandler = getTypeHandler(AdapterHelper.getAdapter().mpjGetConfiguration(info), tableFieldInfo);
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
