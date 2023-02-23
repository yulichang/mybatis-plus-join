package com.github.yulichang.adapter.v33x;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.adapter.base.IAdapter;
import org.apache.ibatis.session.Configuration;

import java.util.Objects;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class AdapterV33x implements IAdapter {

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
}
