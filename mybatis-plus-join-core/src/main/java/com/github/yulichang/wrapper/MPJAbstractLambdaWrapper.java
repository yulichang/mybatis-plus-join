package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.segments.SelectCache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper}
 *
 * @author yulichang
 */
public abstract class MPJAbstractLambdaWrapper<T, Children extends MPJAbstractLambdaWrapper<T, Children>>
        extends MPJAbstractWrapper<T, Children> {

    /**
     * 关联的表
     */
    protected TableList tableList = new TableList();
    /**
     * 表别名
     */
    @Getter
    protected String index;


    @Override
    protected <X> String columnToString(X column, boolean isJoin) {
        return columnToString((SFunction<?, ?>) column, isJoin);
    }

    @Override
    @SafeVarargs
    protected final <X> String columnsToString(boolean isJoin, X... columns) {
        return Arrays.stream(columns).map(i -> columnToString((SFunction<?, ?>) i, isJoin)).collect(joining(StringPool.COMMA));
    }

    protected String columnToString(SFunction<?, ?> column, boolean isJoin) {
        Class<?> entityClass = LambdaUtils.getEntityClass(column);
        return Constant.TABLE_ALIAS + getDefault(entityClass, isJoin) + StringPool.DOT +
                getCache(column).getTagColumn();
    }

    protected SelectCache getCache(SFunction<?, ?> fn) {
        Class<?> aClass = LambdaUtils.getEntityClass(fn);
        Map<String, SelectCache> cacheMap = ColumnCache.getMapField(aClass);
        return cacheMap.get(LambdaUtils.getName(fn));
    }

    protected String getDefault(Class<?> clazz, boolean isJoin) {
        Table table = tableList.get(clazz, index);
        if (Objects.nonNull(table.getIndex())) {
            if (getEntityClass() == null) {
                return table.getIndex();
            }
            if (isJoin && joinClass == getEntityClass()) {
                return StringPool.EMPTY;
            }
            return table.getIndex();
        }
        return StringPool.EMPTY;
    }

    protected String getDefaultSelect(Class<?> clazz, boolean myself) {
        Table table = tableList.get(clazz, index);
        if (Objects.nonNull(table.getIndex())) {
            if (myself) {
                return StringPool.EMPTY;
            }
            return table.getIndex();
        }
        return StringPool.EMPTY;
    }

    public static class TableList {

        private static final Table DEFAULT_TABLE = new Table(null, null);

        private final List<Table> list = new ArrayList<>();

        public void add(Class<?> clazz, String index) {
            this.list.add(new Table(clazz, index));
        }

        private Table get(Class<?> clazz) {
            for (Table t : list) {
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
            for (Table t : list) {
                if (clazz == t.clazz && Objects.equals(index, t.getIndex())) {
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
    }

    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class Table {
        private Class<?> clazz;

        private String index;
    }
}
