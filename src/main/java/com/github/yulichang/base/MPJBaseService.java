package com.github.yulichang.base;

/**
 * 基础service
 * 目前包含两个模块 连表查询 和 关系映射
 *
 * @author yulichang
 * @see MPJBaseJoinService
 * @see MPJBaseDeepService
 */
public interface MPJBaseService<T> extends MPJBaseJoinService<T>, MPJBaseDeepService<T> {
}
