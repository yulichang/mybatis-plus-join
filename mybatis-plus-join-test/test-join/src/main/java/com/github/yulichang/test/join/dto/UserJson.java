package com.github.yulichang.test.join.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserJson implements Serializable {

    private Long id;
    private String name;
}
