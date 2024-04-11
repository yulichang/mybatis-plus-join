package com.github.yulichang.test.mapping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.test.mapping.entity.AddressDO;
import com.github.yulichang.test.mapping.mapper.AddressMapper;
import com.github.yulichang.test.mapping.service.AddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, AddressDO> implements AddressService {

}
