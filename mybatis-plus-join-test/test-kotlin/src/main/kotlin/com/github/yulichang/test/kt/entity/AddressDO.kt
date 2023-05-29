package com.github.yulichang.test.kt.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable

@Suppress("unused")
@TableName("address")
class AddressDO : AddressGeneric<Int?, Int?, Int?, String?, String?, Boolean?>(), Serializable {
    @TableField(exist = false)
     var aaa = false
}
