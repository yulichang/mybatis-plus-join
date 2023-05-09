package com.github.yulichang.test.mapping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.test.mapping.entity.UserDO;
import com.github.yulichang.test.mapping.mapper.UserMapper;
import com.github.yulichang.test.mapping.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
}
