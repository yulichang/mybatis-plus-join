package com.github.yulichang.toolkit.support;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.toolkit.UniqueObject;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import lombok.Getter;

import java.util.Objects;

/**
 * MPJLambdaWrapper 查询字段
 *
 * @author yulichang
 * @since 1.2.5
 */
@Getter
public class SelectColumn implements UniqueObject {

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
     * 字段函数
     */
    private final BaseFuncEnum funcEnum;


    private SelectColumn(Class<?> clazz, String columnName, TableFieldInfo tableFieldInfo, String alias, String tagProperty, Class<?> keyType, BaseFuncEnum funcEnum) {
        this.clazz = clazz;
        this.columnName = columnName;
        this.tableFieldInfo = tableFieldInfo;
        this.alias = alias;
        this.tagProperty = tagProperty;
        this.keyType = keyType;
        this.funcEnum = funcEnum;
    }

    public static SelectColumn of(Class<?> clazz, String columnName, TableFieldInfo tableFieldInfo, String alias, String tagProperty, Class<?> keyType, BaseFuncEnum funcEnum) {
        if (tagProperty != null)
            tagProperty = StringUtils.getTargetColumn(tagProperty);
        return new SelectColumn(clazz, columnName, tableFieldInfo, alias, tagProperty, keyType, funcEnum);
    }

    /**
     * 获取唯一标识
     */
    @Override
    public String getUniqueKey() {
        return String.join(StringPool.AMPERSAND, clazz.getName(), columnName, alias,
                Objects.isNull(funcEnum) ? null : funcEnum.getSql());
    }
}
