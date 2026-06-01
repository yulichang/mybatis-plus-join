package com.github.yulichang.method;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * copy {@link com.baomidou.mybatisplus.core.injector.methods.Update}
 *
 * @author yulichang
 */
public class UpdateJoinAndNull extends MPJAbstractMethod {

    @SuppressWarnings("deprecation")
    public UpdateJoinAndNull() {
        super();
    }

    @SuppressWarnings("unused")
    public UpdateJoinAndNull(String name) {
        super(name);
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.UPDATE_JOIN_AND_NULL;
        String sql = String.format(sqlMethod.getSql(), sqlFirst(), mpjTableName(tableInfo), sqlAlias(), sqlFrom(),
                mpjSqlSet(true, true, tableInfo, true, ENTITY, ENTITY_DOT), sqlWhereEntityWrapper(true, tableInfo), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, removeExtraWhitespaces(sql), modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource);
    }

    @Override
    public String mpjConvertIfEwParam(String param, boolean newLine) {
        try {
            return super.convertIfEwParam(param, newLine);
        } catch (Throwable t) {
            return convertIfEwParamOverride(param, newLine);
        }
    }


    private String convertIfEwParamOverride(final String param, final boolean newLine) {
        return SqlScriptUtils.convertIf(SqlScriptUtils.unSafeParam(param),
                String.format("%s != null and %s != null", WRAPPER, param), newLine);
    }

    /**
     * 转换成 if 标签的脚本片段
     *
     * @param sqlScript     sql 脚本片段
     * @param property      字段名
     * @param fieldStrategy 验证策略
     * @return if 脚本片段
     */
    @Override
    public String mpjConvertIf(TableFieldInfo tableFieldInfo, final String sqlScript, final String property, final FieldStrategy fieldStrategy) {
        if (fieldStrategy == FieldStrategy.NEVER) {
            return null;
        }
        return sqlScript;
    }
}
