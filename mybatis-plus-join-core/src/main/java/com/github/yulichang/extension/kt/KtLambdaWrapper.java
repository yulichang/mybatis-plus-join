package com.github.yulichang.extension.kt;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.extension.kt.interfaces.Query;
import com.github.yulichang.extension.kt.interfaces.QueryLabel;
import com.github.yulichang.extension.kt.toolkit.KtWrapperUtils;
import com.github.yulichang.extension.kt.toolkit.KtWrappers;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.KtUtils;
import com.github.yulichang.toolkit.TableList;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.interfaces.Chain;
import com.github.yulichang.wrapper.interfaces.SelectWrapper;
import com.github.yulichang.wrapper.resultmap.Label;
import com.github.yulichang.wrapper.segments.*;
import kotlin.reflect.KProperty;
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
 * @since 1.4.6
 */
@SuppressWarnings({"unused", "unchecked", "rawtypes", "DuplicatedCode"})
public class KtLambdaWrapper<T> extends KtAbstractLambdaWrapper<T, KtLambdaWrapper<T>> implements
        Query<KtLambdaWrapper<T>>, QueryLabel<KtLambdaWrapper<T>>, Chain<T>, SelectWrapper<T, KtLambdaWrapper<T>> {

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


    /**
     * 推荐使用 带 class 的构造方法
     */
    public KtLambdaWrapper() {
        super();
    }

    /**
     * 推荐使用此构造方法
     */
    public KtLambdaWrapper(Class<T> clazz) {
        super(clazz);
    }

    /**
     * 构造方法
     *
     * @param entity 主表实体
     */
    public KtLambdaWrapper(T entity) {
        super(entity);
    }

    /**
     * 自定义主表别名
     */
    public KtLambdaWrapper(String alias) {
        super(alias);
    }

    /**
     * 构造方法
     *
     * @param clazz 主表class类
     * @param alias 主表别名
     */
    public KtLambdaWrapper(Class<T> clazz, String alias) {
        super(clazz, alias);
    }

    /**
     * 构造方法
     *
     * @param entity 主表实体类
     * @param alias  主表别名
     */
    public KtLambdaWrapper(T entity, String alias) {
        super(entity, alias);
    }

    /**
     * 不建议直接 new 该实例，使用 JoinWrappers.lambda(UserDO.class)
     */
    KtLambdaWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                    Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString paramAlias,
                    SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                    TableList tableList, Integer index, String keyWord, Class<?> joinClass, String tableName) {
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
    }


    /**
     * sql去重
     * select distinct
     */
    public KtLambdaWrapper<T> distinct() {
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
    public KtLambdaWrapper<T> getChildren() {
        return typedThis;
    }


    /**
     * 设置查询字段
     *
     * @param columns 字段数组
     * @return children
     */
    public final KtLambdaWrapper<T> select(KProperty<?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (KProperty<?> s : columns) {
                Map<String, SelectCache> cacheMap = ColumnCache.getMapField(KtUtils.ref(s));
                SelectCache cache = cacheMap.get(s.getName());
                getSelectColum().add(new SelectNormal(cache, index, hasAlias, alias));
            }
        }
        return typedThis;
    }

    @Override
    public KtLambdaWrapper<T> selectAll() {
        return Query.super.selectAll(getEntityClass());
    }

    /**
     * 子查询
     */
    public KtLambdaWrapper<T> selectSub(Class<?> clazz, Consumer<KtLambdaWrapper<?>> consumer, KProperty<?> alias) {
        return selectSub(clazz, ConfigProperties.subQueryAlias, consumer, alias);
    }


    public <E, F> KtLambdaWrapper<T> selectSub(Class<E> clazz, String st, Consumer<KtLambdaWrapper<?>> consumer, KProperty<?> alias) {
        return selectSub(clazz, st, consumer, alias.getName());
    }

    /**
     * 子查询
     */
    public KtLambdaWrapper<T> selectSub(Class<?> clazz, String st,
                                        Consumer<KtLambdaWrapper<?>> consumer, String alias) {
        KtLambdaWrapper<?> wrapper = new KtLambdaWrapper(null, clazz, SharedString.emptyString(), paramNameSeq,
                paramNameValuePairs, new MergeSegments(), new SharedString(this.paramAlias.getStringValue()),
                SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(), new TableList(),
                null, null, null, null) {
        };
        wrapper.tableList.setAlias(st);
        wrapper.tableList.setRootClass(clazz);
        wrapper.tableList.setParent(this.tableList);
        wrapper.alias = st;
        wrapper.subTableAlias = st;
        consumer.accept(wrapper);
        addCustomWrapper(wrapper);
        this.selectColumns.add(new SelectSub(() -> KtWrapperUtils.buildSubSqlByWrapper(
                clazz, wrapper, alias), hasAlias, this.alias, alias));
        return typedThis;
    }

    /**
     * union
     * <p>
     * 推荐使用 union(Class&lt;U&gt; clazz, Consumer&lt;MPJLambdaWrapper&lt;U&gt;&gt; consumer)
     * 例： wrapper.union(UserDO.class, union -> union.selectAll(UserDO.class))
     *
     * @see #union(Class, Consumer)
     * @deprecated union 不支持子查询
     */
    @Deprecated
    @SuppressWarnings("ALL")
    public final KtLambdaWrapper<T> union(KtLambdaWrapper<?>... wrappers) {
        StringBuilder sb = new StringBuilder();
        for (KtLambdaWrapper<?> wrapper : wrappers) {
            addCustomWrapper(wrapper);
            Class<?> entityClass = wrapper.getEntityClass();
            Assert.notNull(entityClass, "请使用 new MPJLambdaWrapper(主表.class) 或 JoinWrappers.lambda(主表.class) 构造方法");
            sb.append(" UNION ").append(KtWrapperUtils.buildUnionSqlByWrapper(entityClass, wrapper));
        }
        if (Objects.isNull(unionSql)) {
            unionSql = SharedString.emptyString();
        }
        unionSql.setStringValue(unionSql.getStringValue() + sb);
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
    public <U> KtLambdaWrapper<T> union(Class<U> clazz, Consumer<KtLambdaWrapper<U>> consumer) {
        KtLambdaWrapper<U> unionWrapper = KtWrappers.query(clazz);
        addCustomWrapper(unionWrapper);
        consumer.accept(unionWrapper);

        String sb = " UNION " + KtWrapperUtils.buildUnionSqlByWrapper(clazz, unionWrapper);

        if (Objects.isNull(unionSql)) {
            unionSql = SharedString.emptyString();
        }
        unionSql.setStringValue(unionSql.getStringValue() + sb);
        return typedThis;
    }

    /**
     * union
     * <p>
     * 推荐使用 unionAll(Class&lt;U&gt; clazz, Consumer&lt;MPJLambdaWrapper&lt;U&gt;&gt; consumer)
     * 例： wrapper.unionAll(UserDO.class, union -> union.selectAll(UserDO.class))
     *
     * @see #unionAll(Class, Consumer)
     * @deprecated union 不支持子查询
     */
    @Deprecated
    @SuppressWarnings("ALL")
    public final KtLambdaWrapper<T> unionAll(KtLambdaWrapper<?>... wrappers) {
        StringBuilder sb = new StringBuilder();
        for (KtLambdaWrapper<?> wrapper : wrappers) {
            addCustomWrapper(wrapper);
            Class<?> entityClass = wrapper.getEntityClass();
            Assert.notNull(entityClass, "请使用 new MPJLambdaWrapper(主表.class) 或 JoinWrappers.lambda(主表.class) 构造方法");
            sb.append(" UNION ALL ").append(KtWrapperUtils.buildUnionSqlByWrapper(entityClass, wrapper));
        }
        if (Objects.isNull(unionSql)) {
            unionSql = SharedString.emptyString();
        }
        unionSql.setStringValue(unionSql.getStringValue() + sb);
        return typedThis;
    }

    /**
     * union
     * <p>
     * 例： wrapper.unionAll(UserDO.class, union -> union.selectAll(UserDO.class))
     *
     * @param clazz union语句的主表类型
     * @since 1.4.8
     */
    public <U> KtLambdaWrapper<T> unionAll(Class<U> clazz, Consumer<KtLambdaWrapper<U>> consumer) {
        KtLambdaWrapper<U> unionWrapper = KtWrappers.query(clazz);
        addCustomWrapper(unionWrapper);
        consumer.accept(unionWrapper);

        String sb = " UNION ALL " + KtWrapperUtils.buildUnionSqlByWrapper(clazz, unionWrapper);

        if (Objects.isNull(unionSql)) {
            unionSql = SharedString.emptyString();
        }
        unionSql.setStringValue(unionSql.getStringValue() + sb);
        return typedThis;
    }

    private void addCustomWrapper(KtLambdaWrapper<?> wrapper) {
        if (Objects.isNull(wrapperIndex)) {
            wrapperIndex = new AtomicInteger(0);
        }
        int index = wrapperIndex.incrementAndGet();
        if (Objects.isNull(wrapperMap)) {
            wrapperMap = new HashMap<>();
        }
        String key = "ew" + index;
        wrapper.setParamAlias(wrapper.getParamAlias() + ".wrapperMap." + key);
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
                    if (i.isLabel()) {
                        if (i.isHasTableAlias()) {
                            prefix = i.getTableAlias();
                        } else {
                            prefix = tableList.getPrefix(i.getIndex(), i.getClazz(), true);
                        }
                    } else {
                        prefix = tableList.getPrefix(i.getIndex(), i.getClazz(), false);
                    }
                }
                String str = prefix + StringPool.DOT + i.getColumn();
                if (i.isFunc()) {
                    SelectFunc.Arg[] args = i.getArgs();
                    if (Objects.isNull(args) || args.length == 0) {
                        return String.format(i.getFunc().getSql(), str) + Constant.AS + i.getAlias();
                    } else {
                        return String.format(i.getFunc().getSql(), Arrays.stream(args).map(arg -> {
                            String prefixByClass = tableList.getPrefixByClass(arg.getClazz());
                            Map<String, SelectCache> mapField = ColumnCache.getMapField(arg.getClazz());
                            SelectCache cache = mapField.get(arg.getProp());
                            return prefixByClass + StringPool.DOT + cache.getColumn();
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
    protected KtLambdaWrapper<T> instance() {
        return instance(index, null, null, null);
    }

    @Override
    protected KtLambdaWrapper<T> instanceEmpty() {
        return new KtLambdaWrapper<>();
    }

    @Override
    protected KtLambdaWrapper<T> instance(Integer index, String keyWord, Class<?> joinClass, String tableName) {
        return new KtLambdaWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), this.paramAlias, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                this.tableList, index, keyWord, joinClass, tableName);
    }

    @Override
    public void clear() {
        super.clear();
        selectDistinct = false;
        sqlSelect.toEmpty();
        if (Objects.nonNull(unionSql)) unionSql.toEmpty();
        selectColumns.clear();
        wrapperIndex = new AtomicInteger(0);
        wrapperMap.clear();
        resultMapMybatisLabel.clear();
    }
}
