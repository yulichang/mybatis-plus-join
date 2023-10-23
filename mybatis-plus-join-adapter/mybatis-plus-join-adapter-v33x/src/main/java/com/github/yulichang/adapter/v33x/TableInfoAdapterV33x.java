package com.github.yulichang.adapter.v33x;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.adapter.base.ITableInfoAdapter;
import com.github.yulichang.adapter.base.metadata.OrderFieldInfo;
import com.github.yulichang.adapter.base.tookit.VersionUtils;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class TableInfoAdapterV33x implements ITableInfoAdapter {

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
    public Configuration mpjGetConfiguration(TableInfo tableInfo) {
        return tableInfo.getConfiguration();
    }

    @Override
    public Field mpjGetField(TableFieldInfo fieldInfo, Supplier<Field> supplier) {
        return is330 ? supplier.get() : ITableInfoAdapter.super.mpjGetField(fieldInfo, null);
    }

    @Override
    public List<OrderFieldInfo> mpjGetOrderField(TableInfo tableInfo) {
        throw new UnsupportedOperationException("不支持排序");
    }
}
