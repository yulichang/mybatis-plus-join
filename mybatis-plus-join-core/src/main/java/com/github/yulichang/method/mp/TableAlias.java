package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.method.MPJBaseMethod;

/**
 * 兼容原生方法
 *
 * @author yulichang
 * @since 1.2.0
 */
public interface TableAlias extends Constants, MPJBaseMethod {

    default String getTableName(TableInfo tableInfo) {
        String from = SqlScriptUtils.convertIf("${ew.from}",
                String.format("%s != null and %s != ''", "ew.from", "ew.from"), true);
        String alias = SqlScriptUtils.convertIf("${ew.alias}" + NEWLINE + from,
                String.format("%s != null and %s instanceof %s", Constants.WRAPPER, Constants.WRAPPER, MPJBaseJoin.class.getName()), true);
        return tableInfo.getTableName() + SPACE + alias;
    }
}
