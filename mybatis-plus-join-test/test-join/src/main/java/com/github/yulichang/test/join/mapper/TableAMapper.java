package com.github.yulichang.test.join.mapper;

import com.github.yulichang.test.join.entity.TableA;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@SuppressWarnings("unused")
public interface TableAMapper extends MyBaseMapper<TableA> {

}
