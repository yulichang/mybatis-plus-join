package com.github.yulichang.test.kt.mapper

import com.github.yulichang.test.kt.entity.UserDO
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper : MyBaseMapper<UserDO?>
