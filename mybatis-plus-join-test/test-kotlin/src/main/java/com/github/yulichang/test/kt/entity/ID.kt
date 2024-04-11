package com.github.yulichang.test.kt.entity

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.extension.activerecord.Model
import lombok.Getter
import lombok.Setter
import java.io.Serializable

open class ID<T : Serializable?> : Model<ID<T>?>() {
    @Getter
    @Setter
    @TableId
    var id: T? = null
}
