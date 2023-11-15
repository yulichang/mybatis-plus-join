package com.github.yulichang.toolkit.support;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.toolkit.Asserts;
import com.github.yulichang.toolkit.FieldStringMap;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.wrapper.segments.SelectCache;

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

    private static final Map<Class<?>, List<SelectCache>> LIST_CACHE = new ConcurrentHashMap<>();

    private static final Map<Class<?>, FieldStringMap<SelectCache>> MAP_CACHE = new ConcurrentHashMap<>();

    public static List<SelectCache> getListField(Class<?> clazz) {
        return LIST_CACHE.computeIfAbsent(clazz, c -> {
            TableInfo tableInfo = TableHelper.get(clazz);
            Asserts.hasTable(tableInfo, c);
            List<SelectCache> list = new ArrayList<>();
            if (ConfigProperties.tableInfoAdapter.mpjHasPK(tableInfo)) {
                list.add(new SelectCache(clazz, true, tableInfo.getKeyColumn(), tableInfo.getKeyType(),
                        tableInfo.getKeyProperty(), null));
            }
            list.addAll(tableInfo.getFieldList().stream().map(f -> new SelectCache(clazz, false, f.getColumn(),
                    f.getPropertyType(), f.getProperty(), f)).collect(Collectors.toList()));
            return list;
        });
    }

    public static Map<String, SelectCache> getMapField(Class<?> clazz) {
        return MAP_CACHE.computeIfAbsent(clazz, c -> getListField(c).stream().collect(Collectors.toMap(
                SelectCache::getColumProperty, Function.identity(), (i, j) -> j, FieldStringMap::new)));
    }
}
