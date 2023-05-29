package com.github.yulichang.test.kt

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@MapperScan(
    value = ["com.github.yulichang.test.kt.mapper"],
    basePackages = ["com.github.yulichang.test.kt.mapper"]
)
@ComponentScan(basePackages = ["com.github.yulichang.test"])
@SpringBootApplication
open class KtApplication

fun main(args: Array<String>) {
    runApplication<KtApplication>(*args)
}
