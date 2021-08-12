package com.baomidou.mybatisplus.core.metadata;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 数据库表反射信息
 *
 * @author yulichang
 * @see TableInfo
 * @since 1.2.0
 */
@Data
@Setter(AccessLevel.PACKAGE)
@Accessors(chain = true)
public class MPJTableInfo {

    private TableInfo tableInfo;

    /**
     * 是否包含映射注解
     */
    private boolean hasMapping;

    /**
     * mapperClass
     */
    private Class<?> mapperClass;

    /**
     * 包含映射注解的字段列表
     */
    private List<MPJTableFieldInfo> fieldList;
}
