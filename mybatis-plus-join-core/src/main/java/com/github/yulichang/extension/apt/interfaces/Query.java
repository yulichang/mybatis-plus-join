package com.github.yulichang.extension.apt.interfaces;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.apt.BaseColumn;
import com.github.yulichang.apt.Column;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.MPJReflectionKit;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.toolkit.support.FieldCache;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import com.github.yulichang.wrapper.enums.DefaultFuncEnum;
import com.github.yulichang.wrapper.segments.*;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.query.Query}
 *
 * @author yulichang
 */
@SuppressWarnings({"unused"})
public interface Query<Children> extends Serializable {

    List<Select> getSelectColum();

    Children getChildren();


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
    default <E> Children selectFilter(BaseColumn<E> baseColumn, Predicate<SelectCache> predicate) {
        TableInfo info = TableHelper.getAssert(baseColumn.getColumnClass());
        List<SelectCache> cacheList = ColumnCache.getListField(baseColumn.getColumnClass());
        cacheList.stream().filter(SelectCache::isSelect).filter(predicate).collect(Collectors.toList()).forEach(
                i -> getSelectColum().add(new SelectApt(i, baseColumn)));
        return getChildren();
    }


    <E> Children select(Column... columns);

    /**
     * String 查询
     *
     * @param columns 列
     */
    default Children select(String... columns) {
        getSelectColum().add(new SelectString(String.join(StringPool.COMMA, columns), null));
        return getChildren();
    }

    /**
     * String 查询
     *
     * @param column 列
     */
    default <E> Children selectAs(String column, SFunction<E, ?> alias) {
        String name = LambdaUtils.getName(alias);
        getSelectColum().add(new SelectString(column + Constant.AS + name, name));
        return getChildren();
    }

    /**
     * String 查询
     *
     * @param column 列
     */
    default <X> Children selectAs(String index, Column column, SFunction<X, ?> alias) {
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(column.getClazz());
        SelectCache cache = cacheMap.get(column.getProperty());
        String name = LambdaUtils.getName(alias);
        getSelectColum().add(new SelectApt(cache, column, name));
        return getChildren();
    }


    default <E> Children selectAsClass(BaseColumn<E> baseColumn, Class<?> tag) {
        Map<String, SelectCache> normalMap = ColumnCache.getMapField(baseColumn.getColumnClass());
        List<FieldCache> fieldList = MPJReflectionKit.getFieldList(tag);
        for (FieldCache cache : fieldList) {
            if (normalMap.containsKey(cache.getField().getName())) {
                SelectCache selectCache = normalMap.get(cache.getField().getName());
                if (selectCache.isSelect()) {
                    getSelectColum().add(new SelectApt(selectCache, baseColumn));
                }
            }
        }
        return getChildren();
    }

    /**
     * ignore
     */
    default <X> Children selectAs(Column column, SFunction<X, ?> alias) {
        return selectAs(column, LambdaUtils.getName(alias));
    }

    /**
     * 别名查询
     */
    default <S> Children selectAs(Column column, String alias) {
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(column.getClazz());
        SelectCache cache = cacheMap.get(column.getProperty());
        getSelectColum().add(new SelectApt(cache, column, alias));
        return getChildren();
    }


    /**
     * 查询实体类全部字段
     */
    default <E> Children selectAll(BaseColumn<E> baseColumn) {
        getSelectColum().addAll(ColumnCache.getListField(baseColumn.getColumnClass()).stream().filter(SelectCache::isSelect).map(i ->
                new SelectApt(i, baseColumn)).collect(Collectors.toList()));
        return getChildren();
    }


    /**
     * 查询实体类全部字段
     *
     * @param baseColumn 查询的实体类
     * @param exclude    排除字段
     */
    default <E> Children selectAll(BaseColumn<E> baseColumn, Column... exclude) {
        Set<String> excludeSet = Arrays.stream(exclude).map(i ->
                i.getProperty().toUpperCase(Locale.ENGLISH)).collect(Collectors.toSet());
        getSelectColum().addAll(ColumnCache.getListField(baseColumn.getColumnClass()).stream().filter(e -> e.isSelect() &&
                !excludeSet.contains(e.getColumProperty().toUpperCase(Locale.ENGLISH))).map(i ->
                new SelectApt(i, baseColumn)).collect(Collectors.toList()));
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
    default <S> Children selectFunc(BaseFuncEnum funcEnum, Column column, String alias) {
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(column.getClazz());
        getSelectColum().add(new SelectApt(cacheMap.get(column.getProperty()), column, funcEnum, alias));
        return getChildren();
    }

    default <S, X> Children selectFunc(BaseFuncEnum funcEnum, Column column, SFunction<X, ?> alias) {
        return selectFunc(funcEnum, column, LambdaUtils.getName(alias));
    }

    default <S> Children selectFunc(BaseFuncEnum funcEnum, Column column) {
        return selectFunc(funcEnum, column, column.getProperty());
    }


    default <X> Children selectFunc(String sql, Function<AptConsumer, Column[]> column, String alias) {
        getSelectColum().add(new SelectApt(column.apply(AptConsumer.func), () -> sql, alias));
        return getChildren();
    }

    default <X, S> Children selectFunc(String sql, Function<AptConsumer, Column[]> column, SFunction<S, ?> alias) {
        return selectFunc(sql, column, LambdaUtils.getName(alias));
    }

    /* 默认聚合函数扩展 */

    /**
     * SUM()
     */
    default <S> Children selectSum(Column column) {
        return selectFunc(DefaultFuncEnum.SUM, column);
    }

    default <S, X> Children selectSum(Column column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.SUM, column, alias);
    }

    default <S, X> Children selectSum(Column column, String alias) {
        return selectFunc(DefaultFuncEnum.SUM, column, alias);
    }

    /**
     * COUNT()
     */
    default <S> Children selectCount(Column column) {
        return selectFunc(DefaultFuncEnum.COUNT, column);
    }

    default <S, X> Children selectCount(Column column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.COUNT, column, alias);
    }

    default <S, X> Children selectCount(Column column, String alias) {
        return selectFunc(DefaultFuncEnum.COUNT, column, alias);
    }

    /**
     * MAX()
     */
    default <S> Children selectMax(Column column) {
        return selectFunc(DefaultFuncEnum.MAX, column);
    }

    default <S, X> Children selectMax(Column column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.MAX, column, alias);
    }

    default <S, X> Children selectMax(Column column, String alias) {
        return selectFunc(DefaultFuncEnum.MAX, column, alias);
    }

    /**
     * MIN()
     */
    default <S> Children selectMin(Column column) {
        return selectFunc(DefaultFuncEnum.MIN, column);
    }

    default <S, X> Children selectMin(Column column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.MIN, column, alias);
    }

    default <S, X> Children selectMin(Column column, String alias) {
        return selectFunc(DefaultFuncEnum.MIN, column, alias);
    }

    /**
     * MIN()
     */
    default <S> Children selectAvg(Column column) {
        return selectFunc(DefaultFuncEnum.AVG, column);
    }

    default <S, X> Children selectAvg(Column column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.AVG, column, alias);
    }

    default <S, X> Children selectAvg(Column column, String alias) {
        return selectFunc(DefaultFuncEnum.AVG, column, alias);
    }

    /**
     * LEN()
     */
    default <S> Children selectLen(Column column) {
        return selectFunc(DefaultFuncEnum.LEN, column);
    }

    default <S, X> Children selectLen(Column column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.LEN, column, alias);
    }

    default <S, X> Children selectLen(Column column, String alias) {
        return selectFunc(DefaultFuncEnum.LEN, column, alias);
    }
}
