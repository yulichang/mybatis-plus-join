package com.github.yulichang.common;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.LambdaUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * copy {@link com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper}
 */
@SuppressWarnings("serial")
public abstract class JoinAbstractLambdaWrapper<T, Children extends JoinAbstractLambdaWrapper<T, Children>>
        extends JoinAbstractWrapper<T, Children> {

    private Map<String, ColumnCache> columnMap = null;
    private boolean initColumnMap = false;

    protected final Map<Class<?>, String> subTable = new HashMap<>();

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
        return getDefault(LambdaUtils.getEntityClass(column)) + StringPool.DOT +
                LambdaUtils.getColumn(column);
    }

    protected String getDefault(Class<?> clazz) {
        String alias = subTable.get(clazz);
        if (StringUtils.isNotBlank(alias)) {
            return alias;
        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        Assert.notNull(tableInfo, "can not find table");
        return tableInfo.getTableName();
    }

}
