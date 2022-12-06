package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.Constant;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.segments.SelectNormal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    protected Map<Class<?>, Integer> subTable = new HashMap<>();

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

    protected SelectNormal getCache(SFunction<?, ?> fn) {
        Class<?> aClass = LambdaUtils.getEntityClass(fn);
        Map<String, SelectNormal> cacheMap = ColumnCache.getMapField(aClass);
        return cacheMap.get(LambdaUtils.getName(fn));
    }

    protected String getDefault(Class<?> clazz, boolean isJoin) {
        Integer index = subTable.get(clazz);
        if (Objects.nonNull(index)) {
            if (getEntityClass() == null) {
                return index.toString();
            }
            if (isJoin && joinClass == getEntityClass()) {
                return StringPool.EMPTY;
            }
            return index.toString();
        }
        return StringPool.EMPTY;
    }

    protected String getDefaultSelect(Class<?> clazz, boolean myself) {
        Integer index = subTable.get(clazz);
        if (Objects.nonNull(index)) {
            if (myself) {
                return StringPool.EMPTY;
            }
            return index.toString();
        }
        return StringPool.EMPTY;
    }

}
