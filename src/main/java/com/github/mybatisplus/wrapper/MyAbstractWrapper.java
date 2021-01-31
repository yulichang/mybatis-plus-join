package com.github.mybatisplus.wrapper;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.interfaces.Join;
import com.baomidou.mybatisplus.core.conditions.interfaces.Nested;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.StringEscape;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.mybatisplus.wrapper.interfaces.MyCompare;
import com.github.mybatisplus.wrapper.interfaces.MyFunc;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.*;
import static com.baomidou.mybatisplus.core.enums.WrapperKeyword.APPLY;
import static java.util.stream.Collectors.joining;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.AbstractWrapper}
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class MyAbstractWrapper<T, Children extends MyAbstractWrapper<T, Children>> extends MyWrapper<T>
        implements MyCompare<Children>, Nested<Children, Children>, Join<Children>, MyFunc<Children> {

    /**
     * 占位符
     */
    protected final Children typedThis = (Children) this;
    /**
     * 必要度量
     */
    protected AtomicInteger paramNameSeq;
    protected Map<String, Object> paramNameValuePairs;
    protected SharedString lastSql;
    /**
     * SQL注释
     */
    protected SharedString sqlComment;
    /**
     * SQL起始语句
     */
    protected SharedString sqlFirst;
    /**
     * ß
     * 数据库表映射实体类
     */
    private T entity;
    protected MergeSegments expression;
    /**
     * 实体类型(主要用于确定泛型以及取TableInfo缓存)
     */
    private Class<T> entityClass;

    @Override
    public T getEntity() {
        return entity;
    }

    public Children setEntity(T entity) {
        this.entity = entity;
        return typedThis;
    }

    public Class<T> getEntityClass() {
        if (entityClass == null && entity != null) {
            entityClass = (Class<T>) entity.getClass();
        }
        return entityClass;
    }

    public Children setEntityClass(Class<T> entityClass) {
        if (entityClass != null) {
            this.entityClass = entityClass;
        }
        return typedThis;
    }

    @Override
    public <X, V> Children allEq(boolean condition, Map<SFunction<X, ?>, V> params, boolean null2IsNull) {
        if (condition && CollectionUtils.isNotEmpty(params)) {
            params.forEach((k, v) -> {
                if (StringUtils.checkValNotNull(v)) {
                    eq(k, v);
                } else {
                    if (null2IsNull) {
                        isNull(k);
                    }
                }
            });
        }
        return typedThis;
    }

    @Override
    public <X, V> Children allEq(boolean condition, BiPredicate<SFunction<X, ?>, V> filter, Map<SFunction<X, ?>, V> params, boolean null2IsNull) {
        if (condition && CollectionUtils.isNotEmpty(params)) {
            params.forEach((k, v) -> {
                if (filter.test(k, v)) {
                    if (StringUtils.checkValNotNull(v)) {
                        eq(k, v);
                    } else {
                        if (null2IsNull) {
                            isNull(k);
                        }
                    }
                }
            });
        }
        return typedThis;
    }

    @Override
    public <X> Children eq(boolean condition, SFunction<X, ?> column, Object val) {
        return addCondition(condition, column, EQ, val);
    }

    @Override
    public <X> Children ne(boolean condition, SFunction<X, ?> column, Object val) {
        return addCondition(condition, column, NE, val);
    }

    @Override
    public <X> Children gt(boolean condition, SFunction<X, ?> column, Object val) {
        return addCondition(condition, column, GT, val);
    }

    @Override
    public <X> Children ge(boolean condition, SFunction<X, ?> column, Object val) {
        return addCondition(condition, column, GE, val);
    }

    @Override
    public <X> Children lt(boolean condition, SFunction<X, ?> column, Object val) {
        return addCondition(condition, column, LT, val);
    }

    @Override
    public <X> Children le(boolean condition, SFunction<X, ?> column, Object val) {
        return addCondition(condition, column, LE, val);
    }

    @Override
    public <X> Children like(boolean condition, SFunction<X, ?> column, Object val) {
        return likeValue(condition, LIKE, column, val, SqlLike.DEFAULT);
    }

    @Override
    public <X> Children notLike(boolean condition, SFunction<X, ?> column, Object val) {
        return likeValue(condition, NOT_LIKE, column, val, SqlLike.DEFAULT);
    }

    @Override
    public <X> Children likeLeft(boolean condition, SFunction<X, ?> column, Object val) {
        return likeValue(condition, LIKE, column, val, SqlLike.LEFT);
    }

    @Override
    public <X> Children likeRight(boolean condition, SFunction<X, ?> column, Object val) {
        return likeValue(condition, LIKE, column, val, SqlLike.RIGHT);
    }

    @Override
    public <X> Children between(boolean condition, SFunction<X, ?> column, Object val1, Object val2) {
        return doIt(condition, () -> columnToString(column), BETWEEN, () -> formatSql("{0}", val1), AND,
                () -> formatSql("{0}", val2));
    }

    @Override
    public <X> Children notBetween(boolean condition, SFunction<X, ?> column, Object val1, Object val2) {
        return doIt(condition, () -> columnToString(column), NOT_BETWEEN, () -> formatSql("{0}", val1), AND,
                () -> formatSql("{0}", val2));
    }

    @Override
    public Children and(boolean condition, Consumer<Children> consumer) {
        return and(condition).addNestedCondition(condition, consumer);
    }

    @Override
    public Children or(boolean condition, Consumer<Children> consumer) {
        return or(condition).addNestedCondition(condition, consumer);
    }

    @Override
    public Children nested(boolean condition, Consumer<Children> consumer) {
        return addNestedCondition(condition, consumer);
    }

    @Override
    public Children not(boolean condition, Consumer<Children> consumer) {
        return not(condition).addNestedCondition(condition, consumer);
    }

    @Override
    public Children or(boolean condition) {
        return doIt(condition, OR);
    }

    @Override
    public Children apply(boolean condition, String applySql, Object... value) {
        return doIt(condition, APPLY, () -> formatSql(applySql, value));
    }

    @Override
    public Children last(boolean condition, String lastSql) {
        if (condition) {
            this.lastSql.setStringValue(StringPool.SPACE + lastSql);
        }
        return typedThis;
    }

    @Override
    public Children comment(boolean condition, String comment) {
        if (condition) {
            this.sqlComment.setStringValue(comment);
        }
        return typedThis;
    }

    @Override
    public Children first(boolean condition, String firstSql) {
        if (condition) {
            this.sqlFirst.setStringValue(firstSql);
        }
        return typedThis;
    }

    @Override
    public Children exists(boolean condition, String existsSql) {
        return doIt(condition, EXISTS, () -> String.format("(%s)", existsSql));
    }

    @Override
    public Children notExists(boolean condition, String existsSql) {
        return not(condition).exists(condition, existsSql);
    }

    @Override
    public <X> Children isNull(boolean condition, SFunction<X, ?> column) {
        return doIt(condition, () -> columnToString(column), IS_NULL);
    }

    @Override
    public <X> Children isNotNull(boolean condition, SFunction<X, ?> column) {
        return doIt(condition, () -> columnToString(column), IS_NOT_NULL);
    }

    @Override
    public <X> Children in(boolean condition, SFunction<X, ?> column, Collection<?> coll) {
        return doIt(condition, () -> columnToString(column), IN, inExpression(coll));
    }

    @Override
    public <X> Children notIn(boolean condition, SFunction<X, ?> column, Collection<?> coll) {
        return doIt(condition, () -> columnToString(column), NOT_IN, inExpression(coll));
    }

    @Override
    public <X> Children inSql(boolean condition, SFunction<X, ?> column, String inValue) {
        return doIt(condition, () -> columnToString(column), IN, () -> String.format("(%s)", inValue));
    }

    @Override
    public <X> Children notInSql(boolean condition, SFunction<X, ?> column, String inValue) {
        return doIt(condition, () -> columnToString(column), NOT_IN, () -> String.format("(%s)", inValue));
    }

    @Override
    public <X> Children groupBy(boolean condition, SFunction<X, ?>... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return typedThis;
        }
        return doIt(condition, GROUP_BY,
                () -> columns.length == 1 ? columnToString(columns[0]) : columnsToString(columns));
    }

    @Override
    public <X> Children orderBy(boolean condition, boolean isAsc, SFunction<X, ?>... columns) {
        if (ArrayUtils.isEmpty(columns)) {
            return typedThis;
        }
        SqlKeyword mode = isAsc ? ASC : DESC;
        for (SFunction<X, ?> column : columns) {
            doIt(condition, ORDER_BY, () -> columnToString(column), mode);
        }
        return typedThis;
    }

    @Override
    public Children having(boolean condition, String sqlHaving, Object... params) {
        return doIt(condition, HAVING, () -> formatSqlIfNeed(condition, sqlHaving, params));
    }

    @Override
    public Children func(boolean condition, Consumer<Children> consumer) {
        if (condition) {
            consumer.accept(typedThis);
        }
        return typedThis;
    }

    /**
     * 内部自用
     * <p>NOT 关键词</p>
     */
    protected Children not(boolean condition) {
        return doIt(condition, NOT);
    }

    /**
     * 内部自用
     * <p>拼接 AND</p>
     */
    protected Children and(boolean condition) {
        return doIt(condition, AND);
    }

    /**
     * 内部自用
     * <p>拼接 LIKE 以及 值</p>
     */
    protected <X> Children likeValue(boolean condition, SqlKeyword keyword, SFunction<X, ?> column, Object val, SqlLike sqlLike) {
        return doIt(condition, () -> columnToString(column), keyword, () -> formatSql("{0}", SqlUtils.concatLike(val, sqlLike)));
    }

    /**
     * 普通查询条件
     *
     * @param condition  是否执行
     * @param column     属性
     * @param sqlKeyword SQL 关键词
     * @param val        条件值
     */
    protected <X> Children addCondition(boolean condition, SFunction<X, ?> column, SqlKeyword sqlKeyword, Object val) {
        return doIt(condition, () -> columnToString(column), sqlKeyword, () -> formatSql("{0}", val));
    }

    /**
     * 多重嵌套查询条件
     *
     * @param condition 查询条件值
     */
    protected Children addNestedCondition(boolean condition, Consumer<Children> consumer) {
        if (condition) {
            final Children instance = instance();
            consumer.accept(instance);
            return doIt(true, APPLY, instance);
        }
        return typedThis;
    }

    /**
     * 子类返回一个自己的新对象
     */
    protected abstract Children instance();

    /**
     * 格式化SQL
     *
     * @param sqlStr SQL语句部分
     * @param params 参数集
     * @return sql
     */
    protected final String formatSql(String sqlStr, Object... params) {
        return formatSqlIfNeed(true, sqlStr, params);
    }

    /**
     * <p>
     * 根据需要格式化SQL<br>
     * <br>
     * Format SQL for methods: EntityQ<T>.where/and/or...("name={0}", value);
     * ALL the {<b>i</b>} will be replaced with #{MPGENVAL<b>i</b>}<br>
     * <br>
     * ew.where("sample_name=<b>{0}</b>", "haha").and("sample_age &gt;<b>{0}</b>
     * and sample_age&lt;<b>{1}</b>", 18, 30) <b>TO</b>
     * sample_name=<b>#{MPGENVAL1}</b> and sample_age&gt;#<b>{MPGENVAL2}</b> and
     * sample_age&lt;<b>#{MPGENVAL3}</b><br>
     * </p>
     *
     * @param need   是否需要格式化
     * @param sqlStr SQL语句部分
     * @param params 参数集
     * @return sql
     */
    protected final String formatSqlIfNeed(boolean need, String sqlStr, Object... params) {
        if (!need || StringUtils.isBlank(sqlStr)) {
            return null;
        }
        if (ArrayUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.length; ++i) {
                String genParamName = Constants.WRAPPER_PARAM + paramNameSeq.incrementAndGet();
                sqlStr = sqlStr.replace(String.format("{%s}", i),
                        String.format(Constants.WRAPPER_PARAM_FORMAT, Constants.WRAPPER, genParamName));
                paramNameValuePairs.put(genParamName, params[i]);
            }
        }
        return sqlStr;
    }

    /**
     * 获取in表达式 包含括号
     *
     * @param value 集合
     */
    private ISqlSegment inExpression(Collection<?> value) {
        return () -> value.stream().map(i -> formatSql("{0}", i))
                .collect(joining(StringPool.COMMA, StringPool.LEFT_BRACKET, StringPool.RIGHT_BRACKET));
    }

    /**
     * 必要的初始化
     */
    protected void initNeed() {
        paramNameSeq = new AtomicInteger(0);
        paramNameValuePairs = new HashMap<>(16);
        expression = new MergeSegments();
        lastSql = SharedString.emptyString();
        sqlComment = SharedString.emptyString();
        sqlFirst = SharedString.emptyString();
    }

    @Override
    public void clear() {
        entity = null;
        paramNameSeq.set(0);
        paramNameValuePairs.clear();
        expression.clear();
        lastSql.toEmpty();
        sqlComment.toEmpty();
        sqlFirst.toEmpty();
    }

    /**
     * 对sql片段进行组装
     *
     * @param condition   是否执行
     * @param sqlSegments sql片段数组
     * @return children
     */
    protected Children doIt(boolean condition, ISqlSegment... sqlSegments) {
        if (condition) {
            expression.add(sqlSegments);
        }
        return typedThis;
    }

    @Override
    public String getSqlSegment() {
        return expression.getSqlSegment() + lastSql.getStringValue();
    }

    @Override
    public String getSqlComment() {
        if (StringUtils.isNotBlank(sqlComment.getStringValue())) {
            return "/*" + StringEscape.escapeRawString(sqlComment.getStringValue()) + "*/";
        }
        return null;
    }

    @Override
    public String getSqlFirst() {
        if (StringUtils.isNotBlank(sqlFirst.getStringValue())) {
            return StringEscape.escapeRawString(sqlFirst.getStringValue());
        }
        return null;
    }

    @Override
    public MergeSegments getExpression() {
        return expression;
    }

    public Map<String, Object> getParamNameValuePairs() {
        return paramNameValuePairs;
    }

    /**
     * 获取 columnName
     */
    protected <X> String columnToString(X column) {
        return (String) column;
    }

    /**
     * 多字段转换为逗号 "," 分割字符串
     *
     * @param columns 多字段
     */
    protected <X> String columnsToString(X... columns) {
        return Arrays.stream(columns).map(this::columnToString).collect(joining(StringPool.COMMA));
    }

    @Override
    @SuppressWarnings("all")
    public Children clone() {
        return SerializationUtils.clone(typedThis);
    }
}
