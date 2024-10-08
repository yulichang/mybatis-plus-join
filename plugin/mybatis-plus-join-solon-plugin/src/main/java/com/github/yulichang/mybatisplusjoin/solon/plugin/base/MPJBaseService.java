package com.github.yulichang.mybatisplusjoin.solon.plugin.base;

import com.baomidou.mybatisplus.solon.service.IService;
import com.github.yulichang.base.JoinService;

/**
 * 基础service
 * 目前包含两个模块 连表查询 和 关系映射
 *
 * @author yulichang
 */
public interface MPJBaseService<T> extends IService<T>, JoinService<T> {

}
