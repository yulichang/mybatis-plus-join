package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.wrapper.interfaces.LambdaJoin;
import com.github.yulichang.wrapper.interfaces.SFunctionQuery;
import com.github.yulichang.wrapper.interfaces.on.OnFunction;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
 * Lambda 语法使用 Wrapper
 * <p>
 * 推荐使用 Wrappers.<UserDO>lambdaJoin();构造
 *
 * @author yulichang
 * @see com.github.yulichang.toolkit.Wrappers
 */
@SuppressWarnings("all")
public class MPJLambdaWrapper<T> extends MPJAbstractLambdaWrapper<T, MPJLambdaWrapper<T>>
        implements SFunctionQuery<MPJLambdaWrapper<T>>, LambdaJoin<MPJLambdaWrapper<T>> {

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
     * 忽略查询的字段
     */
    private final List<SelectColumn> ignoreColumns = new ArrayList<>();

    /**
     * 表序号
     */
    private int tableIndex = 1;

    /**
     * ON sql wrapper集合
     */
    private final List<MPJLambdaWrapper<?>> onWrappers = new ArrayList<>();

    /**
     * 连表关键字 on 条件 func 使用
     */
    @Getter
    private String keyWord;

    /**
     * 连表实体类 on 条件 func 使用
     */
    @Getter
    private Class<?> joinClass;

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery()
     */
    public MPJLambdaWrapper() {
        super.initNeed();
    }


    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(...)
     */
    MPJLambdaWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                     Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                     SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                     Map<Class<?>, Integer> subTable, String keyWord, Class<?> joinClass) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.subTable = subTable;
        this.keyWord = keyWord;
        this.joinClass = joinClass;
    }

    /**
     * SELECT 部分 SQL 设置
     *
     * @param columns 查询字段
     */
    @SafeVarargs
    public final <S> MPJLambdaWrapper<T> select(SFunction<S, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<S, ?> s : columns) {
                selectColumns.add(new SelectColumn(com.github.yulichang.toolkit.LambdaUtils.getEntityClass(s), getCache(s).getColumn(), null));
            }
        }
        return typedThis;
    }

    public <E> MPJLambdaWrapper<T> select(Class<E> entityClass, Predicate<TableFieldInfo> predicate) {
        TableInfo info = TableInfoHelper.getTableInfo(entityClass);
        Assert.notNull(info, "table can not be find");
        info.getFieldList().stream().filter(predicate).collect(Collectors.toList()).forEach(
                i -> selectColumns.add(new SelectColumn(entityClass, i.getColumn(), null)));
        return typedThis;
    }

    public final <S, X> MPJLambdaWrapper<T> selectAs(SFunction<S, ?> columns, SFunction<X, ?> alias) {
        return selectAs(columns, com.github.yulichang.toolkit.LambdaUtils.getName(alias));
    }

    /**
     * @since 1.1.3
     */
    public final <S, X> MPJLambdaWrapper<T> selectAs(SFunction<S, ?> columns, String alias) {
        selectColumns.add(new SelectColumn(com.github.yulichang.toolkit.LambdaUtils.getEntityClass(columns), getCache(columns).getColumn(), alias));
        return typedThis;
    }

    public final MPJLambdaWrapper<T> selectAll(Class<?> clazz) {
        TableInfo info = TableInfoHelper.getTableInfo(clazz);
        Assert.notNull(info, "table can not be find -> %s", clazz);
        if (info.havePK()) {
            selectColumns.add(new SelectColumn(clazz, info.getKeyColumn(), null));
        }
        info.getFieldList().forEach(c ->
                selectColumns.add(new SelectColumn(clazz, c.getColumn(), null)));
        return typedThis;
    }

    /**
     * 忽略查询字段
     * <p>
     * 用法: selectIgnore(UserDO::getId,UserDO::getSex)
     * 注意: 一个selectIgnore只支持一个对象 如果要忽略多个实体的字段,请调用多次
     * <p>
     * .selectIgnore(UserDO::getId,UserDO::getSex)
     * .selectIgnore(UserAddressDO::getArea,UserAddressDO::getCity)
     *
     * @since 1.1.3
     */
    @SafeVarargs
    public final <S> MPJLambdaWrapper<T> selectIgnore(SFunction<S, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<S, ?> s : columns) {
                ignoreColumns.add(new SelectColumn(com.github.yulichang.toolkit.LambdaUtils.getEntityClass(s), getCache(s).getColumn(), null));
            }
        }
        return typedThis;
    }

    @Override
    public String getSqlSelect() {
        if (StringUtils.isBlank(sqlSelect.getStringValue())) {
            if (CollectionUtils.isNotEmpty(ignoreColumns)) {
                selectColumns.removeIf(c -> ignoreColumns.stream().anyMatch(i ->
                        i.getClazz() == c.getClazz() && Objects.equals(c.getColumnName(), i.getColumnName())));
            }
            String s = selectColumns.stream().map(i ->
                    Constant.TABLE_ALIAS + getDefault(subTable.get(i.getClazz())) + StringPool.DOT + i.getColumnName() +
                            (StringUtils.isBlank(i.getAlias()) ? StringPool.EMPTY : (Constant.AS + i.getAlias())))
                    .collect(Collectors.joining(StringPool.COMMA));
            sqlSelect.setStringValue(s);
        }
        return sqlSelect.getStringValue();
    }


    public String getFrom() {
        if (StringUtils.isBlank(from.getStringValue())) {
            StringBuilder value = new StringBuilder();
            for (MPJLambdaWrapper<?> wrapper : onWrappers) {
                String tableName = TableInfoHelper.getTableInfo(wrapper.getJoinClass()).getTableName();
                value.append(wrapper.getKeyWord())
                        .append(tableName)
                        .append(Constant.SPACE_TABLE_ALIAS)
                        .append(subTable.get(wrapper.getJoinClass()))
                        .append(Constant.ON)
                        .append(wrapper.getExpression().getNormal().getSqlSegment());
            }
            from.setStringValue(value.toString());
        }
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
    protected MPJLambdaWrapper<T> instance() {
        return instance(null, null);
    }

    protected MPJLambdaWrapper<T> instance(String keyWord, Class<?> joinClass) {
        return new MPJLambdaWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                this.subTable, keyWord, joinClass);
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
    }

    @Override
    public <R> MPJLambdaWrapper<T> join(String keyWord, boolean condition, Class<R> clazz, OnFunction function) {
        if (condition) {
            MPJLambdaWrapper<?> apply = function.apply(instance(keyWord, clazz));
            onWrappers.add(apply);
            subTable.put(clazz, tableIndex);
            TableInfo leftInfo = TableInfoHelper.getTableInfo(clazz);
            tableIndex++;
        }
        return typedThis;
    }

    /**
     * select字段
     */
    @Data
    public static class SelectColumn {

        private Class<?> clazz;

        private String columnName;

        private String alias;

        public SelectColumn(Class<?> clazz, String columnName, String alias) {
            this.clazz = clazz;
            this.columnName = columnName;
            this.alias = alias;
        }
    }
}
