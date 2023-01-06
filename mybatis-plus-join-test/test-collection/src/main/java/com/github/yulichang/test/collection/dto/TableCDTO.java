package com.github.yulichang.test.collection.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class TableCDTO {

    private Integer id;

    private Integer bid;

    private String name;

    private List<TableDDTO> dList;

    private TableDDTO d;
}
