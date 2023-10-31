package com.github.yulichang.test.join.entity;


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

    @TableId
    private Integer id;


    private Integer userId;

    private Integer tenantId;
}
