package com.github.yulichang.test.join.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Data
@ToString
@FieldNameConstants
@TableName(value = "user_tenanta",autoResultMap = true)
public class UserTenantaDO {

    @TableId("id")
    private Integer idea;

    @TableField("user_id")
    private Integer uuid;

    private Integer tenantId;

    @TableField("中文字段")
    private String detail;
}
