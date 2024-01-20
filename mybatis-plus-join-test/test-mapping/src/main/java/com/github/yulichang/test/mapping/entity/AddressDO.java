package com.github.yulichang.test.mapping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yulichang.annotation.FieldMapping;
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
@TableName("address")
public class AddressDO {

    @TableId
    private Integer id;

    private Integer userId;

    private Integer areaId;

    private String city;

    private String tel;

    private String address;

    @TableLogic
    private Boolean del;

    @TableField(exist = false)
    @FieldMapping(tag = AreaDO.class, thisField = "city", joinField = "cityId", select = "area")
    private String area;
}
