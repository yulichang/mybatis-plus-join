package com.github.yulichang.test.kt.m

import com.github.yulichang.extension.kt.KtLambdaWrapper
import com.github.yulichang.extension.kt.toolkit.KtWrappers
import com.github.yulichang.test.kt.entity.UserDO
import com.github.yulichang.test.util.ThreadLocalUtils
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import kotlin.reflect.KMutableProperty1

@SpringBootTest
class OrderByTest {
    @BeforeEach
    fun setUp() {
        com.github.yulichang.test.util.Reset.reset()
    }

    @org.junit.jupiter.api.Test
    fun orderBy() {
        ThreadLocalUtils.set(
            "SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by " +
                    "FROM `user` t WHERE t.del = false AND (t.id = ?) ORDER BY t.id ASC, t.`name` ASC, t.pid ASC"
        )
        val columList: List<KMutableProperty1<UserDO, *>> =
            listOf<KMutableProperty1<UserDO, *>>(UserDO::id, UserDO::name, UserDO::pid)
        val wrapper: KtLambdaWrapper<UserDO> = KtWrappers.query(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .eq(UserDO::id, 1)
            .orderByAsc(columList)
        val list: List<UserDO> = wrapper.list()
        list.forEach(System.out::println)
    }

    @org.junit.jupiter.api.Test
    fun orderBy1() {
        ThreadLocalUtils.set(
            "SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by " +
                    "FROM `user` t WHERE t.del = false AND (t.id = ?) GROUP BY t.id, t.`name`, t.pid"
        )
        val columList: List<KMutableProperty1<UserDO, *>> =
            listOf<KMutableProperty1<UserDO, *>>(UserDO::id, UserDO::name, UserDO::pid)
        val wrapper: KtLambdaWrapper<UserDO> = KtWrappers.query(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .eq(UserDO::id, 1)
            .groupBy(columList)
        val list: List<UserDO> = wrapper.list()
        list.forEach(System.out::println)
    }
}
