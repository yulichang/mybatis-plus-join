package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.StrUtils;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * selectOne 兼容MP原生方法
 * <p>
 * 查询满足条件一条数据，为了精简注入方法，该方法采用 list.get(0) 处理后续不再使用
 *
 * @see com.baomidou.mybatisplus.core.injector.methods.SelectOne
 */
@SuppressWarnings("deprecation")
public class SelectOne extends com.baomidou.mybatisplus.core.injector.methods.SelectOne implements TableAlias {

    public SelectOne() {
        super();
    }

    @SuppressWarnings("unused")
    public SelectOne(String name) {
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

    @Override
    protected String sqlSelectColumns(TableInfo table, boolean queryWrapper) {
        String selectColumns = super.sqlSelectColumns(table, queryWrapper);
        return SqlScriptUtils.convertChoose(String.format("%s == null or !(%s instanceof %s)", Constants.WRAPPER, Constants.WRAPPER, MPJBaseJoin.class.getName()),
                selectColumns, mpjSqlSelectColumns() + StringPool.SPACE + selectColumns);
    }

    @Override
    protected String sqlOrderBy(TableInfo table) {
        String orderBy = super.sqlOrderBy(table);
        if (StrUtils.isBlank(orderBy)) {
            return orderBy;
        }
        return SqlScriptUtils.convertChoose(String.format("%s == null or !(%s instanceof %s)", Constants.WRAPPER, Constants.WRAPPER, MPJBaseJoin.class.getName()),
                super.sqlOrderBy(table), mpjSqlOrderBy(table));
    }

    @Override
    protected String sqlComment() {
        return super.sqlComment() + StringPool.NEWLINE + SqlScriptUtils.convertIf("${ew.unionSql}", String.format("%s != null and (%s instanceof %s)",
                Constants.WRAPPER, Constants.WRAPPER, MPJBaseJoin.class.getName()), true);
    }
}
