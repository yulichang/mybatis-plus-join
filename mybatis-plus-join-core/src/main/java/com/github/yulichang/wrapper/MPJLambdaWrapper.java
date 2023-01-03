package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.LogicInfoUtils;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.interfaces.Query;
import com.github.yulichang.wrapper.interfaces.QueryJoin;
import com.github.yulichang.wrapper.interfaces.QueryLabel;
import com.github.yulichang.wrapper.resultmap.MybatisLabel;
import com.github.yulichang.wrapper.segments.Select;
import com.github.yulichang.wrapper.segments.SelectCache;
import com.github.yulichang.wrapper.segments.SelectNormal;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
 * Lambda 语法使用 Wrapper
 *
 * @author yulichang
 */
@SuppressWarnings({"unused"})
public class MPJLambdaWrapper<T> extends MPJAbstractLambdaWrapper<T, MPJLambdaWrapper<T>>
        implements Query<MPJLambdaWrapper<T>>, QueryJoin<MPJLambdaWrapper<T>, T>, QueryLabel<MPJLambdaWrapper<T>> {

    /**
     * 查询表
     */
    private final SharedString from = new SharedString();
    /**
     * 查询的字段
     */
    @Getter
    private final List<Select> selectColumns = new ArrayList<>();
    /**
     * 映射关系
     */
    @Getter
    private final List<MybatisLabel<?, ?>> resultMapMybatisLabel = new ArrayList<>();
    /**
     * 是否有表别名
     */
    private boolean hasAlias;
    /**
     * 查询字段 sql
     */
    private SharedString sqlSelect = new SharedString();
    /**
     * 是否 select distinct
     */
    private boolean selectDistinct = false;
    /**
     * 连表关键字 on 条件 func 使用
     */
    @Getter
    private String keyWord;
    /**
     * 副表逻辑删除开关
     */
    private boolean subLogicSql = ConfigProperties.subTableLogic;
    /**
     * 主表逻辑删除开关
     */
    private boolean logicSql = true;

    /**
     * 推荐使用 带 class 的构造方法
     */
    public MPJLambdaWrapper() {
        super.initNeed();
    }

    /**
     * 推荐使用此构造方法
     */
    public MPJLambdaWrapper(Class<T> clazz) {
        super.initNeed();
        setEntityClass(clazz);
    }

    /**
     * 自定义主表别名
     */
    public MPJLambdaWrapper(String alias) {
        this.alias = alias;
        super.initNeed();
    }

    /**
     * 推荐使用此构造方法
     */
    public MPJLambdaWrapper(Class<T> clazz, String alias) {
        this.alias = alias;
        setEntityClass(clazz);
        super.initNeed();
    }


    /**
     * 不建议直接 new 该实例，使用 MPJWrappers.<UserDO>lambdaQuery()
     */
    MPJLambdaWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                     Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                     SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                     TableList tableList, String index, String keyWord, Class<?> joinClass, Node node) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.tableList = tableList;
        this.index = index;
        this.keyWord = keyWord;
        this.joinClass = joinClass;
        this.node = node;
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
    public void addLabel(MybatisLabel<?, ?> label) {
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
                getSelectColum().add(new SelectNormal(cache, index));
            }
        }
        return typedThis;
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
                Table t = tableList.get(i.getClazz());
                String str;
                if (t.isHasAlias()) {
                    str = t.getAlias() + StringPool.DOT + i.getColumn();
                } else {
                    if (i.isLabel() && Objects.nonNull(i.getIndex())) {
                        str = i.getIndex() + StringPool.DOT + i.getColumn();
                    } else {
                        str = t.getAlias() + getDefaultSelect(i.getIndex(), i.getClazz(), i) + StringPool.DOT + i.getColumn();
                    }
                }
                if (i.isFunc()) {
                    SFunction<?, ?>[] args = i.getArgs();
                    if (Objects.isNull(args) || args.length == 0) {
                        return String.format(i.getFunc().getSql(), str) + Constant.AS + i.getAlias();
                    } else {
                        return String.format(i.getFunc().getSql(), Arrays.stream(args).map(arg -> {
                            Class<?> entityClass = LambdaUtils.getEntityClass(arg);
                            Table table = tableList.getPositive(entityClass);
                            Assert.notNull(table, "table not find by class <%s>", entityClass.getSimpleName());
                            Map<String, SelectCache> mapField = ColumnCache.getMapField(entityClass);
                            SelectCache cache = mapField.get(LambdaUtils.getName(arg));
                            return tableList.get(cache.getClazz()).getAlias() + (Objects.isNull(table.getIndex()) ? StringPool.EMPTY : table.getIndex()) + StringPool.DOT + cache.getColumn();
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

    /**
     * 获取连表部分语句
     */
    public String getFrom() {
        if (StringUtils.isBlank(from.getStringValue())) {
            StringBuilder value = new StringBuilder();
            for (MPJLambdaWrapper<?> wrapper : onWrappers) {
                if (StringUtils.isBlank(wrapper.from.getStringValue())) {
                    TableInfo info = TableHelper.get(wrapper.getJoinClass());
                    Assert.notNull(info, "table not find by class <%s>", wrapper.getJoinClass().getSimpleName());
                    String tableName = info.getTableName();
                    value.append(StringPool.SPACE)
                            .append(wrapper.getKeyWord())
                            .append(StringPool.SPACE)
                            .append(tableName)
                            .append(StringPool.SPACE)
                            .append(wrapper.hasAlias ? wrapper.alias : (wrapper.alias + (tableList.get(wrapper.getJoinClass(), wrapper.getIndex()).getIndex())))
                            .append(Constant.ON)
                            .append(wrapper.getExpression().getNormal().getSqlSegment());
                } else {
                    value.append(StringPool.SPACE)
                            .append(wrapper.getKeyWord())
                            .append(StringPool.SPACE)
                            .append(wrapper.from.getStringValue())
                            .append(StringPool.SPACE);
                }
            }
            from.setStringValue(value.toString());
        }
        return from.getStringValue();
    }

    public String getAlias() {
        return alias;
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
        return instance(index, null, null, this.node);
    }

    protected MPJLambdaWrapper<T> instance(String index, String keyWord, Class<?> joinClass, Node node) {
        return new MPJLambdaWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                this.tableList, index, keyWord, joinClass, node);
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
        from.toNull();
        selectColumns.clear();
        tableList.clear();
    }

    /**
     * 关闭副表逻辑删除
     * <p>
     * 副表逻辑删除默认在where语句中
     * 但有时候需要让它出现在on语句中, 这两种写法区别还是很大的
     * 所以可以关闭副表逻辑删除, 通过on语句多条件, 自己实现on语句的逻辑删除
     */
    public MPJLambdaWrapper<T> disableSubLogicDel() {
        this.subLogicSql = false;
        return typedThis;
    }

    public MPJLambdaWrapper<T> enableSubLogicDel() {
        this.subLogicSql = true;
        return typedThis;
    }

    /**
     * 关闭主表逻辑删除
     */
    public MPJLambdaWrapper<T> disableLogicDel() {
        this.logicSql = false;
        return typedThis;
    }

    public MPJLambdaWrapper<T> enableLogicDel() {
        this.logicSql = true;
        return typedThis;
    }

    /**
     * 副表部分逻辑删除支持
     */
    public String getSubLogicSql() {
        if (subLogicSql) {
            if (tableList.isEmpty()) {
                return StringPool.EMPTY;
            }
            return tableList.stream().map(t -> LogicInfoUtils.getLogicInfo(t.getIndex(),
                    t.getClazz(), t.isHasAlias(), t.getAlias())).collect(Collectors.joining(StringPool.SPACE));
        }
        return StringPool.EMPTY;
    }

    /**
     * 主表部分逻辑删除支持
     */
    public boolean getLogicSql() {
        return this.logicSql;
    }

    /**
     * 内部调用, 不建议使用
     */
    @Override
    public <R> MPJLambdaWrapper<T> join(String keyWord, Class<R> clazz, String tableAlias, BiConsumer<MPJAbstractLambdaWrapper<T, ?>, MPJLambdaWrapper<T>> consumer) {
        String oldIndex = this.getIndex();
        String newIndex = String.valueOf(tableIndex);
        Node n = Objects.isNull(oldIndex) ? new Node(clazz, tableIndex, ROOT_NODE) : new Node(clazz, tableIndex, this.node);
        MPJLambdaWrapper<T> instance = instance(newIndex, keyWord, clazz, n);
        this.node = n;
        onWrappers.add(instance);
        if (StringUtils.isBlank(tableAlias)) {
            tableList.add(clazz, newIndex, false, ConfigProperties.tableAlias);
            instance.alias = ConfigProperties.tableAlias;
            instance.hasAlias = false;
            tableIndex++;
        } else {
            tableList.add(clazz, null, true, tableAlias);
            instance.alias = tableAlias;
            instance.hasAlias = true;
        }
        this.index = newIndex;
        consumer.accept(instance, typedThis);
        this.index = oldIndex;
        return typedThis;
    }


    /**
     * 自定义关键词连接
     *
     * @param keyWord   连表关键词
     * @param condition 条件
     * @param joinSql   sql
     */
    @Override
    public MPJLambdaWrapper<T> join(String keyWord, boolean condition, String joinSql) {
        if (condition) {
            MPJLambdaWrapper<T> wrapper = new MPJLambdaWrapper<>();
            wrapper.from.setStringValue(joinSql);
            wrapper.keyWord = keyWord;
            onWrappers.add(wrapper);
        }
        return typedThis;
    }
}
