package com.github.yulichang.method.mp;

import com.baomidou.mybatisplus.core.metadata.MPJTableInfoHelper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.toolkit.Constant;
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
}
