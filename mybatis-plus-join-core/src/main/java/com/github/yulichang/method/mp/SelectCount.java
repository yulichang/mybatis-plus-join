package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.method.SelectJoinCount;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * SelectCount 兼容MP原生方法
 */
public class SelectCount extends com.baomidou.mybatisplus.core.injector.methods.SelectCount implements BaseMethod {

    public SelectCount() {
        super();
    }

    @SuppressWarnings("unused")
    public SelectCount(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.SELECT_COUNT;
        String MP_sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlCount(), tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo), sqlComment());
        String MPJ_sql = new SelectJoinCount(sqlMethod.getMethod()).getSql(tableInfo);
        String sql = SqlScriptUtils.convertChoose(String.format("%s != null and (%s instanceof %s)", WRAPPER, WRAPPER, MPJBaseJoin.class.getName()),
                removeScript(MPJ_sql), removeScript(MP_sql));
        SqlSource sqlSource = super.createSqlSource(configuration, addScript(sql), modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, methodName, sqlSource, Long.class);
    }
}