package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.interfaces.MPJBaseJoin;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * Delete 兼容MP原生方法
 */
public class Update extends com.baomidou.mybatisplus.core.injector.methods.Update implements TableAlias {

    public Update() {
        super();
    }

    @SuppressWarnings("unused")
    public Update(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        return super.injectMappedStatement(mapperClass, modelClass,
                copyAndSetTableName(tableInfo, getTableName(tableInfo)));
    }

    @Override
    protected String sqlWhereEntityWrapper(boolean newLine, TableInfo table) {
        return SqlScriptUtils.convertChoose(String.format("%s == null or !(%s instanceof %s)", Constants.WRAPPER, Constants.WRAPPER, MPJBaseJoin.class.getName()),
                super.sqlWhereEntityWrapper(newLine, table), mpjSqlWhereEntityWrapper(newLine, table));
    }
}
