package com.github.yulichang.test.kt.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable

@TableName("user_dto")
class UserDto : Serializable {
    @TableId
    val id: Int? = null
    val userId: Int? = null

    @TableField(exist = false)
    val userName: String? = null
    val createBy: Int? = null

    @TableField(exist = false)
    val createName: String? = null
    val updateBy: Int? = null

    @TableField(exist = false)
    val updateName: String? = null
}
