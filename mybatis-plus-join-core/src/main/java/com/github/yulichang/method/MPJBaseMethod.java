package com.github.yulichang.method;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.adapter.base.metadata.OrderFieldInfo;
import com.github.yulichang.annotation.DynamicTableName;
import com.github.yulichang.config.ConfigProperties;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.List;
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
        if (AdapterHelper.getAdapter().mpjHasLogic(table)) {
            String sqlScript = getAllSqlWhere(table, true, true, WRAPPER_ENTITY_DOT);
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER_ENTITY), true);
            sqlScript += NEWLINE;
            sqlScript += SqlScriptUtils.convertIf(getLogicDeleteSql(table, true, true),
                    String.format("%s.logicSql", WRAPPER), true);
            if (ConfigProperties.subTableLogic) {
                sqlScript += (NEWLINE + String.format("${%s.subLogicSql}", WRAPPER));
            }
            sqlScript += SqlScriptUtils.convertIf(String.format("AND ${%s}", WRAPPER_SQLSEGMENT),
                    String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT, WRAPPER_NONEMPTYOFNORMAL), true);
            sqlScript += NEWLINE;
            sqlScript = SqlScriptUtils.convertWhere(sqlScript);
            sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
                    String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT, WRAPPER_EMPTYOFNORMAL), true);
            sqlScript = SqlScriptUtils.convertChoose(String.format("%s != null", WRAPPER), sqlScript,
                    table.getLogicDeleteSql(false, true));
            return newLine ? NEWLINE + sqlScript : sqlScript;
        } else {
            String sqlScript = getAllSqlWhere(table, false, true, WRAPPER_ENTITY_DOT);
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER_ENTITY), true);
            sqlScript += NEWLINE;
            if (ConfigProperties.subTableLogic) {
                sqlScript += (String.format("${%s.subLogicSql}", WRAPPER) + NEWLINE);
            }
            String s = SqlScriptUtils.convertIf("AND", WRAPPER_NONEMPTYOFNORMAL, true);
            sqlScript += SqlScriptUtils.convertIf(s + String.format(" ${%s}", WRAPPER_SQLSEGMENT),
                    String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT, WRAPPER_NONEMPTYOFWHERE), true);
            sqlScript = SqlScriptUtils.convertWhere(sqlScript) + NEWLINE;
            sqlScript += SqlScriptUtils.convertIf(String.format(" ${%s}", WRAPPER_SQLSEGMENT),
                    String.format("%s != null and %s != '' and %s", WRAPPER_SQLSEGMENT, WRAPPER_SQLSEGMENT, WRAPPER_EMPTYOFWHERE), true);
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", WRAPPER), true);
            return newLine ? NEWLINE + sqlScript : sqlScript;
        }
    }

    /**
     * order By
     */
    default String mpjSqlOrderBy(TableInfo tableInfo) {
        /* 不存在排序字段，直接返回空 */
        List<OrderFieldInfo> orderByFields;
        try {
            orderByFields = AdapterHelper.getAdapter().mpjGetOrderField(tableInfo);
        } catch (Exception e) {
            return StringPool.EMPTY;
        }
        if (CollectionUtils.isEmpty(orderByFields)) {
            return StringPool.EMPTY;
        }
        orderByFields.sort(Comparator.comparingInt(OrderFieldInfo::getSort));
        String sql = NEWLINE + " ORDER BY " +
                orderByFields.stream().map(tfi -> String.format("${ew.alias}.%s %s", tfi.getColumn(),
                        tfi.getType())).collect(joining(","));
        /* 当wrapper中传递了orderBy属性，@orderBy注解失效 */
        return SqlScriptUtils.convertIf(sql, String.format("%s == null or %s", WRAPPER,
                WRAPPER_EXPRESSION_ORDER), true);
    }


    /**
     * 拷贝 tableInfo 里面的 getAllSqlWhere方法
     */
    default String getAllSqlWhere(TableInfo tableInfo, boolean ignoreLogicDelFiled, boolean withId, final String prefix) {
        final String newPrefix = prefix == null ? EMPTY : prefix;
        String filedSqlScript = tableInfo.getFieldList().stream()
                .filter(i -> {
                    if (ignoreLogicDelFiled) {
                        return !(AdapterHelper.getAdapter().mpjHasLogic(tableInfo) && i.isLogicDelete());
                    }
                    return true;
                })
                .map(i -> getSqlWhere(i, newPrefix)).filter(Objects::nonNull).collect(joining(NEWLINE));
        if (!withId || StringUtils.isBlank(tableInfo.getKeyProperty())) {
            return filedSqlScript;
        }
        String newKeyProperty = newPrefix + tableInfo.getKeyProperty();
        String keySqlScript = "${ew.alias}" + DOT + tableInfo.getKeyColumn() + EQUALS +
                SqlScriptUtils.safeParam(newKeyProperty);
        return SqlScriptUtils.convertIf(keySqlScript, String.format("%s != null", newKeyProperty), false)
                + NEWLINE + filedSqlScript;
    }

    default String getSqlWhere(TableFieldInfo tableFieldInfo, final String prefix) {
        final String newPrefix = prefix == null ? EMPTY : prefix;
        // 默认:  AND column=#{prefix + el}
        String sqlScript = " AND " + String.format(tableFieldInfo.getCondition(), "${ew.alias}" + DOT +
                tableFieldInfo.getColumn(), newPrefix + tableFieldInfo.getEl());
        // 查询的时候只判非空
        return convertIf(tableFieldInfo, sqlScript, convertIfProperty(newPrefix, tableFieldInfo.getProperty()),
                tableFieldInfo.getWhereStrategy());
    }

    default String convertIf(TableFieldInfo tableFieldInfo, final String sqlScript, final String property, final FieldStrategy fieldStrategy) {
        if (fieldStrategy == FieldStrategy.NEVER) {
            return null;
        }
        if (AdapterHelper.getAdapter().mpjIsPrimitive(tableFieldInfo) || fieldStrategy == FieldStrategy.IGNORED) {
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
        if (AdapterHelper.getAdapter().mpjHasLogic(tableInfo)) {
            String logicDeleteSql = formatLogicDeleteSql(tableInfo, isWhere);
            if (startWithAnd) {
                logicDeleteSql = " AND " + logicDeleteSql;
            }
            return logicDeleteSql;
        }
        return EMPTY;
    }


    default String formatLogicDeleteSql(TableInfo tableInfo, boolean isWhere) {
        final String value = isWhere ? AdapterHelper.getAdapter().mpjGetLogicField(tableInfo).getLogicNotDeleteValue() :
                AdapterHelper.getAdapter().mpjGetLogicField(tableInfo).getLogicDeleteValue();
        if (isWhere) {
            if (NULL.equalsIgnoreCase(value)) {
                return "${ew.alias}." + AdapterHelper.getAdapter().mpjGetLogicField(tableInfo).getColumn() +
                        " IS NULL";
            } else {
                return "${ew.alias}." + AdapterHelper.getAdapter().mpjGetLogicField(tableInfo).getColumn() +
                        EQUALS + String.format(AdapterHelper.getAdapter().mpjGetLogicField(tableInfo).isCharSequence() ?
                        "'%s'" : "%s", value);
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

    /**
     * 获取表名
     */
    default String mpjTableName(TableInfo tableInfo) {
        DynamicTableName dynamicTableName = tableInfo.getEntityType().getAnnotation(DynamicTableName.class);
        if (Objects.isNull(dynamicTableName)) {
            return tableInfo.getTableName();
        }
        String tableName = tableInfo.getTableName(), encode;
        try {
            encode = URLEncoder.encode(tableName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            encode = tableName;
        }
        boolean en = tableName.equals(encode);
        return String.format("${ew.getTableName%s(\"%s\")}", en ? "" : "Enc", en ? tableName : encode);
    }

    default String mpjSqlSet(boolean logic, boolean ew, TableInfo table, boolean judgeAliasNull, String alias, String prefix) {
        String sqlScript = mpjGetAllSqlSet(table, logic, prefix);
        if (judgeAliasNull) {
            sqlScript = SqlScriptUtils.convertIf(sqlScript, String.format("%s != null", alias), true);
        }
        if (ew) {
            sqlScript += NEWLINE;
            sqlScript += mpjConvertIfEwParam(U_WRAPPER_SQL_SET, false);
        }
        sqlScript = SqlScriptUtils.convertSet(sqlScript);
        return sqlScript;
    }

    default String mpjConvertIfEwParam(final String param, final boolean newLine) {
        return StringPool.EMPTY;
    }

    /**
     * 获取所有的 sql set 片段
     *
     * @param ignoreLogicDelFiled 是否过滤掉逻辑删除字段
     * @param prefix              前缀
     * @return sql 脚本片段
     */
    default String mpjGetAllSqlSet(TableInfo tableInfo, boolean ignoreLogicDelFiled, final String prefix) {
        final String newPrefix = prefix == null ? EMPTY : prefix;
        return tableInfo.getFieldList().stream()
                .filter(i -> {
                    if (ignoreLogicDelFiled) {
                        return !(AdapterHelper.getAdapter().mpjHasLogic(tableInfo) && i.isLogicDelete());
                    }
                    return true;
                }).map(i -> mpjGetSqlSet(i, newPrefix)).filter(Objects::nonNull).collect(joining(NEWLINE));
    }

    /**
     * 获取 set sql 片段
     *
     * @param prefix 前缀
     * @return sql 脚本片段
     */
    default String mpjGetSqlSet(TableFieldInfo tableFieldInfo, final String prefix) {
        return mpjGetSqlSet(tableFieldInfo, false, prefix);
    }

    /**
     * 获取 set sql 片段
     *
     * @param ignoreIf 忽略 IF 包裹
     * @param prefix   前缀
     * @return sql 脚本片段
     */
    default String mpjGetSqlSet(TableFieldInfo tableFieldInfo, final boolean ignoreIf, final String prefix) {
        final String newPrefix = prefix == null ? EMPTY : prefix;
        // 默认: column=
        String sqlSet = "${ew.alias}." + tableFieldInfo.getColumn() + EQUALS;
        if (StringUtils.isNotBlank(tableFieldInfo.getUpdate())) {
            sqlSet += String.format(tableFieldInfo.getUpdate(), tableFieldInfo.getColumn());
        } else {
            sqlSet += SqlScriptUtils.safeParam(newPrefix + tableFieldInfo.getEl());
        }
        sqlSet += COMMA;
        if (ignoreIf) {
            return sqlSet;
        }
        if (tableFieldInfo.isWithUpdateFill()) {
            // 不进行 if 包裹
            return sqlSet;
        }
        return mpjConvertIf(tableFieldInfo, sqlSet, mpjConvertIfProperty(newPrefix, tableFieldInfo.getProperty()), tableFieldInfo.getUpdateStrategy());
    }

    default String mpjConvertIfProperty(String prefix, String property) {
        return StringUtils.isNotBlank(prefix) ? prefix.substring(0, prefix.length() - 1) + "['" + property + "']" : property;
    }

    default String mpjConvertIf(TableFieldInfo tableFieldInfo, final String sqlScript, final String property, final FieldStrategy fieldStrategy) {
        return StringPool.EMPTY;
    }
}
