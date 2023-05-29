package com.github.yulichang.test.kt.mapper

import com.github.yulichang.base.MPJBaseMapper
import org.apache.ibatis.annotations.Mapper

@Mapper
interface MyBaseMapper<T> : MPJBaseMapper<T> {
    fun insertBatchSomeColumn(entityList: List<T>?): Int
}
