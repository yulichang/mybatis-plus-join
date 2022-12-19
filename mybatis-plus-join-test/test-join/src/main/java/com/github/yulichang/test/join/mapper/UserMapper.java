package com.github.yulichang.test.join.mapper;

import com.github.yulichang.test.join.entity.UserDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends MyBaseMapper<UserDO> {

}
