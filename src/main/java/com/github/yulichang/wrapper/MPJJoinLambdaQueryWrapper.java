package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.wrapper.interfaces.LambdaJoin;
import com.github.yulichang.wrapper.interfaces.SFunctionQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
 *
 * @author yulichang
 */
@SuppressWarnings("all")
public class MPJJoinLambdaQueryWrapper<T> extends MPJAbstractLambdaWrapper<T, MPJJoinLambdaQueryWrapper<T>>
        implements SFunctionQuery<MPJJoinLambdaQueryWrapper<T>>, LambdaJoin<MPJJoinLambdaQueryWrapper<T>> {

    /**
     * 查询字段 sql
     */
    private SharedString sqlSelect = new SharedString();

    /**
     * 查询表
     */
    private final SharedString from = new SharedString();

    /**
     * 主表别名
     */
    private final SharedString alias = new SharedString(Constant.TABLE_ALIAS);

    /**
     * 查询的字段
     */
    private final List<SelectColumn> selectColumns = new ArrayList<>();

    /**
     * 表序号
     */
    private int tableIndex = 1;

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public MPJJoinLambdaQueryWrapper() {
        this((T) null);
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public MPJJoinLambdaQueryWrapper(T entity) {
        super.setEntity(entity);
        super.initNeed();
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public MPJJoinLambdaQueryWrapper(Class<T> entityClass) {
        super.setEntityClass(entityClass);
        super.initNeed();
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(...)
     */
    MPJJoinLambdaQueryWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                              Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                              SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
    }

    /**
     * SELECT 部分 SQL 设置
     *
     * @param columns 查询字段
     */
    @SafeVarargs
    public final <S> MPJJoinLambdaQueryWrapper<T> select(SFunction<S, ?>... columns) {
        return select(true, columns);
    }

    @SafeVarargs
    public final <S> MPJJoinLambdaQueryWrapper<T> select(boolean condition, SFunction<S, ?>... columns) {
        if (condition && ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<S, ?> s : columns) {
                selectColumns.add(new SelectColumn(LambdaUtils.getEntityClass(s), LambdaUtils.getColumn(s), null));
            }
        }
        return typedThis;
    }

    @Override
    public <E> MPJJoinLambdaQueryWrapper<T> select(Class<E> entityClass, Predicate<TableFieldInfo> predicate) {
        return select(true, entityClass, predicate);
    }

    public <E> MPJJoinLambdaQueryWrapper<T> select(boolean condition, Class<E> entityClass, Predicate<TableFieldInfo> predicate) {
        if (condition) {
            TableInfo info = TableInfoHelper.getTableInfo(entityClass);
            Assert.notNull(info, "table can not be find");
            info.getFieldList().stream().filter(predicate).collect(Collectors.toList()).forEach(
                    i -> selectColumns.add(new SelectColumn(entityClass, i.getColumn(), null)));
        }
        return typedThis;
    }


    public final <S, X> MPJJoinLambdaQueryWrapper<T> selectAs(SFunction<S, ?> columns, SFunction<X, ?> alias) {
        return selectAs(true, columns, alias);
    }

    public final <S, X> MPJJoinLambdaQueryWrapper<T> selectAs(boolean condition, SFunction<S, ?> columns, SFunction<X, ?> alias) {
        if (condition) {
            selectColumns.add(new SelectColumn(LambdaUtils.getEntityClass(columns), LambdaUtils.getColumn(columns), LambdaUtils.getName(alias)));
        }
        return typedThis;
    }

    public final MPJJoinLambdaQueryWrapper<T> selectAll(Class<?> clazz) {
        return selectAll(true, clazz);
    }

    public final MPJJoinLambdaQueryWrapper<T> selectAll(boolean condition, Class<?> clazz) {
        if (condition) {
            TableInfo info = TableInfoHelper.getTableInfo(clazz);
            Assert.notNull(info, "table can not be find -> %s", clazz);
            if (info.havePK()) {
                selectColumns.add(new SelectColumn(clazz, info.getKeyColumn(), null));
            }
            info.getFieldList().forEach(c ->
                    selectColumns.add(new SelectColumn(clazz, c.getColumn(), null)));
        }
        return typedThis;
    }


    @Override
    public String getSqlSelect() {
        if (StringUtils.isBlank(sqlSelect.getStringValue())) {
            String s = selectColumns.stream().map(i ->
                    Constant.TABLE_ALIAS + getDefault(subTable.get(i.getClazz())) + StringPool.DOT + i.getColumnName() +
                            (StringUtils.isBlank(i.getAlias()) ? StringPool.EMPTY : (Constant.AS + i.getAlias())))
                    .collect(Collectors.joining(StringPool.COMMA));
            sqlSelect.setStringValue(s);
        }
        return sqlSelect.getStringValue();
    }


    public String getFrom() {
        return from.getStringValue();
    }

    public String getAlias() {
        return alias.getStringValue();
    }

    /**
     * 用于生成嵌套 sql
     * <p>故 sqlSelect 不向下传递</p>
     */
    @Override
    protected MPJJoinLambdaQueryWrapper<T> instance() {
        return new MPJJoinLambdaQueryWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
    }

    @Override
    public <L, X> MPJJoinLambdaQueryWrapper<T> join(String keyWord, boolean condition, Class<L> clazz, SFunction<L, ?> left, SFunction<X, ?> right) {
        if (condition) {
            subTable.put(clazz, tableIndex);
            TableInfo leftInfo = TableInfoHelper.getTableInfo(clazz);

            StringBuilder sb = new StringBuilder(keyWord)
                    .append(leftInfo.getTableName())
                    .append(StringPool.SPACE)
                    .append(Constant.TABLE_ALIAS)
                    .append(tableIndex)
                    .append(StringPool.SPACE)
                    .append(Constant.ON)
                    .append(Constant.TABLE_ALIAS)
                    .append(tableIndex)
                    .append(StringPool.DOT)
                    .append(LambdaUtils.getColumn(left))
                    .append(Constant.EQUALS)
                    .append(Constant.TABLE_ALIAS)
                    .append(getDefault(subTable.get(LambdaUtils.getEntityClass(right))))
                    .append(StringPool.DOT)
                    .append(LambdaUtils.getColumn(right));

            tableIndex++;
            if (StringUtils.isBlank(from.getStringValue())) {
                from.setStringValue(sb.toString());
            } else {
                from.setStringValue(from.getStringValue() + sb.toString());
            }
        }
        return typedThis;
    }


    /**
     * select字段
     */
    public static class SelectColumn {

        private Class<?> clazz;

        private String columnName;

        private String alias;

        public SelectColumn(Class<?> clazz, String columnName, String alias) {
            this.clazz = clazz;
            this.columnName = columnName;
            this.alias = alias;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }
    }
}
