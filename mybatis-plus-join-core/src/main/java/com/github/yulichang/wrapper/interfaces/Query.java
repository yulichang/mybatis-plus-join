package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.Asserts;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.MPJReflectionKit;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.toolkit.support.FieldCache;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import com.github.yulichang.wrapper.enums.DefaultFuncEnum;
import com.github.yulichang.wrapper.segments.*;

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
@SuppressWarnings("unused")
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
    default <E> Children select(Class<E> entityClass, Predicate<TableFieldInfo> predicate) {
        TableInfo info = TableHelper.get(entityClass);
        Asserts.hasTable(info, entityClass);
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
    default <E> Children selectFilter(Class<E> entityClass, Predicate<SelectCache> predicate) {
        TableInfo info = TableHelper.get(entityClass);
        Asserts.hasTable(info, entityClass);
        List<SelectCache> cacheList = ColumnCache.getListField(entityClass);
        cacheList.stream().filter(predicate).collect(Collectors.toList()).forEach(
                i -> getSelectColum().add(new SelectNormal(i, getIndex(), isHasAlias(), getAlias())));
        return getChildren();
    }


    @SuppressWarnings("unchecked")
    <E> Children select(SFunction<E, ?>... columns);

    /**
     * String 查询
     *
     * @param columns 列
     */
    default Children select(String... columns) {
        getSelectColum().addAll(Arrays.stream(columns).map(i -> new SelectString(i, isHasAlias(), getAlias())).collect(Collectors.toList()));
        return getChildren();
    }

    /**
     * String 查询
     *
     * @param column 列
     */
    default <E> Children selectAs(String column, SFunction<E, ?> alias) {
        getSelectColum().add(new SelectString(column + Constants.AS + LambdaUtils.getName(alias), isHasAlias(), getAlias()));
        return getChildren();
    }

    /**
     * String 查询
     *
     * @param column 列
     */
    default <E, X> Children selectAs(String index, SFunction<E, ?> column, SFunction<X, ?> alias) {
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(LambdaUtils.getEntityClass(column));
        SelectCache cache = cacheMap.get(LambdaUtils.getName(column));
        getSelectColum().add(new SelectString(
                index + Constants.DOT + cache.getColumn() + Constants.AS + LambdaUtils.getName(alias),
                isHasAlias(), getAlias()));
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
    default <E> Children selectAsClass(Class<E> source, Class<?> tag) {
        List<SelectCache> normalList = ColumnCache.getListField(source);
        Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(tag);
        for (SelectCache cache : normalList) {
            if (fieldMap.containsKey(cache.getColumProperty())) {
                getSelectColum().add(new SelectNormal(cache, getIndex(), isHasAlias(), getAlias()));
            }
        }
        return getChildren();
    }

    /**
     * ignore
     */
    default <S, X> Children selectAs(SFunction<S, ?> column, SFunction<X, ?> alias) {
        return selectAs(column, LambdaUtils.getName(alias));
    }

    /**
     * 别名查询
     */
    default <S> Children selectAs(SFunction<S, ?> column, String alias) {
        Class<?> aClass = LambdaUtils.getEntityClass(column);
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(aClass);
        getSelectColum().add(new SelectAlias(cacheMap.get(LambdaUtils.getName(column)), getIndex(), alias, isHasAlias(), getAlias()));
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
     * @param funcEnum 函数枚举 {@link com.github.yulichang.wrapper.enums.DefaultFuncEnum}
     * @param column   函数作用的字段
     * @param alias    别名
     */
    default Children selectFunc(BaseFuncEnum funcEnum, Object column, String alias) {
        getSelectColum().add(new SelectFunc(alias, getIndex(), funcEnum, column.toString(), isHasAlias(), getAlias()));
        return getChildren();
    }

    default <S> Children selectFunc(BaseFuncEnum funcEnum, SFunction<S, ?> column, String alias) {
        Class<?> aClass = LambdaUtils.getEntityClass(column);
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(aClass);
        getSelectColum().add(new SelectFunc(cacheMap.get(LambdaUtils.getName(column)), getIndex(), alias, funcEnum, isHasAlias(), getAlias()));
        return getChildren();
    }

    default <S, X> Children selectFunc(BaseFuncEnum funcEnum, SFunction<S, ?> column, SFunction<X, ?> alias) {
        return selectFunc(funcEnum, column, LambdaUtils.getName(alias));
    }

    default <S> Children selectFunc(BaseFuncEnum funcEnum, SFunction<S, ?> column) {
        return selectFunc(funcEnum, column, column);
    }

    default <X> Children selectFunc(BaseFuncEnum funcEnum, Object column, SFunction<X, ?> alias) {
        return selectFunc(funcEnum, column, LambdaUtils.getName(alias));
    }


    default <X> Children selectFunc(String sql, Function<SelectFunc.Func, SFunction<?, ?>[]> column, String alias) {
        getSelectColum().add(new SelectFunc(alias, getIndex(), () -> sql, column.apply(new SelectFunc.Func()),
                isHasAlias(), getAlias()));
        return getChildren();
    }

    default <X, S> Children selectFunc(String sql, Function<SelectFunc.Func, SFunction<?, ?>[]> column, SFunction<S, ?> alias) {
        getSelectColum().add(new SelectFunc(LambdaUtils.getName(alias), getIndex(), () -> sql,
                column.apply(new SelectFunc.Func()), isHasAlias(), getAlias()));
        return getChildren();
    }

    /* 默认聚合函数扩展 */

    /**
     * SUM()
     */
    default <S> Children selectSum(SFunction<S, ?> column) {
        return selectFunc(DefaultFuncEnum.SUM, column);
    }

    default <S, X> Children selectSum(SFunction<S, ?> column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.SUM, column, alias);
    }

    default <S, X> Children selectSum(SFunction<S, ?> column, String alias) {
        return selectFunc(DefaultFuncEnum.SUM, column, alias);
    }

    /**
     * COUNT()
     */
    default <S> Children selectCount(SFunction<S, ?> column) {
        return selectFunc(DefaultFuncEnum.COUNT, column);
    }

    default <X> Children selectCount(Object column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.COUNT, column, alias);
    }

    default Children selectCount(Object column, String alias) {
        return selectFunc(DefaultFuncEnum.COUNT, column, alias);
    }

    default <S, X> Children selectCount(SFunction<S, ?> column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.COUNT, column, alias);
    }

    default <S, X> Children selectCount(SFunction<S, ?> column, String alias) {
        return selectFunc(DefaultFuncEnum.COUNT, column, alias);
    }

    /**
     * MAX()
     */
    default <S> Children selectMax(SFunction<S, ?> column) {
        return selectFunc(DefaultFuncEnum.MAX, column);
    }

    default <S, X> Children selectMax(SFunction<S, ?> column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.MAX, column, alias);
    }

    default <S, X> Children selectMax(SFunction<S, ?> column, String alias) {
        return selectFunc(DefaultFuncEnum.MAX, column, alias);
    }

    /**
     * MIN()
     */
    default <S> Children selectMin(SFunction<S, ?> column) {
        return selectFunc(DefaultFuncEnum.MIN, column);
    }

    default <S, X> Children selectMin(SFunction<S, ?> column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.MIN, column, alias);
    }

    default <S, X> Children selectMin(SFunction<S, ?> column, String alias) {
        return selectFunc(DefaultFuncEnum.MIN, column, alias);
    }

    /**
     * MIN()
     */
    default <S> Children selectAvg(SFunction<S, ?> column) {
        return selectFunc(DefaultFuncEnum.AVG, column);
    }

    default <S, X> Children selectAvg(SFunction<S, ?> column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.AVG, column, alias);
    }

    default <S, X> Children selectAvg(SFunction<S, ?> column, String alias) {
        return selectFunc(DefaultFuncEnum.AVG, column, alias);
    }

    /**
     * LEN()
     */
    default <S> Children selectLen(SFunction<S, ?> column) {
        return selectFunc(DefaultFuncEnum.LEN, column);
    }

    default <S, X> Children selectLen(SFunction<S, ?> column, SFunction<X, ?> alias) {
        return selectFunc(DefaultFuncEnum.LEN, column, alias);
    }

    default <S, X> Children selectLen(SFunction<S, ?> column, String alias) {
        return selectFunc(DefaultFuncEnum.LEN, column, alias);
    }
}
