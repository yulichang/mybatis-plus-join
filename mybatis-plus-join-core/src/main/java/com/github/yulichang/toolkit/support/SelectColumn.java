package com.github.yulichang.toolkit.support;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import lombok.Getter;

/**
 * MPJLambdaWrapper 查询字段
 *
 * @author yulichang
 * @since 1.3.0
 */
@Getter
public class SelectColumn {

    /**
     * 字段实体类
     */
    private final Class<?> clazz;

    /**
     * 数据库字段名
     */
    private final String columnName;

    /**
     * 字段信息
     */
    private final TableFieldInfo tableFieldInfo;

    /**
     * 字段别名
     */
    private final String alias;

    /**
     * 目标属性
     */
    private final String tagProperty;

    /**
     * 主键类型
     */
    private final Class<?> keyType;

    /**
     * 是否是对多的列
     */
    private final boolean label;

    /**
     * 字段函数
     */
    private final BaseFuncEnum funcEnum;


    private SelectColumn(Class<?> clazz, String columnName, TableFieldInfo tableFieldInfo, String alias,
                         String tagProperty, Class<?> keyType, boolean label, BaseFuncEnum funcEnum) {
        this.clazz = clazz;
        this.columnName = columnName;
        this.tableFieldInfo = tableFieldInfo;
        this.alias = alias;
        this.tagProperty = tagProperty;
        this.keyType = keyType;
        this.label = label;
        this.funcEnum = funcEnum;
    }

    public static SelectColumn of(Class<?> clazz, String columnName, TableFieldInfo tableFieldInfo, String alias,
                                  String tagProperty, Class<?> keyType, boolean label, BaseFuncEnum funcEnum) {
        if (tagProperty != null)
            tagProperty = StringUtils.getTargetColumn(tagProperty);
        return new SelectColumn(clazz, columnName, tableFieldInfo, alias, tagProperty, keyType, label, funcEnum);
    }
}
