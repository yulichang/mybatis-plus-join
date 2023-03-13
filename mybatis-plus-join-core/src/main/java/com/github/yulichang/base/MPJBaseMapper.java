package com.github.yulichang.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.mapper.MPJJoinMapper;
import com.github.yulichang.base.mapper.MPJRelationMapper;

/**
 * @author yulichang
 * @see BaseMapper
 */
public interface MPJBaseMapper<T> extends MPJJoinMapper<T>, MPJRelationMapper<T> {


}
