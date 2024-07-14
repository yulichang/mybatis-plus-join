package com.github.yulichang.wrapper.resultmap;

import com.github.yulichang.apt.BaseColumn;
import com.github.yulichang.wrapper.segments.SelectCache;
import org.apache.ibatis.type.JdbcType;

public interface IResult {

    boolean isId();

    String getIndex();

    SelectCache getSelectNormal();

    String getProperty();

    Class<?> getJavaType();

    JdbcType getJdbcType();

    default BaseColumn<?> getBaseColumn() {
        return null;
    }
}
