package com.github.yulichang.test.join.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

@Data
public class AddressGeneric<ID, USER_ID, AREA_ID, TEL, ADDRESS, DEL> {

    @TableId
    private ID id;

    private USER_ID userId;

    private AREA_ID areaId;

    private TEL tel;

    private ADDRESS address;

    @TableLogic
    private DEL del;
}
