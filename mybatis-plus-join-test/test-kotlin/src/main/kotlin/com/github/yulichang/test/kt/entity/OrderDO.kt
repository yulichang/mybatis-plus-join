package com.github.yulichang.test.kt.entity

import com.baomidou.mybatisplus.annotation.OrderBy
import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable


@TableName("order_t")
class OrderDO : Serializable {
    @TableId
    val id: Int? = null
    val userId: Int? = null

    @OrderBy
    val name: String? = null

    @TableField(exist = false)
    val userName: String? = null
}
