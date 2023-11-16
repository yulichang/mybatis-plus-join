package com.github.yulichang.test.join.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode
@TableName("area")
public class AreaDO implements Serializable {

    @TableId
    private Integer id;

    private String province;

    private String city;

    private String area;

    private String Postcode;

    @TableLogic
    private Boolean del;
}
