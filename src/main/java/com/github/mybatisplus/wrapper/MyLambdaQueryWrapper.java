package com.github.mybatisplus.wrapper;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.mybatisplus.func.MySFunction;
import com.github.mybatisplus.toolkit.Constant;
import com.github.mybatisplus.toolkit.MyLambdaUtils;
import com.github.mybatisplus.wrapper.interfaces.*;

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
 * 只允许主表有条件查询,内部的表只允许select和继续连表的功能,
 * 所以将条件查询接口在此实现 结构臃肿,有待进一步优化
 *
 * @author yulichang
 * @see com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 * @see com.baomidou.mybatisplus.core.conditions.interfaces.Compare
 * @see com.baomidou.mybatisplus.core.conditions.interfaces.Join
 * @see com.baomidou.mybatisplus.core.conditions.interfaces.Nested
 * @see com.baomidou.mybatisplus.core.conditions.interfaces.Func
 * @since 2021/01/19
 */
@SuppressWarnings("all")
public class MyLambdaQueryWrapper<T> extends MyAbstractLambdaWrapper<T, MyLambdaQueryWrapper<T>>
        implements
        Query<MyLambdaQueryWrapper<T>, T, SFunction<T, ?>>,
        MyJoin<MyLambdaQueryWrapper<T>, T>,
        MyCompare<MyLambdaQueryWrapper<T>>,
        MyNested<MyLambdaQueryWrapper<T>, MyLambdaQueryWrapper<T>>,
        MyFunc<MyLambdaQueryWrapper<T>>,
        MyMPJoin<MyLambdaQueryWrapper<T>> {


    private MyJoinLambdaQueryWrapper<T> wrapper = new MyJoinLambdaQueryWrapper<>();


    /**
     * 表别名初始序号
     */
    public static final int TABLE_ALIAS_INDEX = 0;


    public MyLambdaQueryWrapper() {
        this(null);
    }

    public MyLambdaQueryWrapper(T entity) {
        wrapper.rUid = TABLE_ALIAS_INDEX;
        wrapper.setEntity(entity);
        super.initNeed();
    }

    MyLambdaQueryWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                         Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                         SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                         int rUid, List<MyJoinLambdaQueryWrapper.SelectColumn> selectColumns, List<MyJoinLambdaQueryWrapper.SubTable> classList) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.wrapper.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;

        wrapper.rUid = rUid;
        wrapper.selectColumnList = selectColumns;
        wrapper.classList = classList;
    }

    @Override
    protected MyLambdaQueryWrapper<T> instance() {
        return new MyLambdaQueryWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                wrapper.rUid, wrapper.selectColumnList, wrapper.classList);
    }


    @Override
    public <R, TE, RE> MyLambdaQueryWrapper<T> join(boolean condition,
                                                    String alias,
                                                    String keyWord,
                                                    MySFunction<T, TE> leftCondition,
                                                    MySFunction<R, RE> rightCondition,
                                                    Function<MyJoinLambdaQueryWrapper<R>, MyJoinLambdaQueryWrapper<R>> rightWrapper) {
        if (condition) {
            setEntityClass(MyLambdaUtils.getEntityClass(leftCondition));
            Class<R> clazz = MyLambdaUtils.getEntityClass(rightCondition);
            TableInfo info = TableInfoHelper.getTableInfo(clazz);
            Assert.notNull(info, "can not find table to entity %s", clazz);
            wrapper.classList.add(new MyJoinLambdaQueryWrapper.SubTable(alias,
                    keyWord,
                    wrapper.rUid,
                    MyLambdaUtils.getColumn(leftCondition),
                    wrapper.classList.size() + 1,
                    MyLambdaUtils.getColumn(rightCondition),
                    info.getTableName()));
            MyJoinLambdaQueryWrapper<R> apply = rightWrapper.apply(new MyJoinLambdaQueryWrapper<>(wrapper.classList.size()));
            wrapper.classList.addAll(apply.classList);
            wrapper.selectColumnList.addAll(apply.selectColumnList);
        }
        return this;
    }

    @SafeVarargs
    @Override
    public final MyLambdaQueryWrapper<T> select(SFunction<T, ?>... columns) {
        wrapper.select(columns);
        return this;
    }

    @Override
    public MyLambdaQueryWrapper<T> select(Predicate<TableFieldInfo> predicate) {
        wrapper.select(predicate);
        return this;
    }

    @Override
    public MyLambdaQueryWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        wrapper.select(entityClass, predicate);
        return this;
    }


    public MyLambdaQueryWrapper<T> selectAll(Class<T> clazz) {
        wrapper.selectAll(clazz);
        return this;
    }

    public MyLambdaQueryWrapper<T> selectDistinct() {
        wrapper.selectDistinct();
        return this;
    }

    @Override
    public String getSqlSelect() {
        if (StringUtils.isBlank(wrapper.sqlSelect.getStringValue()) && CollectionUtils.isNotEmpty(wrapper.selectColumnList)) {
            List<String> collect = new ArrayList<>();
            wrapper.selectColumnList.forEach(c -> {
                String s = Constant.TABLE_ALIAS + (c.getUid() == TABLE_ALIAS_INDEX ? "" : c.getUid()) + StringPool.DOT + c.getColumn();
                if (c.getFunction() != null) {
                    s = String.format(c.getFunction().getSql(), s);
                }
                if (Objects.isNull(c.getAs())) {
                    collect.add(s);
                } else {
                    collect.add(s + Constant.AS + c.getAs());
                }
            });
            wrapper.sqlSelect.setStringValue(String.join(StringPool.COMMA, collect));
        }
        return wrapper.sqlSelect.getStringValue();
    }

    @Override
    public void clear() {
        super.clear();
    }

    public String getFrom() {
        if (StringUtils.isNotBlank(wrapper.from.getStringValue())) {
            return wrapper.from.getStringValue();
        }
        StringBuilder sb = new StringBuilder();
        wrapper.classList.forEach(right ->
                sb.append(right.getKeyWord()).append(right.getRightTableName())
                        .append(StringPool.SPACE)
                        .append(Constant.TABLE_ALIAS)
                        .append(right.getRightUid() == TABLE_ALIAS_INDEX ? StringPool.EMPTY : right.getRightUid())
                        .append(Constant.ON)
                        .append(Constant.TABLE_ALIAS)
                        .append(right.getLeftUid() == TABLE_ALIAS_INDEX ? StringPool.EMPTY : right.getLeftUid())
                        .append(StringPool.DOT)
                        .append(right.getLeftColumn())
                        .append(Constant.EQUALS)
                        .append(Constant.TABLE_ALIAS)
                        .append(right.getRightUid() == TABLE_ALIAS_INDEX ? StringPool.EMPTY : right.getRightUid())
                        .append(StringPool.DOT)
                        .append(right.getRightColumn())
                        .append(StringPool.SPACE)
        );
        wrapper.from.setStringValue(sb.toString());
        return wrapper.from.getStringValue();
    }

    /**
     * 获取表前缀
     */
    private String getClassTablePrefix(Class<?> clazz) {
        if (getEntityClass() == clazz) {
            return Constant.TABLE_ALIAS;
        } else {
            TableInfo info = TableInfoHelper.getTableInfo(clazz);
            Assert.notNull(info, "can not find table to entity %s", clazz);
            String tableName = info.getTableName();
            for (MyJoinLambdaQueryWrapper.SubTable sub : wrapper.classList) {
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
    private <E, F> String getColumn(MySFunction<E, F> column, String alias) {
        if (alias != null) {
            if (alias.equals(Constant.TABLE_ALIAS)) {
                return Constant.TABLE_ALIAS + StringPool.DOT + column2String(column, true);
            }
            for (MyJoinLambdaQueryWrapper.SubTable sub : wrapper.classList) {
                if (alias.equals(sub.getAlias())) {
                    return Constant.TABLE_ALIAS + sub.getRightUid() + StringPool.DOT + column2String(column, true);
                }
            }
        }
        return getClassTablePrefix(MyLambdaUtils.getEntityClass(column)) + StringPool.DOT + column2String(column, true);
    }

    private <E, F> String getColumn(String alias, MySFunction<E, F>... column) {
        return Arrays.stream(column).map(i -> getColumn(i, alias)).collect(joining(StringPool.COMMA));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> eq(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), EQ,
                () -> formatSql("{0}", val));
    }

    @Override
    public <E, F, X, Y> MyLambdaQueryWrapper<T> eq(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), EQ, () -> getColumn(val, as));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> ne(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), NE, () -> formatSql("{0}", val));
    }

    @Override
    public <E, F, X, Y> MyLambdaQueryWrapper<T> ne(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), NE, () -> getColumn(val, as));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> gt(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), GT, () -> formatSql("{0}", val));
    }

    @Override
    public <E, F, X, Y> MyLambdaQueryWrapper<T> gt(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), GT, () -> getColumn(val, as));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> ge(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), GE, () -> formatSql("{0}", val));
    }

    @Override
    public <E, F, X, Y> MyLambdaQueryWrapper<T> ge(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), GE, () -> getColumn(val, as));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> lt(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), LT, () -> formatSql("{0}", val));
    }

    @Override
    public <E, F, X, Y> MyLambdaQueryWrapper<T> lt(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), LT, () -> getColumn(val, as));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> le(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return doIt(condition, () -> getColumn(column, alias), LE, () -> formatSql("{0}", val));
    }

    @Override
    public <E, F, X, Y> MyLambdaQueryWrapper<T> le(boolean condition, String alias, MySFunction<E, F> column, String as, MySFunction<X, Y> val) {
        return doIt(condition, () -> getColumn(column, alias), LE, () -> getColumn(val, as));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> between(boolean condition, String alias, MySFunction<E, F> column, Object val1, Object val2) {
        return doIt(condition, () -> getColumn(column, alias), BETWEEN, () -> formatSql("{0}", val1), AND,
                () -> formatSql("{0}", val2));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> notBetween(boolean condition, String alias, MySFunction<E, F> column, Object val1, Object val2) {
        return doIt(condition, () -> getColumn(column, alias), NOT_BETWEEN, () -> formatSql("{0}", val1), AND,
                () -> formatSql("{0}", val2));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> like(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return likeValue(condition, LIKE, getColumn(column, alias), val, SqlLike.DEFAULT);
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> notLike(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return likeValue(condition, NOT_LIKE, getColumn(column, alias), val, SqlLike.DEFAULT);
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> likeLeft(boolean condition, String alias, MySFunction<E, F> column, Object val) {
        return likeValue(condition, LIKE, getColumn(column, alias), val, SqlLike.LEFT);
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> likeRight(boolean condition, String alias, MySFunction<E, F> column, Object val) {
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

    @Override
    public MyLambdaQueryWrapper<T> or(boolean condition) {
        return doIt(condition, OR);
    }

    @Override
    public MyLambdaQueryWrapper<T> apply(boolean condition, String applySql, Object... value) {
        return doIt(condition, APPLY, () -> formatSql(applySql, value));
    }

    @Override
    public MyLambdaQueryWrapper<T> last(boolean condition, String lastSql) {
        if (condition) {
            this.lastSql.setStringValue(StringPool.SPACE + lastSql);
        }
        return typedThis;
    }

    @Override
    public MyLambdaQueryWrapper<T> comment(boolean condition, String comment) {
        if (condition) {
            this.sqlComment.setStringValue(comment);
        }
        return typedThis;
    }

    @Override
    public MyLambdaQueryWrapper<T> first(boolean condition, String firstSql) {
        if (condition) {
            this.sqlFirst.setStringValue(firstSql);
        }
        return typedThis;
    }

    @Override
    public MyLambdaQueryWrapper<T> exists(boolean condition, String existsSql) {
        return doIt(condition, EXISTS, () -> String.format("(%s)", existsSql));
    }

    @Override
    public MyLambdaQueryWrapper<T> notExists(boolean condition, String existsSql) {
        return not(condition).exists(condition, existsSql);
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

    private MyLambdaQueryWrapper<T> likeValue(boolean condition, SqlKeyword keyword, String column, Object val, SqlLike sqlLike) {
        return doIt(condition, () -> column, keyword, () -> formatSql("{0}", SqlUtils.concatLike(val, sqlLike)));
    }

    protected MyLambdaQueryWrapper<T> addCondition(boolean condition, String column, SqlKeyword sqlKeyword, Object val) {
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
    public <E, F> MyLambdaQueryWrapper<T> isNull(boolean condition, String alias, MySFunction<E, F> column) {
        return doIt(condition, () -> getColumn(column, alias), IS_NULL);
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> isNotNull(boolean condition, String alias, MySFunction<E, F> column) {
        return doIt(condition, () -> getColumn(column, alias), IS_NOT_NULL);
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> in(boolean condition, String alias, MySFunction<E, F> column, Collection<?> coll) {
        return doIt(condition, () -> getColumn(column, alias), IN, inExpression(coll));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> notIn(boolean condition, String alias, MySFunction<E, F> column, Collection<?> coll) {
        return doIt(condition, () -> getColumn(column, alias), NOT_IN, inExpression(coll));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> inSql(boolean condition, String alias, MySFunction<E, F> column, String inValue) {
        return doIt(condition, () -> getColumn(column, alias), IN, () -> String.format("(%s)", inValue));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> notInSql(boolean condition, String alias, MySFunction<E, F> column, String inValue) {
        return doIt(condition, () -> getColumn(column, alias), NOT_IN, () -> String.format("(%s)", inValue));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> groupBy(boolean condition, String alias, MySFunction<E, F>... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return this;
        }
        return doIt(condition, GROUP_BY,
                () -> columns.length == 1 ? getColumn(columns[0], alias) : getColumn(alias, columns));
    }

    @Override
    public <E, F> MyLambdaQueryWrapper<T> orderBy(boolean condition, String alias, boolean isAsc, MySFunction<E, F>... columns) {
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
    public MyLambdaQueryWrapper<T> having(boolean condition, String alias, String sqlHaving, Object... params) {
        return doIt(condition, HAVING, () -> formatSqlIfNeed(condition, sqlHaving, params));
    }

    @Override
    public MyLambdaQueryWrapper<T> func(boolean condition, String alias, Consumer<MyLambdaQueryWrapper<T>> consumer) {
        if (condition) {
            consumer.accept(this);
        }
        return this;
    }
}
