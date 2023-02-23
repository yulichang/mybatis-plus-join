package com.github.yulichang.test.mapping.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.github.yulichang.annotation.EntityMapping;
import com.github.yulichang.test.mapping.enums.Sex;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UU {

    private Integer id;

    private Integer pid;

    private Map<String, String> name;

    private Sex sex;

    private String headImg;

    private Integer addressId;

    private Boolean del;

    @TableField(exist = false)
    @EntityMapping(thisField = "pid", joinField = "id")
    private UserDO pUser;

    @TableField(exist = false)
    @EntityMapping(thisField = "id", joinField = "pid")
    private List<UserDO> userList;
}
