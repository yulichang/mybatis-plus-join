package com.github.yulichang.test.join.entity;

import com.baomidou.mybatisplus.annotation.OrderBy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ToString
@Accessors(chain = true)
@TableName("order_t")
public class OrderDO implements Serializable {

    @TableId
    private Integer id;

    private Integer userId;

    @OrderBy
    private String name;

    @TableField(exist = false)
    private String userName;
}
