package com.github.yulichang.test.kt.m

import com.github.yulichang.test.kt.dto.AreaDTO
import com.github.yulichang.test.kt.entity.AreaDO
import com.github.yulichang.test.kt.entity.UserDto
import com.github.yulichang.test.kt.mapper.AreaMapper
import com.github.yulichang.test.util.Reset
import com.github.yulichang.toolkit.KtWrappers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class FieldNameTest {
    @Autowired
    private val areaMapper: AreaMapper? = null

    @BeforeEach
    fun setUp() {
        Reset.reset()
    }

    @Test
    fun testFieldName() {
        val list = areaMapper?.selectJoinList(AreaDO::class.java, KtWrappers.query(AreaDO::class.java)
                .select(AreaDO::Postcode)
                .leftJoin(UserDto::class.java, UserDto::id, AreaDO::id))
        list?.forEach(System.out::println)
        assert(list?.get(0)?.Postcode != null)
    }

    @Test
    fun testFieldName1() {
        val list = areaMapper?.selectJoinList(AreaDTO::class.java, KtWrappers.query(AreaDO::class.java)
                .selectAs(AreaDO::Postcode, AreaDTO::postcode)
                .leftJoin(UserDto::class.java, UserDto::id, AreaDO::id))
        list?.forEach(System.out::println)
        assert(list?.get(0)?.postcode != null)
    }
}
