package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class TableHelper {

    private static final Map<Class<?>, TableInfo> TABLE_INFO_CACHE = new ConcurrentHashMap<>();

    public static void init(Class<?> newClass, Class<?> oldClass) {
        if (Objects.nonNull(newClass)) {
            TableInfo info = TableInfoHelper.getTableInfo(newClass);
            if (Objects.isNull(info)) {
                if (Objects.nonNull(oldClass)) {
                    TableInfo oldInfo = TableInfoHelper.getTableInfo(oldClass);
                    if (Objects.nonNull(oldInfo)) {
                        TABLE_INFO_CACHE.put(newClass, oldInfo);
                    }
                }
            }
        }
    }

    public static TableInfo get(Class<?> clazz) {
        if (Objects.nonNull(clazz)) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
            if (Objects.nonNull(tableInfo)) {
                return tableInfo;
            }
            return TABLE_INFO_CACHE.get(clazz);
        } else {
            return null;
        }
    }
}
