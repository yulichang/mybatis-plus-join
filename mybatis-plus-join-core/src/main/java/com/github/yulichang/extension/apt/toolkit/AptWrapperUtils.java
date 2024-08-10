package com.github.yulichang.extension.apt.toolkit;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.toolkit.LogicInfoUtils;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.sql.SqlScriptUtils;
import com.github.yulichang.extension.apt.AptQueryWrapper;

import java.util.Objects;
import java.util.Optional;

/**
 * @author yulichang
 * @since 1.4.5
 */
@SuppressWarnings("DuplicatedCode")
public class AptWrapperUtils {

    public static <T> String buildSubSqlByWrapper(Class<T> clazz, AptQueryWrapper<T> wrapper, String alias) {
        TableInfo tableInfo = TableHelper.getAssert(clazz);
        String first = Optional.ofNullable(wrapper.getSqlFirst()).orElse(StringPool.EMPTY);
        boolean hasWhere = false;
        String entityWhere = getEntitySql(tableInfo, wrapper);
        if (StringUtils.isNotBlank(entityWhere)) {
            hasWhere = true;
        }
        String mainLogic = mainLogic(hasWhere, clazz, wrapper);
        if (StringUtils.isNotBlank(mainLogic)) {
            hasWhere = true;
        }
        String subLogic = subLogic(hasWhere, wrapper);
        if (StringUtils.isNotBlank(subLogic)) {
            hasWhere = true;
        }
        String sqlSegment = (wrapper.getSqlSegment() != null && StringUtils.isNotBlank(wrapper.getSqlSegment())) ?
                ((wrapper.isEmptyOfNormal() ? StringPool.EMPTY : (hasWhere ? " AND " : " WHERE ")) + wrapper.getSqlSegment()) : StringPool.EMPTY;

        String sqlComment = Optional.ofNullable(wrapper.getSqlComment()).orElse(StringPool.EMPTY);
        return String.format(" (%s SELECT %s FROM %s %s %s %s %s %s %s) AS %s ",
                first,
                wrapper.getSqlSelect(),
                wrapper.getTableName(tableInfo.getTableName()),
                wrapper.getAlias(),
                wrapper.getFrom(),
                mainLogic,
                subLogic,
                sqlSegment,
                sqlComment,
                alias);
    }

    public static String buildUnionSqlByWrapper(Class<?> clazz, AptQueryWrapper<?> wrapper) {
        TableInfo tableInfo = TableHelper.getAssert(clazz);
        String first = Optional.ofNullable(wrapper.getSqlFirst()).orElse(StringPool.EMPTY);
        boolean hasWhere = false;
        String entityWhere = getEntitySql(tableInfo, wrapper);
        if (StringUtils.isNotBlank(entityWhere)) {
            hasWhere = true;
        }
        String mainLogic = mainLogic(hasWhere, clazz, wrapper);
        if (StringUtils.isNotBlank(mainLogic)) {
            hasWhere = true;
        }
        String subLogic = subLogic(hasWhere, wrapper);
        if (StringUtils.isNotBlank(subLogic)) {
            hasWhere = true;
        }
        String sqlSegment = (wrapper.getSqlSegment() != null && StringUtils.isNotBlank(wrapper.getSqlSegment())) ?
                ((wrapper.isEmptyOfNormal() ? StringPool.EMPTY : (hasWhere ? " AND " : " WHERE ")) + wrapper.getSqlSegment()) : StringPool.EMPTY;

        String sqlComment = Optional.ofNullable(wrapper.getSqlComment()).orElse(StringPool.EMPTY);
        return String.format(" %s SELECT %s FROM %s %s %s %s %s %s %s ",
                first,
                wrapper.getSqlSelect(),
                wrapper.getTableName(tableInfo.getTableName()),
                wrapper.getAlias(),
                wrapper.getFrom(),
                mainLogic,
                subLogic,
                sqlSegment,
                sqlComment);
    }

    private static <T> String formatParam(AptQueryWrapper<T> wrapper, Object param) {
        final String genParamName = Constants.WRAPPER_PARAM + wrapper.getParamNameSeq().incrementAndGet();
        final String paramStr = wrapper.getParamAlias() + ".paramNameValuePairs." + genParamName;
        wrapper.getParamNameValuePairs().put(genParamName, param);
        return SqlScriptUtils.safeParam(paramStr, null);
    }

    private static String getEntitySql(TableInfo tableInfo, AptQueryWrapper<?> wrapper) {
        Object obj = wrapper.getEntity();
        if (Objects.isNull(obj)) {
            return StringPool.EMPTY;
        }
        StringBuilder sb = new StringBuilder(StringPool.EMPTY);
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
            if (AdapterHelper.getAdapter().mpjHasLogic(tableInfo) && fieldInfo.isLogicDelete()) {
                continue;
            }
            Object val;
            try {
                val = fieldInfo.getField().get(obj);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (Objects.isNull(val)) {
                continue;
            }
            String s = wrapper.getAptIndex().get(wrapper.getBaseColumn());
            sb.append(" AND ").append(s == null ? wrapper.getAlias() : s).append(Constants.DOT)
                    .append(fieldInfo.getColumn()).append(Constants.EQUALS).append(formatParam(wrapper, val));
        }
        //条件不为空 加上 where
        if (sb.length() > 0) {
            sb.delete(0, 4);
            sb.insert(0, " WHERE ");
        }
        return sb.toString();
    }

    private static String mainLogic(boolean hasWhere, Class<?> clazz, AptQueryWrapper<?> wrapper) {
        if (!wrapper.getLogicSql()) {
            return StringPool.EMPTY;
        }
        String info = LogicInfoUtils.getLogicInfo(null, clazz, true, wrapper.getAlias());
        if (StringUtils.isNotBlank(info)) {
            if (hasWhere) {
                return " AND " + info;
            }
            return " WHERE " + info.substring(4);
        }
        return StringPool.EMPTY;
    }

    private static String subLogic(boolean hasWhere, AptQueryWrapper<?> wrapper) {
        String sql = wrapper.getSubLogicSql();
        if (StringUtils.isNotBlank(sql)) {
            if (hasWhere) {
                return sql;
            }
            return " WHERE " + sql.substring(4);
        }
        return StringPool.EMPTY;
    }
}
