package com.github.yulichang.test.join.mapper;

import com.github.yulichang.test.join.entity.AreaDO;
import org.apache.ibatis.annotations.Mapper;

import java.awt.geom.Area;
import java.util.List;

@Mapper
@SuppressWarnings("unused")
public interface AreaMapper extends MyBaseMapper<AreaDO> {

    List<Area> all();
}
