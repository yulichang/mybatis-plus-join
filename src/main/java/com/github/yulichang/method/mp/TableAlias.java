package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.toolkit.Constant;

/**
 * 兼容原生方法
 *
 * @author yulichang
 * @since 1.2.0
 */
public interface TableAlias extends Constants {

    default String getTableName(TableInfo tableInfo) {
        return tableInfo.getTableName() + SPACE + SqlScriptUtils.convertIf("${ew.alias}",
                String.format("%s != null and %s", Constant.PARAM_TYPE, Constant.PARAM_TYPE), false)
                + SPACE + SqlScriptUtils.convertIf("${ew.from}",
                String.format("%s != null and %s and %s != null and %s != ''", Constant.PARAM_TYPE, Constant.PARAM_TYPE,
                        "ew.from", "ew.from"), false);

    }

    default String mpjSqlWhereEntityWrapper(boolean newLine, TableInfo table) {
        if (table.isWithLogicDelete()) {
            String sqlScript = (NEWLINE + getLogicDeleteSql(table, true, true) + NEWLINE);
            String normalSqlScript = SqlScriptUtils.convertIf(String.format("AND ${%s}", WRAPPER_SQLSEGMENT),
                    String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                            WRAPPER_NONEMPTYOFNORMAL), true);
            normalSqlScript += NEWLINE;
            normalSqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
                    String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                            WRAPPER_EMPTYOFNORMAL), true);
            sqlScript += normalSqlScript;
            sqlScript = SqlScriptUtils.convertChoose(String.format("%s != null", WRAPPER), sqlScript,
                    table.getLogicDeleteSql(false, true));
            sqlScript = SqlScriptUtils.convertWhere(sqlScript);
            return newLine ? NEWLINE + sqlScript : sqlScript;
        } else {
            String sqlScript = SqlScriptUtils.convertIf(String.format("${%s}", WRAPPER_SQLSEGMENT),
                    String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                            WRAPPER_NONEMPTYOFWHERE), true);
            sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
            sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
                    String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                            WRAPPER_EMPTYOFWHERE), true);
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER), true);
            return newLine ? NEWLINE + sqlScript : sqlScript;
        }
    }

    default String getLogicDeleteSql(TableInfo tableInfo, boolean startWithAnd, boolean isWhere) {
        if (tableInfo.isWithLogicDelete()) {
            String logicDeleteSql = formatLogicDeleteSql(tableInfo, isWhere);
            if (startWithAnd) {
                logicDeleteSql = " AND " + logicDeleteSql;
            }
            return logicDeleteSql;
        }
        return EMPTY;
    }


    default String formatLogicDeleteSql(TableInfo tableInfo, boolean isWhere) {
        final String value = isWhere ? tableInfo.getLogicDeleteFieldInfo().getLogicNotDeleteValue() :
                tableInfo.getLogicDeleteFieldInfo().getLogicDeleteValue();
        if (isWhere) {
            if (NULL.equalsIgnoreCase(value)) {
                return "${ew.alias}." + tableInfo.getLogicDeleteFieldInfo().getColumn() + " IS NULL";
            } else {
                return "${ew.alias}." + tableInfo.getLogicDeleteFieldInfo().getColumn() + EQUALS + String.format(
                        tableInfo.getLogicDeleteFieldInfo().isCharSequence() ? "'%s'" : "%s", value);
            }
        }
        final String targetStr = "${ew.alias}." + tableInfo.getLogicDeleteFieldInfo().getColumn() + EQUALS;
        if (NULL.equalsIgnoreCase(value)) {
            return targetStr + NULL;
        } else {
            return targetStr + String.format(tableInfo.getLogicDeleteFieldInfo().isCharSequence() ? "'%s'" : "%s", value);
        }
    }
}
