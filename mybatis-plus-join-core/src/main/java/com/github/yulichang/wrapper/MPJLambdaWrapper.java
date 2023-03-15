package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.config.enums.LogicDelTypeEnum;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.*;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.interfaces.Chain;
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

import static com.baomidou.mybatisplus.core.enums.WrapperKeyword.APPLY;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
 * Lambda 语法使用 Wrapper
 *
 * @author yulichang
 */
@SuppressWarnings({"unused"})
public class MPJLambdaWrapper<T> extends MPJAbstractLambdaWrapper<T, MPJLambdaWrapper<T>> implements
        Query<MPJLambdaWrapper<T>>, QueryJoin<MPJLambdaWrapper<T>, T>, QueryLabel<MPJLambdaWrapper<T>>, Chain<T> {

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
    @Getter
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
        tableList.setRootClass(clazz);
    }

    /**
     * 构造方法
     *
     * @param entity 主表实体
     */
    public MPJLambdaWrapper(T entity) {
        super.initNeed();
        setEntity(entity);
        if (entity != null) {
            tableList.setRootClass(entity.getClass());
        }
    }

    /**
     * 自定义主表别名
     */
    public MPJLambdaWrapper(String alias) {
        this.alias = alias;
        super.initNeed();
        tableList.setAlias(alias);
    }

    /**
     * 构造方法
     *
     * @param clazz 主表class类
     * @param alias 主表别名
     */
    public MPJLambdaWrapper(Class<T> clazz, String alias) {
        this.alias = alias;
        setEntityClass(clazz);
        super.initNeed();
        tableList.setAlias(alias);
        tableList.setRootClass(clazz);
    }

    /**
     * 构造方法
     *
     * @param entity 主表实体类
     * @param alias  主表别名
     */
    public MPJLambdaWrapper(T entity, String alias) {
        this.alias = alias;
        setEntity(entity);
        super.initNeed();
        tableList.setAlias(alias);
        if (entity != null) {
            tableList.setRootClass(entity.getClass());
        }
    }


    /**
     * 不建议直接 new 该实例，使用 MPJWrappers.<UserDO>lambdaQuery()
     */
    MPJLambdaWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                     Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                     SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                     TableList tableList, Integer index, String keyWord, Class<?> joinClass, String tableName) {
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
        this.tableName = tableName;
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
                getSelectColum().add(new SelectNormal(cache, index, hasAlias, alias));
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
                    SFunction<?, ?>[] args = i.getArgs();
                    if (Objects.isNull(args) || args.length == 0) {
                        return String.format(i.getFunc().getSql(), str) + Constant.AS + i.getAlias();
                    } else {
                        return String.format(i.getFunc().getSql(), Arrays.stream(args).map(arg -> {
                            Class<?> entityClass = LambdaUtils.getEntityClass(arg);
                            String prefixByClass = tableList.getPrefixByClass(entityClass);
                            Map<String, SelectCache> mapField = ColumnCache.getMapField(entityClass);
                            SelectCache cache = mapField.get(LambdaUtils.getName(arg));
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

    /**
     * 获取连表部分语句
     */
    public String getFrom() {
        if (StringUtils.isBlank(from.getStringValue())) {
            StringBuilder value = new StringBuilder();
            for (MPJLambdaWrapper<?> wrapper : onWrappers) {
                if (wrapper.subLogicSql && this.logicDelType == LogicDelTypeEnum.ON) {
                    TableInfo tableInfo = TableHelper.get(wrapper.getJoinClass());
                    if (ConfigProperties.adapter.mpjHasLogic(tableInfo)) {
                        wrapper.appendSqlSegments(APPLY, () -> LogicInfoUtils.getLogicInfoNoAnd(
                                wrapper.getIndex(), wrapper.getJoinClass(), wrapper.isHasAlias(), wrapper.getAlias()
                        ));
                    }
                }
                if (StringUtils.isBlank(wrapper.from.getStringValue())) {
                    value.append(StringPool.SPACE)
                            .append(wrapper.getKeyWord())
                            .append(StringPool.SPACE)
                            .append(wrapper.getTableName())
                            .append(StringPool.SPACE)
                            .append(wrapper.hasAlias ? wrapper.alias : (wrapper.alias + wrapper.getIndex()))
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
        return instance(index, null, null, null);
    }

    protected MPJLambdaWrapper<T> instance(Integer index, String keyWord, Class<?> joinClass, String tableName) {
        return new MPJLambdaWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                this.tableList, index, keyWord, joinClass, tableName);
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
        if (subLogicSql && logicDelType == LogicDelTypeEnum.WHERE) {
            if (tableList.getAll().isEmpty()) {
                return StringPool.EMPTY;
            }
            return tableList.getAll().stream().map(t -> LogicInfoUtils.getLogicInfo(t.getIndex(),
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
     * 调整逻辑删除位置为ON语句
     */
    public MPJLambdaWrapper<T> logicDelToOn() {
        this.logicDelType = LogicDelTypeEnum.ON;
        return typedThis;
    }

    /**
     * 调整逻辑删除位置为WHERE语句
     */
    public MPJLambdaWrapper<T> logicDelToWhere() {
        this.logicDelType = LogicDelTypeEnum.WHERE;
        return typedThis;
    }

    /**
     * 内部调用, 不建议使用
     */
    @Override
    public <R> MPJLambdaWrapper<T> join(String keyWord, Class<R> clazz, String tableAlias, BiConsumer<MPJAbstractLambdaWrapper<T, ?>, MPJLambdaWrapper<T>> consumer) {
        Integer oldIndex = this.getIndex();
        int newIndex = tableIndex;
        TableInfo info = TableHelper.get(clazz);
        Assert.notNull(info, "table not find by class <%s>", clazz.getSimpleName());
        MPJLambdaWrapper<T> instance = instance(newIndex, keyWord, clazz, info.getTableName());
        instance.isNo = true;
        instance.isMain = false;
        onWrappers.add(instance);
        if (StringUtils.isBlank(tableAlias)) {
            tableList.put(oldIndex, clazz, false, ConfigProperties.tableAlias, newIndex);
            instance.alias = ConfigProperties.tableAlias;
            instance.hasAlias = false;
        } else {
            tableList.put(oldIndex, clazz, true, tableAlias, newIndex);
            instance.alias = tableAlias;
            instance.hasAlias = true;
        }
        tableIndex++;
        this.index = newIndex;
        boolean isM = this.isMain;
        this.isMain = false;
        consumer.accept(instance, typedThis);
        this.isMain = isM;
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
