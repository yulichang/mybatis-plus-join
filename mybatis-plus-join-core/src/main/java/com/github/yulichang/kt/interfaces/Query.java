package com.github.yulichang.kt.interfaces;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.github.yulichang.kt.segments.FuncArgs;
import com.github.yulichang.toolkit.KtUtils;
import com.github.yulichang.toolkit.MPJReflectionKit;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.toolkit.support.FieldCache;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import com.github.yulichang.wrapper.enums.DefaultFuncEnum;
import com.github.yulichang.wrapper.segments.*;
import kotlin.reflect.KProperty;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.query.Query}
 *
 * @author yulichang
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public interface Query<Children> extends Serializable {


    List<Select> getSelectColum();

    Children getChildren();

    Integer getIndex();

    boolean isHasAlias();

    String getAlias();


    /**
     * 过滤查询的字段信息(主键除外!)
     * 推荐使用 selectFilter(Class, Predicate) 含主键
     *
     * @param predicate 过滤方式
     * @return children
     * @see Query#selectFilter(Class, Predicate)
     */
    @Deprecated
    default Children select(Class<?> entityClass, Predicate<TableFieldInfo> predicate) {
        TableInfo info = TableHelper.getAssert(entityClass);
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(entityClass);
        info.getFieldList().stream().filter(predicate).collect(Collectors.toList()).forEach(
                i -> getSelectColum().add(new SelectNormal(cacheMap.get(i.getProperty()), getIndex(), isHasAlias(), getAlias())));
        return getChildren();
    }

    /**
     * 过滤查询的字段信息
     * <p>例1: 只要 java 字段名以 "test" 开头的             -> select(i -> i.getProperty().startsWith("test"))</p>
     * <p>例2: 只要 java 字段属性是 CharSequence 类型的     -> select(TableFieldInfo::isCharSequence)</p>
     * <p>例3: 只要 java 字段没有填充策略的                 -> select(i -> i.getFieldFill() == FieldFill.DEFAULT)</p>
     * <p>例4: 要全部字段                                   -> select(i -> true)</p>
     * <p>例5: 只要主键字段                                 -> select(i -> false)</p>
     *
     * @param predicate 过滤方式
     * @return children
     */
    default Children selectFilter(Class<?> entityClass, Predicate<SelectCache> predicate) {
        TableInfo info = TableHelper.getAssert(entityClass);
        List<SelectCache> cacheList = ColumnCache.getListField(entityClass);
        cacheList.stream().filter(predicate).collect(Collectors.toList()).forEach(
                i -> getSelectColum().add(new SelectNormal(i, getIndex(), isHasAlias(), getAlias())));
        return getChildren();
    }


    Children select(KProperty<?>... columns);

    /**
     * String 查询
     *
     * @param columns 列
     */
    default Children select(String... columns) {
        getSelectColum().addAll(Arrays.stream(columns).map(i ->
                new SelectString(i, null)).collect(Collectors.toList()));
        return getChildren();
    }

    /**
     * String 查询
     *
     * @param column 列
     */
    default Children selectAs(String column, KProperty<?> alias) {
        getSelectColum().add(new SelectString(column + Constants.AS + alias.getName(), alias.getName()));
        return getChildren();
    }

    /**
     * String 查询
     *
     * @param column 列
     */
    default Children selectAs(String index, KProperty<?> column, KProperty<?> alias) {
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(KtUtils.ref(column));
        SelectCache cache = cacheMap.get(column.getName());
        getSelectColum().add(new SelectString(
                index + Constants.DOT + cache.getColumn() + Constants.AS + alias.getName(), alias.getName()));
        return getChildren();
    }

    /**
     * 说明：
     * 比如我们需要查询用户表有10个字段，然而我们只需要3个就够了，用mybatis-plus提供的select<p />
     * 需要一个属性一个属性填入很不优雅，现在我们可以用selectAsClass(UserDO.class, UserVo.class)<p />
     * 即可按所需的UserVo返回，前提是UserVo.class中的属性必须是UserDO.class中存在的
     *
     * @param source 数据源实体类
     * @param tag    目标类
     * @return children
     */
    default Children selectAsClass(Class<?> source, Class<?> tag) {
        Map<String, SelectCache> normalMap = ColumnCache.getMapField(source);
        List<FieldCache> fieldList = MPJReflectionKit.getFieldList(tag);
        for (FieldCache cache : fieldList) {
            if (normalMap.containsKey(cache.getField().getName())) {
                SelectCache selectCache = normalMap.get(cache.getField().getName());
                getSelectColum().add(new SelectNormal(selectCache, getIndex(), isHasAlias(), getAlias()));
            }
        }
        return getChildren();
    }

    /**
     * ignore
     */
    default Children selectAs(KProperty<?> column, KProperty<?> alias) {
        return selectAs(column, alias.getName());
    }

    /**
     * 别名查询
     */
    default Children selectAs(KProperty<?> column, String alias) {
        Class<?> aClass = KtUtils.ref(column);
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(aClass);
        getSelectColum().add(new SelectAlias(cacheMap.get(column.getName()), getIndex(), alias, isHasAlias(), getAlias()));
        return getChildren();
    }


    /**
     * 查询实体类全部字段
     */
    default Children selectAll(Class<?> clazz) {
        getSelectColum().addAll(ColumnCache.getListField(clazz).stream().map(i ->
                new SelectNormal(i, getIndex(), isHasAlias(), getAlias())).collect(Collectors.toList()));
        return getChildren();
    }

    /**
     * 查询实体类全部字段
     */
    default Children selectAll(Class<?> clazz, String prefix) {
        getSelectColum().addAll(ColumnCache.getListField(clazz).stream().map(i ->
                new SelectNormal(i, getIndex(), true, prefix)).collect(Collectors.toList()));
        return getChildren();
    }

    /**
     * select sql 片段
     */
    String getSqlSelect();

    /**
     * 聚合函数查询
     * <p>
     * wrapper.selectFunc(() -> "COUNT(%s)", "t.id", "total");
     * <p>
     * lambda
     * wrapper.selectFunc(() -> "COUNT(%s)", UserDO::getId, UserDTO::getTotal);
     *
     * @param funcEnum 函数枚举 {@link DefaultFuncEnum}
     * @param column   函数作用的字段
     * @param alias    别名
     */
    default Children selectFunc(BaseFuncEnum funcEnum, Object column, String alias) {
        getSelectColum().add(new SelectFunc(alias, getIndex(), funcEnum, column.toString(), isHasAlias(), getAlias()));
        return getChildren();
    }

    default Children selectFunc(BaseFuncEnum funcEnum, KProperty<?> column, String alias) {
        Class<?> aClass = KtUtils.ref(column);
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(aClass);
        getSelectColum().add(new SelectFunc(cacheMap.get(column.getName()), getIndex(), alias, funcEnum, isHasAlias(), getAlias()));
        return getChildren();
    }

    default Children selectFunc(BaseFuncEnum funcEnum, KProperty<?> column, KProperty<?> alias) {
        return selectFunc(funcEnum, column, alias.getName());
    }

    default Children selectFunc(BaseFuncEnum funcEnum, KProperty<?> column) {
        return selectFunc(funcEnum, column, column);
    }

    default Children selectFunc(BaseFuncEnum funcEnum, Object column, KProperty<?> alias) {
        return selectFunc(funcEnum, column, alias.getName());
    }


    default Children selectFunc(String sql, Function<FuncArgs, SelectFunc.Arg[]> column, String alias) {
        getSelectColum().add(new SelectFunc(alias, getIndex(), () -> sql, column.apply(new FuncArgs()),
                isHasAlias(), getAlias()));
        return getChildren();
    }

    default Children selectFunc(String sql, Function<FuncArgs, SelectFunc.Arg[]> column, KProperty<?> alias) {
        getSelectColum().add(new SelectFunc(alias.getName(), getIndex(), () -> sql,
                column.apply(new FuncArgs()), isHasAlias(), getAlias()));
        return getChildren();
    }

    /* 默认聚合函数扩展 */

    /**
     * SUM()
     */
    default Children selectSum(KProperty<?> column) {
        return selectFunc(DefaultFuncEnum.SUM, column);
    }

    default Children selectSum(KProperty<?> column, KProperty<?> alias) {
        return selectFunc(DefaultFuncEnum.SUM, column, alias);
    }

    default Children selectSum(KProperty<?> column, String alias) {
        return selectFunc(DefaultFuncEnum.SUM, column, alias);
    }

    /**
     * COUNT()
     */
    default Children selectCount(KProperty<?> column) {
        return selectFunc(DefaultFuncEnum.COUNT, column);
    }

    default Children selectCount(Object column, KProperty<?> alias) {
        return selectFunc(DefaultFuncEnum.COUNT, column, alias);
    }

    default Children selectCount(Object column, String alias) {
        return selectFunc(DefaultFuncEnum.COUNT, column, alias);
    }

    default Children selectCount(KProperty<?> column, KProperty<?> alias) {
        return selectFunc(DefaultFuncEnum.COUNT, column, alias);
    }

    default Children selectCount(KProperty<?> column, String alias) {
        return selectFunc(DefaultFuncEnum.COUNT, column, alias);
    }

    /**
     * MAX()
     */
    default Children selectMax(KProperty<?> column) {
        return selectFunc(DefaultFuncEnum.MAX, column);
    }

    default Children selectMax(KProperty<?> column, KProperty<?> alias) {
        return selectFunc(DefaultFuncEnum.MAX, column, alias);
    }

    default Children selectMax(KProperty<?> column, String alias) {
        return selectFunc(DefaultFuncEnum.MAX, column, alias);
    }

    /**
     * MIN()
     */
    default Children selectMin(KProperty<?> column) {
        return selectFunc(DefaultFuncEnum.MIN, column);
    }

    default Children selectMin(KProperty<?> column, KProperty<?> alias) {
        return selectFunc(DefaultFuncEnum.MIN, column, alias);
    }

    default Children selectMin(KProperty<?> column, String alias) {
        return selectFunc(DefaultFuncEnum.MIN, column, alias);
    }

    /**
     * MIN()
     */
    default Children selectAvg(KProperty<?> column) {
        return selectFunc(DefaultFuncEnum.AVG, column);
    }

    default Children selectAvg(KProperty<?> column, KProperty<?> alias) {
        return selectFunc(DefaultFuncEnum.AVG, column, alias);
    }

    default Children selectAvg(KProperty<?> column, String alias) {
        return selectFunc(DefaultFuncEnum.AVG, column, alias);
    }

    /**
     * LEN()
     */
    default Children selectLen(KProperty<?> column) {
        return selectFunc(DefaultFuncEnum.LEN, column);
    }

    default Children selectLen(KProperty<?> column, KProperty<?> alias) {
        return selectFunc(DefaultFuncEnum.LEN, column, alias);
    }

    default Children selectLen(KProperty<?> column, String alias) {
        return selectFunc(DefaultFuncEnum.LEN, column, alias);
    }
}
