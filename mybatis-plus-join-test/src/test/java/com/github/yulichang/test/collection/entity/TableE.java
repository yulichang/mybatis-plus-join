package com.github.yulichang.test.collection.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("table_e")
public class TableE {

    @TableId
    private Integer id;

    private Integer did;

    private String name;
}
