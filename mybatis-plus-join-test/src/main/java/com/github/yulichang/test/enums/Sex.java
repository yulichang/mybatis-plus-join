package com.github.yulichang.test.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Sex {

    MAN(0, "男"),

    WOMAN(1, "女");

    @EnumValue
    private final int code;
    private final String des;

    Sex(int code, String des) {
        this.code = code;
        this.des = des;
    }

}
