package com.github.yulichang.test.mapping.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.test.mapping.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends MPJBaseMapper<UserDO> {

}
