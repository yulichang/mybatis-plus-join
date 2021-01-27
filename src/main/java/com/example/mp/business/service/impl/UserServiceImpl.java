package com.example.mp.business.service.impl;

import com.example.mp.business.entity.UserEntity;
import com.example.mp.business.mapper.UserMapper;
import com.example.mp.business.service.UserService;
import com.example.mp.mybatis.plus.base.MyBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends MyBaseServiceImpl<UserMapper, UserEntity> implements UserService {

}
