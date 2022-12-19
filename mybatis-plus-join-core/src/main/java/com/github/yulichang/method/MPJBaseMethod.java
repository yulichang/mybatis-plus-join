package com.github.yulichang.method;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.config.ConfigProperties;

import java.util.Objects;

import static java.util.stream.Collectors.joining;

/**
 * 连表sql条件
 *
 * @author yulichang
 * @since 1.2.0
 */
public interface MPJBaseMethod extends Constants {

    default String mpjSqlWhereEntityWrapper(boolean newLine, TableInfo table) {
        if (table.isWithLogicDelete()) {
            String sqlScript = getAllSqlWhere(table, true, true, WRAPPER_ENTITY_DOT);
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER_ENTITY), true);
            sqlScript += NEWLINE;
            sqlScript += SqlScriptUtils.convertIf(getLogicDeleteSql(table, true, true), String.format("%s.logicSql", WRAPPER), true);
            if (ConfigProperties.subTableLogic) {
                sqlScript += (NEWLINE + String.format("${%s.subLogicSql}", WRAPPER));
            }
            sqlScript += SqlScriptUtils.convertIf(String.format("AND ${%s}", WRAPPER_SQLSEGMENT), String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT, WRAPPER_NONEMPTYOFNORMAL), true);
            sqlScript += NEWLINE;
            sqlScript = SqlScriptUtils.convertWhere(sqlScript);
            sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT), String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT, WRAPPER_EMPTYOFNORMAL), true);
            sqlScript = SqlScriptUtils.convertChoose(String.format("%s != null", WRAPPER), sqlScript, table.getLogicDeleteSql(false, true));
            return newLine ? NEWLINE + sqlScript : sqlScript;
        } else {
            String sqlScript = getAllSqlWhere(table, false, true, WRAPPER_ENTITY_DOT);
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER_ENTITY), true);
            sqlScript += NEWLINE;
            if (ConfigProperties.subTableLogic) {
                sqlScript += (String.format("${%s.subLogicSql}", WRAPPER) + NEWLINE);
            }
            sqlScript += SqlScriptUtils.convertIf(String.format("AND ${%s}", WRAPPER_SQLSEGMENT), String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT, WRAPPER_NONEMPTYOFWHERE), true);
            sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
            sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT), String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT, WRAPPER_EMPTYOFWHERE), true);
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER), true);
            return newLine ? NEWLINE + sqlScript : sqlScript;
        }
    }


    /**
     * 拷贝 tableInfo 里面的 getAllSqlWhere方法
     */
    default String getAllSqlWhere(TableInfo tableInfo, boolean ignoreLogicDelFiled, boolean withId, final String prefix) {
        final String newPrefix = prefix == null ? EMPTY : prefix;
        String filedSqlScript = tableInfo.getFieldList().stream()
                .filter(i -> {
                    if (ignoreLogicDelFiled) {
                        return !(tableInfo.isWithLogicDelete() && i.isLogicDelete());
                    }
                    return true;
                })
                .map(i -> getSqlWhere(i, newPrefix)).filter(Objects::nonNull).collect(joining(NEWLINE));
        if (!withId || StringUtils.isBlank(tableInfo.getKeyProperty())) {
            return filedSqlScript;
        }
        String newKeyProperty = newPrefix + tableInfo.getKeyProperty();
        String keySqlScript = ConfigProperties.tableAlias + DOT + tableInfo.getKeyColumn() + EQUALS + SqlScriptUtils.safeParam(newKeyProperty);
        return SqlScriptUtils.convertIf(keySqlScript, String.format("%s != null", newKeyProperty), false)
                + NEWLINE + filedSqlScript;
    }

    default String getSqlWhere(TableFieldInfo tableFieldInfo, final String prefix) {
        final String newPrefix = prefix == null ? EMPTY : prefix;
        // 默认:  AND column=#{prefix + el}
        String sqlScript = " AND " + String.format(tableFieldInfo.getCondition(), ConfigProperties.tableAlias + DOT + tableFieldInfo.getColumn(), newPrefix + tableFieldInfo.getEl());
        // 查询的时候只判非空
        return convertIf(tableFieldInfo, sqlScript, convertIfProperty(newPrefix, tableFieldInfo.getProperty()), tableFieldInfo.getWhereStrategy());
    }

    default String convertIf(TableFieldInfo tableFieldInfo, final String sqlScript, final String property, final FieldStrategy fieldStrategy) {
        if (fieldStrategy == FieldStrategy.NEVER) {
            return null;
        }
        if (tableFieldInfo.isPrimitive() || fieldStrategy == FieldStrategy.IGNORED) {
            return sqlScript;
        }
        if (fieldStrategy == FieldStrategy.NOT_EMPTY && tableFieldInfo.isCharSequence()) {
            return SqlScriptUtils.convertIf(sqlScript, String.format("%s != null and %s != ''", property, property),
                    false);
        }
        return SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", property), false);
    }

    default String convertIfProperty(String prefix, String property) {
        return StringUtils.isNotBlank(prefix) ? prefix.substring(0, prefix.length() - 1) + "['" + property + "']" : property;
    }


    default String getLogicDeleteSql(TableInfo tableInfo, boolean startWithAnd, boolean isWhere) {
        if (tableInfo.isWithLogicDelete()) {
            String logicDeleteSql = formatLogicDeleteSql(tableInfo, isWhere);
            if (startWithAnd) {
                logicDeleteSql = " AND " + logicDeleteSql;
            }
            return logicDeleteSql;
        }
        return EMPTY;
    }


    default String formatLogicDeleteSql(TableInfo tableInfo, boolean isWhere) {
        final String value = isWhere ? tableInfo.getLogicDeleteFieldInfo().getLogicNotDeleteValue() : tableInfo.getLogicDeleteFieldInfo().getLogicDeleteValue();
        if (isWhere) {
            if (NULL.equalsIgnoreCase(value)) {
                return "${ew.alias}." + tableInfo.getLogicDeleteFieldInfo().getColumn() + " IS NULL";
            } else {
                return "${ew.alias}." + tableInfo.getLogicDeleteFieldInfo().getColumn() + EQUALS + String.format(tableInfo.getLogicDeleteFieldInfo().isCharSequence() ? "'%s'" : "%s", value);
            }
        }
        final String targetStr = "${ew.alias}." + tableInfo.getLogicDeleteFieldInfo().getColumn() + EQUALS;
        if (NULL.equalsIgnoreCase(value)) {
            return targetStr + NULL;
        } else {
            return targetStr + String.format(tableInfo.getLogicDeleteFieldInfo().isCharSequence() ? "'%s'" : "%s", value);
        }
    }

    default String mpjSqlSelectColumns() {
        return SqlScriptUtils.convertIf("DISTINCT", "ew.selectDistinct", false);
    }

}
