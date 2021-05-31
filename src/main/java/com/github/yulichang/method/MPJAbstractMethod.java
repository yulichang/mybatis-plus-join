package com.github.yulichang.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.toolkit.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yulichang
 * @see AbstractMethod
 */
public abstract class MPJAbstractMethod extends AbstractMethod {

    /**
     * 连表操作不考虑entity查询和逻辑删除
     */
    @Override
    protected String sqlWhereEntityWrapper(boolean newLine, TableInfo table) {
        String sqlScript = EMPTY;
        sqlScript += SqlScriptUtils.convertIf(String.format(SqlScriptUtils.convertIf(" AND", String.format("%s and %s", WRAPPER_NONEMPTYOFENTITY, WRAPPER_NONEMPTYOFNORMAL), false) + " ${%s}", WRAPPER_SQLSEGMENT),
                String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                        WRAPPER_NONEMPTYOFWHERE), true);
        sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
        sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
                String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT,
                        WRAPPER_EMPTYOFWHERE), true);
        sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER), true);
        return newLine ? NEWLINE + sqlScript : sqlScript;
    }

    @Override
    protected String sqlSelectColumns(TableInfo table, boolean queryWrapper) {
        String selectColumns = ASTERISK;
        if (table.getResultMap() == null || (table.getResultMap() != null)) {
            selectColumns = table.getAllSqlSelect();
            String[] columns = selectColumns.split(StringPool.COMMA);
            List<String> selectColumnList = new ArrayList<>();
            for (String c : columns) {
                selectColumnList.add(Constant.TABLE_ALIAS + StringPool.DOT + c);
            }
            selectColumns = String.join(StringPool.COMMA, selectColumnList);
        }
        if (!queryWrapper) {
            return selectColumns;
        }
        return SqlScriptUtils.convertChoose(String.format("%s != null and %s != null", WRAPPER, Q_WRAPPER_SQL_SELECT),
                SqlScriptUtils.unSafeParam(Q_WRAPPER_SQL_SELECT), selectColumns);
    }

    @Override
    protected String sqlCount() {
        return SqlScriptUtils.convertChoose(String.format("%s != null and %s != null and %s != ''", WRAPPER,
                Q_WRAPPER_SQL_SELECT, Q_WRAPPER_SQL_SELECT),
                SqlScriptUtils.unSafeParam(Q_WRAPPER_SQL_SELECT), ASTERISK);
    }

    protected String sqlAlias() {
        return SqlScriptUtils.convertIf("${ew.alias}", String.format("%s != null and %s != ''", "ew.alias", "ew.alias"), false);
    }

    protected String sqlFrom() {
        return SqlScriptUtils.convertIf("${ew.from}", String.format("%s != null and %s != ''", "ew.from", "ew.from"), false);
    }


}
