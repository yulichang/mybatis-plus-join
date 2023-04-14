package com.github.yulichang.test.join.service.impl;

import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.join.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends MPJBaseServiceImpl<UserMapper, UserDO> implements UserService {


}
