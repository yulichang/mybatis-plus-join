package com.example.mp.mybatis.plus.method;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.example.mp.mybatis.plus.toolkit.Constant;
import com.example.mp.mybatis.plus.wrapper.MyLambdaQueryWrapper;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.Map;

/**
 * copy {@link com.baomidou.mybatisplus.core.injector.methods.SelectMaps}
 */
public class SelectJoinMaps extends MyAbstractMethod {

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.SELECT_JOIN_MAPS;
        String sql = String.format(sqlMethod.getSql(), sqlSelectColumns(tableInfo, true),
                tableInfo.getTableName(), Constant.TABLE_ALIAS + MyLambdaQueryWrapper.TABLE_ALIAS_INDEX, sqlWhereEntityWrapper(true, tableInfo), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addSelectMappedStatementForOther(mapperClass, sqlMethod.getMethod(), sqlSource, Map.class);
    }
}
