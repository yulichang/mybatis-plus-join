package com.example.mp.business.mapper;

import com.example.mp.business.entity.UserEntity;
import com.example.mp.mybatis.plus.base.MyBaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends MyBaseMapper<UserEntity> {
}
