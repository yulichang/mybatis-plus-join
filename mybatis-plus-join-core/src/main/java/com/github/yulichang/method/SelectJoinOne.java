package com.github.yulichang.method;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.interfaces.MPJBaseJoin;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * copy {@link com.baomidou.mybatisplus.core.injector.methods.SelectMaps}
 *
 * @author yulichang
 */
public class SelectJoinOne extends MPJAbstractMethod {

    @SuppressWarnings("deprecation")
    public SelectJoinOne() {
        super();
    }

    @SuppressWarnings("unused")
    public SelectJoinOne(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.SELECT_JOIN_ONE;
        String sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlDistinct(), sqlSelectColumns(tableInfo, true),
                mpjTableName(tableInfo), sqlAlias(), sqlFrom(), sqlWhereEntityWrapper(true, tableInfo), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, removeExtraWhitespaces(sql), modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, sqlMethod.getMethod(), sqlSource, tableInfo.getEntityType());
    }

    @Override
    protected String sqlComment() {
        return super.sqlComment() + StringPool.NEWLINE + SqlScriptUtils.convertIf("${ew.unionSql}", String.format("%s != null and (%s instanceof %s)",
                Constants.WRAPPER, Constants.WRAPPER, MPJBaseJoin.class.getName()), true);
    }
}
