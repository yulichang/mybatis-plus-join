package com.github.yulichang.extension.kt;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.config.enums.LogicDelTypeEnum;
import com.github.yulichang.extension.kt.interfaces.QueryJoin;
import com.github.yulichang.toolkit.*;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.enums.PrefixEnum;
import com.github.yulichang.wrapper.interfaces.MFunction;
import com.github.yulichang.wrapper.segments.PageInfo;
import com.github.yulichang.wrapper.segments.SelectCache;
import kotlin.reflect.KProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
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
public abstract class KtAbstractLambdaWrapper<T, Children extends KtAbstractLambdaWrapper<T, Children>>
        extends KtAbstractWrapper<T, Children> implements QueryJoin<Children, T> {

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
    @Getter
    protected boolean resultMapCollection = false;

    @Setter
    protected PageInfo pageInfo;

    @Getter
    protected boolean pageByMain = false;
    /**
     * 表序号
     */
    protected int tableIndex = 1;

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

    /**
     * 推荐使用 带 class 的构造方法
     */
    public KtAbstractLambdaWrapper() {
        initNeed();
    }

    /**
     * 推荐使用此构造方法
     */
    public KtAbstractLambdaWrapper(Class<T> clazz) {
        initNeed();
        setEntityClass(clazz);
        tableList.setRootClass(clazz);
    }

    /**
     * 构造方法
     *
     * @param entity 主表实体
     */
    public KtAbstractLambdaWrapper(T entity) {
        initNeed();
        setEntity(entity);
        if (entity != null) {
            tableList.setRootClass(entity.getClass());
        }
    }

    /**
     * 自定义主表别名
     */
    public KtAbstractLambdaWrapper(String alias) {
        this.alias = alias;
        initNeed();
        tableList.setAlias(alias);
    }

    /**
     * 构造方法
     *
     * @param clazz 主表class类
     * @param alias 主表别名
     */
    public KtAbstractLambdaWrapper(Class<T> clazz, String alias) {
        this.alias = alias;
        setEntityClass(clazz);
        initNeed();
        tableList.setAlias(alias);
        tableList.setRootClass(clazz);
    }

    /**
     * 构造方法
     *
     * @param entity 主表实体类
     * @param alias  主表别名
     */
    public KtAbstractLambdaWrapper(T entity, String alias) {
        this.alias = alias;
        setEntity(entity);
        initNeed();
        tableList.setAlias(alias);
        if (entity != null) {
            tableList.setRootClass(entity.getClass());
        }
    }

    /**
     * 根据主表分页
     */
    public Children pageByMain() {
        this.pageByMain = true;
        return typedThis;
    }

    /**
     * 根据主表分页
     */
    public Children pageByMain(MFunction<PageInfo> function) {
        this.pageByMain = true;
        function.apply(getPageInfo());
        return typedThis;
    }

    public PageInfo getPageInfo() {
        if (pageInfo == null) {
            pageInfo = new PageInfo();
        }
        return pageInfo;
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
    protected final String columnsToString(Integer index, PrefixEnum prefixEnum, String alias, KProperty<?>... columns) {
        return Arrays.stream(columns).map(i -> columnToString(index, alias, i, false, prefixEnum)).collect(joining(StringPool.COMMA));
    }

    protected String columnToString(Integer index, String alias, KProperty<?> column, boolean isJoin, PrefixEnum prefixEnum) {
        Class<?> entityClass = KtUtils.ref(column);
        return (alias == null ? getDefault(index, entityClass, isJoin, prefixEnum) : alias) + StringPool.DOT + getCache(column).getColumn();
    }

    protected SelectCache getCache(KProperty<?> fn) {
        Class<?> aClass = KtUtils.ref(fn);
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(aClass);
        return cacheMap.get(fn.getName());
    }

    /**
     * 返回前缀
     */
    protected String getDefault(Integer index, Class<?> clazz, boolean isJoin, PrefixEnum prefixEnum) {
        if (prefixEnum == PrefixEnum.ON_FIRST) {
            return tableList.getPrefix(index, clazz, false);
        } else if (prefixEnum == PrefixEnum.ON_SECOND) {
            return isJoin ? tableList.getPrefixOther(index, clazz) : tableList.getPrefix(index, clazz, false);
        } else if (prefixEnum == PrefixEnum.CD_FIRST) {
            return tableList.getPrefix(index, clazz, false);
        } else if (prefixEnum == PrefixEnum.CD_SECOND) {
            return isJoin ? tableList.getPrefixOther(index, clazz) :
                    tableList.getPrefix(index, clazz, false);
        } else if (prefixEnum == PrefixEnum.CD_ON_FIRST) {
            return tableList.getPrefix(index, clazz, false);
        } else if (prefixEnum == PrefixEnum.CD_ON_SECOND) {
            return isJoin ? tableList.getPrefixOther(index, clazz) :
                    tableList.getPrefix(index, clazz, false);
        } else {
            return tableList.getAlias();
        }
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
        if (StrUtils.isBlank(from.getStringValue())) {
            StringBuilder value = new StringBuilder();
            for (Children wrapper : onWrappers) {
                if (StrUtils.isBlank(wrapper.from.getStringValue())) {
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
     * 内部调用, 不建议使用
     */
    @Override
    public Children join(String keyWord, Class<?> clazz, String tableAlias, BiConsumer<KtAbstractLambdaWrapper<?, ?>, Children> consumer) {
        Integer oldIndex = this.getIndex();
        int newIndex = tableIndex;
        TableInfo info = TableHelper.getAssert(clazz);
        Children instance = instance(newIndex, keyWord, clazz, info.getTableName());
        instance.isNo = true;
        instance.isMain = false;
        onWrappers.add(instance);
        if (StrUtils.isBlank(tableAlias)) {
            tableList.put(oldIndex, clazz, false, subTableAlias, newIndex);
            instance.alias = subTableAlias;
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
    public Children join(String keyWord, boolean condition, String joinSql,Object... args) {
        if (condition) {
            Children wrapper = instanceEmpty();
            wrapper.from.setStringValue(formatSqlMaybeWithParam(joinSql,args));
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
        if (StrUtils.isBlank(_sqlSegment)) {
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
        tableList = new TableList();
        tableList.setAlias(alias);
    }

    @Override
    public void clear() {
        super.clear();
        this.alias = ConfigProperties.tableAlias;
        this.resultMap = false;
        this.resultMapCollection = false;
        this.pageInfo = null;
        this.pageByMain = false;
        this.tableIndex = 1;
        this.dynamicTableName = false;
        this.tableFunc = null;
        this.logicDelType = ConfigProperties.logicDelType;
        this.from.toNull();
        this.hasAlias = false;
        this.keyWord = null;
        this.logicSql = true;
        this.onWrappers.clear();
    }
}
