package com.github.yulichang.test.kt.mapper

import com.github.yulichang.base.MPJBaseMapper
import com.github.yulichang.test.kt.entity.UserDto
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserDTOMapper : MPJBaseMapper<UserDto?>
