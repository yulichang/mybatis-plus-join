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
 *
 * @author yulichang
 */
@SuppressWarnings("serial")
public abstract class JoinAbstractLambdaWrapper<T, Children extends JoinAbstractLambdaWrapper<T, Children>>
        extends JoinAbstractWrapper<T, Children> {

    protected final Map<Class<?>, String> subTable = new HashMap<>();

    @Override
    protected <X> String columnToString(X column) {
        return columnToString((SFunction<?, ?>) column, true);
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
