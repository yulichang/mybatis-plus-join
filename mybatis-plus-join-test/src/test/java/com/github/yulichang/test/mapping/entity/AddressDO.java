package com.github.yulichang.test.mapping.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode
@TableName("address")
public class AddressDO {

    @TableId
    private Integer id;

    private Integer userId;

    private Integer areaId;

    private String tel;

    private String address;

    @TableLogic
    private Boolean del;
}
