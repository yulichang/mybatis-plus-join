package com.github.yulichang.test.kt.dto

import com.github.yulichang.test.kt.entity.AreaDO

class AddressDTO {
    val id: Int? = null
    val userId: Int? = null
    val areaId: Int? = null
    val tel: String? = null
    val address: String? = null
    val del: Boolean? = null
    val areaList: List<AreaDO>? = null
    val area: AreaDO? = null

    override fun toString(): String {
        return "AddressDTO(id=$id, userId=$userId, areaId=$areaId, tel=$tel, address=$address, del=$del, areaList=$areaList, area=$area)"
    }


}
