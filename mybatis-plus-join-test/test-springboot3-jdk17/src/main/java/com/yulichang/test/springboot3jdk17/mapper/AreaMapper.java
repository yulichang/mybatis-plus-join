package com.yulichang.test.springboot3jdk17.mapper;

import com.yulichang.test.springboot3jdk17.entity.AreaDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
@SuppressWarnings("unused")
public interface AreaMapper extends MyBaseMapper<AreaDO> {
}
