package com.example.mp.mybatis.plus.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.example.mp.mybatis.plus.toolkit.Constant;
import com.example.mp.mybatis.plus.wrapper.MyLambdaQueryWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yulichang
 * @see AbstractMethod
 */
public abstract class MyAbstractMethod extends AbstractMethod {

    @Override
    protected String sqlSelectColumns(TableInfo table, boolean queryWrapper) {
        String selectColumns = ASTERISK;
        if (table.getResultMap() == null || (table.getResultMap() != null)) {
            selectColumns = table.getAllSqlSelect();
            String[] columns = selectColumns.split(StringPool.COMMA);
            List<String> selectColumnList = new ArrayList<>();
            for (String c : columns) {
                selectColumnList.add(Constant.TABLE_ALIAS + MyLambdaQueryWrapper.TABLE_ALIAS_INDEX + StringPool.DOT + c);
            }
            selectColumns = String.join(StringPool.COMMA, selectColumnList);
        }
        if (!queryWrapper) {
            return selectColumns;
        }
        return SqlScriptUtils.convertChoose(String.format("%s != null and %s != null", WRAPPER, Q_WRAPPER_SQL_SELECT),
                SqlScriptUtils.unSafeParam(Q_WRAPPER_SQL_SELECT), selectColumns);
    }
}
