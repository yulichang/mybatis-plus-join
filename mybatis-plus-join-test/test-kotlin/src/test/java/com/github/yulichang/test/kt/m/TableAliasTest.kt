package com.github.yulichang.test.kt.m

import com.github.yulichang.kt.KtLambdaWrapper
import com.github.yulichang.test.kt.entity.AddressDO
import com.github.yulichang.test.kt.entity.AreaDO
import com.github.yulichang.test.kt.entity.UserDO
import com.github.yulichang.test.kt.mapper.UserMapper
import com.github.yulichang.test.util.Reset
import com.github.yulichang.test.util.ThreadLocalUtils
import com.github.yulichang.toolkit.KtWrappers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class TableAliasTest {

    @Autowired
    private val userMapper: UserMapper? = null

    @BeforeEach
    fun setUp() {
        Reset.reset()
    }

    @Test
    fun tableAlias() {
        ThreadLocalUtils.set(
            "SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                    "LEFT JOIN address addr1 ON (addr1.id = t.address_id) " +
                    "LEFT JOIN address addr2 ON (addr2.id = t.address_id2) " +
                    "LEFT JOIN area area1 ON (area1.id = addr1.area_id) " +
                    "WHERE t.del = false AND addr1.del = false AND addr2.del = false AND area1.del = false " +
                    "GROUP BY t.id"
        )
        val wrapper: KtLambdaWrapper<UserDO> = KtWrappers.query(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .leftJoin(AddressDO::class.java, "addr1", AddressDO::id, UserDO::addressId)
            .leftJoin(AddressDO::class.java, "addr2", AddressDO::id, UserDO::addressId2)
            .leftJoin(AreaDO::class.java, "area1", AreaDO::id, "addr1", AddressDO::areaId)
            .groupBy(UserDO::id)
        val dos: List<UserDO> = userMapper!!.selectJoinList(UserDO::class.java, wrapper)
        dos.forEach(System.out::println)
    }

    @Test
    fun tableAlias2() {
        ThreadLocalUtils.set(
            "SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                    "LEFT JOIN address addr1 ON (addr1.id = t.address_id) " +
                    "LEFT JOIN address addr2 ON (addr2.id = t.address_id2) " +
                    "LEFT JOIN area area1 ON (area1.id = addr2.area_id) " +
                    "WHERE t.del = false AND addr1.del = false AND addr2.del = false AND area1.del = false " +
                    "GROUP BY t.id ORDER BY addr1.id DESC"
        )
        val wrapper: KtLambdaWrapper<UserDO> = KtWrappers.query(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .leftJoin(AddressDO::class.java, "addr1", AddressDO::id, UserDO::addressId)
            .leftJoin(AddressDO::class.java, "addr2", AddressDO::id, UserDO::addressId2)
            .leftJoin(AreaDO::class.java, "area1", AreaDO::id, "addr2", AddressDO::areaId)
            .groupBy(UserDO::id)
            .orderByDesc("addr1", AddressDO::id)
        val dos: List<UserDO> = userMapper!!.selectJoinList(UserDO::class.java, wrapper)
        dos.forEach(System.out::println)
    }

    @Test
    fun tableAliasEQ() {
        ThreadLocalUtils.set(
            "SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                    "LEFT JOIN address addr1 ON (addr1.id = t.address_id) " +
                    "LEFT JOIN address addr2 ON (addr2.id = t.address_id2) " +
                    "LEFT JOIN area area1 ON (area1.id = addr2.area_id) WHERE t.del = false AND addr1.del = false AND addr2.del = false AND area1.del = false " +
                    "AND (addr1.id = ? AND addr2.id = ? AND addr1.id = ?)"
        )
        val wrapper: KtLambdaWrapper<UserDO> = KtWrappers.query(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .leftJoin(AddressDO::class.java, "addr1", AddressDO::id, UserDO::addressId)
            .leftJoin(AddressDO::class.java, "addr2", AddressDO::id, UserDO::addressId2)
            .leftJoin(AreaDO::class.java, "area1", AreaDO::id, "addr2", AddressDO::areaId)
            .eq("addr1", AddressDO::id, 1)
            .eq("addr2", AddressDO::id, 2)
            .eq("addr1", AddressDO::id, 3)
        val dos: List<UserDO> = userMapper!!.selectJoinList(UserDO::class.java, wrapper)
        dos.forEach(System.out::println)
    }
}
