package com.github.yulichang.test.kt.mapper

import com.github.yulichang.test.kt.entity.OrderDO
import org.apache.ibatis.annotations.Mapper

@Mapper
interface OrderMapper : MyBaseMapper<OrderDO?>
