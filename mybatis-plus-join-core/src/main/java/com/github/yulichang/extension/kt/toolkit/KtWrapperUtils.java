package com.github.yulichang.extension.kt.toolkit;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.extension.kt.KtLambdaWrapper;
import com.github.yulichang.toolkit.LogicInfoUtils;
import com.github.yulichang.toolkit.StrUtils;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.sql.SqlScriptUtils;

import java.util.Objects;
import java.util.Optional;

/**
 * @author yulichang
 * @since 1.4.6
 */
@SuppressWarnings("DuplicatedCode")
public class KtWrapperUtils {

    public static String buildSubSqlByWrapper(Class<?> clazz, KtLambdaWrapper<?> wrapper, String alias) {
        TableInfo tableInfo = TableHelper.getAssert(clazz);
        String first = Optional.ofNullable(wrapper.getSqlFirst()).orElse(StringPool.EMPTY);
        boolean hasWhere = false;
        String entityWhere = getEntitySql(tableInfo, wrapper);
        if (StrUtils.isNotBlank(entityWhere)) {
            hasWhere = true;
        }
        String mainLogic = mainLogic(hasWhere, clazz, wrapper);
        if (StrUtils.isNotBlank(mainLogic)) {
            hasWhere = true;
        }
        String subLogic = subLogic(hasWhere, wrapper);
        if (StrUtils.isNotBlank(subLogic)) {
            hasWhere = true;
        }
        String sqlSegment = (wrapper.getSqlSegment() != null && StrUtils.isNotBlank(wrapper.getSqlSegment())) ?
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

    public static String buildUnionSqlByWrapper(Class<?> clazz, KtLambdaWrapper<?> wrapper) {
        TableInfo tableInfo = TableHelper.getAssert(clazz);
        String first = Optional.ofNullable(wrapper.getSqlFirst()).orElse(StringPool.EMPTY);
        boolean hasWhere = false;
        String entityWhere = getEntitySql(tableInfo, wrapper);
        if (StrUtils.isNotBlank(entityWhere)) {
            hasWhere = true;
        }
        String mainLogic = mainLogic(hasWhere, clazz, wrapper);
        if (StrUtils.isNotBlank(mainLogic)) {
            hasWhere = true;
        }
        String subLogic = subLogic(hasWhere, wrapper);
        if (StrUtils.isNotBlank(subLogic)) {
            hasWhere = true;
        }
        String sqlSegment = (wrapper.getSqlSegment() != null && StrUtils.isNotBlank(wrapper.getSqlSegment())) ?
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

    private static <T> String formatParam(KtLambdaWrapper<T> wrapper, Object param) {
        final String genParamName = Constants.WRAPPER_PARAM + wrapper.getParamNameSeq().incrementAndGet();
        final String paramStr = wrapper.getParamAlias() + ".paramNameValuePairs." + genParamName;
        wrapper.getParamNameValuePairs().put(genParamName, param);
        return SqlScriptUtils.safeParam(paramStr, null);
    }

    private static String getEntitySql(TableInfo tableInfo, KtLambdaWrapper<?> wrapper) {
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
            sb.append(" AND ").append(wrapper.getTableList().getPrefixByClass(obj.getClass())).append(Constants.DOT)
                    .append(fieldInfo.getColumn()).append(Constants.EQUALS).append(formatParam(wrapper, val));
        }
        //条件不为空 加上 where
        if (sb.length() > 0) {
            sb.delete(0, 4);
            sb.insert(0, " WHERE ");
        }
        return sb.toString();
    }

    private static String mainLogic(boolean hasWhere, Class<?> clazz, KtLambdaWrapper<?> wrapper) {
        if (!wrapper.getLogicSql()) {
            return StringPool.EMPTY;
        }
        String info = LogicInfoUtils.getLogicInfo(null, clazz, true, wrapper.getAlias());
        if (StrUtils.isNotBlank(info)) {
            if (hasWhere) {
                return " AND " + info;
            }
            return " WHERE " + info.substring(4);
        }
        return StringPool.EMPTY;
    }

    private static String subLogic(boolean hasWhere, KtLambdaWrapper<?> wrapper) {
        String sql = wrapper.getSubLogicSql();
        if (StrUtils.isNotBlank(sql)) {
            if (hasWhere) {
                return sql;
            }
            return " WHERE " + sql.substring(4);
        }
        return StringPool.EMPTY;
    }
}
