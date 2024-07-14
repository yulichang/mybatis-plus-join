package com.github.yulichang.test.join.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yulichang.annotation.Table;
import lombok.Data;

import java.io.Serializable;

@Table
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
