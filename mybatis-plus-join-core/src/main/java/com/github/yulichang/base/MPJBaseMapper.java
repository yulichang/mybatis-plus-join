package com.github.yulichang.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author yulichang
 * @see BaseMapper
 */
public interface MPJBaseMapper<T> extends BaseMapper<T>, JoinMapper<T> {

}
