package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.*;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import com.github.yulichang.wrapper.interfaces.LambdaJoin;
import com.github.yulichang.wrapper.interfaces.Query;
import com.github.yulichang.wrapper.interfaces.on.OnFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@SuppressWarnings("unused")
public class MPJLambdaWrapper<T> extends MPJAbstractLambdaWrapper<T, MPJLambdaWrapper<T>>
        implements Query<MPJLambdaWrapper<T>>, LambdaJoin<MPJLambdaWrapper<T>> {

    /**
     * 查询字段 sql
     */
    private final SharedString sqlSelect = new SharedString();

    /**
     * 查询表
     */
    private final SharedString from = new SharedString();

    /**
     * 查询的字段
     */
    private final List<String> selectColumns = new ArrayList<>();

    /**
     * 忽略查询的字段
     */
    private final List<String> ignoreColumns = new ArrayList<>();

    /**
     * ON sql wrapper集合
     */
    private final List<String> joinSql = new ArrayList<>();

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery()
     */
    public MPJLambdaWrapper() {
        this.hasAlias = true;
        super.initNeed();
    }


    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(...)
     */
    MPJLambdaWrapper(Class<?> entityClass, AtomicInteger paramNameSeq,
                     Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                     SharedString lastSql, SharedString sqlComment, SharedString sqlFirst, boolean hasAlias) {
        this.entityClass = entityClass;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.hasAlias = hasAlias;
    }

    @Override
    protected MPJLambdaWrapper<T> instance() {
        return instance(true, null);
    }

    protected MPJLambdaWrapper<T> instance(boolean hasAlias, Class<?> entityClass) {
        return new MPJLambdaWrapper<>(entityClass, paramNameSeq, paramNameValuePairs, new MergeSegments(),
                SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(), hasAlias);
    }

    @Override
    @SafeVarargs
    public final <S> MPJLambdaWrapper<T> select(SFunction<S, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<S, ?> s : columns) {
                selectColumns.add(getThisAlias(s) + getCache(s).getColumn());
            }
        }
        return typedThis;
    }

    @Override
    public <E> MPJLambdaWrapper<T> select(Class<E> entityClass, Predicate<TableFieldInfo> predicate) {
        TableInfo info = TableInfoHelper.getTableInfo(entityClass);
        Assert.notNull(info, "table can not be find");
        MPJTableAliasHelper.TableAlias alias = MPJTableAliasHelper.get(entityClass);
        info.getFieldList().stream().filter(predicate).collect(Collectors.toList()).forEach(i ->
                selectColumns.add((hasAlias ? alias.getAliasDOT() : StringPool.EMPTY) + i.getColumn()));
        return typedThis;
    }

    @Override
    public <S> MPJLambdaWrapper<T> selectAs(SFunction<S, ?> column, String alias) {
        selectColumns.add(getThisAlias(column) + getCache(column).getColumn() + Constants.AS + alias);
        return typedThis;
    }

    public <S> MPJLambdaWrapper<T> selectFunc(boolean condition, BaseFuncEnum funcEnum, SFunction<S, ?> column,
                                              String alias) {
        if (condition) {
            selectColumns.add(String.format(funcEnum.getSql(), getThisAlias(column) + getCache(column).getColumn())
                    + Constants.AS + alias);
        }
        return typedThis;
    }

    @Override
    public MPJLambdaWrapper<T> selectFunc(boolean condition, BaseFuncEnum funcEnum, Object column, String alias) {
        if (condition) {
            selectColumns.add(String.format(funcEnum.getSql(), column.toString()) + Constants.AS + alias);
        }
        return typedThis;
    }

    public final MPJLambdaWrapper<T> selectAll(Class<?> clazz) {
        TableInfo info = TableInfoHelper.getTableInfo(clazz);
        Assert.notNull(info, "table can not be find -> %s", clazz);
        String dot = hasAlias ? MPJTableAliasHelper.get(clazz).getAliasDOT() : StringPool.EMPTY;
        if (info.havePK()) {
            selectColumns.add(dot + info.getKeyColumn());
        }
        info.getFieldList().stream().filter(TableFieldInfo::isSelect).forEach(c ->
                selectColumns.add(dot + c.getColumn()));
        return typedThis;
    }

    @Override
    @SafeVarargs
    public final <S> MPJLambdaWrapper<T> selectIgnore(SFunction<S, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<S, ?> s : columns) {
                ignoreColumns.add(getThisAlias(s) + getCache(s).getColumn());
            }
        }
        return typedThis;
    }


    @Override
    public MPJLambdaWrapper<T> selectQuery(Class<?> clazz, OnFunction function, String alias) {
        MPJLambdaWrapper<?> apply = function.apply(instance(false, clazz));
        selectColumns.add(String.format("(SELECT %s FROM %s %s)", apply.getSqlSelect(),
                TableInfoHelper.getTableInfo(clazz).getTableName(), apply.getCustomSqlSegment()) + Constants.AS + alias);
        return this;
    }

    /**
     * 查询条件 SQL 片段
     */
    @Override
    public String getSqlSelect() {
        if (StringUtils.isBlank(sqlSelect.getStringValue())) {
            if (CollectionUtils.isNotEmpty(ignoreColumns)) {
                selectColumns.removeIf(ignoreColumns::contains);
            }
            sqlSelect.setStringValue(String.join(StringPool.COMMA, selectColumns));
        }
        return sqlSelect.getStringValue();
    }

    /**
     * 获取连表部分语句
     */
    public String getFrom() {
        if (StringUtils.isBlank(from.getStringValue())) {
            from.setStringValue(String.join(StringPool.SPACE, joinSql));
        }
        return from.getStringValue();
    }

    public boolean getAutoAlias() {
        return true;
    }


    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
        from.toNull();
        selectColumns.clear();
        ignoreColumns.clear();
        joinSql.clear();
    }

    @Override
    public <R> MPJLambdaWrapper<T> join(String keyWord, boolean condition, Class<R> clazz, OnFunction function) {
        if (condition) {
            joinSql.add(keyWord + TableInfoHelper.getTableInfo(clazz).getTableName() +
                    Constants.SPACE + MPJTableAliasHelper.get(clazz).getAlias() +
                    Constant.ON + function.apply(instance()).getExpression().getNormal().getSqlSegment());
        }
        return typedThis;
    }

    private String getThisAlias(SFunction<?, ?> function) {
        return hasAlias ? MPJTableAliasHelper.get(LambdaUtils.getEntityClass(function)).getAliasDOT() :
                StringPool.EMPTY;
    }
}
