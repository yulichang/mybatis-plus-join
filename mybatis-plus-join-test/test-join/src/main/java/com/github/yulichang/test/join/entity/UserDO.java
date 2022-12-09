package com.github.yulichang.test.join.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.github.yulichang.test.join.enums.Sex;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode
@TableName(value = "`user`", autoResultMap = true)
public class UserDO {

    @TableId
    private Integer id;

    private Integer pid;

    @TableField("`name`")
    private String name;

    @TableField(value = "`json`", typeHandler = JacksonTypeHandler.class)
    private Map<String, String> json;

    private Sex sex;

    @TableField("head_img")
    private String img;

    private LocalDateTime createTime;

    private Integer addressId;

    @TableLogic
    private Boolean del;

    @TableField(exist = false)
    private List<UserDO> children;
}
