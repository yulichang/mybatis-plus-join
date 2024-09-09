package com.github.yulichang.adapter.v3431;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.adapter.base.IAdapter;
import com.github.yulichang.adapter.base.metadata.OrderFieldInfo;
import com.github.yulichang.adapter.base.tookit.VersionUtils;
import com.github.yulichang.adapter.jsqlparser.v46.JSqlParserHelperV46;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author yulichang
 * @since 1.4.7
 */
@AllArgsConstructor
public class Adapter3431 implements IAdapter {

    private static final boolean v = VersionUtils.compare(MybatisPlusVersion.getVersion(), "3.4.3") < 0;

    @Override
    public String mpjMapping(TableFieldInfo tableFieldInfo) {
        if (v) {
            String el = tableFieldInfo.getEl();
            if (StringUtils.isNotBlank(el) && el.contains(StringPool.COMMA)) {
                return el.substring(el.indexOf(StringPool.COMMA) + 1);
            }
            return null;
        }
        return IAdapter.super.mpjMapping(tableFieldInfo);
    }

    @Override
    public List<OrderFieldInfo> mpjGetOrderField(TableInfo tableInfo) {
        return v ? null : tableInfo.getOrderByFields().stream().map(f ->
                new OrderFieldInfo(f.getColumn(), f.getOrderByType(), f.getOrderBySort())).collect(Collectors.toList());
    }

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

    @Override
    public void checkCollectionPage() {
        throw ExceptionUtils.mpe("page by main need MP version 3.5.6+, current version: " + MybatisPlusVersion.getVersion());
    }
}
