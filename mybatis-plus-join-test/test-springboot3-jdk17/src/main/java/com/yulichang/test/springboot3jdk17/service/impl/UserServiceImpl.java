package com.yulichang.test.springboot3jdk17.service.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.yulichang.test.springboot3jdk17.entity.UserDO;
import com.yulichang.test.springboot3jdk17.mapper.UserMapper;
import com.yulichang.test.springboot3jdk17.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends MPJBaseServiceImpl<UserMapper, UserDO> implements UserService {

}
