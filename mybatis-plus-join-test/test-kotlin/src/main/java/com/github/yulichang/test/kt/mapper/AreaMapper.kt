package com.github.yulichang.test.kt.mapper

import com.github.yulichang.test.kt.entity.AreaDO
import org.apache.ibatis.annotations.Mapper

@Suppress("unused")
@Mapper
interface AreaMapper : MyBaseMapper<AreaDO?>
