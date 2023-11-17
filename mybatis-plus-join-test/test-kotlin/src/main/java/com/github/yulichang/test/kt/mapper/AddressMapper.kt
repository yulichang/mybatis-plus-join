package com.github.yulichang.test.kt.mapper

import com.github.yulichang.test.kt.entity.AddressDO
import org.apache.ibatis.annotations.Mapper

@Mapper
interface AddressMapper : MyBaseMapper<AddressDO?>
