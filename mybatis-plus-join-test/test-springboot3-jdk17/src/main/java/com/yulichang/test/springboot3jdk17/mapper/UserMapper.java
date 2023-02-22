package com.yulichang.test.springboot3jdk17.mapper;

import com.yulichang.test.springboot3jdk17.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends MyBaseMapper<UserDO> {

}
