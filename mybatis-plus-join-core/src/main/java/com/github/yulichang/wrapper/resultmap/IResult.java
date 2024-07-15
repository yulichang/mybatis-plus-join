package com.github.yulichang.wrapper.resultmap;

import com.github.yulichang.wrapper.segments.SelectCache;
import org.apache.ibatis.type.JdbcType;

import java.io.Serializable;

public interface IResult extends Serializable {

    boolean isId();

    String getIndex();

    SelectCache getSelectNormal();

    String getProperty();

    Class<?> getJavaType();

    JdbcType getJdbcType();
}
