package com.github.yulichang.wrapper.segments;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import org.apache.ibatis.type.TypeHandler;

/**
 * 查询列
 *
 * @author yulichang
 * @since 1.3.10
 */
public interface Select {

    Class<?> getClazz();

    boolean isPk();

    String getColumn();

    Class<?> getColumnType();

    String getTagColumn();

    String getColumProperty();

    boolean hasTypeHandle();

    TypeHandler<?> getTypeHandle();

    boolean isHasAlias();

    String getAlias();

    TableFieldInfo getTableFieldInfo();

    boolean isFunc();

    BaseFuncEnum getFunc();

    boolean isLabel();
}
