package com.github.yulichang.adapter.v355;

import com.github.yulichang.adapter.base.IAdapter;
import com.github.yulichang.adapter.jsqlparser.v46.JSqlParserHelperV46;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * @author yulichang
 * @since 1.4.12
 */
public class Adapter355 implements IAdapter {

    @Override
    public void parserColum(String alias, String from, String selectSql, Consumer<String> columConsumer) {
        JSqlParserHelperV46.parserColum(alias, from, selectSql, columConsumer);
    }

    @Override
    public TypeHandler<?> getTypeHandler(Configuration configuration, Class<?> propertyType, Class<? extends TypeHandler<?>> typeHandlerClass, Field field) {
        TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();
        TypeHandler<?> typeHandler = registry.getMappingTypeHandler(typeHandlerClass);
        if (typeHandler == null) {
            typeHandler = registry.getInstance(propertyType, typeHandlerClass);
        }
        return typeHandler;
    }
}