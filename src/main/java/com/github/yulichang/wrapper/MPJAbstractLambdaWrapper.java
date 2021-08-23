package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.metadata.MPJTableAliasHelper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.LambdaUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper}
 *
 * @author yulichang
 */
public abstract class MPJAbstractLambdaWrapper<T, Children extends MPJAbstractLambdaWrapper<T, Children>>
        extends MPJAbstractWrapper<T, Children> {
    /**
     * 缓存字段
     */
    protected Map<Class<?>, Map<String, ColumnCache>> columnMap = new HashMap<>();

    @Override
    protected <X> String columnToString(X column) {
        return columnToString((SFunction<?, ?>) column, hasAlias || entityClass !=
                LambdaUtils.getEntityClass((SFunction<?, ?>) column));
    }

    @Override
    protected <X> String columnToString(X column, boolean hasAlias) {
        return columnToString((SFunction<?, ?>) column, hasAlias);
    }

    @Override
    @SafeVarargs
    protected final <X> String columnsToString(X... columns) {
        return Arrays.stream(columns).map(i -> columnToString((SFunction<?, ?>) i)).collect(joining(StringPool.COMMA));
    }

    protected String columnToString(SFunction<?, ?> column, boolean hasAlias) {
        return (hasAlias ? MPJTableAliasHelper.get(LambdaUtils.getEntityClass(column)).getAliasDOT() :
                StringPool.EMPTY) + getCache(column).getColumn();
    }

    protected ColumnCache getCache(SFunction<?, ?> fn) {
        Class<?> aClass = LambdaUtils.getEntityClass(fn);
        Map<String, ColumnCache> cacheMap = columnMap.get(aClass);
        if (cacheMap == null) {
            cacheMap = LambdaUtils.getColumnMap(aClass);
            columnMap.put(aClass, cacheMap);
        }
        return cacheMap.get(LambdaUtils.formatKey(LambdaUtils.getName(fn)));
    }
}
