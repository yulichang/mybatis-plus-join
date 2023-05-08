package com.github.yulichang.test.join.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.extension.mapping.base.MPJDeepMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MyBaseMapper<T> extends MPJBaseMapper<T>, MPJDeepMapper<T> {

    @SuppressWarnings("UnusedReturnValue")
    int insertBatchSomeColumn(List<T> entityList);
}
