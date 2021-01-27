package com.example.mp.mybatis.plus.wrapper;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.sql.StringEscape;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.joining;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.AbstractWrapper}
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class MyAbstractWrapper<T, R, Children extends MyAbstractWrapper<T, R, Children>> extends Wrapper<T> {


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
    protected String columnToString(R column) {
        return (String) column;
    }

    /**
     * 多字段转换为逗号 "," 分割字符串
     *
     * @param columns 多字段
     */
    protected String columnsToString(R... columns) {
        return Arrays.stream(columns).map(this::columnToString).collect(joining(StringPool.COMMA));
    }

    @Override
    @SuppressWarnings("all")
    public Children clone() {
        return SerializationUtils.clone(typedThis);
    }
}
