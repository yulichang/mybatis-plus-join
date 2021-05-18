package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.LambdaUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.joining;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper}
 */
@SuppressWarnings("serial")
public abstract class MPJAbstractLambdaWrapper<T, Children extends MPJAbstractLambdaWrapper<T, Children>>
        extends MPJAbstractWrapper<T, Children> {

    /**
     * 关联的表
     */
    protected Map<Class<?>, Integer> subTable = new HashMap<>();

    private Map<String, ColumnCache> columnMap = null;
    private boolean initColumnMap = false;

    @Override
    protected <X> String columnToString(X column) {
        return columnToString((SFunction<?, ?>) column, true);
    }

    @Override
    protected <X> String columnsToString(X... columns) {
        return Arrays.stream(columns).map(i -> columnToString((SFunction<?, ?>) i, true)).collect(joining(StringPool.COMMA));
    }

    protected String columnToString(SFunction<?, ?> column, boolean onlyColumn) {
        if (!initColumnMap) {
            columnMap = com.baomidou.mybatisplus.core.toolkit.LambdaUtils.getColumnMap(LambdaUtils.getEntityClass(column));
            initColumnMap = true;
        }
        return Constant.TABLE_ALIAS + getDefault(subTable.get(LambdaUtils.getEntityClass(column))) + StringPool.DOT +
                columnMap.get(LambdaUtils.getName(column)).getColumn();
    }

    protected String getDefault(Integer i) {
        if (Objects.nonNull(i)) {
            return i.toString();
        }
        return StringPool.EMPTY;
    }

}
