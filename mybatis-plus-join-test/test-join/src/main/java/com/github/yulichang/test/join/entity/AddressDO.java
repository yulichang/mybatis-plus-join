package com.github.yulichang.test.join.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@ToString
@Accessors(chain = true)
@TableName("address")
public class AddressDO extends AddressGeneric<Integer, Integer, Integer, String, String, Boolean> implements Serializable {

}
