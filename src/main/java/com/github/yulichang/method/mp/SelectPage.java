package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.MPJTableInfoHelper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.toolkit.Constant;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * SelectPage 兼容MP原生方法
 */
public class SelectPage extends com.baomidou.mybatisplus.core.injector.methods.SelectPage implements TableAlias {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        return super.injectMappedStatement(mapperClass, modelClass,
                MPJTableInfoHelper.copyAndSetTableName(tableInfo, getTableName(tableInfo)));
    }

    @Override
    protected String sqlWhereEntityWrapper(boolean newLine, TableInfo table) {
        return SqlScriptUtils.convertChoose(String.format("%s == null or !%s", Constant.PARAM_TYPE, Constant.PARAM_TYPE),
                super.sqlWhereEntityWrapper(newLine, table), mpjSqlWhereEntityWrapper(newLine, table));
    }

    @Override
    protected String sqlSelectColumns(TableInfo table, boolean queryWrapper) {
        String selectColumns = super.sqlSelectColumns(table, queryWrapper);
        return SqlScriptUtils.convertChoose(String.format("%s == null or !%s", Constant.PARAM_TYPE, Constant.PARAM_TYPE),
                selectColumns, mpjSqlSelectColumns() + StringPool.SPACE + selectColumns);
    }
}
