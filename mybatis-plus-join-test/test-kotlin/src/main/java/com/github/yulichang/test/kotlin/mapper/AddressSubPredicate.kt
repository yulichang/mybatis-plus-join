package com.github.yulichang.test.kotlin.mapper

import com.baomidou.mybatisplus.core.toolkit.support.SFunction
import com.github.yulichang.test.kotlin.dto.AddressDTO
import com.github.yulichang.test.kotlin.dto.UserDTO
import com.github.yulichang.test.kotlin.entity.AddressDO
import com.github.yulichang.test.kotlin.entity.UserDO
import com.github.yulichang.wrapper.MPJLambdaWrapper

fun <T,S> MPJLambdaWrapper<T>.addressesSubPredicate(dtoAddressesField:SFunction<S,Collection<AddressDTO>>,
                                                    entityUserIDField:SFunction<T,Any>): MPJLambdaWrapper<T> {
    return this.selectCollection<S, AddressDO,Any,Collection<AddressDTO>>(AddressDO::class.java, dtoAddressesField)
        .rightJoin(AddressDO::class.java, AddressDO::getUserID, entityUserIDField)
}

fun <T,S> MPJLambdaWrapper<T>.addressesSubPredicateFromJavaMapper(dtoAddressesField:SFunction<S,Collection<AddressDTO>>,
                                                    entityUserIDField:SFunction<T,Any>): MPJLambdaWrapper<T> {
    return AddressMapper.appendAddressesSubPredicate(this,dtoAddressesField,entityUserIDField)
}