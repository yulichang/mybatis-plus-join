package com.github.yulichang.test.kt.service.impl

import com.github.yulichang.base.MPJBaseServiceImpl
import com.github.yulichang.test.kt.entity.UserDO
import com.github.yulichang.test.kt.mapper.UserMapper
import com.github.yulichang.test.kt.service.UserService
import org.springframework.stereotype.Service

@Service
open class UserServiceImpl : MPJBaseServiceImpl<UserMapper?, UserDO?>(), UserService
