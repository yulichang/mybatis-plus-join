package com.github.yulichang.test.join.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.github.yulichang.annotation.DynamicTableName;
import com.github.yulichang.test.join.enums.Sex;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@DynamicTableName
@FieldNameConstants
@TableName(value = "`user`", autoResultMap = true)
public class UserDO extends ID<Integer> implements Serializable {

    private Integer pid;

    @TableField("`name`")
    private String name;

    @TableField(value = "`json`", typeHandler = JacksonTypeHandler.class)
    private Map<String, String> json;

    private Sex sex;

    @TableField("head_img")
    private String img;

    private LocalDateTime createTime;

    private Integer addressId;

    private Integer addressId2;

    @TableLogic
    private Boolean del;

    private Integer createBy;

    @TableField(exist = false)
    private String createName;

    private Integer updateBy;

    @TableField(exist = false)
    private String updateName;

    @TableField(exist = false)
    private String alias;

    @TableField(exist = false)
    private List<UserDO> children;

    @TableField(exist = false)
    private List<AddressDO> addressList;

    @TableField(exist = false)
    private List<AddressDO> addressList2;
}
