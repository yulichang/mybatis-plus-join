package com.github.yulichang.method;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.adapter.AdapterHelper;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * copy {@link com.baomidou.mybatisplus.core.injector.methods.Delete}
 *
 * @author yulichang
 */
public class DeleteJoin extends MPJAbstractMethod {

    @SuppressWarnings("deprecation")
    public DeleteJoin() {
        super();
    }

    @SuppressWarnings("unused")
    public DeleteJoin(String name) {
        super(name);
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.LOGIC_DELETE_JOIN;
        if (AdapterHelper.getTableInfoAdapter().mpjHasLogic(tableInfo)) {
            String sql = String.format(sqlMethod.getSql(), sqlFirst(), mpjTableName(tableInfo), sqlAlias(), sqlFrom(),
                    mpjDeleteLogic(tableInfo), sqlWhereEntityWrapper(true, tableInfo), sqlComment());
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
            return this.addUpdateMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource);
        } else {
            sqlMethod = SqlMethod.DELETE_JOIN;
            String sql = String.format(sqlMethod.getSql(), sqlFirst(), mpjDelete(), mpjTableName(tableInfo),
                    sqlAlias(), sqlFrom(), sqlWhereEntityWrapper(true, tableInfo), sqlComment());
            SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
            return this.addDeleteMappedStatement(mapperClass, sqlMethod.getMethod(), sqlSource);
        }
    }

    private String mpjDelete() {
        return SqlScriptUtils.convertChoose(String.format("%s == null or %s.deleteSql == ''", Constants.WRAPPER, Constants.WRAPPER),
                "${ew.alias}", "${ew.deleteSql}");
    }

    private String mpjDeleteLogic(TableInfo tableInfo) {
        return "SET ${ew.alias}." + tableInfo.getLogicDeleteSql(false, false) + " ${ew.deleteLogicSql}";
    }
}
