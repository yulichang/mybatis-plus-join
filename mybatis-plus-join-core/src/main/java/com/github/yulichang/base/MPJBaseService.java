package com.github.yulichang.base;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 基础service
 * 目前包含两个模块 连表查询 和 关系映射
 *
 * @author yulichang
 */
@SuppressWarnings({"unused"})
public interface MPJBaseService<T> extends IService<T>, JoinService<T> {

}
