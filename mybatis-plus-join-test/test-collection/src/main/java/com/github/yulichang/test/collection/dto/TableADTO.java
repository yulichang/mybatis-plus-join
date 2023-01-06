package com.github.yulichang.test.collection.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class TableADTO {

    private Integer id;

    private String name;

    private List<TableBDTO> bList;

    private TableBDTO b;
}
