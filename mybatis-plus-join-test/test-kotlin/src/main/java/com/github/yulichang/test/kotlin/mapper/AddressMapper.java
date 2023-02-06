package com.github.yulichang.test.kotlin.mapper;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.test.kotlin.dto.AddressDTO;
import com.github.yulichang.test.kotlin.entity.AddressDO;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;

@Mapper
@SuppressWarnings("unused")
public interface AddressMapper extends MPJBaseMapper<AddressDO> {
    public static <T,S> MPJLambdaWrapper<T> appendAddressesSubPredicate(MPJLambdaWrapper<T> pred,
                                                                        SFunction<S, Collection<AddressDTO>> dtoAddressesField,
                                                                        SFunction<T,?> entityUserIDField){
        return pred.selectCollection(AddressDO.class, dtoAddressesField)
        .rightJoin(AddressDO.class, AddressDO::getUserID, entityUserIDField);
    }
}
