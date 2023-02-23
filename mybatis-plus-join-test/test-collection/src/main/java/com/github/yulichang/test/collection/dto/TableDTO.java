package com.github.yulichang.test.collection.dto;

import lombok.Data;

@Data
public class TableDTO {

    private Integer id;

    private String name;

    private Integer aid1;

    private Integer aid2;

    private TableADTO table1;

    private TableADTO table2;
}
