package com.example.mp.business.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.mp.mybatis.plus.base.MyBaseEntity;
import lombok.Data;

@Data
@TableName("user")
public class UserEntity extends MyBaseEntity {

    @TableId
    private Integer id;

    private String name;

    private String sex;

    private String headImg;
}
