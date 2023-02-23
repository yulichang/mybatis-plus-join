package com.github.yulichang.test.collection.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("table_t")
public class TableT {

    @TableId
    private Integer id;

    private Integer aid1;

    private Integer aid2;

    private String name;
}
