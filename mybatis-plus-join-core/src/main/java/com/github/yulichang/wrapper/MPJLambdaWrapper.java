package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.*;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.enums.IfExistsSqlKeyWordEnum;
import com.github.yulichang.wrapper.interfaces.*;
import com.github.yulichang.wrapper.resultmap.Label;
import com.github.yulichang.wrapper.segments.*;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
 * Lambda 语法使用 Wrapper
 *
 * @author yulichang
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public class MPJLambdaWrapper<T> extends JoinAbstractLambdaWrapper<T, MPJLambdaWrapper<T>> implements Fill<MPJLambdaWrapper<T>>,
        Query<MPJLambdaWrapper<T>>, QueryLabel<MPJLambdaWrapper<T>>, Chain<T>, SelectWrapper<T, MPJLambdaWrapper<T>> {

    /**
     * 查询字段 sql
     */
    private SharedString sqlSelect = new SharedString();
    /**
     * 是否 select distinct
     */
    private boolean selectDistinct = false;
    /**
     * 查询的字段
     */
    @Getter
    private final List<Select> selectColumns = new ArrayList<>();
    /**
     * 映射关系
     */
    @Getter
    private final List<Label<?>> resultMapMybatisLabel = new ArrayList<>();

    /**
     * union sql
     */
    private SharedString unionSql;

    /**
     * 自定义wrapper索引
     */
    private AtomicInteger wrapperIndex;

    /**
     * 自定义wrapper
     */
    @Getter
    private Map<String, Wrapper<?>> wrapperMap;

    private List<MConsumer<Object>> fill;

    /**
     * 推荐使用 带 class 的构造方法
     */
    public MPJLambdaWrapper() {
        super();
    }

    /**
     * 推荐使用此构造方法
     */
    public MPJLambdaWrapper(Class<T> clazz) {
        super(clazz);
    }

    /**
     * 构造方法
     *
     * @param entity 主表实体
     */
    public MPJLambdaWrapper(T entity) {
        super(entity);
    }

    /**
     * 自定义主表别名
     */
    public MPJLambdaWrapper(String alias) {
        super(alias);
    }

    /**
     * 构造方法
     *
     * @param clazz 主表class类
     * @param alias 主表别名
     */
    public MPJLambdaWrapper(Class<T> clazz, String alias) {
        super(clazz, alias);
    }

    /**
     * 构造方法
     *
     * @param entity 主表实体类
     * @param alias  主表别名
     */
    public MPJLambdaWrapper(T entity, String alias) {
        super(entity, alias);
    }

    /**
     * 不建议直接 new 该实例，使用 JoinWrappers.lambda(UserDO.class)
     */
    protected MPJLambdaWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                               Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString paramAlias,
                               SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                               TableList tableList, Integer index, String keyWord, Class<?> joinClass, String tableName,
                               MBiPredicate<Object, IfExistsSqlKeyWordEnum> IfExists) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.paramAlias = paramAlias;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.tableList = tableList;
        this.index = index;
        this.keyWord = keyWord;
        this.joinClass = joinClass;
        this.tableName = tableName;
        this.ifExists = IfExists;
    }


    /**
     * sql去重
     * select distinct
     */
    public MPJLambdaWrapper<T> distinct() {
        this.selectDistinct = true;
        return typedThis;
    }


    @Override
    public List<Select> getSelectColum() {
        return this.selectColumns;
    }

    @Override
    public void addLabel(Label<?> label, boolean isCollection) {
        if (isCollection) {
            this.resultMapCollection = true;
        }
        this.resultMap = true;
        this.resultMapMybatisLabel.add(label);
    }

    @Override
    public MPJLambdaWrapper<T> getChildren() {
        return typedThis;
    }


    /**
     * 设置查询字段
     *
     * @param columns 字段数组
     * @return children
     */
    @SafeVarargs
    public final <E> MPJLambdaWrapper<T> select(SFunction<E, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            Class<?> aClass = LambdaUtils.getEntityClass(columns[0]);
            Map<String, SelectCache> cacheMap = ColumnCache.getMapField(aClass);
            for (SFunction<E, ?> s : columns) {
                SelectCache cache = cacheMap.get(LambdaUtils.getName(s));
                getSelectColum().add(new SelectNormal(cache, index, hasAlias, alias));
            }
        }
        return typedThis;
    }

    @Override
    public MPJLambdaWrapper<T> selectAll(Class<?> clazz) {
        return Query.super.selectAll(clazz);
    }

    /**
     * 查询实体类全部字段
     *
     * @param clazz   查询的实体类
     * @param exclude 排除字段
     */
    @Override
    @SafeVarargs
    public final <E> MPJLambdaWrapper<T> selectAll(Class<E> clazz, SFunction<E, ?>... exclude) {
        return Query.super.selectAll(clazz, exclude);
    }

    /**
     * 查询实体类全部字段
     *
     * @param clazz   查询的实体类
     * @param exclude 排除字段
     */
    @Override
    @SafeVarargs
    public final <E> MPJLambdaWrapper<T> selectAll(Class<E> clazz, String prefix, SFunction<E, ?>... exclude) {
        return Query.super.selectAll(clazz, prefix, exclude);
    }

    /**
     * 查询主表全部字段
     * <p>
     * 需要使用 使用 JoinWrappers.lambda(clazz) 或者 new MPJLambdaQueryWrapper&lt;&lt;(clazz) 构造
     *
     * @return children
     */
    public MPJLambdaWrapper<T> selectAll() {
        Assert.notNull(getEntityClass(), "使用 JoinWrappers.lambda(clazz) 或者 new MPJLambdaQueryWrapper<>(clazz)");
        return selectAll(getEntityClass());
    }

    @Override
    public MPJLambdaWrapper<T> selectFunc(String sql, MFunction<FuncConsumer> column, String alias) {
        FuncConsumer funcConsumer = column.apply(new FuncConsumer());
        String formatSql;
        if (ArrayUtils.isEmpty(funcConsumer.getValues())) {
            formatSql = sql;
        } else {
            formatSql = formatSqlMaybeWithParam(sql, null, funcConsumer.getValues());
        }
        getSelectColum().add(new SelectFunc(alias, getIndex(), () -> formatSql,
                funcConsumer.getArgs(), isHasAlias(), getAlias()));
        return typedThis;
    }

    /**
     * 子查询
     */
    public <E, F> MPJLambdaWrapper<T> selectSub(Class<E> clazz, Consumer<MPJLambdaWrapper<E>> consumer, SFunction<F, ?> alias) {
        return selectSub(clazz, ConfigProperties.subQueryAlias, consumer, alias);
    }

    public <E, F> MPJLambdaWrapper<T> selectSub(Class<E> clazz, String st, Consumer<MPJLambdaWrapper<E>> consumer, SFunction<F, ?> alias) {
        return selectSub(clazz, st, consumer, LambdaUtils.getName(alias));
    }

    /**
     * 子查询
     */
    public <E, F> MPJLambdaWrapper<T> selectSub(Class<E> clazz, String st, Consumer<MPJLambdaWrapper<E>> consumer, String alias) {
        MPJLambdaWrapper<E> wrapper = new MPJLambdaWrapper<E>(null, clazz, SharedString.emptyString(),
                paramNameSeq, paramNameValuePairs, new MergeSegments(), new SharedString(this.paramAlias
                .getStringValue()), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                new TableList(), null, null, null, null, ifExists) {
        };
        wrapper.tableList.setAlias(st);
        wrapper.tableList.setRootClass(clazz);
        wrapper.tableList.setParent(this.tableList);
        wrapper.alias = st;
        wrapper.subTableAlias = st;
        addCustomWrapper(wrapper);
        consumer.accept(wrapper);
        this.selectColumns.add(new SelectSub(() -> WrapperUtils.buildSubSqlByWrapper(clazz, wrapper, alias), hasAlias, this.alias, alias));
        return typedThis;
    }

    /**
     * union
     * <p>
     * 例： wrapper.union(UserDO.class, union -> union.selectAll(UserDO.class))
     *
     * @param clazz union语句的主表类型
     * @since 1.4.8
     */
    public <U> MPJLambdaWrapper<T> union(Class<U> clazz, Consumer<MPJLambdaWrapper<U>> consumer) {
        MPJLambdaWrapper<U> unionWrapper = JoinWrappers.lambda(clazz);
        addCustomWrapper(unionWrapper);
        consumer.accept(unionWrapper);

        String sb = " UNION " + WrapperUtils.buildUnionSqlByWrapper(clazz, unionWrapper);

        if (Objects.isNull(unionSql)) {
            unionSql = SharedString.emptyString();
        }
        unionSql.setStringValue(unionSql.getStringValue() + sb);
        return typedThis;
    }

    @Override
    public MPJLambdaWrapper<T> addFill(MConsumer<Object> consumer) {
        if (this.fill == null) {
            this.fill = new ArrayList<>();
        }
        this.fill.add(consumer);
        return typedThis;
    }

    @Override
    public void doFill(Object obj) {
        if (this.fill != null) {
            this.fill.forEach(c -> c.accept(obj));
        }
    }

    /**
     * union
     * <p>
     * 例： wrapper.unionAll(UserDO.class, union -> union.selectAll(UserDO.class))
     *
     * @param clazz union语句的主表类型
     * @since 1.4.8
     */
    public <U> MPJLambdaWrapper<T> unionAll(Class<U> clazz, Consumer<MPJLambdaWrapper<U>> consumer) {
        MPJLambdaWrapper<U> unionWrapper = JoinWrappers.lambda(clazz);
        addCustomWrapper(unionWrapper);
        consumer.accept(unionWrapper);

        String sb = " UNION ALL " + WrapperUtils.buildUnionSqlByWrapper(clazz, unionWrapper);

        if (Objects.isNull(unionSql)) {
            unionSql = SharedString.emptyString();
        }
        unionSql.setStringValue(unionSql.getStringValue() + sb);
        return typedThis;
    }

    private void addCustomWrapper(MPJLambdaWrapper<?> wrapper) {
        if (Objects.isNull(wrapperIndex)) {
            wrapperIndex = new AtomicInteger(0);
        }
        int index = wrapperIndex.incrementAndGet();
        if (Objects.isNull(wrapperMap)) {
            wrapperMap = new HashMap<>();
        }
        String key = "ew" + index;
        wrapper.setParamAlias(getParamAlias() + ".wrapperMap." + key);
        wrapperMap.put(key, wrapper);
    }

    /**
     * 查询条件 SQL 片段
     */
    @Override
    public String getSqlSelect() {
        if (StringUtils.isBlank(sqlSelect.getStringValue()) && CollectionUtils.isNotEmpty(selectColumns)) {
            String s = selectColumns.stream().map(i -> {
                if (i.isStr()) {
                    return i.getColumn();
                }
                String prefix;
                if (i.isHasTableAlias()) {
                    prefix = i.getTableAlias();
                } else {
                    prefix = tableList.getPrefix(i.getIndex(), i.getClazz(), i.isLabel());
                }
                String str = prefix + StringPool.DOT + i.getColumn();
                if (i.isFunc()) {
                    SelectFunc.Arg[] args = i.getArgs();
                    if (Objects.isNull(args) || args.length == 0) {
                        return String.format(i.getFunc().getSql(), str) + Constant.AS + i.getAlias();
                    } else {
                        return String.format(i.getFunc().getSql(), Arrays.stream(args).map(arg -> {
                            String pf = arg.isHasTableAlias() ? arg.getTableAlias() : tableList.getPrefixByClass(arg.getClazz());
                            Map<String, SelectCache> mapField = ColumnCache.getMapField(arg.getClazz());
                            SelectCache cache = mapField.get(arg.getProp());
                            return pf + StringPool.DOT + cache.getColumn();
                        }).toArray()) + Constant.AS + i.getAlias();
                    }
                } else {
                    return i.isHasAlias() ? (str + Constant.AS + i.getAlias()) : str;
                }
            }).collect(Collectors.joining(StringPool.COMMA));
            sqlSelect.setStringValue(s);
        }
        return sqlSelect.getStringValue();
    }

    @Override
    public String getUnionSql() {
        return Optional.ofNullable(unionSql).map(SharedString::getStringValue).orElse(StringPool.EMPTY);
    }

    public boolean getSelectDistinct() {
        return selectDistinct;
    }

    /**
     * 用于生成嵌套 sql
     * <p>故 sqlSelect 不向下传递</p>
     */
    @Override
    protected MPJLambdaWrapper<T> instance() {
        return instance(index, null, null, null);
    }

    @Override
    protected MPJLambdaWrapper<T> instanceEmpty() {
        return new MPJLambdaWrapper<>();
    }

    @Override
    protected MPJLambdaWrapper<T> instance(Integer index, String keyWord, Class<?> joinClass, String tableName) {
        return new MPJLambdaWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), this.paramAlias, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                this.tableList, index, keyWord, joinClass, tableName, ifExists);
    }

    @Override
    public void clear() {
        super.clear();
        selectDistinct = false;
        sqlSelect.toNull();
        selectColumns.clear();
        wrapperIndex = new AtomicInteger(0);
        if (Objects.nonNull(wrapperMap)) wrapperMap.clear();
        if (Objects.nonNull(unionSql)) unionSql.toEmpty();
        resultMapMybatisLabel.clear();
        ifExists = ConfigProperties.ifExists;
        fill = null;
    }
}
