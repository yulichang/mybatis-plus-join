package com.github.yulichang.test.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.test.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends MPJBaseMapper<UserDO> {

}
