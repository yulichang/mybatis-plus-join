package com.github.yulichang.test.mapping.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode
@FieldNameConstants
@TableName("area")
public class AreaDO {

    private Integer areaId;

    private String province;

    private String cityId;

    private String area;

    private String postcode;

    @TableLogic
    private Boolean del;
}
