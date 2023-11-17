package com.github.yulichang.test.kt.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableLogic

open class AddressGeneric<ID, USER_ID, AREA_ID, TEL, ADDRESS, DEL> {
    @TableId
    var id: ID? = null
    var userId: USER_ID? = null
    var areaId: AREA_ID? = null
    var tel: TEL? = null
    var address: ADDRESS? = null

    @TableLogic
    var del: DEL? = null
}
