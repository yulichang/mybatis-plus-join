package com.example.mp.business.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.mp.mybatis.plus.base.MyBaseEntity;
import lombok.Data;

@Data
@TableName("area")
public class AreaEntity extends MyBaseEntity {

    @TableId
    private Integer id;

    private String province;

    private String city;

    private String area;

    private String postcode;

}
