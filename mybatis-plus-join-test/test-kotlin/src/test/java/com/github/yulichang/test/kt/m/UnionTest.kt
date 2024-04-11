package com.github.yulichang.test.kt.m

import com.github.yulichang.test.kt.entity.AddressDO
import com.github.yulichang.test.kt.entity.AreaDO
import com.github.yulichang.test.kt.entity.UserDO
import com.github.yulichang.test.util.Reset
import com.github.yulichang.test.util.ThreadLocalUtils
import com.github.yulichang.toolkit.KtWrappers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@Suppress("DEPRECATION")
@SpringBootTest
class UnionTest {
    @BeforeEach
    fun setUp() {
        Reset.reset()
    }

    @Test
    fun union() {
        ThreadLocalUtils.set("SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t WHERE t.del=false AND (t.id = ?) UNION SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t WHERE (t.`name` = ? AND (t.`name` = ?)) UNION SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t WHERE t.del=false AND (t.pid = ?)")
        val wrapper = KtWrappers.query(UserDO::class.java)
                .selectAll(UserDO::class.java)
                .eq(UserDO::id, 1)
        val wrapper1 = KtWrappers.query(UserDO::class.java)
                .selectAll(UserDO::class.java)
                .disableLogicDel()
                .eq(UserDO::name, "张三 2")
                .and { w -> w.eq(UserDO::name, "张三 2") }
        val wrapper2 = KtWrappers.query(UserDO::class.java)
                .selectAll(UserDO::class.java)
                .eq(UserDO::pid, 2)
        wrapper.union(wrapper1, wrapper2)
        val list: List<UserDO> = wrapper.list()
        println(wrapper.unionSql)
        assert(list.size == 7 && list[0].id != null)
    }


    @Test
    fun unionAll() {
        ThreadLocalUtils.set("SELECT t.id FROM `user` t WHERE t.del = false AND (t.id = ?) UNION ALL SELECT t.id FROM address t UNION ALL SELECT t.id FROM area t WHERE t.del = false AND (t.id = ?)")
        val wrapper = KtWrappers.query(UserDO::class.java)
                .select(UserDO::id)
                .eq(UserDO::id, 1)
        val wrapper1 = KtWrappers.query(AddressDO::class.java)
                .select(AddressDO::id)
                .disableLogicDel()
        val wrapper2 = KtWrappers.query(AreaDO::class.java)
                .select(AreaDO::id)
                .eq(AreaDO::id, 2)
        wrapper.unionAll(wrapper1, wrapper2)
        val list: List<UserDO> = wrapper.list()
        assert(list.size == 23 && list[0].id != null)
    }

    @Test
    fun unionAll1() {
        ThreadLocalUtils.set("SELECT t.id FROM `user` t WHERE t.del = false AND (t.id = ?) UNION ALL SELECT t.id FROM address t WHERE (t.id = ?) UNION ALL SELECT (SELECT st.id FROM area st WHERE st.del = false AND (st.id = ? AND (st.id = ?))) AS id FROM area t WHERE t.del = false AND (t.id = ? AND (t.id = ?))")
        val wrapper = KtWrappers.query(UserDO::class.java)
                .select(UserDO::id)
                .eq(UserDO::id, 1)
                .unionAll(AddressDO::class.java) { union ->
                    union.select(AddressDO::id)
                            .disableLogicDel()
                            .eq(UserDO::id, 2)
                }
                .unionAll(AreaDO::class.java) { union ->
                    union.selectSub(AreaDO::class.java, { sub ->
                        sub.select(AreaDO::id)
                                .eq(AreaDO::id, 3)
                                .and { and -> and.eq(AreaDO::id, 4) }
                    }, AreaDO::id)
                            .eq(AreaDO::id, 5)
                            .and { and -> and.eq(AreaDO::id, 6) }
                }
        println(wrapper.unionSql)
        val list: List<UserDO> = wrapper.list()
        assert(list.size == 2 && list[0].id != null)
    }
}
