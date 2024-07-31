package com.github.yulichang.wrapper.apt;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.apt.BaseColumn;
import com.github.yulichang.apt.Column;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.*;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.apt.interfaces.Query;
import com.github.yulichang.wrapper.apt.interfaces.QueryLabel;
import com.github.yulichang.wrapper.enums.IfExistsSqlKeyWordEnum;
import com.github.yulichang.wrapper.interfaces.Chain;
import com.github.yulichang.wrapper.interfaces.SelectWrapper;
import com.github.yulichang.wrapper.resultmap.Label;
import com.github.yulichang.wrapper.segments.Select;
import com.github.yulichang.wrapper.segments.SelectApt;
import com.github.yulichang.wrapper.segments.SelectCache;
import com.github.yulichang.wrapper.segments.SelectSub;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Lambda 语法使用 Wrapper
 *
 * @author yulichang
 * @since 1.5.0
 */
@SuppressWarnings("unused")
public class AptQueryWrapper<T> extends AptAbstractWrapper<T, AptQueryWrapper<T>> implements
        Query<AptQueryWrapper<T>>, QueryLabel<AptQueryWrapper<T>>, Chain<T>, SelectWrapper<T, AptQueryWrapper<T>> {

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
     * 推荐使用此构造方法
     */
    public AptQueryWrapper(BaseColumn<T> baseColumn) {
        super(baseColumn);
    }


    public AptQueryWrapper(BaseColumn<T> baseColumn, T entity) {
        super(baseColumn, entity);
    }


    /**
     * 不建议直接 new 该实例，使用 JoinWrappers.lambda(UserDO.class)
     */
    protected AptQueryWrapper(T entity, BaseColumn<T> baseColumn, SharedString sqlSelect, AtomicInteger paramNameSeq,
                              Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString paramAlias,
                              SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                              TableMap aptIndex, Integer index, String keyWord, Class<?> joinClass, String tableName,
                              BiPredicate<Object, IfExistsSqlKeyWordEnum> IfExists) {
        super(baseColumn);
        super.setEntity(entity);
        super.setEntityClass(baseColumn.getColumnClass());
        this.baseColumn = baseColumn;
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.paramAlias = paramAlias;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        if (aptIndex != null) {
            this.aptIndex = aptIndex;
        }
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
    public AptQueryWrapper<T> distinct() {
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
    public AptQueryWrapper<T> getChildren() {
        return typedThis;
    }


    /**
     * 设置查询字段
     *
     * @param columns 字段数组
     * @return children
     */
    public final AptQueryWrapper<T> select(Column... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (Column s : columns) {
                Map<String, SelectCache> cacheMap = ColumnCache.getMapField(s.getClazz());
                SelectCache cache = cacheMap.get(s.getProperty());
                getSelectColum().add(new SelectApt(cache, s));
            }
        }
        return typedThis;
    }


    /**
     * 查询实体类全部字段
     */
    @Override
    public final <E> AptQueryWrapper<T> selectAll(BaseColumn<E> baseColumn, Column... exclude) {
        return Query.super.selectAll(baseColumn, exclude);
    }


    /**
     * 查询主表全部字段
     * <p>
     * 需要使用 使用 JoinWrappers.lambda(clazz) 或者 new MPJLambdaQueryWrapper&lt;&lt;(clazz) 构造
     *
     * @return children
     */
    @Override
    public AptQueryWrapper<T> selectAll() {
        Assert.notNull(getEntityClass(), "使用 JoinWrappers.apt(clazz) 或者 new JoinAptQueryWrapper<>(BaseColum)");
        return selectAll(getBaseColumn());
    }

    /**
     * 子查询
     */
    public <E, F> AptQueryWrapper<T> selectSub(BaseColumn<E> baseColumn, Consumer<AptQueryWrapper<E>> consumer, SFunction<F, ?> alias) {
        AptQueryWrapper<E> wrapper = new AptQueryWrapper<E>(null, baseColumn, SharedString.emptyString(),
                paramNameSeq, paramNameValuePairs, new MergeSegments(), new SharedString(this.paramAlias
                .getStringValue()), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                null, null, null, null, null, ifExists) {
        };
        wrapper.aptIndex.setParent(this.aptIndex);
        if (null == baseColumn.getAlias()) {
            wrapper.alias = ConfigProperties.subQueryAlias;
            wrapper.aptIndex.setRootAlias(ConfigProperties.subQueryAlias);
        }
        wrapper.subTableAlias = ConfigProperties.subQueryAlias;
        consumer.accept(wrapper);
        addCustomWrapper(wrapper);
        String name = LambdaUtils.getName(alias);
        this.selectColumns.add(new SelectSub(() -> AptWrapperUtils.buildSubSqlByWrapper(baseColumn.getColumnClass(), wrapper, name), hasAlias, this.alias, name));
        return typedThis;
    }


    /**
     * union
     * <p>
     * 例： wrapper.union(UserDO.class, union -> union.selectAll(UserDO.class))
     *
     * @param baseColumn union语句的主表类型
     * @since 1.4.8
     */
    public <U> AptQueryWrapper<T> union(BaseColumn<U> baseColumn, Consumer<AptQueryWrapper<U>> consumer) {
        AptQueryWrapper<U> unionWrapper = JoinWrappers.apt(baseColumn);
        addCustomWrapper(unionWrapper);
        consumer.accept(unionWrapper);

        String sb = " UNION " + AptWrapperUtils.buildUnionSqlByWrapper(baseColumn.getColumnClass(), unionWrapper);

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
     * @param baseColumn union语句的主表类型
     * @since 1.4.8
     */
    public <U> AptQueryWrapper<T> unionAll(BaseColumn<U> baseColumn, Consumer<AptQueryWrapper<U>> consumer) {
        AptQueryWrapper<U> unionWrapper = JoinWrappers.apt(baseColumn);
        addCustomWrapper(unionWrapper);
        consumer.accept(unionWrapper);

        String sb = " UNION ALL " + AptWrapperUtils.buildUnionSqlByWrapper(baseColumn.getColumnClass(), unionWrapper);

        if (Objects.isNull(unionSql)) {
            unionSql = SharedString.emptyString();
        }
        unionSql.setStringValue(unionSql.getStringValue() + sb);
        return typedThis;
    }

    private void addCustomWrapper(AptQueryWrapper<?> wrapper) {
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
                if (i.isFunc()) {
                    return String.format(i.getFunc().getSql(), Arrays.stream(i.getColumns()).map(c ->
                            this.aptIndex.get(c.getRoot()) + StringPool.DOT + i.getColumn()).toArray()) + Constant.AS + i.getAlias();
                } else {
                    String prefix;
                    if (null == i.getTableAlias() && null != i.getBaseColumn()) {
                        prefix = this.aptIndex.get(i.getBaseColumn());
                    } else {
                        prefix = i.getTableAlias();
                    }
                    String col = prefix + StringPool.DOT + i.getColumn();
                    return i.isHasAlias() ? col + Constants.AS + i.getAlias() : col;
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
    protected AptQueryWrapper<T> instance() {
        return instance(index, null, null, null);
    }

    @Override
    protected AptQueryWrapper<T> instanceEmpty() {
        return new AptQueryWrapper<>(getBaseColumn());
    }

    @Override
    protected AptQueryWrapper<T> instance(Integer index, String keyWord, Class<?> joinClass, String tableName) {
        return new AptQueryWrapper<>(getEntity(), baseColumn, null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), this.paramAlias, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                this.aptIndex, index, keyWord, joinClass, tableName, ifExists);
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
    }
}
