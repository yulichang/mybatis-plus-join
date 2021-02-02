package com.github.mybatisplus.wrapper;

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
import com.github.mybatisplus.toolkit.Constant;
import com.github.mybatisplus.toolkit.MyLambdaUtils;
import com.github.mybatisplus.wrapper.interfaces.MyLambdaJoin;
import com.github.mybatisplus.wrapper.interfaces.MySFunctionQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
 */
@SuppressWarnings("serial")
public class MyJoinLambdaQueryWrapper<T> extends MyAbstractLambdaWrapper<T, MyJoinLambdaQueryWrapper<T>>
        implements MySFunctionQuery<MyJoinLambdaQueryWrapper<T>>, MyLambdaJoin<MyJoinLambdaQueryWrapper<T>> {

    /**
     * 查询字段 sql
     */
    private SharedString sqlSelect = new SharedString();

    /**
     * 查询表
     */
    private SharedString from = new SharedString();

    /**
     * 主表别名
     */
    private SharedString alias = new SharedString();

    /**
     * 查询的字段
     */
    private List<SelectColumn> selectColumns = new ArrayList<>();


    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public MyJoinLambdaQueryWrapper() {
        this((T) null);
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public MyJoinLambdaQueryWrapper(T entity) {
        super.setEntity(entity);
        super.initNeed();
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public MyJoinLambdaQueryWrapper(Class<T> entityClass) {
        super.setEntityClass(entityClass);
        super.initNeed();
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(...)
     */
    MyJoinLambdaQueryWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
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
    public final <S> MyJoinLambdaQueryWrapper<T> select(SFunction<S, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<S, ?> s : columns) {
                Class<S> clazz = MyLambdaUtils.getEntityClass(s);
                TableInfo info = TableInfoHelper.getTableInfo(clazz);
                selectColumns.add(new SelectColumn(clazz, info.getTableName(), MyLambdaUtils.getColumn(s), null));
            }
        }
        return typedThis;
    }

    @Override
    public <E> MyJoinLambdaQueryWrapper<T> select(Class<E> entityClass, Predicate<TableFieldInfo> predicate) {
        TableInfo info = TableInfoHelper.getTableInfo(entityClass);
        Assert.notNull(info, "table can not be find");
        info.getFieldList().stream().filter(predicate).collect(Collectors.toList()).forEach(
                i -> selectColumns.add(new SelectColumn(entityClass, info.getTableName(), i.getColumn(), null)));
        return typedThis;
    }


    public final <S, X> MyJoinLambdaQueryWrapper<T> selectAs(SFunction<S, ?> columns, SFunction<X, ?> alias) {
        Class<S> clazz = MyLambdaUtils.getEntityClass(columns);
        TableInfo info = TableInfoHelper.getTableInfo(clazz);
        Assert.notNull(info, "table can not be find for lambda");
        selectColumns.add(new SelectColumn(clazz, info.getTableName(), MyLambdaUtils.getColumn(columns), MyLambdaUtils.getName(alias)));
        return typedThis;
    }

    public final MyJoinLambdaQueryWrapper<T> selectAll(Class<?> clazz) {
        TableInfo info = TableInfoHelper.getTableInfo(clazz);
        Assert.notNull(info, "table can not be find -> %s", clazz);
        selectColumns.add(new SelectColumn(clazz, info.getTableName(), info.getKeyColumn(), null));
        info.getFieldList().forEach(c ->
                selectColumns.add(new SelectColumn(clazz, info.getTableName(), c.getColumn(), null)));
        return typedThis;
    }

    @Override
    public String getSqlSelect() {
        if (StringUtils.isBlank(sqlSelect.getStringValue())) {
            String s = selectColumns.stream().map(i ->
                    i.getTableName() + StringPool.DOT + i.getColumnName() +
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
    protected MyJoinLambdaQueryWrapper<T> instance() {
        return new MyJoinLambdaQueryWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
    }

    @Override
    public <L, X> MyJoinLambdaQueryWrapper<T> join(String keyWord, boolean condition, Class<L> clazz, SFunction<L, ?> left, SFunction<X, ?> right) {
        if (condition) {
            TableInfo leftInfo = TableInfoHelper.getTableInfo(clazz);
            TableInfo rightInfo = TableInfoHelper.getTableInfo(MyLambdaUtils.getEntityClass(right));

            String s = keyWord + leftInfo.getTableName() + Constant.ON + leftInfo.getTableName() + StringPool.DOT
                    + MyLambdaUtils.getColumn(left) + Constant.EQUALS + rightInfo.getTableName() + StringPool.DOT
                    + MyLambdaUtils.getColumn(right);

            if (StringUtils.isBlank(from.getStringValue())) {
                from.setStringValue(s);
            } else {
                from.setStringValue(from.getStringValue() + s);
            }
        }
        return typedThis;
    }

    /**
     * select字段
     */
    public static class SelectColumn {

        private Class<?> clazz;

        private String tableName;

        private String columnName;

        private String alias;

        public SelectColumn(Class<?> clazz, String tableName, String columnName, String alias) {
            this.clazz = clazz;
            this.tableName = tableName;
            this.columnName = columnName;
            this.alias = alias;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public String getTableName() {
            return tableName;
        }

        public void setTableName(String tableName) {
            this.tableName = tableName;
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
