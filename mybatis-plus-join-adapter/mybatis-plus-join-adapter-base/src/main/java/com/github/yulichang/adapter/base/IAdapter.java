package com.github.yulichang.adapter.base;

import com.baomidou.mybatisplus.core.handlers.IJsonTypeHandler;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.MybatisUtils;
import com.github.yulichang.adapter.base.metadata.OrderFieldInfo;
import com.github.yulichang.adapter.jsqlparser.JSqlParserHelper;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author yulichang
 * @since 1.4.3
 */
public interface IAdapter {

    default boolean mpjHasLogic(TableInfo tableInfo) {
        return tableInfo.isWithLogicDelete();
    }

    default boolean mpjIsPrimitive(TableFieldInfo tableFieldInfo) {
        return tableFieldInfo.isPrimitive();
    }

    default String mpjMapping(TableFieldInfo tableFieldInfo) {
        return tableFieldInfo.getMapping();
    }

    default TableFieldInfo mpjGetLogicField(TableInfo tableInfo) {
        return tableInfo.getLogicDeleteFieldInfo();
    }

    default boolean mpjHasPK(TableInfo tableInfo) {
        return tableInfo.havePK();
    }

    default Configuration mpjGetConfiguration(TableInfo tableInfo) {
        return tableInfo.getConfiguration();
    }

    default Field mpjGetField(TableFieldInfo fieldInfo, Supplier<Field> supplier) {
        return fieldInfo.getField();
    }

    default List<OrderFieldInfo> mpjGetOrderField(TableInfo tableInfo) {
        return tableInfo.getOrderByFields().stream().map(f ->
                new OrderFieldInfo(f.getColumn(), f.getType(), f.getSort())).collect(Collectors.toList());
    }

    default void parserColum(String alias, String from, String selectSql, Consumer<String> columConsumer) {
        JSqlParserHelper.parserColum(alias, from, selectSql, columConsumer);
    }

    default TypeHandler<?> getTypeHandler(Configuration configuration, Class<?> propertyType, Class<? extends TypeHandler<?>> typeHandlerClass, Field field) {
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        TypeHandler<?> typeHandler = registry.getMappingTypeHandler(typeHandlerClass);
        if (typeHandlerClass != null && typeHandlerClass != UnknownTypeHandler.class) {
            if (IJsonTypeHandler.class.isAssignableFrom(typeHandlerClass)) {
                // 保证每次实例化
                typeHandler = MybatisUtils.newJsonTypeHandler(typeHandlerClass, propertyType, field);
            } else {
                if (typeHandler == null) {
                    typeHandler = registry.getInstance(propertyType, typeHandlerClass);
                }
            }
        }
        return typeHandler;
    }

    default void checkCollectionPage() {
    }
}
