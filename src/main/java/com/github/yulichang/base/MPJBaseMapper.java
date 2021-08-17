package com.github.yulichang.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.yulichang.base.mapper.MPJDeepMapper;
import com.github.yulichang.base.mapper.MPJJoinMapper;

/**
 * @author yulichang
 * @see BaseMapper
 */
public interface MPJBaseMapper<T> extends MPJJoinMapper<T>, MPJDeepMapper<T> {


}
