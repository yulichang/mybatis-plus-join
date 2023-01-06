package com.github.yulichang.test.join.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.test.join.entity.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDTOMapper extends MPJBaseMapper<UserDto> {
}
