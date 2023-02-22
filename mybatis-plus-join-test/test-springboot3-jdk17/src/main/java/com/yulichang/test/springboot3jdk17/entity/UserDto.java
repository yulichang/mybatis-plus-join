package com.yulichang.test.springboot3jdk17.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user_dto")
public class UserDto implements Serializable {

    @TableId
    private Integer id;

    private Integer userId;

    @TableField(exist = false)
    private String userName;

    private Integer createBy;

    @TableField(exist = false)
    private String createName;

    private Integer updateBy;

    @TableField(exist = false)
    private String updateName;
}
