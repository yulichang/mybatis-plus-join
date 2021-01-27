package com.example.mp.business.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.mp.mybatis.plus.base.MyBaseEntity;
import lombok.Data;

@Data
@TableName("user_address")
public class UserAddressEntity extends MyBaseEntity {

    @TableId
    private Integer id;

    private Integer userId;

    private Integer areaId;

    private String tel;

    private String address;
}
