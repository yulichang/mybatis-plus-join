package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.method.SelectJoinOne;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * selectOne 兼容MP原生方法
 * <p>
 * 查询满足条件一条数据，为了精简注入方法，该方法采用 list.get(0) 处理后续不再使用
 *
 * @see com.baomidou.mybatisplus.core.injector.methods.SelectOne
 */
@SuppressWarnings("deprecation")
public class SelectOne extends com.baomidou.mybatisplus.core.injector.methods.SelectOne implements BaseMethod {

    public SelectOne() {
        super();
    }

    @SuppressWarnings("unused")
    public SelectOne(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.SELECT_ONE;
        String MP_sql = String.format(sqlMethod.getSql(),
                sqlFirst(), sqlSelectColumns(tableInfo, true), tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo), sqlComment());
        String MPJ_sql = new SelectJoinOne(sqlMethod.getMethod()).getSql(tableInfo);
        String sql = SqlScriptUtils.convertChoose(String.format("%s != null and (%s instanceof %s)", WRAPPER, WRAPPER, MPJBaseJoin.class.getName()),
                removeScript(MPJ_sql), removeScript(MP_sql));
        SqlSource sqlSource = super.createSqlSource(configuration, addScript(sql), modelClass);
        return this.addSelectMappedStatementForTable(mapperClass, methodName, sqlSource, tableInfo);
    }
}
