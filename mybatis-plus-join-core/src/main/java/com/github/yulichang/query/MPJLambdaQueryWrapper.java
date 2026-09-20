package com.github.yulichang.query;

import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.query.interfaces.CompareIfExists;
import com.github.yulichang.query.interfaces.StringJoin;
import com.github.yulichang.toolkit.StrUtils;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.ThrowOptional;
import com.github.yulichang.wrapper.enums.IfExistsSqlKeyWordEnum;
import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * MPJLambdaQueryWrapper
 * 参考 -> {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
 *
 * @author yulichang
 * @see com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper
 */
@SuppressWarnings("unused")
public class MPJLambdaQueryWrapper<T> extends AbstractLambdaWrapper<T, MPJLambdaQueryWrapper<T>>
        implements Query<MPJLambdaQueryWrapper<T>, T, SFunction<T, ?>>, StringJoin<MPJLambdaQueryWrapper<T>, T>,
        CompareIfExists<MPJLambdaQueryWrapper<T>, SFunction<T, ?>> {

    /**
     * 查询字段
     */
    private SharedString sqlSelect = new SharedString();

    /**
     * 连表字段
     */
    private SharedString from = SharedString.emptyString();

    /**
     * 主表别名
     */
    @Getter
    private String alias = ConfigProperties.tableAlias;

    /**
     * 查询的列
     */
    private List<String> selectColumns = new ArrayList<>();

    /**
     * 排除的字段
     */
    private List<String> ignoreColumns = new ArrayList<>();

    /**
     * 是否 select distinct
     */
    private boolean selectDistinct = false;
    /**
     * 主表逻辑删除
     */
    private boolean logicSql = true;
    /**
     * 动态表名
     */
    private Function<String, String> tableNameFunc;

    @Getter
    private BiPredicate<Object, IfExistsSqlKeyWordEnum> ifExists = ConfigProperties.ifExists;

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
     */
    public MPJLambdaQueryWrapper() {
        super.initNeed();
    }

    /**
     * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(...)
     */
    MPJLambdaQueryWrapper(T entity, Class<T> entityClass, SharedString from, SharedString sqlSelect, AtomicInteger paramNameSeq,
                          Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                          SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                          List<String> selectColumns, List<String> ignoreColumns, boolean selectDistinct,
                          BiPredicate<Object, IfExistsSqlKeyWordEnum> IfExists) {
        super.setEntity(entity);
        setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.from = from;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        ThrowOptional.tryDo(() -> this.sqlFirst = sqlFirst).catchDo();
        this.selectColumns = selectColumns;
        this.ignoreColumns = ignoreColumns;
        this.selectDistinct = selectDistinct;
        this.ifExists = IfExists;
    }

    /**
     * SELECT 部分 SQL 设置
     *
     * @param columns 查询字段
     */
    @SafeVarargs
    public final MPJLambdaQueryWrapper<T> select(SFunction<T, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<T, ?> s : columns) {
                selectColumns.add(columnToString(s, false));
            }
        }
        return typedThis;
    }

    @Override
    public MPJLambdaQueryWrapper<T> select(boolean condition, List<SFunction<T, ?>> columns) {
        if (condition && CollectionUtils.isNotEmpty(columns)) {
            for (SFunction<T, ?> s : columns) {
                selectColumns.add(columnToString(s, false));
            }
        }
        return typedThis;
    }

    /**
     * 忽略查询字段
     * <p>
     * 用法: selectIgnore("t.id","t.sex","a.area")
     *
     * @since 1.1.3
     */
    public MPJLambdaQueryWrapper<T> selectIgnore(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            ignoreColumns.addAll(Arrays.asList(columns));
        }
        return typedThis;
    }

    /**
     * 忽略查询字段
     * <p>
     * 用法: selectIgnore("t.id","t.sex","a.area")
     *
     * @since 1.1.3
     */
    @SafeVarargs
    public final MPJLambdaQueryWrapper<T> selectIgnore(SFunction<T, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<T, ?> s : columns) {
                ignoreColumns.add(columnToString(s));
            }
        }
        return typedThis;
    }

    @Override
    protected String columnToString(SFunction<T, ?> column, boolean onlyColumn) {
        return alias + StringPool.DOT + super.columnToString(column, onlyColumn);
    }

    public MPJLambdaQueryWrapper<T> select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            Collections.addAll(selectColumns, columns);
        }
        return typedThis;
    }

    /**
     * 只针对主表
     * <p>
     * 过滤查询的字段信息(主键除外!)
     * <p>例1: 只要 java 字段名以 "test" 开头的             -> select(i -&gt; i.getProperty().startsWith("test"))</p>
     * <p>例2: 只要 java 字段属性是 CharSequence 类型的     -> select(TableFieldInfo::isCharSequence)</p>
     * <p>例3: 只要 java 字段没有填充策略的                 -> select(i -&gt; i.getFieldFill() == FieldFill.DEFAULT)</p>
     * <p>例4: 要全部字段                                   -> select(i -&gt; true)</p>
     * <p>例5: 只要主键字段                                 -> select(i -&gt; false)</p>
     *
     * @param predicate 过滤方式
     * @return this
     */
    @Override
    public MPJLambdaQueryWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        TableInfo info = TableHelper.getAssert(entityClass);
        selectColumns.addAll(info.getFieldList().stream().filter(predicate).map(c ->
                alias + StringPool.DOT + c.getColumn()).collect(Collectors.toList()));
        return typedThis;
    }


    /**
     * 查询主表全部字段
     *
     * @param clazz 主表class
     */
    public final MPJLambdaQueryWrapper<T> selectAll(Class<T> clazz) {
        return selectAll(clazz, alias);
    }

    /**
     * 查询表全部字段
     *
     * @param clazz 表实体
     * @param as    表别名
     */
    @SuppressWarnings("DuplicatedCode")
    public final MPJLambdaQueryWrapper<T> selectAll(Class<?> clazz, String as) {
        TableInfo info = TableHelper.getAssert(clazz);
        if (info.havePK()) {
            selectColumns.add(as + StringPool.DOT + info.getKeyColumn());
        }
        selectColumns.addAll(info.getFieldList().stream().map(i ->
                as + StringPool.DOT + i.getColumn()).collect(Collectors.toList()));
        return typedThis;
    }

    /**
     * 返回一个支持 lambda 函数写法的 wrapper
     */
    public MPJQueryWrapper<T> stringQuery() {
        return new MPJQueryWrapper<>(getEntity(), getEntityClass(), paramNameSeq, paramNameValuePairs,
                expression, sqlSelect, from, lastSql, sqlComment, sqlFirst, selectColumns, ignoreColumns,
                selectDistinct, ifExists);
    }

    @Override
    public String getSqlSelect() {
        if (StrUtils.isBlank(sqlSelect.getStringValue())) {
            if (CollectionUtils.isNotEmpty(ignoreColumns)) {
                selectColumns.removeIf(ignoreColumns::contains);
            }
            sqlSelect.setStringValue(String.join(StringPool.COMMA, selectColumns));
        }
        return sqlSelect.getStringValue();
    }

    /**
     * sql去重
     * select distinct
     */
    public MPJLambdaQueryWrapper<T> distinct() {
        this.selectDistinct = true;
        return typedThis;
    }

    public String getFrom() {
        return from.getStringValue();
    }

    public MPJLambdaQueryWrapper<T> setAlias(String alias) {
        Assert.isTrue(StrUtils.isNotBlank(alias), "别名不能为空");
        this.alias = alias;
        return typedThis;
    }

    /**
     * 逻辑删除
     */
    public String getSubLogicSql() {
        return StringPool.EMPTY;
    }

    /**
     * 关闭主表逻辑删除
     */
    public MPJLambdaQueryWrapper<T> disableLogicDel() {
        this.logicSql = false;
        return typedThis;
    }

    /**
     * 启用主表逻辑删除
     */
    public MPJLambdaQueryWrapper<T> enableLogicDel() {
        this.logicSql = true;
        return typedThis;
    }

    /**
     * 逻辑删除
     */
    public boolean getLogicSql() {
        return logicSql;
    }


    public boolean getSelectDistinct() {
        return selectDistinct;
    }

    /**
     * 动态表名
     * <p>
     * 如果主表需要动态表名
     * <p>
     */
    public MPJLambdaQueryWrapper<T> setTableName(Function<String, String> func) {
        this.tableNameFunc = func;
        return typedThis;
    }

    public String getTableName(String tableName) {
        if (this.tableNameFunc == null) {
            return tableName;
        }
        return this.tableNameFunc.apply(tableName);
    }

    @SuppressWarnings("DuplicatedCode")
    public String getTableNameEnc(String tableName) {
        String decode;
        try {
            decode = URLDecoder.decode(tableName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            decode = tableName;
        }
        if (this.tableNameFunc == null) {
            return decode;
        }
        return this.tableNameFunc.apply(decode);
    }

    public MPJLambdaQueryWrapper<T> setIfExists(BiPredicate<Object, IfExistsSqlKeyWordEnum> IfExists) {
        this.ifExists = IfExists;
        return typedThis;
    }

    public MPJLambdaQueryWrapper<T> setIfExists(Predicate<Object> IfExists) {
        this.ifExists = (o, k) -> IfExists.test(o);
        return typedThis;
    }

    /**
     * 用于生成嵌套 sql
     * <p>故 sqlSelect selectColumn ignoreColumns from不向下传递</p>
     */
    @Override
    protected MPJLambdaQueryWrapper<T> instance() {
        return new MPJLambdaQueryWrapper<>(getEntity(), getEntityClass(), null, null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                null, null, selectDistinct, ifExists);
    }

    @Override
    public Class<T> getEntityClass() {
        try {
            return super.getEntityClass();
        } catch (Throwable throwable) {
            return null;
        }
    }

    @Override
    public MPJLambdaQueryWrapper<T> setEntityClass(Class<T> entityClass) {
        try {
            return super.setEntityClass(entityClass);
        } catch (Throwable throwable) {
            return this;
        }
    }

    public SharedString getSqlFirstField() {
        try {
            return sqlSelect;
        } catch (Throwable throwable) {
            return SharedString.emptyString();
        }
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
        from.toNull();
        selectColumns.clear();
        ignoreColumns.clear();
        ifExists = ConfigProperties.ifExists;
    }

    @Override
    public MPJLambdaQueryWrapper<T> join(String keyWord, boolean condition, String joinSql, Object... args) {
        if (condition) {
            from.setStringValue(from.getStringValue() + keyWord + mpjFormatSqlMaybeWithParam(joinSql, args));
        }
        return typedThis;
    }

    @SuppressWarnings("DuplicatedCode")
    protected final String mpjFormatSqlMaybeWithParam(String sqlStr, Object... params) {
        if (StrUtils.isBlank(sqlStr)) {
            return null;
        }
        if (ArrayUtils.isNotEmpty(params)) {
            for (int i = 0; i < params.length; ++i) {
                String target = Constants.LEFT_BRACE + i + Constants.RIGHT_BRACE;
                if (sqlStr.contains(target)) {
                    sqlStr = sqlStr.replace(target, formatParam(null, params[i]));
                } else {
                    Matcher matcher = Pattern.compile("[{]" + i + ",[a-zA-Z0-9.,=]+}").matcher(sqlStr);
                    if (!matcher.find()) {
                        throw ExceptionUtils.mpe("Please check the syntax correctness! sql not contains: \"%s\"", target);
                    }
                    String group = matcher.group();
                    sqlStr = sqlStr.replace(group, formatParam(group.substring(target.length(), group.length() - 1), params[i]));
                }
            }
        }
        return sqlStr;
    }
}
