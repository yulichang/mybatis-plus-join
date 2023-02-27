package com.github.yulichang.test.join.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class ID<T extends Serializable> extends Model<ID<T>> {

    @Getter
    @Setter
    @TableId
    private T id;
}
