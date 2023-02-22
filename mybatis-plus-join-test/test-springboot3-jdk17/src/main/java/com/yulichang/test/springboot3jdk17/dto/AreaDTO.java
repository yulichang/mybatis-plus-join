package com.yulichang.test.springboot3jdk17.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode
@TableName("area")
public class AreaDTO {

    private Integer id;

    private String province;

    private String city;

    private String area;

    private String postcode;

    private Boolean del;
}
