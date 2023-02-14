package com.github.yulichang.test.kotlin.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {
    private Integer id;

    private String userID;

    private String name;

    private List<AddressDTO> addresses;

}
