package com.github.mybatisplus.query;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.mybatisplus.query.interfaces.MyJoin;
import com.github.mybatisplus.toolkit.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.query.QueryWrapper}
 */
@SuppressWarnings("serial")
public class MyQueryWrapper<T> extends AbstractWrapper<T, String, MyQueryWrapper<T>>
        implements Query<MyQueryWrapper<T>, T, String>, MyJoin<MyQueryWrapper<T>> {

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
    private final SharedString alias = new SharedString(Constant.TABLE_ALIAS);


    public MyQueryWrapper() {
        this(null);
    }

    public MyQueryWrapper(T entity) {
        super.setEntity(entity);
        super.initNeed();
    }

    public MyQueryWrapper(T entity, String... columns) {
        super.setEntity(entity);
        super.initNeed();
        this.select(columns);
    }

    /**
     * 非对外公开的构造方法,只用于生产嵌套 sql
     *
     * @param entityClass 本不应该需要的
     */
    public MyQueryWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq,
                          Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                          SharedString sqlSelect, SharedString from, SharedString lastSql, SharedString sqlComment, SharedString sqlFirst) {
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
    }

    @Override
    public MyQueryWrapper<T> select(String... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            this.sqlSelect.setStringValue(String.join(StringPool.COMMA, columns));
        }
        return typedThis;
    }

    @Override
    public MyQueryWrapper<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
        super.setEntityClass(entityClass);
        this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(getEntityClass()).chooseSelect(predicate));
        return typedThis;
    }


    public final MyQueryWrapper<T> selectAll(Class<T> clazz) {
        TableInfo info = TableInfoHelper.getTableInfo(clazz);
        List<String> list = new ArrayList<>();
        list.add(Constant.TABLE_ALIAS + StringPool.DOT + info.getKeyColumn());
        list.addAll(info.getFieldList().stream().map(i -> Constant.TABLE_ALIAS + StringPool.DOT + i.getColumn()).collect(Collectors.toList()));
        String join = String.join(StringPool.COMMA, list);
        if (StringUtils.isBlank(sqlSelect.getStringValue())) {
            this.sqlSelect.setStringValue(join);
        } else {
            this.sqlSelect.setStringValue(this.getSqlSelect() + StringPool.COMMA + join);
        }
        return typedThis;
    }

    @Override
    public String getSqlSelect() {
        return sqlSelect.getStringValue();
    }


    public String getFrom() {
        return from.getStringValue();
    }

    public String getAlias() {
        return alias.getStringValue();
    }

    /**
     * 返回一个支持 lambda 函数写法的 wrapper
     */
    public MyLambdaQueryWrapper<T> lambda() {
        return new MyLambdaQueryWrapper<>(getEntity(), getEntityClass(), from, sqlSelect, paramNameSeq, paramNameValuePairs,
                expression, lastSql, sqlComment, sqlFirst);
    }

    /**
     * 用于生成嵌套 sql
     * <p>
     * 故 sqlSelect from 不向下传递
     * </p>
     */
    @Override
    protected MyQueryWrapper<T> instance() {
        return new MyQueryWrapper<>(getEntity(), getEntityClass(), paramNameSeq, paramNameValuePairs, new MergeSegments(),
                null, null, SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString());
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
    }

    @Override
    public MyQueryWrapper<T> join(String keyWord, boolean condition, String joinSql) {
        if (condition) {
            from.setStringValue(from.getStringValue() + keyWord + joinSql);
        }
        return typedThis;
    }
}
