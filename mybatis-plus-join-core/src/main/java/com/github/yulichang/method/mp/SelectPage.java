package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.MPJTableInfoHelper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.interfaces.MPJBaseJoin;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * SelectPage 兼容MP原生方法
 */
public class SelectPage extends com.baomidou.mybatisplus.core.injector.methods.SelectPage implements TableAlias {

    public SelectPage() {
        super();
    }

    @SuppressWarnings("unused")
    public SelectPage(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        return super.injectMappedStatement(mapperClass, modelClass,
                MPJTableInfoHelper.copyAndSetTableName(tableInfo, getTableName(tableInfo)));
    }

    @Override
    protected String sqlWhereEntityWrapper(boolean newLine, TableInfo table) {
        return SqlScriptUtils.convertChoose(String.format("%s == null or !(%s instanceof %s)", Constants.WRAPPER, Constants.WRAPPER, MPJBaseJoin.class.getName()),
                super.sqlWhereEntityWrapper(newLine, table), mpjSqlWhereEntityWrapper(newLine, table));
    }

    @Override
    protected String sqlSelectColumns(TableInfo table, boolean queryWrapper) {
        String selectColumns = super.sqlSelectColumns(table, queryWrapper);
        return SqlScriptUtils.convertChoose(String.format("%s == null or !(%s instanceof %s)", Constants.WRAPPER, Constants.WRAPPER, MPJBaseJoin.class.getName()),
                selectColumns, mpjSqlSelectColumns() + StringPool.SPACE + selectColumns);
    }
}
