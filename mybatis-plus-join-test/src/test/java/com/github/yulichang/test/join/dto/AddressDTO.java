package com.github.yulichang.test.join.dto;

import com.github.yulichang.test.join.entity.AreaDO;
import lombok.Data;

import java.util.List;

@Data
public class AddressDTO {

    private Integer id;

    private Integer userId;

    private Integer areaId;

    private String tel;

    private String address;

    private Boolean del;

    private List<AreaDO> areaList;

    private AreaDO area;
}
