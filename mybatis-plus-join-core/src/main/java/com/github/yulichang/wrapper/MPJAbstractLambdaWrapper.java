package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.segments.Select;
import com.github.yulichang.wrapper.segments.SelectCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.*;
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
     * 关联的表
     */
    protected TableList tableList = new TableList();


    @Override
    protected <X> String columnToString(String index, X column, boolean isJoin) {
        return columnToString(index, (SFunction<?, ?>) column, isJoin);
    }

    @Override
    @SafeVarargs
    protected final <X> String columnsToString(String index, boolean isJoin, X... columns) {
        return Arrays.stream(columns).map(i -> columnToString(index, (SFunction<?, ?>) i, isJoin)).collect(joining(StringPool.COMMA));
    }

    protected String columnToString(String index, SFunction<?, ?> column, boolean isJoin) {
        Class<?> entityClass = LambdaUtils.getEntityClass(column);
        return Constant.TABLE_ALIAS + getDefault(index, entityClass, isJoin) + StringPool.DOT +
                getCache(column).getTagColumn();
    }

    protected SelectCache getCache(SFunction<?, ?> fn) {
        Class<?> aClass = LambdaUtils.getEntityClass(fn);
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(aClass);
        return cacheMap.get(LambdaUtils.getName(fn));
    }

    protected String getDefault(String index, Class<?> clazz, boolean isJoin) {
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
            if (isJoin) {
                //除自己以外的倒序第一个
                Table t = tableList.getOrElse(clazz, index);
                if (Objects.isNull(t.getIndex())) {
                    return StringPool.EMPTY;
                }
                return t.getIndex();
            }
            return table.getIndex();
        }
        return StringPool.EMPTY;
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

        private static final Table DEFAULT_TABLE = new Table(null, StringPool.EMPTY);

        private final List<Table> list = new ArrayList<>();

        public void add(Class<?> clazz, String index) {
            this.list.add(new Table(clazz, index));
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
    }
}
