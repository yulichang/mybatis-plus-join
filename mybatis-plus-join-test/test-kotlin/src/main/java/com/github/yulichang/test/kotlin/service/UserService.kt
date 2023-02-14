package com.github.yulichang.test.kotlin.service

import com.github.yulichang.test.kotlin.dto.UserDTO
import com.github.yulichang.test.kotlin.entity.UserDO
import com.github.yulichang.test.kotlin.mapper.UserMapper
import com.github.yulichang.test.kotlin.mapper.addressesSubPredicate
import com.github.yulichang.test.kotlin.mapper.addressesSubPredicateFromJavaMapper
import com.github.yulichang.wrapper.MPJLambdaWrapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    lateinit var mapper:UserMapper;

    fun get(userID:String):UserDTO{
        val pred=MPJLambdaWrapper<UserDO>()
            .selectAll(UserDO::class.java)
            .addressesSubPredicate(UserDTO::getAddresses,UserDO::getUserID)
            .eq(UserDO::getUserID,userID)
        val result= mapper.selectJoinOne(UserDTO::class.java,pred)
        return result
    }

    fun getFromFuncVar(userID:String):UserDTO{
        val dtoGetAddressesRef=UserDTO::getAddresses
        val pred=MPJLambdaWrapper<UserDO>()
            .selectAll(UserDO::class.java)
            .addressesSubPredicate(dtoGetAddressesRef,UserDO::getUserID)
            .eq(UserDO::getUserID,userID)
        val result= mapper.selectJoinOne(UserDTO::class.java,pred)
        return result
    }
}