package com.github.yulichang.test.join.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_dto")
public class UserDto {

    @TableId
    private Integer id;

    private Integer userId;

    private Integer createBy;

    @TableField(exist = false)
    private String createName;

    private Integer updateBy;

    @TableField(exist = false)
    private String updateName;
}
