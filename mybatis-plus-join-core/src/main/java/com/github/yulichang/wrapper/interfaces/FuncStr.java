/*
 * Copyright (c) 2011-2022, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.interfaces.Func}
 *
 * @since 1.3.12
 */
@SuppressWarnings("unused")
public interface FuncStr<Children> extends Serializable {

    /**
     * ignore
     */
    default Children isNull(String column) {
        return isNull(true, column);
    }

    /**
     * 字段 IS NULL
     * <p>例: isNull("name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    Children isNull(boolean condition, String column);

    /**
     * ignore
     */
    default Children isNotNull(String column) {
        return isNotNull(true, column);
    }

    /**
     * 字段 IS NOT NULL
     * <p>例: isNotNull("name")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    Children isNotNull(boolean condition, String column);

    /**
     * ignore
     */
    default Children in(String column, Collection<?> coll) {
        return in(true, column, coll);
    }

    /**
     * 字段 IN (value.get(0), value.get(1), ...)
     * <p>例: in("id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * <li> 注意！集合为空若存在逻辑错误，请在 condition 条件中判断 </li>
     * <li> 如果集合为 empty 则不会进行 sql 拼接 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    Children in(boolean condition, String column, Collection<?> coll);

    /**
     * ignore
     */
    default Children in(String column, Object... values) {
        return in(true, column, values);
    }

    /**
     * 字段 IN (v0, v1, ...)
     * <p>例: in("id", 1, 2, 3, 4, 5)</p>
     *
     * <li> 注意！数组为空若存在逻辑错误，请在 condition 条件中判断 </li>
     * <li> 如果动态数组为 empty 则不会进行 sql 拼接 </li>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    Children in(boolean condition, String column, Object... values);


    /**
     * 仅在集合非空的情况下添加条件
     * <li> 注意！仅对集合做非空判断，不对集合元素做判断</li>
     *
     * @param column 字段
     * @param coll   数据集合
     * @return children
     */
    default Children inIfNotEmpty(String column, Collection<?> coll) {
        return in(CollectionUtils.isNotEmpty(coll), column, coll);
    }


    /**
     * ignore
     */
    default Children notIn(String column, Collection<?> coll) {
        return notIn(true, column, coll);
    }

    /**
     * 字段 NOT IN (value.get(0), value.get(1), ...)
     * <p>例: notIn("id", Arrays.asList(1, 2, 3, 4, 5))</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param coll      数据集合
     * @return children
     */
    Children notIn(boolean condition, String column, Collection<?> coll);

    /**
     * ignore
     */
    default Children notIn(String column, Object... value) {
        return notIn(true, column, value);
    }

    /**
     * 字段 NOT IN (v0, v1, ...)
     * <p>例: notIn("id", 1, 2, 3, 4, 5)</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param values    数据数组
     * @return children
     */
    Children notIn(boolean condition, String column, Object... values);


    /**
     * 仅在集合非空的情况下添加条件
     * <li> 注意！仅对集合做非空判断，不对集合元素做判断</li>
     *
     * @param column 字段
     * @param coll   数据集合
     * @return children
     */
    default Children notInIfNotEmpty(String column, Collection<?> coll) {
        return notIn(CollectionUtils.isNotEmpty(coll), column, coll);
    }

    /**
     * ignore
     */
    default Children inSql(String column, String inValue) {
        return inSql(true, column, inValue);
    }

    /**
     * 字段 IN ( sql语句 )
     * <p>!! sql 注入方式的 in 方法 !!</p>
     * <p>例1: inSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例2: inSql("id", "select id from table where id &lt; 3")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    Children inSql(boolean condition, String column, String inValue);

    /**
     * 字段 &gt; ( sql语句 )
     * <p>例1: gtSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: gtSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    Children gtSql(boolean condition, String column, String inValue);

    /**
     * ignore
     */
    default Children gtSql(String column, String inValue) {
        return gtSql(true, column, inValue);
    }

    /**
     * 字段 >= ( sql语句 )
     * <p>例1: geSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: geSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    Children geSql(boolean condition, String column, String inValue);

    /**
     * ignore
     */
    default Children geSql(String column, String inValue) {
        return geSql(true, column, inValue);
    }

    /**
     * 字段 &lt; ( sql语句 )
     * <p>例1: ltSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: ltSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    Children ltSql(boolean condition, String column, String inValue);

    /**
     * ignore
     */
    default Children ltSql(String column, String inValue) {
        return ltSql(true, column, inValue);
    }

    /**
     * 字段 <= ( sql语句 )
     * <p>例1: leSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: leSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    Children leSql(boolean condition, String column, String inValue);

    /**
     * ignore
     */
    default Children eqSql(String column, String inValue) {
        return eqSql(true, column, inValue);
    }

    /**
     * 字段 = ( sql语句 )
     * <p>例1: eqSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例1: eqSql("id", "select id from table where name = 'JunJun'")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句
     * @return children
     */
    Children eqSql(boolean condition, String column, String inValue);

    /**
     * ignore
     */
    default Children leSql(String column, String inValue) {
        return leSql(true, column, inValue);
    }

    /**
     * ignore
     */
    default Children notInSql(String column, String inValue) {
        return notInSql(true, column, inValue);
    }

    /**
     * 字段 NOT IN ( sql语句 )
     * <p>!! sql 注入方式的 not in 方法 !!</p>
     * <p>例1: notInSql("id", "1, 2, 3, 4, 5, 6")</p>
     * <p>例2: notInSql("id", "select id from table where id &lt; 3")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @param inValue   sql语句 ---&gt; 1,2,3,4,5,6 或者 select id from table where id &lt; 3
     * @return children
     */
    Children notInSql(boolean condition, String column, String inValue);

    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: groupBy("id")</p>
     *
     * @param condition 执行条件
     * @param column    单个字段
     * @return children
     */
    Children groupBy(boolean condition, String column);

    default Children groupBy(String column) {
        return groupBy(true, column);
    }

    /**
     * 分组：GROUP BY 字段, ...
     * <p>例: groupBy(Arrays.asList("id", "name"))</p>
     *
     * @param condition 执行条件
     * @param columns   字段数组
     * @return children
     */
    Children groupByStr(boolean condition, List<String> columns);

    default Children groupByStr(List<String> columns) {
        return groupByStr(true, columns);
    }

    default Children groupBy(String column, String... columns) {
        return groupBy(true, column, columns);
    }

    /**
     * 分组：GROUP BY 字段, ...
     */
    Children groupBy(boolean condition, String column, String... columns);

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: orderByAsc(true, "id")</p>
     *
     * @param condition 执行条件
     * @param column    单个字段
     * @return children
     */
    default Children orderByAsc(boolean condition, String column) {
        return orderBy(condition, true, column);
    }

    default Children orderByAsc(String column) {
        return orderByAsc(true, column);
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     * <p>例: orderByAsc(true, Arrays.asList("id", "name"))</p>
     *
     * @param condition 执行条件
     * @param columns   字段数组
     * @return children
     */
    default Children orderByAscStr(boolean condition, List<String> columns) {
        return orderByStr(condition, true, columns);
    }

    default Children orderByAscStr(List<String> columns) {
        return orderByAscStr(true, columns);
    }

    default Children orderByAsc(String column, String... columns) {
        return orderByAsc(true, column, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... ASC
     */
    default Children orderByAsc(boolean condition, String column, String... columns) {
        return orderBy(condition, true, column, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: orderByDesc(true, "id")</p>
     *
     * @param condition 执行条件
     * @param column    字段
     * @return children
     */
    default Children orderByDesc(boolean condition, String column) {
        return orderBy(condition, false, column);
    }

    default Children orderByDesc(String column) {
        return orderByDesc(true, column);
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     * <p>例: orderByDesc(true, Arrays.asList("id", "name"))</p>
     *
     * @param condition 执行条件
     * @param columns   字段列表
     * @return children
     */
    default Children orderByDescStr(boolean condition, List<String> columns) {
        return orderByStr(condition, false, columns);
    }

    default Children orderByDescStr(List<String> columns) {
        return orderByDescStr(true, columns);
    }

    default Children orderByDesc(String column, String... columns) {
        return orderByDesc(true, column, columns);
    }

    /**
     * 排序：ORDER BY 字段, ... DESC
     */
    default Children orderByDesc(boolean condition, String column, String... columns) {
        return orderBy(condition, false, column, columns);
    }

    /**
     * 排序：ORDER BY 字段, ...
     * <p>例: orderBy(true, "id")</p>
     *
     * @param condition 执行条件
     * @param isAsc     是否是 ASC 排序
     * @param column    单个字段
     * @return children
     */
    Children orderBy(boolean condition, boolean isAsc, String column);

    /**
     * 排序：ORDER BY 字段, ...
     * <p>例: orderBy(true, Arrays.asList("id", "name"))</p>
     *
     * @param condition 执行条件
     * @param isAsc     是否是 ASC 排序
     * @param columns   字段列表
     * @return children
     */
    Children orderByStr(boolean condition, boolean isAsc, List<String> columns);

    /**
     * 排序：ORDER BY 字段, ...
     */
    Children orderBy(boolean condition, boolean isAsc, String column, String... columns);

}
