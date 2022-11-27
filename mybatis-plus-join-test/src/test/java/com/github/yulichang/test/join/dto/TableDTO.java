package com.github.yulichang.test.join.dto;

import lombok.Data;

import java.util.List;

@Data
public class TableDTO {

    private Integer id;

    private String name;

    private List<TableBDTO> bbList;

    @Data
    public static class TableBDTO {

        private Integer id;

        private Integer aid;

        private String name;

        private List<TableCDTO> ccList;
    }

    @Data
    public static class TableCDTO {

        private Integer id;

        private Integer bid;

        private String name;

        private List<TableDDTO> ddList;
    }

    @Data
    public static class TableDDTO {

        private Integer id;

        private Integer cid;

        private String name;

        private List<TableEDTO> eeList;
    }

    @Data
    public static class TableEDTO {

        private Integer id;

        private Integer did;

        private String name;
    }
}
