package com.github.yulichang.wrapper.segments;

import com.github.yulichang.apt.BaseColumn;
import com.github.yulichang.apt.Column;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.io.Serializable;

/**
 * 查询列
 *
 * @author yulichang
 * @since 1.3.10
 */
public interface Select extends Serializable {

    Class<?> getClazz();

    Integer getIndex();

    boolean isHasTableAlias();

    String getTableAlias();

    boolean isPk();

    String getColumn();

    Class<?> getColumnType();

    String getTagColumn();

    String getColumProperty();

    boolean hasTypeHandle();

    TypeHandler<?> getTypeHandle();

    boolean isHasAlias();

    String getAlias();

    Class<?> getPropertyType();

    JdbcType getJdbcType();

    boolean isFunc();

    SelectFunc.Arg[] getArgs();

    BaseFuncEnum getFunc();

    boolean isLabel();

    boolean isStr();

    default Column[] getColumns() {
        return null;
    }

    default BaseColumn<?> getBaseColumn() {
        return null;
    }
}
