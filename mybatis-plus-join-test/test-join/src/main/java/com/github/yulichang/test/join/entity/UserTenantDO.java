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
@TableName(value = "user_tenant")
public class UserTenantDO {

    @TableId("id")
    private Integer idea;

    @TableField("user_id")
    private Integer uuid;

    private Integer tenantId;
}
