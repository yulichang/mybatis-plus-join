package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.MyLambdaUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper}
 */
@SuppressWarnings("serial")
public abstract class MyAbstractLambdaWrapper<T, Children extends MyAbstractLambdaWrapper<T, Children>>
        extends MyAbstractWrapper<T, Children> {

    private Map<String, ColumnCache> columnMap = null;
    private boolean initColumnMap = false;

    /**
     * 关联的表
     */
    protected final Map<Class<?>, Integer> subTable = new HashMap<>();

    @SuppressWarnings("unchecked")
    protected <X> String columnsToString(SFunction<X, ?>... columns) {
        return columnsToString(true, columns);
    }

    @Override
    protected <X> String columnToString(X column) {
        return columnToString((SFunction<?, ?>) column, true);
    }

    @SuppressWarnings("unchecked")
    protected <X> String columnsToString(boolean onlyColumn, SFunction<X, ?>... columns) {
        return Arrays.stream(columns).map(i -> columnToString(i, onlyColumn)).collect(joining(StringPool.COMMA));
    }

    @Override
    protected <X> String columnsToString(X... columns) {
        return Arrays.stream(columns).map(i -> columnToString((SFunction<?, ?>) i, true)).collect(joining(StringPool.COMMA));
    }

    protected String columnToString(SFunction<?, ?> column, boolean onlyColumn) {
        return Constant.TABLE_ALIAS + getDefault(subTable.get(MyLambdaUtils.getEntityClass(column))) + StringPool.DOT +
                MyLambdaUtils.getColumn(column);
    }

    protected String getDefault(Integer i) {
        if (Objects.nonNull(i)) {
            return i.toString();
        }
        return StringPool.SPACE;
    }

}
