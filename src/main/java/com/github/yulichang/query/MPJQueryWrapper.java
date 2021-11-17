package com.github.yulichang.query;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.query.interfaces.MPJJoin;
import com.github.yulichang.toolkit.Constant;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
 * 推荐使用 Wrappers.<UserDO>queryJoin();构造
 *
 * @author yulichang
 * @see com.github.yulichang.toolkit.Wrappers
 */
@SuppressWarnings("unused")
public class MPJQueryWrapper<T> extends AbstractWrapper<T, String, MPJQueryWrapper<T>>
        implements Query<MPJQueryWrapper<T>, T, String>, MPJJoin<MPJQueryWrapper<T>> {

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
    private String alias = Constant.TABLE_ALIAS;

    /**
     * 查询的列
     */
    private List<String> selectColumns = new ArrayList<>();

    /**
     * 排除的字段
     */
    private List<String> ignoreColumns = new ArrayList<>();


    public MPJQueryWrapper() {
        super.initNeed();
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     *
     * @param entityClass 本不应该需要的
     */
    public MPJQueryWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq,
                           Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                           SharedString sqlSelect, SharedString from, SharedString lastSql,
                           SharedString sqlComment, SharedString sqlFirst,
                           List<String> selectColumns, List<String> ignoreColumns) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.from = from;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.selectColumns = selectColumns;
        this.ignoreColumns = ignoreColumns;
    }

    /**
     * 设置表别名(默认为 t 可以调用此方法修改,调用后才生效，所以最好第一个调用)
     */
    public final MPJQueryWrapper<T> alias(String tableAlias) {
        this.alias = tableAlias;
        return typedThis;
    }

    @Override
    public MPJQueryWrapper<T> select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            selectColumns.addAll(Arrays.asList(columns));
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
    public MPJQueryWrapper<T> selectIgnore(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            ignoreColumns.addAll(Arrays.asList(columns));
        }
        return typedThis;
    }

    /**
     * 此方法只能用于主表
     * 不好含主键
     *
     * @param entityClass 主表class
     * @param predicate   条件lambda
     */
    @Override
    public MPJQueryWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        TableInfo info = TableInfoHelper.getTableInfo(entityClass);
        Assert.notNull(info, "can not find table info");
        selectColumns.addAll(info.getFieldList().stream().filter(predicate).map(c ->
                this.alias + StringPool.DOT + c.getColumn()).collect(Collectors.toList()));
        return typedThis;
    }


    /**
     * 查询主表全部字段
     *
     * @param clazz 主表class
     */
    public final MPJQueryWrapper<T> selectAll(Class<T> clazz) {
        selectAll(clazz, this.alias);
        return typedThis;
    }

    /**
     * 查询指定实体全部字段
     *
     * @param as 实体对应的别名
     */
    @SuppressWarnings({"DuplicatedCode", "UnusedReturnValue"})
    public final MPJQueryWrapper<T> selectAll(Class<?> clazz, String as) {
        TableInfo info = TableInfoHelper.getTableInfo(clazz);
        Assert.notNull(info, "can not find table info");
        if (info.havePK()) {
            selectColumns.add(as + StringPool.DOT + info.getKeyColumn());
        }
        selectColumns.addAll(info.getFieldList().stream().filter(TableFieldInfo::isSelect).map(i ->
                as + StringPool.DOT + i.getColumn()).collect(Collectors.toList()));
        return typedThis;
    }

    @Override
    public String getSqlSelect() {
        if (StringUtils.isBlank(sqlSelect.getStringValue())) {
            if (CollectionUtils.isNotEmpty(ignoreColumns)) {
                selectColumns.removeIf(ignoreColumns::contains);
            }
            sqlSelect.setStringValue(String.join(StringPool.COMMA, selectColumns));
        }
        return sqlSelect.getStringValue();
    }


    public String getFrom() {
        return from.getStringValue();
    }

    public boolean getAutoAlias() {
        return false;
    }

    public String getAlias() {
        return alias;
    }

    /**
     * 返回一个支持 lambda 函数写法的 wrapper
     */
    @SuppressWarnings("deprecation")
    public MPJLambdaQueryWrapper<T> lambda() {
        return new MPJLambdaQueryWrapper<>(getEntity(), getEntityClass(), from, sqlSelect, paramNameSeq, paramNameValuePairs,
                expression, lastSql, sqlComment, sqlFirst, selectColumns, ignoreColumns);
    }

    /**
     * 用于生成嵌套 sql
     * <p>故 sqlSelect selectColumn ignoreColumns from不向下传递</p>
     */
    @Override
    protected MPJQueryWrapper<T> instance() {
        return new MPJQueryWrapper<>(getEntity(), getEntityClass(), paramNameSeq, paramNameValuePairs, new MergeSegments(),
                null, null, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(), null, null);
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
        from.toNull();
        selectColumns.clear();
        ignoreColumns.clear();
    }

    @Override
    public MPJQueryWrapper<T> join(String keyWord, boolean condition, String joinSql) {
        if (condition) {
            from.setStringValue(from.getStringValue() + keyWord + joinSql);
        }
        return typedThis;
    }
}
