package com.github.yulichang.interfaces;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

/**
 * @author yulichang
 */
@SuppressWarnings("unused")
public interface MPJBaseJoin<T> {

    default String getDeleteSql() {
        return StringPool.EMPTY;
    }

    default String getDeleteLogicSql() {
        return StringPool.EMPTY;
    }
}
