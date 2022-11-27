package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

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

    private static final Map<Class<?>, String> LOGIC_CACHE = new ConcurrentHashMap<>();


    @SuppressWarnings("ConstantConditions")
    public static String getLogicInfo(int tableIndex, Class<?> clazz) {
        String logicStr = LOGIC_CACHE.get(clazz);
        if (Objects.nonNull(logicStr)) {
            return logicStr;
        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        Assert.notNull(tableInfo, "%s 不是数据库实体或没有注册到mybatis plus中", clazz.getName());
        if (tableInfo.isWithLogicDelete() && Objects.nonNull(tableInfo.getLogicDeleteFieldInfo())) {
            final String value = tableInfo.getLogicDeleteFieldInfo().getLogicNotDeleteValue();
            if (NULL.equalsIgnoreCase(value)) {
                logicStr = " AND " + Constant.TABLE_ALIAS + tableIndex + DOT + tableInfo.getLogicDeleteFieldInfo().getColumn() + " IS NULL";
            } else {
                logicStr = " AND " + Constant.TABLE_ALIAS + tableIndex + DOT + tableInfo.getLogicDeleteFieldInfo().getColumn() + EQUALS + String.format(tableInfo.getLogicDeleteFieldInfo().isCharSequence() ? "'%s'" : "%s", value);
            }
        } else {
            logicStr = StringPool.EMPTY;
        }
        LOGIC_CACHE.put(clazz, logicStr);
        return logicStr;
    }
}
