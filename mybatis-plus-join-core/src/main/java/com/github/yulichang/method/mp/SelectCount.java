package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.MPJTableInfoHelper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.interfaces.MPJBaseJoin;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * SelectCount 兼容MP原生方法
 */
public class SelectCount extends com.baomidou.mybatisplus.core.injector.methods.SelectCount implements TableAlias {

    public SelectCount() {
        super();
    }

    @SuppressWarnings("unused")
    public SelectCount(String name) {
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
}