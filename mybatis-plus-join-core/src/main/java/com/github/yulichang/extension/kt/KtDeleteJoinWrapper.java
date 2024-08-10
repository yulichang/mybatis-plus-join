package com.github.yulichang.extension.kt;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.toolkit.LogicInfoUtils;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.TableList;
import com.github.yulichang.wrapper.interfaces.DeleteChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author yulichang
 * @since 1.4.5
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public class KtDeleteJoinWrapper<T> extends KtAbstractLambdaWrapper<T, KtDeleteJoinWrapper<T>> implements DeleteChain<T> {

    /**
     * 删除表
     */
    private final SharedString deleteSql = new SharedString();

    /**
     * 删除的表
     */
    private List<Class<?>> deleteTableList;

    /**
     * 删除的表
     */
    private List<String> deleteTableName;

    /**
     * 是否删除主表以及所有副表
     */
    private boolean deleteAll = false;


    private KtDeleteJoinWrapper() {
        super();
    }

    /**
     * 推荐使用此构造方法
     */
    public KtDeleteJoinWrapper(Class<T> clazz) {
        super(clazz);
    }

    /**
     * 构造方法
     *
     * @param clazz 主表class类
     * @param alias 主表别名
     */
    public KtDeleteJoinWrapper(Class<T> clazz, String alias) {
        super(clazz, alias);
    }

    /**
     * 获取删除的表
     */
    @Override
    public String getDeleteSql() {
        if (StringUtils.isNotBlank(this.deleteSql.getStringValue())) {
            return this.deleteSql.getStringValue();
        }
        String delete = null;
        if (deleteAll) {
            check();
            List<String> tables = tableList.getAll().stream().map(i -> i.isHasAlias() ? i.getAlias() :
                    (i.getAlias() + i.getIndex())).collect(Collectors.toList());
            tables.add(0, this.alias);
            delete = String.join(StringPool.COMMA, tables);
        } else {
            if (CollectionUtils.isNotEmpty(deleteTableList)) {
                delete = deleteTableList.stream().map(c -> tableList.getPrefixByClassAssert(c)).collect(Collectors.joining(StringPool.COMMA));
            }
        }
        if (CollectionUtils.isNotEmpty(deleteTableName)) {
            delete = delete + StringPool.COMMA + String.join(StringPool.COMMA, deleteTableName);
        }
        if (StringUtils.isBlank(delete)) {
            delete = this.alias;
        }
        deleteSql.setStringValue(delete);
        return delete;
    }

    /**
     * 获取删除的表
     */
    @Override
    public String getDeleteLogicSql() {
        if (StringUtils.isNotBlank(this.deleteSql.getStringValue())) {
            return this.deleteSql.getStringValue();
        }
        String delete = null;
        if (deleteAll) {
            check();
            delete = tableList.getAll().stream().map(i -> LogicInfoUtils.getLogicInfoInvert(i.getIndex(), i.getClazz(),
                    i.isHasAlias(), i.getAlias())).collect(Collectors.joining(StringPool.COMMA));
        } else {
            if (CollectionUtils.isNotEmpty(deleteTableList)) {
                delete = deleteTableList.stream().map(c -> tableList.getByClassFirst(c)).map(i ->
                                LogicInfoUtils.getLogicInfoInvert(i.getIndex(), i.getClazz(), i.isHasAlias(), i.getAlias()))
                        .collect(Collectors.joining(StringPool.COMMA));
            }
        }
        if (CollectionUtils.isNotEmpty(deleteTableName)) {
            delete = delete + StringPool.COMMA + String.join(StringPool.COMMA, deleteTableName);
        }
        if (StringUtils.isNotBlank(delete)) {
            delete = StringPool.COMMA + delete;
        } else {
            delete = StringPool.EMPTY;
        }
        deleteSql.setStringValue(delete);
        return delete;
    }

    /**
     * 删除表
     */
    public KtDeleteJoinWrapper<T> deleteAll() {
        this.deleteAll = true;
        return typedThis;
    }

    /**
     * 删除表
     * 注意!!!
     * 字符串不支持逻辑删除校验
     * 也就算说此方法不管副表有没有逻辑删除 都按照主表的方式执行delete或update
     */
    public KtDeleteJoinWrapper<T> delete(String... tables) {
        if (CollectionUtils.isEmpty(deleteTableName)) {
            deleteTableName = new ArrayList<>();
            deleteTableName.addAll(Arrays.asList(tables));
        }
        return typedThis;
    }

    /**
     * 删除表
     */
    public KtDeleteJoinWrapper<T> delete(Class<?>... deleteClass) {
        Class<T> entityClass = getEntityClass();
        Assert.notNull(entityClass, "缺少主表类型, 请使用 new MPJLambdaWrapper<>(主表.class) 或 JoinWrappers.lambda(主表.class) 构造方法");
        if (CollectionUtils.isEmpty(deleteTableList)) {
            deleteTableList = new ArrayList<>();
        }
        check(Arrays.asList(deleteClass));
        deleteTableList.addAll(Arrays.asList(deleteClass));
        return typedThis;
    }

    private void check(List<Class<?>> classList) {
        Class<T> entityClass = getEntityClass();
        TableInfo tableInfo = TableHelper.getAssert(entityClass);
        //检查
        boolean mainLogic = AdapterHelper.getAdapter().mpjHasLogic(tableInfo);
        boolean check = classList.stream().allMatch(t -> {
            TableInfo ti = TableHelper.getAssert(t);
            return mainLogic == AdapterHelper.getAdapter().mpjHasLogic(ti);
        });
        if (!check) {
            throw ExceptionUtils.mpe("连表删除只适用于全部表(主表和副表)都是物理删除或全部都是逻辑删除, " +
                            "不支持同时存在物理删除和逻辑删除 [物理删除->(%s)] [逻辑删除->(%s)]",
                    classList.stream().filter(t -> !AdapterHelper.getAdapter().mpjHasLogic(TableHelper.getAssert(t)))
                            .map(Class::getSimpleName).collect(Collectors.joining(StringPool.COMMA)),
                    classList.stream().filter(t -> AdapterHelper.getAdapter().mpjHasLogic(TableHelper.getAssert(t)))
                            .map(Class::getSimpleName).collect(Collectors.joining(StringPool.COMMA)));
        }
    }

    private void check() {
        if (CollectionUtils.isNotEmpty(tableList.getAll())) {
            Class<T> entityClass = getEntityClass();
            Assert.notNull(entityClass, "缺少主表类型, 请使用 new MPJLambdaWrapper<>(主表.class) 或 JoinWrappers.lambda(主表.class) 构造方法");
            ArrayList<Class<?>> list = tableList.getAll().stream().map(TableList.Node::getClazz)
                    .collect(Collectors.toCollection(ArrayList::new));
            list.add(entityClass);
            check(list);
        }
    }

    /**
     * 用于生成嵌套 sql
     * <p>故 sqlSelect 不向下传递</p>
     */
    @Override
    protected KtDeleteJoinWrapper<T> instance() {
        return instance(index, null, null, null);
    }

    @Override
    protected KtDeleteJoinWrapper<T> instanceEmpty() {
        return new KtDeleteJoinWrapper<>();
    }

    @Override
    protected KtDeleteJoinWrapper<T> instance(Integer index, String keyWord, Class<?> joinClass, String tableName) {
        return new KtDeleteJoinWrapper<>(getEntity(), getEntityClass(), paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                this.tableList, index, keyWord, joinClass, tableName);
    }

    /**
     * 不建议直接 new 该实例，使用 JoinWrappers.delete(User.class)
     */
    public KtDeleteJoinWrapper(T entity, Class<T> entityClass, AtomicInteger paramNameSeq,
                               Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                               SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                               TableList tableList, Integer index, String keyWord, Class<?> joinClass, String tableName) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.tableList = tableList;
        this.index = index;
        this.keyWord = keyWord;
        this.joinClass = joinClass;
        this.tableName = tableName;
    }

    @Override
    public void clear() {
        super.clear();
        if (CollectionUtils.isNotEmpty(deleteTableList)) {
            deleteTableList.clear();
        }
        if (CollectionUtils.isNotEmpty(deleteTableName)) {
            deleteTableName.clear();
        }
        this.deleteSql.toNull();
        this.deleteAll = false;
    }
}
