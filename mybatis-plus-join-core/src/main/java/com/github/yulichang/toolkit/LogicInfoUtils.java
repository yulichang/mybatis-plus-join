package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.github.yulichang.adapter.AdapterHelper;

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

    private static final Map<Class<?>, Map<String, String>> LOGIC_CACHE_INVERT = new ConcurrentHashMap<>();

    public static String getLogicInfo(Integer tableIndex, Class<?> clazz, boolean hasAlias, String alias) {
        Map<String, String> absent = LOGIC_CACHE.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
        return absent.computeIfAbsent(hasAlias ? alias : (alias + tableIndex), key -> getLogicStr(key, clazz, true, false));
    }

    public static String getLogicInfoApt(Class<?> clazz, String alias) {
        Map<String, String> absent = LOGIC_CACHE.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
        return absent.computeIfAbsent(alias, key -> getLogicStr(key, clazz, true, false));
    }

    public static String getLogicInfoNoAnd(Integer tableIndex, Class<?> clazz, boolean hasAlias, String alias) {
        Map<String, String> absent = LOGIC_CACHE_NO_AND.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
        return absent.computeIfAbsent(hasAlias ? alias : (alias + tableIndex), key -> getLogicStr(key, clazz, false, false));
    }

    public static String getLogicInfoInvert(Integer tableIndex, Class<?> clazz, boolean hasAlias, String alias) {
        Map<String, String> absent = LOGIC_CACHE_INVERT.computeIfAbsent(clazz, k -> new ConcurrentHashMap<>());
        return absent.computeIfAbsent(hasAlias ? alias : (alias + tableIndex), key -> getLogicStr(key, clazz, false, true));
    }

    private static String getLogicStr(String prefix, Class<?> clazz, boolean and, boolean invert) {
        String logicStr;
        TableInfo tableInfo = TableHelper.getAssert(clazz);
        TableFieldInfo logicField = AdapterHelper.getAdapter().mpjGetLogicField(tableInfo);
        if (AdapterHelper.getAdapter().mpjHasLogic(tableInfo) && Objects.nonNull(logicField)) {
            final String notDeleteValue = logicField.getLogicNotDeleteValue();
            final String deleteValue = logicField.getLogicDeleteValue();
            if (NULL.equalsIgnoreCase(notDeleteValue)) {
                logicStr = (and ? " AND " : EMPTY) + prefix + DOT + logicField.getColumn() + (invert ? " IS NOT NULL" : " IS NULL");
            } else {
                logicStr = (and ? " AND " : EMPTY) + prefix + DOT + logicField.getColumn() + EQUALS +
                        String.format(logicField.isCharSequence() ? "'%s'" : "%s", invert ? deleteValue : notDeleteValue);
            }
        } else {
            logicStr = StringPool.EMPTY;
        }
        return logicStr;
    }
}
