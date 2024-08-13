package com.github.yulichang.wrapper.segments;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.toolkit.MPJStringUtils;
import com.github.yulichang.toolkit.ReflectionKit;
import com.github.yulichang.toolkit.TableHelper;
import lombok.Getter;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存
 *
 * @author yulichang
 * @since 1.3.10
 */
@Getter
public class SelectCache implements Serializable {

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
     * 使用使用 hasTypeHandle
     */
    private final boolean hasTypeHandle;

    /**
     * tableFieldInfo中信息
     */
    private final Class<?> propertyType;
    private final JdbcType jdbcType;
    private final Class<? extends TypeHandler<?>> typeHandlerClass;

    /**
     * 是否查询
     */
    private final boolean isSelect;

    public SelectCache(Class<?> clazz, boolean isPk, String column, Class<?> columnType, String columProperty, boolean isSelect, TableFieldInfo tableFieldInfo) {
        this.clazz = clazz;
        this.isPk = isPk;
        this.column = column;
        this.columnType = columnType;
        this.columProperty = columProperty;
        this.tagColumn = MPJStringUtils.getTargetColumn(column);
        this.isSelect = isSelect;
        if (Objects.isNull(tableFieldInfo)) {
            this.hasTypeHandle = false;
            this.propertyType = null;
            this.jdbcType = null;
            this.typeHandlerClass = null;
        } else {
            this.propertyType = tableFieldInfo.getPropertyType();
            this.jdbcType = tableFieldInfo.getJdbcType();
            this.typeHandlerClass = tableFieldInfo.getTypeHandler();
            this.hasTypeHandle = tableFieldInfo.getTypeHandler() != null && tableFieldInfo.getTypeHandler() != UnknownTypeHandler.class;
        }
    }

    public TypeHandler<?> getTypeHandler() {
        if (this.hasTypeHandle) {
            return Cache.getTypeHandlerCache(this.clazz, this.typeHandlerClass, this.propertyType, this.columProperty);
        }
        return null;
    }

    public static class Cache {
        private static final Map<Class<?>, Map<Class<?>, TypeHandler<?>>> CACHE = new ConcurrentHashMap<>();

        public static TypeHandler<?> getTypeHandlerCache(Class<?> table, Class<? extends TypeHandler<?>> typeHandler, Class<?> propertyType, String columProperty) {
            if (table == null || typeHandler == null) {
                return null;
            }
            Map<Class<?>, TypeHandler<?>> map = CACHE.computeIfAbsent(table, k -> new ConcurrentHashMap<>());
            return map.computeIfAbsent(typeHandler, k -> {
                TableInfo info = TableHelper.getAssert(table);
                @SuppressWarnings("OptionalGetWithoutIsPresent")
                TableFieldInfo fieldInfo = info.getFieldList().stream().filter(f -> f.getProperty().equals(columProperty)).findFirst().get();
                Field field = AdapterHelper.getAdapter().mpjGetField(fieldInfo, () -> ReflectionKit.getFieldMap(table).get(columProperty));
                return AdapterHelper.getAdapter().getTypeHandler(AdapterHelper.getAdapter().mpjGetConfiguration(info), propertyType, typeHandler, field);
            });
        }
    }
}
