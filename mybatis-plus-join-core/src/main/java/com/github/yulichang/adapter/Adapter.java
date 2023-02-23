package com.github.yulichang.adapter;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.github.yulichang.adapter.base.IAdapter;
import org.apache.ibatis.session.Configuration;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class Adapter implements IAdapter {

    @Override
    public boolean mpjHasLogic(TableInfo tableInfo) {
        return tableInfo.isWithLogicDelete();
    }

    @Override
    public boolean mpjIsPrimitive(TableFieldInfo tableFieldInfo) {
        return tableFieldInfo.isPrimitive();
    }

    @Override
    public TableFieldInfo mpjGetLogicField(TableInfo tableInfo) {
        return tableInfo.getLogicDeleteFieldInfo();
    }

    @Override
    public boolean mpjHasPK(TableInfo tableInfo) {
        return tableInfo.havePK();
    }

    @Override
    public Configuration mpjGetConfiguration(TableInfo tableInfo) {
        return tableInfo.getConfiguration();
    }
}
