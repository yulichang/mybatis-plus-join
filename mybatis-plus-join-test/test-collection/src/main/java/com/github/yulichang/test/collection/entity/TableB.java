package com.github.yulichang.test.collection.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("table_b")
public class TableB {

    @TableId
    private Integer id;

    private Integer aid;

    private String name;
}
