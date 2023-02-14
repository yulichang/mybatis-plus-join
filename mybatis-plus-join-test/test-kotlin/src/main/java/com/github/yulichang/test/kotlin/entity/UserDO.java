package com.github.yulichang.test.kotlin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.github.yulichang.annotation.EntityMapping;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

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

    @TableField("user_id")
    private String userID;

    private String name;
}
