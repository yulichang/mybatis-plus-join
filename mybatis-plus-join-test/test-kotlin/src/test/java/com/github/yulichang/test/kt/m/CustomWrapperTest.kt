package com.github.yulichang.test.kt.m

import com.github.yulichang.kt.KtLambdaWrapper
import com.github.yulichang.test.kt.entity.UserDO
import com.github.yulichang.test.kt.mapper.UserMapper
import com.github.yulichang.test.util.Reset
import com.github.yulichang.test.util.ThreadLocalUtils
import com.github.yulichang.toolkit.Ref
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import kotlin.reflect.KProperty

@SpringBootTest
class CustomWrapperTest {
    @Autowired
    private val userMapper: UserMapper? = null

    @BeforeEach
    fun setUp() {
        Reset.reset()
    }

    //自定义wrapper扩展
    class CWrapper<T> : KtLambdaWrapper<T>() {
        fun eqIfAbsent(column: KProperty<*>, `val`: Any?): CWrapper<T> {
            super.eq(Objects.nonNull(`val`), column, `val`)
            return this
        }

        companion object {
            fun <T> toCWrapper(): CWrapper<T>? {
                return null
            }
        }
    }

    @Test
    fun testWrapperCustomer() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false AND (t.id = ?)")
        val wrapper: CWrapper<UserDO> = CWrapper<UserDO>()
                .selectAll(UserDO::class.java)
                .toChildren<CWrapper<UserDO>> { CWrapper.toCWrapper() }
                .eqIfAbsent(UserDO::id, 1)
        val dos = userMapper?.selectList(wrapper)
        dos?.forEach(System.out::println)
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false")
        val wrapper1: CWrapper<UserDO> = CWrapper<UserDO>()
                .selectAll(UserDO::class.java)
                .toChildren(Ref<CWrapper<UserDO>>())
                .eqIfAbsent(UserDO::id, null)
        val dos1 = userMapper?.selectList(wrapper1)
        dos1?.forEach(System.out::println)
    }
}
