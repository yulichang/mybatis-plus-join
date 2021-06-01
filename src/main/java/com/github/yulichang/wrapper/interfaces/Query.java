package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.query.Query}
 *
 * @author yulichang
 */
public interface Query<Children> extends Serializable {

    /**
     * 设置查询字段
     *
     * @param columns 字段数组
     * @return children
     */
    @SuppressWarnings("unchecked")
    <E> Children select(SFunction<E, ?>... columns);

    /**
     * 过滤查询的字段信息(主键除外!)
     * <p>例1: 只要 java 字段名以 "test" 开头的             -> select(i -> i.getProperty().startsWith("test"))</p>
     * <p>例2: 只要 java 字段属性是 CharSequence 类型的     -> select(TableFieldInfo::isCharSequence)</p>
     * <p>例3: 只要 java 字段没有填充策略的                 -> select(i -> i.getFieldFill() == FieldFill.DEFAULT)</p>
     * <p>例4: 要全部字段                                   -> select(i -> true)</p>
     * <p>例5: 只要主键字段                                 -> select(i -> false)</p>
     *
     * @param predicate 过滤方式
     * @return children
     */
    <E> Children select(Class<E> entityClass, Predicate<TableFieldInfo> predicate);

    default <S, X> Children selectAs(SFunction<S, ?> columns, SFunction<X, ?> alias) {
        return selectAs(columns, LambdaUtils.getName(alias));
    }

    /**
     * 别名查询
     */
    <S> Children selectAs(SFunction<S, ?> column, String alias);


    default <S, X> Children selectFunc(BaseFuncEnum funcEnum, SFunction<S, ?> column) {
        return selectFunc(funcEnum, column, column);
    }


    default <S, X> Children selectFunc(BaseFuncEnum funcEnum, SFunction<S, ?> column, SFunction<X, ?> alias) {
        return selectFunc(funcEnum, column, LambdaUtils.getName(alias));
    }

    /**
     * 聚合函数查询
     *
     * @param funcEnum 函数枚举 {@link com.github.yulichang.wrapper.enums.DefaultFuncEnum}
     * @param column   函数作用的字段
     * @param alias    别名
     */
    <S> Children selectFunc(BaseFuncEnum funcEnum, SFunction<S, ?> column, String alias);

    default <X> Children selectFunc(BaseFuncEnum funcEnum, Object column, SFunction<X, ?> alias) {
        return selectFunc(funcEnum, column, LambdaUtils.getName(alias));
    }

    Children selectFunc(BaseFuncEnum funcEnum, Object column, String alias);

    /**
     * select sql 片段
     */
    String getSqlSelect();
}
