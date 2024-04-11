package com.github.yulichang.test.kt.dto

import com.github.yulichang.test.kt.entity.UserDO
import com.github.yulichang.test.kt.enums.Sex
import lombok.Data
import lombok.ToString

/**
 * userDTO
 */
@Suppress("unused")
@Data
@ToString
class UserDTO {
    /** user  */
     val id: Int? = null

    /** user  */
     val PName: String? = null

    /** user  */
     val json: Map<String, String>? = null

    /** user  */
     val sex: Sex? = null

    /** user  */
     val headImg: String? = null

    /** user  */
     val userHeadImg: String? = null //同 headImg 别名测试

    /** user_address  */
     val tel: String? = null

    /** user_address  */
     val address: String? = null

    /** user_address  */
     val userAddress: String? = null

    /** area  */
     val province: String? = null

    /** area  */
     val city: String? = null

    /** area  */
     val area: Map<String, String>? = null
     val addressList: List<AddressDTO>? = null
     val addressDTO: AddressDTO? = null
     val addressIds: List<Int>? = null
     val children: List<UserDO>? = null
}
