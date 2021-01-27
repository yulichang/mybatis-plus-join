package com.example.mp.business.service.impl;

import com.example.mp.business.entity.UserAddressEntity;
import com.example.mp.business.mapper.UserAddressMapper;
import com.example.mp.business.service.UserAddressService;
import com.example.mp.mybatis.plus.base.MyBaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserAddressServiceImpl extends MyBaseServiceImpl<UserAddressMapper, UserAddressEntity> implements UserAddressService {
}
