package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.yulichang.config.ConfigProperties;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 逻辑删除信息类
 *
 * @author yulichang
 * @since 1.3.7
 */
public class LogicInfoUtils implements Constants {

    private static final Map<Class<?>, Map<String, String>> LOGIC_CACHE = new ConcurrentHashMap<>();

    private static final Map<Class<?>, Map<String, String>> LOGIC_CACHE_NO_AND = new ConcurrentHashMap<>();

    public static String getLogicInfo(Integer tableIndex, Class<?> clazz, boolean hasAlias, String alias) {
        Map<String, String> absent = LOGIC_CACHE.get(clazz);
        if (absent == null) {
            absent = new ConcurrentHashMap<>();
            LOGIC_CACHE.put(clazz, absent);
        }
        return absent.computeIfAbsent(hasAlias ? alias : (alias + tableIndex), key -> getLogicStr(key, clazz, true));
    }

    public static String getLogicInfoNoAnd(Integer tableIndex, Class<?> clazz, boolean hasAlias, String alias) {
        Map<String, String> absent = LOGIC_CACHE_NO_AND.get(clazz);
        if (absent == null) {
            absent = new ConcurrentHashMap<>();
            LOGIC_CACHE_NO_AND.put(clazz, absent);
        }
        return absent.computeIfAbsent(hasAlias ? alias : (alias + tableIndex), key -> getLogicStr(key, clazz, false));
    }

    private static String getLogicStr(String prefix, Class<?> clazz, boolean and) {

        String logicStr;
        TableInfo tableInfo = TableHelper.get(clazz);
        Assert.notNull(tableInfo, "table not find by class <%s>", clazz.getSimpleName());
        TableFieldInfo logicField = ConfigProperties.adapter.mpjGetLogicField(tableInfo);
        if (ConfigProperties.adapter.mpjHasLogic(tableInfo) && Objects.nonNull(logicField)) {
            final String value = logicField.getLogicNotDeleteValue();
            if (NULL.equalsIgnoreCase(value)) {
                logicStr = (and ? " AND " : EMPTY) + prefix + DOT + logicField.getColumn() + " IS NULL";
            } else {
                logicStr = (and ? " AND " : EMPTY) + prefix + DOT + logicField.getColumn() + EQUALS + String.format(logicField.isCharSequence() ? "'%s'" : "%s", value);
            }
        } else {
            logicStr = StringPool.EMPTY;
        }
        return logicStr;
    }
}
