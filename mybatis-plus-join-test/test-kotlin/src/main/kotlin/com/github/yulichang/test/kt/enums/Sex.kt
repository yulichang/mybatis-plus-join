package com.github.yulichang.test.kt.enums

import com.baomidou.mybatisplus.annotation.EnumValue

@Suppress("unused")
enum class Sex(@field:EnumValue private val code: Int, private val des: String) {
    MAN(0, "男"),
    WOMAN(1, "女")
}
