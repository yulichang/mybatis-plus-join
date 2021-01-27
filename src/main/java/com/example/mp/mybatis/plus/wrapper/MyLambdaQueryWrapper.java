package com.example.mp.mybatis.plus.wrapper;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.example.mp.mybatis.plus.base.MyBaseEntity;
import com.example.mp.mybatis.plus.func.MySFunction;
import com.example.mp.mybatis.plus.toolkit.Constant;
import com.example.mp.mybatis.plus.toolkit.MyLambdaUtils;
import com.example.mp.mybatis.plus.wrapper.interfaces.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.*;
import static com.baomidou.mybatisplus.core.enums.WrapperKeyword.APPLY;
import static java.util.stream.Collectors.joining;

/**
 * 连接查询Query
 *
 * @author yulichang
 * @see com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 * @see com.baomidou.mybatisplus.core.conditions.interfaces.Compare
 * @see com.baomidou.mybatisplus.core.conditions.interfaces.Join
 * @see com.baomidou.mybatisplus.core.conditions.interfaces.Nested
 * @see com.baomidou.mybatisplus.core.conditions.interfaces.Func
 * @since 2021/01/19
 */
public class MyLambdaQueryWrapper<T extends MyBaseEntity> extends MyJoinLambdaQueryWrapper<T>
        implements MyCompare<MyLambdaQueryWrapper<T>>, MyNested<MyLambdaQueryWrapper<T>, MyLambdaQueryWrapper<T>>,
        MyFunc<MyLambdaQueryWrapper<T>> {

    /**
     * 表别名初始序号
     */
    public static final int TABLE_ALIAS_INDEX = 0;

    /**
     * 主表默认别名
     */
    public static final String DEFAULT_ALIAS = "t";

    public MyLambdaQueryWrapper() {
        this(null);
    }

    public MyLambdaQueryWrapper(T entity) {
        super.rUid = TABLE_ALIAS_INDEX;
        super.setEntity(entity);
        super.initNeed();
    }

    MyLambdaQueryWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                         Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                         SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                         int rUid, List<SelectColumn> selectColumns, List<SubTable> classList) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.rUid = rUid;
        this.selectColumnList = selectColumns;
        this.classList = classList;
    }

    @Override
    protected MyLambdaQueryWrapper<T> instance() {
        return new MyLambdaQueryWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                rUid, selectColumnList, classList);
    }


    @Override
    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> leftJoin(boolean condition, String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return join(condition, alias, Constant.LEFT_JOIN, leftCondition, rightCondition, rightWrapper);
    }

    @Override
    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> leftJoin(MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return this.leftJoin(true, null, leftCondition, rightCondition, rightWrapper);
    }

    @Override
    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> leftJoin(String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return this.leftJoin(true, alias, leftCondition, rightCondition, rightWrapper);
    }

    @Override
    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> leftJoin(boolean condition, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return this.leftJoin(condition, null, leftCondition, rightCondition, rightWrapper);
    }

    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> rightJoin(boolean condition, String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return join(condition, alias, Constant.RIGHT_JOIN, leftCondition, rightCondition, rightWrapper);
    }

    @Override
    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> rightJoin(MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return this.rightJoin(true, null, leftCondition, rightCondition, rightWrapper);
    }

    @Override
    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> rightJoin(String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return this.rightJoin(true, alias, leftCondition, rightCondition, rightWrapper);
    }

    @Override
    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> rightJoin(boolean condition, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return this.rightJoin(condition, null, leftCondition, rightCondition, rightWrapper);
    }


    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> innerJoin(boolean condition, String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return join(condition, alias, Constant.INNER_JOIN, leftCondition, rightCondition, rightWrapper);
    }

    @Override
    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> innerJoin(MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return this.innerJoin(true, null, leftCondition, rightCondition, rightWrapper);
    }

    @Override
    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> innerJoin(String alias, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return this.innerJoin(true, alias, leftCondition, rightCondition, rightWrapper);
    }

    @Override
    public <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> innerJoin(boolean condition, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        return this.innerJoin(condition, null, leftCondition, rightCondition, rightWrapper);
    }

    private <R extends MyBaseEntity, TE, RE> MyLambdaQueryWrapper<T> join(boolean condition, String alias, String keyWord, MySFunction<T, TE> leftCondition, MySFunction<R, RE> rightCondition, Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        if (condition) {
            setEntityClass(MyLambdaUtils.getEntityClass(leftCondition));
            classList.add(new SubTable(alias, keyWord, rUid, MyLambdaUtils.getColumn(leftCondition), classList.size() + 1, MyLambdaUtils.getColumn(rightCondition), TableInfoHelper.getTableInfo(MyLambdaUtils.getEntityClass(rightCondition)).getTableName()));
            MyJoinLambdaQueryWrapper<R> apply = rightWrapper.apply(new MyJoinLambdaQueryWrapper<>(classList.size()));
            classList.addAll(apply.classList);
            this.selectColumnList.addAll(apply.selectColumnList);
        }
        return this;
    }

    @SafeVarargs
    @Override
    public final MyLambdaQueryWrapper<T> select(SFunction<T, ?>... columns) {
        super.select(columns);
        return this;
    }

    @Override
    public MyLambdaQueryWrapper<T> selectAll(Class<T> clazz) {
        super.selectAll(clazz);
        return this;
    }

    @Override
    public MyLambdaQueryWrapper<T> selectDistinct() {
        super.selectDistinct();
        return this;
    }

    @Override
    public MyLambdaQueryWrapper<T> select(Predicate<TableFieldInfo> predicate) {
        super.select(predicate);
        return this;
    }

    @Override
    public MyLambdaQueryWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        super.select(entityClass, predicate);
        return this;
    }

    @Override
    public <DTO> MyLambdaQueryWrapper<T> as(SFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        super.as(entityColumn, DTOColumn);
        return this;
    }

    public <DTO> MyLambdaQueryWrapper<T> asCount(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        super.asCount(entityColumn, DTOColumn);
        return this;
    }

    public <DTO> MyLambdaQueryWrapper<T> asSum(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        super.asSum(entityColumn, DTOColumn);
        return this;
    }

    public <DTO> MyLambdaQueryWrapper<T> asAvg(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        super.asAvg(entityColumn, DTOColumn);
        return this;
    }

    public <DTO> MyLambdaQueryWrapper<T> asMax(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        super.asMax(entityColumn, DTOColumn);
        return this;
    }

    public <DTO> MyLambdaQueryWrapper<T> asMin(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        super.asMin(entityColumn, DTOColumn);
        return this;
    }

    public <DTO> MyLambdaQueryWrapper<T> asDateFormat(MySFunction<T, ?> entityColumn, SFunction<DTO, ?> DTOColumn) {
        super.asDateFormat(entityColumn, DTOColumn);
        return this;
    }


    @Override
    public String getSqlSelect() {
        if (StringUtils.isBlank(sqlSelect.getStringValue()) && CollectionUtils.isNotEmpty(selectColumnList)) {
            List<String> collect = new ArrayList<>();
            selectColumnList.forEach(c -> {
                String s = Constant.TABLE_ALIAS + c.getUid() + StringPool.DOT + c.getColumn();
                if (c.getFunction() != null) {
                    s = String.format(c.getFunction().getSql(), s);
                }
                if (Objects.isNull(c.getAs())) {
                    collect.add(s);
                } else {
                    collect.add(s + Constant.AS + c.getAs());
                }
            });
            this.sqlSelect.setStringValue(String.join(StringPool.COMMA, collect));
        }
        return sqlSelect.getStringValue();
    }

    @Override
    public void clear() {
        super.clear();
    }

    public String getFrom() {
        if (StringUtils.isNotBlank(from.getStringValue())) {
            return from.getStringValue();
        }
        StringBuilder sb = new StringBuilder();
        this.classList.forEach(right ->
                sb.append(right.getKeyWord()).append(right.getRightTableName())
                        .append(StringPool.SPACE)
                        .append(Constant.TABLE_ALIAS)
                        .append(right.getRightUid())
                        .append(Constant.ON)
                        .append(Constant.TABLE_ALIAS)
                        .append(right.getLeftUid())
                        .append(StringPool.DOT)
                        .append(right.getLeftColumn())
                        .append(Constant.EQUALS)
                        .append(Constant.TABLE_ALIAS)
                        .append(right.getRightUid())
                        .append(StringPool.DOT)
                        .append(right.getRightColumn())
                        .append(StringPool.SPACE)
        );
        this.from.setStringValue(sb.toString());
        return this.from.getStringValue();
    }

    /**
     * 获取表前缀
     */
    private String getClassTablePrefix(Class<?> tag) {
        if (getEntityClass() == tag) {
            return Constant.TABLE_ALIAS + rUid;
        } else {
            String tableName = TableInfoHelper.getTableInfo(tag).getTableName();
            for (SubTable sub : classList) {
                if (sub.getRightTableName().equals(tableName)) {
                    return Constant.TABLE_ALIAS + sub.getRightUid();
                }
            }
        }
        throw new MybatisPlusException("table not find");
    }

    /**
     * 获取column
     */
    private <E extends MyBaseEntity, F> String getColumn(MySFunction<E, F> column, String alias) {
        if (alias != null) {
            if (alias.equals(DEFAULT_ALIAS)) {
                return Constant.TABLE_ALIAS + rUid + StringPool.DOT + column2String(column, true);
            }
            for (SubTable sub : classList) {
                if (alias.equals(sub.getAlias())) {
                    return Constant.TABLE_ALIAS + sub.getRightUid() + StringPool.DOT + column2String(column, true);
                }
            }
        }
        return getClassTablePrefix(MyLambdaUtils.getEntityClass(column)) + StringPool.DOT + column2String(column, true);
    }

    private <E extends MyBaseEntity, F> String getColumn(String alias, MySFunction<E, F>... column) {
        return Arrays.stream(column).map(i -> getColumn(i, alias)).collect(joining(StringPool.COMMA));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> eq(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), EQ,
                () -> formatSql("{0}", val));
    }

    @Override
    public <E extends MyBaseEntity, F, X extends MyBaseEntity, Y> MyLambdaQueryWrapper<T> eq(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), EQ, () -> getColumn(val, as));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> ne(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), NE, () -> formatSql("{0}", val));
    }

    @Override
    public <E extends MyBaseEntity, F, X extends MyBaseEntity, Y> MyLambdaQueryWrapper<T> ne(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), NE, () -> getColumn(val, as));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> gt(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), GT, () -> formatSql("{0}", val));
    }

    @Override
    public <E extends MyBaseEntity, F, X extends MyBaseEntity, Y> MyLambdaQueryWrapper<T> gt(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), GT, () -> getColumn(val, as));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> ge(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), GE, () -> formatSql("{0}", val));
    }

    @Override
    public <E extends MyBaseEntity, F, X extends MyBaseEntity, Y> MyLambdaQueryWrapper<T> ge(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), GE, () -> getColumn(val, as));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> lt(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), LT, () -> formatSql("{0}", val));
    }

    @Override
    public <E extends MyBaseEntity, F, X extends MyBaseEntity, Y> MyLambdaQueryWrapper<T> lt(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), LT, () -> getColumn(val, as));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> le(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), LE, () -> formatSql("{0}", val));
    }

    @Override
    public <E extends MyBaseEntity, F, X extends MyBaseEntity, Y> MyLambdaQueryWrapper<T> le(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), LE, () -> getColumn(val, as));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> between(boolean condition, String alias, MySFunction<E, F> column, Object val1, Object val2) {
        return doIt(condition, () -> getColumn(column, alias), BETWEEN, () -> formatSql("{0}", val1), AND,
                () -> formatSql("{0}", val2));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> notBetween(boolean condition, String alias, MySFunction<E, F> column, Object val1, Object val2) {
        return doIt(condition, () -> getColumn(column, alias), NOT_BETWEEN, () -> formatSql("{0}", val1), AND,
                () -> formatSql("{0}", val2));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> like(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return likeValue(condition, LIKE, getColumn(column, alias), val, SqlLike.DEFAULT);
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> notLike(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return likeValue(condition, NOT_LIKE, getColumn(column, alias), val, SqlLike.DEFAULT);
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> likeLeft(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return likeValue(condition, LIKE, getColumn(column, alias), val, SqlLike.LEFT);
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> likeRight(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return likeValue(condition, LIKE, getColumn(column, alias), val, SqlLike.RIGHT);
    }

    @Override
    public MyLambdaQueryWrapper<T> and(boolean condition, Consumer<MyLambdaQueryWrapper<T>> consumer) {
        return and(condition).addNestedCondition(condition, consumer);
    }

    @Override
    public MyLambdaQueryWrapper<T> or(boolean condition, Consumer<MyLambdaQueryWrapper<T>> consumer) {
        return or(condition).addNestedCondition(condition, consumer);
    }

    @Override
    public MyLambdaQueryWrapper<T> nested(boolean condition, Consumer<MyLambdaQueryWrapper<T>> consumer) {
        return addNestedCondition(condition, consumer);
    }

    @Override
    public MyLambdaQueryWrapper<T> not(boolean condition, Consumer<MyLambdaQueryWrapper<T>> consumer) {
        return not(condition).addNestedCondition(condition, consumer);
    }


    private MyLambdaQueryWrapper<T> and(boolean condition) {
        return doIt(condition, AND);
    }

    private MyLambdaQueryWrapper<T> or(boolean condition) {
        return doIt(condition, OR);
    }

    private MyLambdaQueryWrapper<T> not(boolean condition) {
        return doIt(condition, NOT);
    }

    private MyLambdaQueryWrapper<T> doIt(boolean condition, ISqlSegment... sqlSegments) {
        if (condition) {
            expression.add(sqlSegments);
        }
        return this;
    }

    private <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> likeValue(boolean condition, SqlKeyword keyword, String column, Object val, SqlLike sqlLike) {
        return doIt(condition, () -> column, keyword, () -> formatSql("{0}", SqlUtils.concatLike(val, sqlLike)));
    }

    protected <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> addCondition(boolean condition, String column, SqlKeyword sqlKeyword, Object val) {
        return doIt(condition, () -> column, sqlKeyword, () -> formatSql("{0}", val));
    }

    private MyLambdaQueryWrapper<T> addNestedCondition(boolean condition, Consumer<MyLambdaQueryWrapper<T>> consumer) {
        if (condition) {
            final MyLambdaQueryWrapper<T> instance = instance();
            consumer.accept(instance);
            return doIt(true, APPLY, instance);
        }
        return this;
    }

    private ISqlSegment inExpression(Collection<?> value) {
        return () -> value.stream().map(i -> formatSql("{0}", i))
                .collect(joining(StringPool.COMMA, StringPool.LEFT_BRACKET, StringPool.RIGHT_BRACKET));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> isNull(boolean condition, String alias, MySFunction<E, F> column) {
        return doIt(condition, () -> getColumn(column, alias), IS_NULL);
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> isNotNull(boolean condition, String alias, MySFunction<E, F> column) {
        return doIt(condition, () -> getColumn(column, alias), IS_NOT_NULL);
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> in(boolean condition, String alias, MySFunction<E, F> column, Collection<?> coll) {
        return doIt(condition, () -> getColumn(column, alias), IN, inExpression(coll));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> notIn(boolean condition, String alias, MySFunction<E, F> column, Collection<?> coll) {
        return doIt(condition, () -> getColumn(column, alias), NOT_IN, inExpression(coll));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> inSql(boolean condition, String alias, MySFunction<E, F> column, String inValue) {
        return doIt(condition, () -> getColumn(column, alias), IN, () -> String.format("(%s)", inValue));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> notInSql(boolean condition, String alias, MySFunction<E, F> column, String inValue) {
        return doIt(condition, () -> getColumn(column, alias), NOT_IN, () -> String.format("(%s)", inValue));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> groupBy(boolean condition, String alias, MySFunction<E, F>... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return this;
        }
        return doIt(condition, GROUP_BY,
                () -> columns.length == 1 ? getColumn(columns[0], alias) : getColumn(alias, columns));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> orderBy(boolean condition, String alias, boolean isAsc, MySFunction<E, F>... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return this;
        }
        SqlKeyword mode = isAsc ? ASC : DESC;
        for (MySFunction<E, F> column : columns) {
            doIt(condition, ORDER_BY, () -> getColumn(column, alias), mode);
        }
        return this;
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> having(boolean condition, String alias, String sqlHaving, Object... params) {
        return doIt(condition, HAVING, () -> formatSqlIfNeed(condition, sqlHaving, params));
    }

    @Override
    public <E extends MyBaseEntity, F> MyLambdaQueryWrapper<T> func(boolean condition, String alias, Consumer<MyLambdaQueryWrapper<T>> consumer) {
        if (condition) {
            consumer.accept(this);
        }
        return this;
    }
}
