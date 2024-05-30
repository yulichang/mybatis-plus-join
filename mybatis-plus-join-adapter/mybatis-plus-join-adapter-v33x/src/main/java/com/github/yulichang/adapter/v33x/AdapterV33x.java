package com.github.yulichang.adapter.v33x;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.adapter.base.IAdapter;
import com.github.yulichang.adapter.base.metadata.OrderFieldInfo;
import com.github.yulichang.adapter.base.tookit.VersionUtils;
import com.github.yulichang.adapter.jsqlparser.v46.JSqlParserHelperV46;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class AdapterV33x implements IAdapter {

    private static final boolean is330 = VersionUtils.compare(MybatisPlusVersion.getVersion(), "3.3.0") == 0;

    @Override
    public boolean mpjHasLogic(TableInfo tableInfo) {
        return tableInfo.isLogicDelete();
    }

    @Override
    public boolean mpjIsPrimitive(TableFieldInfo tableFieldInfo) {
        return tableFieldInfo.getPropertyType().isPrimitive();
    }

    @Override
    public String mpjMapping(TableFieldInfo tableFieldInfo) {
        String el = tableFieldInfo.getEl();
        if (StringUtils.isNotBlank(el) && el.contains(StringPool.COMMA)) {
            return el.substring(el.indexOf(StringPool.COMMA) + 1);
        }
        return null;
    }

    @Override
    public TableFieldInfo mpjGetLogicField(TableInfo tableInfo) {
        return tableInfo.getFieldList().stream().filter(f -> Objects.nonNull(f.getLogicDeleteValue())
                || Objects.nonNull(f.getLogicNotDeleteValue())).findFirst().orElse(null);
    }

    @Override
    public boolean mpjHasPK(TableInfo tableInfo) {
        return StringUtils.isNotBlank(tableInfo.getKeyProperty()) ||
                StringUtils.isNotBlank(tableInfo.getKeyColumn());
    }

    @Override
    public Field mpjGetField(TableFieldInfo fieldInfo, Supplier<Field> supplier) {
        return is330 ? supplier.get() : IAdapter.super.mpjGetField(fieldInfo, null);
    }

    @Override
    public List<OrderFieldInfo> mpjGetOrderField(TableInfo tableInfo) {
        throw new UnsupportedOperationException("不支持排序");
    }

    @Override
    public void parserColum(String alias, String from, String selectSql, Consumer<String> columConsumer) {
        JSqlParserHelperV46.parserColum(alias, from, selectSql, columConsumer);
    }
}
