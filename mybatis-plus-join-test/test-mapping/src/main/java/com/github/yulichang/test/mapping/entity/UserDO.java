package com.github.yulichang.test.mapping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.github.yulichang.annotation.Condition;
import com.github.yulichang.annotation.EntityMapping;
import com.github.yulichang.annotation.FieldMapping;
import com.github.yulichang.annotation.enums.SqlKeyword;
import com.github.yulichang.test.mapping.enums.Sex;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.util.List;
import java.util.Map;

@Data
@ToString
@Accessors(chain = true)
@EqualsAndHashCode
@FieldNameConstants
@TableName(value = "`user`", autoResultMap = true)
public class UserDO {

    @TableId
    private Integer id;

    private Integer pid;

    @TableField(value = "`name`", typeHandler = JacksonTypeHandler.class)
    private Map<String, String> name;

    private Sex sex;

    private String headImg;

    private Integer addressId;

    @TableLogic
    private Boolean del;

    @TableField(exist = false)
//    @EntityMapping(thisField = "pid", joinField = "id")
    @EntityMapping(thisField = Fields.pid, joinField = "id")
    private UserDO pUser;

    @TableField(exist = false)
//    @EntityMapping(thisField = "id", joinField = "pid")
    @FieldMapping(tag = UserDO.class, thisField = "id", joinField = "pid", select = "head_img")
    private List<String> pName;

    @TableField(exist = false)
    @EntityMapping(thisField = "id", joinField = "userId", condition = {
            @Condition(column = "userId", keyWord = SqlKeyword.GE, value = "0")
    }, orderByAsc = "userId", orderByDesc = "areaId")
    private List<AddressDO> addressList;
}
