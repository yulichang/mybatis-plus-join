package com.github.yulichang.test.kt.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableLogic
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable

@Suppress("unused")
@TableName("area")
class AreaDO : Serializable {
    @TableId
    var id: Int? = null
    var province: String? = null
    var city: String? = null
    var area: String? = null
    var postcode: String? = null

    @TableLogic
    var del: Boolean? = null
}
