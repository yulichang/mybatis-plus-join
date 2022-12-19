package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.segments.Select;
import com.github.yulichang.wrapper.segments.SelectCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper}
 *
 * @author yulichang
 */
@SuppressWarnings("DuplicatedCode")
public abstract class MPJAbstractLambdaWrapper<T, Children extends MPJAbstractLambdaWrapper<T, Children>>
        extends MPJAbstractWrapper<T, Children> {

    /**
     * 主表别名
     */
    protected String alias = ConfigProperties.tableAlias;
    /**
     * 是否构建是否存在一对多
     */
    @Getter
    protected boolean resultMap = false;
    /**
     * 表序号
     */
    protected int tableIndex = 1;
    /**
     * 关联的表
     */
    protected TableList tableList;


    @Override
    protected <X> String columnToString(String index, int node, X column, boolean isJoin, Class<?> parent) {
        return columnToString(index, node, (SFunction<?, ?>) column, isJoin, parent);
    }

    @Override
    @SafeVarargs
    protected final <X> String columnsToString(String index, int node, boolean isJoin, Class<?> parent, X... columns) {
        return Arrays.stream(columns).map(i -> columnToString(index, node, (SFunction<?, ?>) i, isJoin, parent)).collect(joining(StringPool.COMMA));
    }

    protected String columnToString(String index, int node, SFunction<?, ?> column, boolean isJoin, Class<?> parent) {
        Class<?> entityClass = LambdaUtils.getEntityClass(column);
        return getDefault(index, node, entityClass, isJoin, parent) + StringPool.DOT +
                getCache(column).getTagColumn();
    }

    protected SelectCache getCache(SFunction<?, ?> fn) {
        Class<?> aClass = LambdaUtils.getEntityClass(fn);
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(aClass);
        return cacheMap.get(LambdaUtils.getName(fn));
    }

    /**
     * 返回前缀
     */
    protected String getDefault(String index, int node, Class<?> clazz, boolean isJoin, Class<?> parent) {
        //外层where条件
        if (Objects.isNull(index)) {
            if (!isJoin && Objects.equals(clazz, getEntityClass())) {
                return this.alias;
            }
            //正序
            Table table = tableList.getPositive(clazz);
            return table.hasAlias ? table.alias : (table.alias + (Objects.isNull(table.index) ? StringPool.EMPTY : table.index));
        }
        Table table = tableList.get(clazz, index);
        if (table.hasAlias) {
            return table.alias;
        }
        if (Objects.nonNull(table.getIndex())) {
            if (isJoin && (Objects.equals(clazz, getEntityClass()) || Objects.equals(parent, clazz))) {
                if (node == -1) {
                    return table.alias;
                } else if (node == 0) {
                    //除自己以外的倒序第一个
                    Table t = tableList.getOrElse(clazz, index);
                    if (Objects.isNull(t.getIndex())) {
                        return t.alias;
                    }
                    return t.alias + t.getIndex();
                } else {
                    return table.alias + node;
                }
            }
            return table.alias + table.getIndex();
        }
        return table.alias;
    }

    protected String getDefaultSelect(String index, Class<?> clazz, Select s) {
        if (s.isLabel()) {
            if (Objects.nonNull(s.getIndex())) {
                return s.getIndex();
            } else {
                Table table = tableList.get(s.getClazz());
                return Objects.isNull(table.index) ? StringPool.EMPTY : table.index;
            }
        }
        //外层select
        if (Objects.isNull(index)) {
            if (Objects.equals(clazz, getEntityClass())) {
                return StringPool.EMPTY;
            }
            //正序
            Table table = tableList.getPositive(clazz);
            return Objects.isNull(table.index) ? StringPool.EMPTY : table.index;
        }
        Table table = tableList.get(clazz, index);
        if (Objects.nonNull(table.getIndex())) {
            return table.getIndex();
        }
        return StringPool.EMPTY;
    }

    public static class TableList {

        private final Table DEFAULT_TABLE;

        public TableList(Class<?> clazz, String index, String alias) {
            DEFAULT_TABLE = new Table(clazz, index, false, alias);
        }

        private final List<Table> list = new ArrayList<>();

        public void add(Class<?> clazz, String index, boolean hasAlias, String alias) {
            this.list.add(new Table(clazz, index, hasAlias, alias));
        }

        public Table get(Class<?> clazz) {
            if (list.isEmpty()) {
                return DEFAULT_TABLE;
            }
            for (int i = list.size() - 1; i >= 0; i--) {
                Table t = list.get(i);
                if (clazz == t.clazz) {
                    return t;
                }
            }
            return DEFAULT_TABLE;
        }


        public Table get(Class<?> clazz, String index) {
            if (Objects.isNull(index)) {
                return get(clazz);
            }
            if (list.isEmpty()) {
                return DEFAULT_TABLE;
            }
            //倒序
            for (int i = list.size() - 1; i >= 0; i--) {
                Table t = list.get(i);
                if (clazz == t.clazz && Objects.equals(index, t.getIndex())) {
                    return t;
                }
            }
            return get(clazz);
        }

        @SuppressWarnings("unused")
        public Table getPositive(Class<?> clazz, String index) {
            if (Objects.isNull(index)) {
                return get(clazz);
            }
            if (list.isEmpty()) {
                return DEFAULT_TABLE;
            }
            for (Table t : list) {
                if (clazz == t.clazz && Objects.equals(index, t.getIndex())) {
                    return t;
                }
            }
            return getPositive(clazz);
        }

        public Table getPositive(Class<?> clazz) {
            if (list.isEmpty()) {
                return DEFAULT_TABLE;
            }
            for (Table t : list) {
                if (clazz == t.clazz) {
                    return t;
                }
            }
            return DEFAULT_TABLE;
        }

        public Stream<Table> stream() {
            return list.stream();
        }

        public void clear() {
            list.clear();
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }

        public Table getOrElse(Class<?> clazz, String index) {
            if (Objects.isNull(index)) {
                return get(clazz);
            }
            //倒序
            for (int i = list.size() - 1; i >= 0; i--) {
                Table t = list.get(i);
                if (clazz == t.clazz) {
                    if (Objects.equals(index, t.getIndex())) {
                        continue;
                    }
                    if (Integer.parseInt(t.getIndex()) < Integer.parseInt(index)) {
                        return t;
                    }
                }
            }
            return DEFAULT_TABLE;
        }
    }

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class Table {
        private final Class<?> clazz;

        private final String index;

        private boolean hasAlias;

        private final String alias;
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
        node = ROOT_NODE;
        tableList = new TableList(getEntityClass(), null, alias);
    }
}
