package com.github.yulichang.wrapper.apt;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.apt.BaseColumn;
import com.github.yulichang.apt.Column;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.config.enums.LogicDelTypeEnum;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.LogicInfoUtils;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.TableMap;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.apt.interfaces.QueryJoin;
import com.github.yulichang.wrapper.interfaces.MFunction;
import com.github.yulichang.wrapper.segments.SelectCache;
import lombok.Getter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.baomidou.mybatisplus.core.enums.WrapperKeyword.APPLY;
import static java.util.stream.Collectors.joining;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper}
 *
 * @author yulichang
 */
@SuppressWarnings({"DuplicatedCode", "unused"})
public abstract class AptAbstractWrapper<T, Children extends AptAbstractWrapper<T, Children>>
        extends JoinAbstractWrapper<T, Children> implements QueryJoin<Children, T> {

    /**
     * 主表别名
     */
    @Getter
    protected String alias = ConfigProperties.tableAlias;
    /**
     * 副表别名
     */
    protected String subTableAlias = ConfigProperties.tableAlias;
    /**
     * 是否构建是否存在一对多
     */
    @Getter
    protected boolean resultMap = false;
    /**
     * 表序号
     */
    protected int tableIndex = 1;

    protected boolean isMain = true;

    /**
     * 主表 表名处理方法
     */
    protected boolean dynamicTableName = false;

    /**
     * 主表 表名处理方法
     */
    protected Function<String, String> tableFunc;

    /**
     * 逻辑删除位置
     */
    protected LogicDelTypeEnum logicDelType = ConfigProperties.logicDelType;

    /**
     * 查询表
     */
    protected final SharedString from = new SharedString();
    /**
     * 是否有表别名
     */
    @Getter
    protected boolean hasAlias;
    /**
     * 连表关键字 on 条件 func 使用
     */
    @Getter
    protected String keyWord;
    /**
     * 副表逻辑删除开关
     */
    protected boolean subLogicSql = ConfigProperties.subTableLogic;
    /**
     * 主表逻辑删除开关
     */
    protected boolean logicSql = true;

    @Getter
    protected BaseColumn<T> baseColumn;

    /**
     * apt别名对应
     */
    @Getter
    protected TableMap aptIndex;

    /**
     * 构造方法
     */
    protected AptAbstractWrapper(BaseColumn<T> baseColumn) {
        this.alias = StringUtils.isNotBlank(baseColumn.getAlias()) ? baseColumn.getAlias() : this.alias;
        setEntityClass(baseColumn.getColumnClass());
        this.baseColumn = baseColumn;
        initNeed();
    }

    /**
     * 构造方法
     *
     * @param entity 主表实体类
     */
    protected AptAbstractWrapper(BaseColumn<T> baseColumn, T entity) {
        this.alias = StringUtils.isNotBlank(baseColumn.getAlias()) ? baseColumn.getAlias() : this.alias;
        setEntity(entity);
        this.baseColumn = baseColumn;
        initNeed();
    }

    /**
     * 设置表别名
     * 设置表别名注意sql注入问题
     *
     * @return 自定义表别名
     */
    public Children setTableName(Function<String, String> tableFunc) {
        if (isMain) {
            if (tableFunc != null) {
                this.dynamicTableName = true;
                this.tableFunc = tableFunc;
            }
        } else {
            this.tableName = tableFunc.apply(this.tableName);
        }
        return typedThis;
    }

    public String getTableName(String tableName) {
        if (isMain) {
            if (dynamicTableName) {
                return tableFunc.apply(tableName);
            }
            return tableName;
        }
        return super.getTableName();
    }


    public String getTableNameEnc(String tableName) {
        Class<T> entityClass = getEntityClass();
        if (entityClass != null) {
            TableInfo tableInfo = TableHelper.get(entityClass);
            if (tableInfo != null) {
                if (dynamicTableName) {
                    return tableFunc.apply(tableInfo.getTableName());
                }
                return tableInfo.getTableName();
            }
        }
        String decode;
        try {
            decode = URLDecoder.decode(tableName, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (dynamicTableName) {
            return tableFunc.apply(decode);
        }
        return decode;
    }

    @Override
    protected String columnsToString(Column... columns) {
        return Arrays.stream(columns).map(this::columnToString)
                .collect(joining(StringPool.COMMA));
    }

    @Override
    protected String columnToString(Column column) {
        String pf = column.getAlias().get();
        if (Objects.nonNull(pf)) {
            return pf + StringPool.DOT + getCache(column).getColumn();
        }
        return this.aptIndex.get(column.getRoot()) + StringPool.DOT + getCache(column).getColumn();
    }

    protected SelectCache getCache(Column fn) {
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(fn.getClazz());
        return cacheMap.get(fn.getProperty());
    }


    /**
     * 关闭副表逻辑删除
     * <p>
     * 副表逻辑删除默认在where语句中
     * 但有时候需要让它出现在on语句中, 这两种写法区别还是很大的
     * 所以可以关闭副表逻辑删除, 通过on语句多条件, 自己实现on语句的逻辑删除
     */
    public Children disableSubLogicDel() {
        this.subLogicSql = false;
        return typedThis;
    }

    public Children enableSubLogicDel() {
        this.subLogicSql = true;
        return typedThis;
    }

    /**
     * 关闭主表逻辑删除
     */
    public Children disableLogicDel() {
        this.logicSql = false;
        return typedThis;
    }

    public Children enableLogicDel() {
        this.logicSql = true;
        return typedThis;
    }

    /**
     * 副表部分逻辑删除支持
     */
    public String getSubLogicSql() {
        if (subLogicSql && logicDelType == LogicDelTypeEnum.WHERE) {
            if (aptIndex.isEmpty()) {
                return StringPool.EMPTY;
            }
            return aptIndex.keyList().stream().map(key -> LogicInfoUtils.getLogicInfoApt(key.getColumnClass(), this.aptIndex.get(key)))
                    .collect(Collectors.joining(StringPool.SPACE));
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
    public Children logicDelToOn() {
        this.logicDelType = LogicDelTypeEnum.ON;
        return typedThis;
    }

    /**
     * 调整逻辑删除位置为WHERE语句
     */
    public Children logicDelToWhere() {
        this.logicDelType = LogicDelTypeEnum.WHERE;
        return typedThis;
    }

    /**
     * 获取连表部分语句
     */
    public String getFrom() {
        if (StringUtils.isBlank(from.getStringValue())) {
            StringBuilder value = new StringBuilder();
            for (Children wrapper : onWrappers) {
                if (StringUtils.isBlank(wrapper.from.getStringValue())) {
                    if (this.subLogicSql && this.logicDelType == LogicDelTypeEnum.ON) {
                        TableInfo tableInfo = TableHelper.getAssert(wrapper.getJoinClass());
                        if (AdapterHelper.getAdapter().mpjHasLogic(tableInfo)) {
                            wrapper.appendSqlSegments(APPLY, () -> LogicInfoUtils.getLogicInfoNoAnd(
                                    wrapper.getIndex(), wrapper.getJoinClass(), wrapper.isHasAlias(), wrapper.getAlias()
                            ));
                        }
                    }
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

    /**
     * 检验表是否已连接
     */
    public boolean checkJoinTable(Class<?> clazz) {
        return onWrappers.stream().anyMatch(wrapper -> {
            if (Objects.equals(wrapper.getJoinClass(), clazz)) {
                return true;
            } else {
                TableInfo info = TableHelper.getAssert(clazz);
                String tableName = info.getTableName();
                return Optional.ofNullable(wrapper.from.getStringValue())
                        .map(w -> w.contains(Constant.JOIN + StringPool.SPACE + tableName + StringPool.SPACE))
                        .orElse(false);
            }
        });
    }

    /**
     * 内部调用, 不建议使用
     */
    @Override
    public <R> Children join(String keyWord, BaseColumn<R> baseColumn, MFunction<AptAbstractWrapper<T, ?>> function) {
        Integer oldIndex = this.getIndex();
        int newIndex = tableIndex;
        TableInfo info = TableHelper.getAssert(baseColumn.getColumnClass());
        Children instance = instance(newIndex, keyWord, baseColumn.getColumnClass(), info.getTableName());
        instance.isMain = false;
        onWrappers.add(instance);
        if (StringUtils.isBlank(baseColumn.getAlias())) {
            aptIndex.put(baseColumn, subTableAlias + newIndex);
            instance.alias = subTableAlias;
            instance.hasAlias = false;
        } else {
            aptIndex.put(baseColumn, baseColumn.getAlias());
            instance.alias = baseColumn.getAlias();
            instance.hasAlias = true;
        }
        tableIndex++;
        function.apply(instance);
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
    public Children join(String keyWord, boolean condition, String joinSql) {
        if (condition) {
            Children wrapper = instanceEmpty();
            wrapper.from.setStringValue(joinSql);
            wrapper.keyWord = keyWord;
            onWrappers.add(wrapper);
        }
        return typedThis;
    }

    /**
     * 是否使用默认注解 {@link OrderBy} 排序
     *
     * @return true 使用 false 不使用
     */
    public boolean isUseAnnotationOrderBy() {
        final String _sqlSegment = this.getSqlSegment();
        if (StringUtils.isBlank(_sqlSegment)) {
            return true;
        }
        final String _sqlSegmentToUpperCase = _sqlSegment.toUpperCase();
        return !(_sqlSegmentToUpperCase.contains(Constants.ORDER_BY)
                || _sqlSegmentToUpperCase.contains(Constants.LIMIT));
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
        aptIndex = new TableMap(this.baseColumn, this.alias);
    }

    @Override
    public void clear() {
        super.clear();
        this.alias = ConfigProperties.tableAlias;
        this.resultMap = false;
        this.tableIndex = 1;
        this.dynamicTableName = false;
        this.tableFunc = null;
        this.logicDelType = ConfigProperties.logicDelType;
        this.from.toEmpty();
        this.hasAlias = false;
        this.keyWord = null;
        this.logicSql = true;
        this.isMain = true;
        this.checkSqlInjection = false;
        this.onWrappers.clear();
        this.aptIndex.clear();
    }
}
