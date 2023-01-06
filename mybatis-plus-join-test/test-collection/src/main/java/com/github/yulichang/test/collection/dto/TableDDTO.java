package com.github.yulichang.test.collection.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class TableDDTO {

    private Integer id;

    private Integer cid;

    private String name;

    private List<TableEDTO> eList;

    private TableEDTO e;
}
