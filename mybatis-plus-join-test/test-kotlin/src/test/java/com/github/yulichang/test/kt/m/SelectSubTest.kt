package com.github.yulichang.test.kt.m

import com.github.yulichang.test.kt.entity.AddressDO
import com.github.yulichang.test.kt.entity.AreaDO
import com.github.yulichang.test.kt.entity.UserDO
import com.github.yulichang.test.util.ThreadLocalUtils
import com.github.yulichang.toolkit.KtWrappers
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SelectSubTest {
    @BeforeEach
    fun setUp() {
        com.github.yulichang.test.util.Reset.reset()
    }

    /**
     * select 子查询
     */
    @org.junit.jupiter.api.Test
    fun sub() {
        ThreadLocalUtils.set("SELECT (SELECT st.id FROM `user` st WHERE st.del = false AND (st.id = t.id AND st.id = ?) LIMIT 1) AS id, (SELECT st.id FROM `user` st WHERE st.del = false AND (st.id = t.id AND st.id = ?) LIMIT 1) AS name FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del = false AND t1.del = false AND (t.id <= ?)")
        val wrapper = KtWrappers.query(UserDO::class.java)
                .selectSub(UserDO::class.java, { w ->
                    w.select(UserDO::id)
                            .eq(UserDO::id, UserDO::id)
                            .eq(UserDO::id, 2)
                            .last("limit 1")
                }, UserDO::id)
                .selectSub(UserDO::class.java, { w ->
                    w.select(UserDO::id)
                            .eq(UserDO::id, UserDO::id)
                            .eq(UserDO::id, 3)
                            .last("limit 1")
                }, UserDO::name)
                .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
                .le(UserDO::id, 100)
        wrapper.list()


        ThreadLocalUtils.set("SELECT (SELECT st.id FROM area st WHERE st.del = false AND (st.id = t1.id) LIMIT 1) AS id FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del = false AND t1.del = false AND (t.id <= ?)")
        val wrapper1 = KtWrappers.query(UserDO::class.java)
                .selectSub(AreaDO::class.java, { w ->
                    w.select(AreaDO::id)
                            .eq(AreaDO::id, AddressDO::id)
                            .last("limit 1")
                }, UserDO::id)
                .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
                .le(UserDO::id, 100)
        wrapper1.list()
    }
}
