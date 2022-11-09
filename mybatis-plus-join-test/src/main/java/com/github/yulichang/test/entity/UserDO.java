package com.github.yulichang.test.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.yulichang.test.enums.Sex;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode
@TableName("`user`")
public class UserDO {

    @TableId
    private Integer id;

    @TableField("name")
    private String sdafrrvnbioiure;

    private Sex sex;

    private String headImg;

    private Integer addressId;

    @TableLogic
    private Boolean del;
}
