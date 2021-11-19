package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.method.MPJBaseMethod;
import com.github.yulichang.toolkit.Constant;

/**
 * 兼容原生方法
 *
 * @author yulichang
 * @since 1.2.0
 */
public interface TableAlias extends Constants, MPJBaseMethod {

    default String getTableName(TableInfo tableInfo) {
        return tableInfo.getTableName() + SPACE + SqlScriptUtils.convertIf("${ew.alias}",
                String.format("%s != null and %s", Constant.PARAM_TYPE, Constant.PARAM_TYPE), false)
                + SPACE + SqlScriptUtils.convertIf("${ew.from}",
                String.format("%s != null and %s and %s != null and %s != ''", Constant.PARAM_TYPE, Constant.PARAM_TYPE,
                        "ew.from", "ew.from"), false);
    }
}
