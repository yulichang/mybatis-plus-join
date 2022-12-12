package com.github.yulichang.test.join.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
@SuppressWarnings("unused")
public interface MyBaseMapper<T> extends MPJBaseMapper<T> {

    int insertBatchSomeColumn(List<T> entityList);
}
