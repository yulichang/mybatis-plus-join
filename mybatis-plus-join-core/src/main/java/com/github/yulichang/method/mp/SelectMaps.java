package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.method.SelectJoinList;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * SelectMaps 兼容MP原生方法
 */
public class SelectMaps extends com.baomidou.mybatisplus.core.injector.methods.SelectMaps implements BaseMethod {

    public SelectMaps() {
        super();
    }

    @SuppressWarnings("unused")
    public SelectMaps(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.SELECT_MAPS;
        String MP_sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlSelectColumns(tableInfo, true), tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo), sqlOrderBy(tableInfo), sqlComment());
        String MPJ_sql = new SelectJoinList(sqlMethod.getMethod()).getSql(tableInfo);
        String sql = SqlScriptUtils.convertChoose(String.format("%s != null and (%s instanceof %s)", WRAPPER, WRAPPER, MPJBaseJoin.class.getName()),
                removeScript(MPJ_sql), removeScript(MP_sql));
        SqlSource sqlSource = super.createSqlSource(configuration, addScript(sql), modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, methodName, sqlSource, Map.class);
    }
}
