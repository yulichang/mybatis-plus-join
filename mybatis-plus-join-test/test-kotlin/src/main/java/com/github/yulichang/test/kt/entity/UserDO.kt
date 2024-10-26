package com.github.yulichang.test.kt.entity

import com.baomidou.mybatisplus.annotation.TableField
import com.baomidou.mybatisplus.annotation.TableLogic
import com.baomidou.mybatisplus.annotation.TableName
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler
import com.github.yulichang.test.kt.enums.Sex
import lombok.experimental.FieldNameConstants
import java.io.Serializable
import java.time.LocalDateTime

@Suppress("unused")
@FieldNameConstants
@TableName(value = "`user`", autoResultMap = true)
open class UserDO : ID<Int?>(), Serializable {
    var pid: Int? = null

    @TableField("`name`")
    var name: String? = null

    @TableField(value = "`json`", typeHandler = JacksonTypeHandler::class)
    var json: Map<String, String>? = null
    var sex: Sex? = null

    @TableField("head_img")
    var img: String? = null
    var createTime: LocalDateTime? = null
    var addressId: Int? = null
    var addressId2: Int? = null

    @TableLogic
    var del: Boolean? = null
    var createBy: Int? = null

    @TableField(exist = false)
    var createName: String? = null
    var updateBy: Int? = null

    @TableField(exist = false)
    var updateName: String? = null

    @TableField(exist = false)
    var alias: String? = null

    @TableField(exist = false)
    var children: List<UserDO>? = null

    @TableField(exist = false)
    var addressList: List<AddressDO>? = null

    @TableField(exist = false)
    var addressList2: List<AddressDO>? = null
    override fun toString(): String {
        return "UserDO(pid=$pid, name=$name, json=$json, sex=$sex, img=$img, createTime=$createTime, addressId=$addressId, addressId2=$addressId2, del=$del, createBy=$createBy, createName=$createName, updateBy=$updateBy, updateName=$updateName, alias=$alias, children=$children, addressList=$addressList, addressList2=$addressList2)"
    }
}
