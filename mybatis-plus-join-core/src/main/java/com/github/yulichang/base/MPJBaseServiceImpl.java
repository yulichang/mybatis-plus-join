package com.github.yulichang.base;

import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;

/**
 * @author yulichang
 * @see ServiceImpl
 */
@SuppressWarnings("unused")
public class MPJBaseServiceImpl<M extends MPJBaseMapper<T>, T> extends ServiceImpl<M, T> implements MPJBaseService<T> {

}
