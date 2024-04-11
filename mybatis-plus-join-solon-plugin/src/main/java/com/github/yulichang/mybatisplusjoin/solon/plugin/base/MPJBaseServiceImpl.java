package com.github.yulichang.mybatisplusjoin.solon.plugin.base;

import com.baomidou.mybatisplus.solon.service.impl.ServiceImpl;
import com.github.yulichang.base.MPJBaseMapper;

/**
 * @author yulichang
 * @see ServiceImpl
 */
@SuppressWarnings("unused")
public class MPJBaseServiceImpl<M extends MPJBaseMapper<T>, T> extends ServiceImpl<M, T> implements MPJBaseService<T> {

}
