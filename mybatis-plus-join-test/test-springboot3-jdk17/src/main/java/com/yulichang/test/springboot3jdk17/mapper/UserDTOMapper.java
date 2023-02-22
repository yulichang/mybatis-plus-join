package com.yulichang.test.springboot3jdk17.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.yulichang.test.springboot3jdk17.entity.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDTOMapper extends MPJBaseMapper<UserDto> {
}
