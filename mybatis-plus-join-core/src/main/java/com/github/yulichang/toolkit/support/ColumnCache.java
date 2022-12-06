package com.github.yulichang.toolkit.support;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.github.yulichang.wrapper.segments.SelectNormal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * select缓存
 *
 * @author yulichang
 * @since 1.3.10
 */
public class ColumnCache {

    private static final Map<Class<?>, List<SelectNormal>> LIST_CACHE = new ConcurrentHashMap<>();

    private static final Map<Class<?>, Map<String, SelectNormal>> MAP_CACHE = new ConcurrentHashMap<>();

    public static List<SelectNormal> getListField(Class<?> clazz) {
        return LIST_CACHE.computeIfAbsent(clazz, c -> {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
            Assert.notNull(tableInfo, "table not find by class <%s>", c.getSimpleName());
            List<SelectNormal> list = new ArrayList<>();
            if (tableInfo.havePK()) {
                list.add(new SelectNormal(clazz, true, tableInfo.getKeyColumn(), tableInfo.getKeyType(), tableInfo.getKeyProperty(), null));
            }
            list.addAll(tableInfo.getFieldList().stream().map(f -> new SelectNormal(clazz, false, f.getColumn(), f.getPropertyType(), f.getProperty(), f)).collect(Collectors.toList()));
            return list;
        });
    }

    public static Map<String, SelectNormal> getMapField(Class<?> clazz) {
        return MAP_CACHE.computeIfAbsent(clazz, c -> getListField(c).stream().collect(Collectors.toMap(SelectNormal::getColumProperty, Function.identity(), (i, j) -> j)));
    }
}
