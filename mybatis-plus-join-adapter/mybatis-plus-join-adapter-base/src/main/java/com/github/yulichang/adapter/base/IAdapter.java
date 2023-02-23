package com.github.yulichang.adapter.base;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.session.Configuration;

public interface IAdapter {

    boolean mpjHasLogic(TableInfo tableInfo);

    boolean mpjIsPrimitive(TableFieldInfo tableFieldInfo);

    TableFieldInfo mpjGetLogicField(TableInfo tableInfo);

    boolean mpjHasPK(TableInfo tableInfo);

    Configuration mpjGetConfiguration(TableInfo tableInfo);
}
