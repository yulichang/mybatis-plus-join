package com.github.yulichang.test.kt

import com.baomidou.mybatisplus.core.metadata.IPage
import com.baomidou.mybatisplus.core.toolkit.StringUtils
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.github.yulichang.kt.KtDeleteJoinWrapper
import com.github.yulichang.kt.KtLambdaWrapper
import com.github.yulichang.kt.KtUpdateJoinWrapper
import com.github.yulichang.test.kt.dto.AddressDTO
import com.github.yulichang.test.kt.dto.UserDTO
import com.github.yulichang.test.kt.entity.*
import com.github.yulichang.test.kt.mapper.AddressMapper
import com.github.yulichang.test.kt.mapper.OrderMapper
import com.github.yulichang.test.kt.mapper.UserDTOMapper
import com.github.yulichang.test.kt.mapper.UserMapper
import com.github.yulichang.test.util.Reset
import com.github.yulichang.test.util.ThreadLocalUtils
import com.github.yulichang.toolkit.JoinWrappers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.BadSqlGrammarException
import java.sql.Timestamp
import java.util.*

/**
 * 连表测试类
 * <p>
 * 支持mybatis-plus 查询枚举字段
 * 支持mybatis-plus typeHandle功能
 * <p>
 * 移除了mybatis-plus 逻辑删除支持，逻辑删除需要在连表查询时自己添加对应的条件
 */
@Suppress(
    "all", "unused", "UNUSED_VARIABLE", "PLATFORM_CLASS_MAPPED_TO_KOTLIN",
    "SpringJavaInjectionPointsAutowiringInspection"
)
@SpringBootTest
class LambdaWrapperTest {
    @Autowired
    private val userMapper: UserMapper? = null

    @Autowired
    private val userDTOMapper: UserDTOMapper? = null

    @Autowired
    private val addressMapper: AddressMapper? = null

    @Autowired
    private val orderMapper: OrderMapper? = null

    @BeforeEach
    fun setUp() {
        Reset.reset()
    }

    @Test
    fun testJoin() {
        ThreadLocalUtils.set(
            "SELECT t.id,\n" +
                    "       t.pid,\n" +
                    "       t.`name`,\n" +
                    "       t.`json`,\n" +
                    "       t.sex,\n" +
                    "       t.head_img,\n" +
                    "       t.create_time,\n" +
                    "       t.address_id,\n" +
                    "       t.address_id2,\n" +
                    "       t.del,\n" +
                    "       t.create_by,\n" +
                    "       t.update_by,\n" +
                    "       t1.id  AS joina_id,\n" +
                    "       t1.user_id,\n" +
                    "       t1.area_id,\n" +
                    "       t1.tel,\n" +
                    "       t1.address,\n" +
                    "       t1.del AS joina_del,\n" +
                    "       t2.id  AS joinb_id,\n" +
                    "       t2.province,\n" +
                    "       t2.city,\n" +
                    "       t2.area,\n" +
                    "       t2.postcode,\n" +
                    "       t2.del AS joinb_del\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN address t1 ON (t1.user_id = t.id)\n" +
                    "         LEFT JOIN area t2 ON (t2.id = t1.area_id)\n" +
                    "WHERE t.del = false\n" +
                    "  AND t1.del = false\n" +
                    "  AND t2.del = false\n" +
                    "  AND (t.id <= ?)\n" +
                    "ORDER BY t.id DESC"
        )
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .selectCollection(AddressDO::class.java, UserDTO::addressList) { addr ->
                addr.association(AreaDO::class.java, AddressDTO::area)
            }
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId)
            .le(UserDO::id, 10000)
            .orderByDesc(UserDO::id) 
        val list = userMapper!!.selectJoinList(UserDTO::class.java, wrapper) 
        assert(list[0].addressList != null && list[0]!!.addressList!![0].id != null) 
        list.forEach(System.out::println) 
    }

    @Test
    fun testJoinField() {
        ThreadLocalUtils.set(
            "SELECT t.id,\n" +
                    "       t.pid,\n" +
                    "       t.`name`,\n" +
                    "       t.`json`,\n" +
                    "       t.sex,\n" +
                    "       t.head_img,\n" +
                    "       t.create_time,\n" +
                    "       t.address_id,\n" +
                    "       t.address_id2,\n" +
                    "       t.del,\n" +
                    "       t.create_by,\n" +
                    "       t.update_by,\n" +
                    "       t1.id AS joina_id\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN address t1 ON (t1.user_id = t.id)\n" +
                    "WHERE t.del = false\n" +
                    "  AND t1.del = false\n" +
                    "  AND (t.id <= ?)\n" +
                    "ORDER BY t.id DESC"
        ) 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .selectCollection(AddressDO::class.java, UserDTO::addressIds) { e ->
                e.id(AddressDO::id)
            }
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .le(UserDO::id, 10000)
            .orderByDesc(UserDO::id) 
        val list = userMapper!!.selectJoinList(UserDTO::class.java, wrapper) 
        assert(list[0].addressIds != null) 
        list.forEach(System.out::println) 
    }


    @Test
    fun testJoin1() {
        ThreadLocalUtils.set(
            "SELECT t.id,\n" +
                    "       t.pid,\n" +
                    "       t.`name`,\n" +
                    "       t.`json`,\n" +
                    "       t.sex,\n" +
                    "       t.head_img,\n" +
                    "       t.create_time,\n" +
                    "       t.address_id,\n" +
                    "       t.address_id2,\n" +
                    "       t.del,\n" +
                    "       t.create_by,\n" +
                    "       t.update_by,\n" +
                    "       t1.id  AS joina_id,\n" +
                    "       t1.user_id,\n" +
                    "       t1.area_id,\n" +
                    "       t1.tel,\n" +
                    "       t1.address,\n" +
                    "       t1.del AS joina_del,\n" +
                    "       t2.id  AS joinb_id,\n" +
                    "       t2.province,\n" +
                    "       t2.city,\n" +
                    "       t2.area,\n" +
                    "       t2.postcode,\n" +
                    "       t2.del AS joinb_del\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN address t1 ON (t1.user_id = t.id)\n" +
                    "         LEFT JOIN area t2 ON (t2.id = t1.area_id)\n" +
                    "WHERE t.del = false\n" +
                    "  AND t1.del = false\n" +
                    "  AND t2.del = false\n" +
                    "ORDER BY t.id DESC"
        ) 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .selectCollection(AddressDO::class.java, UserDTO::addressList) { addr ->
                addr.association(AreaDO::class.java, AddressDTO::area)
            }
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId)
            .orderByDesc(UserDO::id) 
        val list = userMapper!!.selectJoinList(UserDTO::class.java, wrapper) 
        assert(list[0]!!.addressList!![0].id != null) 
        list.forEach(System.out::println) 
    }

    /**
     * 基本数据类型测试
     */
    @Test
    fun testWrapper() {
        ThreadLocalUtils.set(
            "SELECT t.id\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN address t1 ON (t1.user_id = t.id)\n" +
                    "         LEFT JOIN area t2 ON (t2.id = t1.area_id)\n" +
                    "WHERE t.del = false\n" +
                    "  AND t1.del = false\n" +
                    "  AND t2.del = false"
        ) 
        //基本数据类型 和 String
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .select(UserDO::id)
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId) 
        val list: List<Integer> = userMapper!!.selectJoinList(Integer::class.java, wrapper) 
        println(list) 


        ThreadLocalUtils.set(
            "SELECT t.create_time\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN address t1 ON (t1.user_id = t.id)\n" +
                    "         LEFT JOIN area t2 ON (t2.id = t1.area_id)\n" +
                    "WHERE t.del = false\n" +
                    "  AND t1.del = false\n" +
                    "  AND t2.del = false"
        ) 
        //java.sql包下的类
        val wrapper1: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .select(UserDO::createTime)
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId) 
        val list1: List<Timestamp> = userMapper.selectJoinList(Timestamp::class.java, wrapper1) 
        println(list1) 
    }


    @Test
    @SuppressWarnings("unchecked")
    fun testMSCache() {
        ThreadLocalUtils.set(
            "SELECT t.id,\n" +
                    "       t.pid,\n" +
                    "       t.`name`,\n" +
                    "       t.`json`,\n" +
                    "       t.sex,\n" +
                    "       t.head_img,\n" +
                    "       t.create_time,\n" +
                    "       t.address_id,\n" +
                    "       t.address_id2,\n" +
                    "       t.del,\n" +
                    "       t.create_by,\n" +
                    "       t.update_by\n" +
                    "FROM `user` t\n" +
                    "WHERE t.id = ?\n" +
                    "  AND t.del = false\n" +
                    "  AND (t.id <= ?)\n" +
                    "ORDER BY t.id ASC, t.`name` ASC"
        ) 
        val user = UserDO() 
        user.id = 1 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(user)
            .selectAll(UserDO::class.java)
            .le(UserDO::id, 100)
            .orderByAsc(UserDO::id, UserDO::name) 
        val list = userMapper!!.selectList(wrapper) 
        list.forEach(System.out::println) 
    }

    @Test
    fun testTableAliasR() {
        ThreadLocalUtils.set(
            "SELECT tt.id,\n" +
                    "       tt.user_id,\n" +
                    "       tt.create_by,\n" +
                    "       tt.update_by,\n" +
                    "       ua.`name` AS userName,\n" +
                    "       ub.`name` AS createName,\n" +
                    "       uc.`name` AS updateName\n" +
                    "FROM user_dto tt\n" +
                    "         LEFT JOIN `user` ua ON (ua.id = tt.user_id)\n" +
                    "         LEFT JOIN `user` ub ON (ub.id = tt.create_by)\n" +
                    "         LEFT JOIN `user` uc ON (uc.id = tt.update_by)\n" +
                    "WHERE ua.del = false\n" +
                    "  AND ub.del = false\n" +
                    "  AND uc.del = false\n" +
                    "  AND (ua.id <= ? AND ub.id >= ?)"
        ) 
        val wrapper: KtLambdaWrapper<UserDto> = JoinWrappers.kt("tt", UserDto::class.java)
            .selectAll(UserDto::class.java)
            .leftJoin(UserDO::class.java, "ua", UserDO::id, UserDto::userId) { ext ->
                ext.selectAs(UserDO::name, UserDto::userName)
                    .le(UserDO::id, 100)
            }
            .leftJoin(UserDO::class.java, "ub", UserDO::id, UserDto::createBy) { ext ->
                ext.selectAs(UserDO::name, UserDto::createName)
                    .ge(UserDO::id, 0)
            }
            .leftJoin(UserDO::class.java, "uc", UserDO::id, UserDto::updateBy) { ext ->
                ext.selectAs(UserDO::name, UserDto::updateName)
            } 
        val userDtos: List<UserDto> = userDTOMapper!!.selectJoinList(UserDto::class.java, wrapper) 
        assert(StringUtils.isNotBlank(userDtos[0].userName)) 
        assert(StringUtils.isNotBlank(userDtos[0].createName)) 
        assert(StringUtils.isNotBlank(userDtos[0].updateName)) 


        ThreadLocalUtils.set(
            "SELECT tt.id,\n" +
                    "       tt.pid,\n" +
                    "       tt.`name`,\n" +
                    "       tt.`json`,\n" +
                    "       tt.sex,\n" +
                    "       tt.head_img,\n" +
                    "       tt.create_time,\n" +
                    "       tt.address_id,\n" +
                    "       tt.address_id2,\n" +
                    "       tt.del,\n" +
                    "       tt.create_by,\n" +
                    "       tt.update_by,\n" +
                    "       ua.id,\n" +
                    "       ub.head_img\n" +
                    "FROM `user` tt\n" +
                    "         LEFT JOIN `user` ua ON (ua.id = tt.pid)\n" +
                    "         LEFT JOIN `user` ub ON (ub.id = tt.create_by)\n" +
                    "         LEFT JOIN `user` uc ON (uc.id = tt.update_by)\n" +
                    "WHERE tt.del = false\n" +
                    "  AND ua.del = false\n" +
                    "  AND ub.del = false\n" +
                    "  AND uc.del = false\n" +
                    "  AND (ua.head_img = tt.`name` AND tt.id = ua.id)"
        ) 
        val w: KtLambdaWrapper<UserDO> = JoinWrappers.kt("tt", UserDO::class.java)
            .selectAll(UserDO::class.java)
            .leftJoin(UserDO::class.java, "ua", UserDO::id, UserDO::pid) { ext ->
                ext.select(UserDO::id)
                    .eq(UserDO::img, UserDO::name)
            }
            .leftJoin(UserDO::class.java, "ub", UserDO::id, UserDO::createBy) { ext ->
                ext.select(UserDO::img)
            }
            .leftJoin(UserDO::class.java, "uc", UserDO::id, UserDO::updateBy)
            .eq(UserDO::id, UserDO::id) 
        userMapper!!.selectJoinList(UserDO::class.java, w) 
        println(1) 
    }

    /**
     * 自连接测试
     */
    @Test
    fun testInner() {
        ThreadLocalUtils.set(
            "SELECT t.id,\n" +
                    "       t.pid,\n" +
                    "       t.`name`,\n" +
                    "       t.`json`,\n" +
                    "       t.sex,\n" +
                    "       t.head_img,\n" +
                    "       t.create_time,\n" +
                    "       t.address_id,\n" +
                    "       t.address_id2,\n" +
                    "       t.del,\n" +
                    "       t.create_by,\n" +
                    "       t.update_by,\n" +
                    "       t1.id          AS joina_id,\n" +
                    "       t1.pid         AS joina_pid,\n" +
                    "       t1.`name`      AS joina_name,\n" +
                    "       t1.`json`      AS joina_json,\n" +
                    "       t1.sex         AS joina_sex,\n" +
                    "       t1.head_img    AS joina_head_img,\n" +
                    "       t1.create_time AS joina_create_time,\n" +
                    "       t1.address_id  AS joina_address_id,\n" +
                    "       t1.address_id2 AS joina_address_id2,\n" +
                    "       t1.del         AS joina_del,\n" +
                    "       t1.create_by   AS joina_create_by,\n" +
                    "       t1.update_by   AS joina_update_by\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN `user` t1 ON (t1.pid = t.id)\n" +
                    "WHERE t.del = false\n" +
                    "  AND (t.id > ?)"
        ) 
        //自连接
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .disableSubLogicDel()//关闭副表逻辑删除
            .selectAll(UserDO::class.java)
            .selectCollection(UserDO::class.java, UserDO::children)
            .leftJoin(UserDO::class.java, UserDO::pid, UserDO::id)
            .gt(UserDO::id, 0) 
        val list: List<UserDO> = userMapper!!.selectJoinList(UserDO::class.java, wrapper) 
        println(list) 

        ThreadLocalUtils.set(
            "SELECT t.id,\n" +
                    "       t.pid,\n" +
                    "       t.`name`,\n" +
                    "       t.`json`,\n" +
                    "       t.sex,\n" +
                    "       t.head_img,\n" +
                    "       t.create_time,\n" +
                    "       t.address_id,\n" +
                    "       t.address_id2,\n" +
                    "       t.del,\n" +
                    "       t.create_by,\n" +
                    "       t.update_by,\n" +
                    "       t1.`name` AS createName,\n" +
                    "       t2.`name` AS updateName\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN `user` t1 ON (t1.id = t.create_by)\n" +
                    "         LEFT JOIN `user` t2 ON (t2.id = t.update_by)\n" +
                    "WHERE (t2.id = t.update_by AND t.id = t1.id)"
        ) 
        //关联一张表多次
        val wrapper1: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .disableLogicDel()
            .disableSubLogicDel()
            .selectAll(UserDO::class.java)
            .leftJoin(UserDO::class.java, UserDO::id, UserDO::createBy) { ext ->
                ext.selectAs(UserDO::name, UserDO::createName)
            }
            .leftJoin(UserDO::class.java) { on, ext ->
                on.eq(UserDO::id, UserDO::updateBy) 
                ext.selectAs(UserDO::name, UserDO::updateName)
                    .eq(UserDO::id, UserDO::updateBy) 
            }
            .eq(UserDO::id, UserDO::id) 
        val dos: List<UserDO> = userMapper.selectJoinList(UserDO::class.java, wrapper1) 
        assert(dos[0].createName != null && dos[0].updateName != null) 


        ThreadLocalUtils.set(
            "SELECT t.id,\n" +
                    "       t.pid,\n" +
                    "       t.`name`,\n" +
                    "       t.`json`,\n" +
                    "       t.sex,\n" +
                    "       t.head_img,\n" +
                    "       t.create_time,\n" +
                    "       t.address_id,\n" +
                    "       t.address_id2,\n" +
                    "       t.del,\n" +
                    "       t.create_by,\n" +
                    "       t.update_by,\n" +
                    "       t1.`name`      AS alias,\n" +
                    "       t1.id          AS joina_id,\n" +
                    "       t1.pid         AS joina_pid,\n" +
                    "       t1.`name`      AS joina_name,\n" +
                    "       t1.`json`      AS joina_json,\n" +
                    "       t1.sex         AS joina_sex,\n" +
                    "       t1.head_img    AS joina_head_img,\n" +
                    "       t1.create_time AS joina_create_time,\n" +
                    "       t1.address_id  AS joina_address_id,\n" +
                    "       t1.address_id2 AS joina_address_id2,\n" +
                    "       t1.del         AS joina_del,\n" +
                    "       t1.create_by   AS joina_create_by,\n" +
                    "       t1.update_by   AS joina_update_by,\n" +
                    "       t2.id          AS joinb_id,\n" +
                    "       t2.pid         AS joinb_pid,\n" +
                    "       t2.`name`      AS joinb_name,\n" +
                    "       t2.`json`      AS joinb_json,\n" +
                    "       t2.sex         AS joinb_sex,\n" +
                    "       t2.head_img    AS joinb_head_img,\n" +
                    "       t2.create_time AS joinb_create_time,\n" +
                    "       t2.address_id  AS joinb_address_id,\n" +
                    "       t2.address_id2 AS joinb_address_id2,\n" +
                    "       t2.del         AS joinb_del,\n" +
                    "       t2.create_by   AS joinb_create_by,\n" +
                    "       t2.update_by   AS joinb_update_by\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN `user` t1 ON (t1.pid = t.id)\n" +
                    "         LEFT JOIN `user` t2 ON (t2.pid = t1.id)\n" +
                    "WHERE t.del = false\n" +
                    "  AND (t1.id <= ? AND t.id <= ?)"
        ) 
        val wrapper2: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .disableSubLogicDel()
            .selectAll(UserDO::class.java)
            .selectCollection("t1", UserDO::class.java, UserDO::children) { c ->
                c.collection("t2", UserDO::class.java, UserDO::children)
            }
            .leftJoin(UserDO::class.java, UserDO::pid, UserDO::id) { ext ->
                ext.selectAs(UserDO::name, UserDO::alias)
                    .leftJoin(UserDO::class.java, UserDO::pid, UserDO::id)
                    .le(UserDO::id, 5)
            }
            .le(UserDO::id, 4) 
        val list1: List<UserDO> = userMapper.selectJoinList(UserDO::class.java, wrapper2) 
        println(list1) 
    }

    /**
     * 逻辑删除测试
     */
    @Test
    fun testLogicDel() {
        val l1: List<UserDTO> = userMapper!!.selectJoinList(UserDTO::class.java, JoinWrappers.kt(UserDO::class.java)) 
        assert(l1.size == 14) 

        val l2: List<UserDTO> = userMapper.selectJoinList(
            UserDTO::class.java, JoinWrappers.kt(UserDO::class.java)
                .selectAll(UserDO::class.java)
                .select(AddressDO::address)
                .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
        ) 
        assert(l2.size == 10) 

        val l3: List<UserDTO> = userMapper.selectJoinList(
            UserDTO::class.java, JoinWrappers.kt(UserDO::class.java)
                .disableSubLogicDel()
                .selectAll(UserDO::class.java)
                .selectCollection(AddressDO::class.java, UserDTO::addressList)
                .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
        ) 
        assert(l3.size == 14 && l3[0].addressList!!.size == 9) 

        val l4: List<UserDTO> = userMapper.selectJoinList(
            UserDTO::class.java,
            JoinWrappers.kt(UserDO::class.java)
                .disableSubLogicDel()
                .selectAll(UserDO::class.java)
                .selectCollection(AddressDO::class.java, UserDTO::addressList)
                .leftJoin(AddressDO::class.java) { on ->
                    on.eq(AddressDO::userId, UserDO::id)
                        .eq(AddressDO::del, false)
                }
        ) 
        assert(l4.size == 14 && l4[0].addressList!!.size == 5) 
    }


    /**
     * 别名测试
     */
    @Test
    fun testAlias() {
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .selectCollection(UserDO::class.java, UserDO::children)
            .leftJoin(UserDO::class.java, UserDO::pid, UserDO::id) 
        val list: List<UserDO> = userMapper!!.selectJoinList(UserDO::class.java, wrapper) 
        assert(list[0].name != null && list[0].children!![0].name != null) 
        assert(list[0].img != null && list[0].children!![0].img != null) 
        println(list) 
    }

    @Test
    fun testTableAlias() {
        ThreadLocalUtils.set(
            "SELECT t.id,\n" +
                    "       t.pid,\n" +
                    "       t.`name`,\n" +
                    "       t.`json`,\n" +
                    "       t.sex,\n" +
                    "       t.head_img,\n" +
                    "       t.create_time,\n" +
                    "       t.address_id,\n" +
                    "       t.address_id2,\n" +
                    "       t.del,\n" +
                    "       t.create_by,\n" +
                    "       t.update_by,\n" +
                    "       aa.id,\n" +
                    "       aa.user_id,\n" +
                    "       aa.area_id,\n" +
                    "       aa.tel,\n" +
                    "       aa.address,\n" +
                    "       aa.del\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN address aa ON (aa.user_id = t.id)\n" +
                    "WHERE t.del = false\n" +
                    "  AND aa.del = false"
        ) 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .selectAll(AddressDO::class.java, "aa")
            .leftJoin(AddressDO::class.java, "aa", AddressDO::userId, UserDO::id) 
        val list: List<UserDO> = userMapper!!.selectJoinList(UserDO::class.java, wrapper) 
        println(list) 
    }

    @Test
    fun testLabel() {
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .disableSubLogicDel()
            .selectAll(UserDO::class.java)
            .selectCollection("t1", AddressDO::class.java, UserDO::addressList)
            .selectCollection("t2", AddressDO::class.java, UserDO::addressList2)
            .leftJoin(AddressDO::class.java, AddressDO::id, UserDO::addressId)
            .leftJoin(AddressDO::class.java, AddressDO::id, UserDO::addressId2) 
        val list: List<UserDO> = userMapper!!.selectJoinList(UserDO::class.java, wrapper) 

        assert(list[0].addressList!![0].address != null) 
        assert(list[0].addressList2!![0].address != null) 
        println(list) 
    }


    /**
     * 简单的分页关联查询 lambda
     */
    @Test
    fun test1() {
        val page: Page<UserDTO> = Page(1, 10) 
        page.setSearchCount(false) 
        val iPage: IPage<UserDTO> = userMapper!!.selectJoinPage(
            page, UserDTO::class.java,
            JoinWrappers.kt(UserDO::class.java)
                .selectAll(
                    UserDO::class.java
                )
                .select(AddressDO::address)
                .select(AreaDO::province)
                .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
                .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId)
        ) 
        iPage.records.forEach(System.out::println) 
    }

    /**
     * 简单的分页关联查询 lambda
     * ON语句多条件
     */
    @Test
    fun test3() {
        ThreadLocalUtils.set(
            "SELECT t.id,\n" +
                    "       t.pid,\n" +
                    "       t.`name`,\n" +
                    "       t.`json`,\n" +
                    "       t.sex,\n" +
                    "       t.head_img,\n" +
                    "       t.create_time,\n" +
                    "       t.address_id,\n" +
                    "       t.address_id2,\n" +
                    "       t.del,\n" +
                    "       t.create_by,\n" +
                    "       t.update_by,\n" +
                    "       t1.address\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN address t1 ON (t.id = t1.user_id AND t.id = t1.user_id)\n" +
                    "WHERE t.del = false\n" +
                    "  AND t1.del = false\n" +
                    "  AND (t.id = ? AND (t.head_img = ? OR t1.user_id = ?) AND t.id = ?)\n" +
                    "LIMIT ?"
        ) 
        val page: IPage<UserDTO> = userMapper!!.selectJoinPage(
            Page(1, 10), UserDTO::class.java,
            JoinWrappers.kt(UserDO::class.java)
                .selectAll(UserDO::class.java)
                .select(AddressDO::address)
                .leftJoin(AddressDO::class.java) { on ->
                    on.eq(UserDO::id, AddressDO::userId)
                        .eq(UserDO::id, AddressDO::userId)
                }
                .eq(UserDO::id, 1)
                .and { i ->
                    i.eq(UserDO::img, "er")
                        .or()
                        .eq(AddressDO::userId, 1)
                }
                .eq(UserDO::id, 1)
        ) 
        page.records.forEach(System.out::println) 
    }

    /**
     * 简单的函数使用
     */
    @Test
    fun test4() {
        val one: UserDTO = userMapper!!.selectJoinOne(
            UserDTO::class.java, JoinWrappers.kt(UserDO::class.java)
                .selectSum(UserDO::id)
                .selectMax(UserDO::id, UserDTO::headImg)
                .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
        ) 
        println(one) 
    }


    /**
     * 忽略个别查询字段
     */
    @Test
    fun test6() {
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .selectFilter(AddressDO::class.java) { true }
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .eq(UserDO::id, 1) 
        val page: IPage<UserDTO> = userMapper!!.selectJoinPage(Page(1, 10), UserDTO::class.java, wrapper) 
        assert(page.records[0].address != null) 
        page.records.forEach(System.out::println) 
    }

    /**
     * 忽略个别查询字段
     */
    @Test
    fun test8() {
        ThreadLocalUtils.set("SELECT t.`name` FROM `user` t WHERE t.del=false AND (t.`name` = ?)") 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .select(UserDO::name)
            .eq(UserDO::name, "ref") 
        userMapper!!.selectList(wrapper) 
        try {
            userMapper.insertBatchSomeColumn(ArrayList()) 
        } catch (ignored: BadSqlGrammarException) {
        }
    }


    /**
     * 关联查询返回map
     */
    @Test
    fun test7() {
        val list: List<Map<String, Any>> = userMapper!!.selectJoinMaps(
            JoinWrappers.kt(UserDO::class.java)
                .selectAll(UserDO::class.java)
                .select(AddressDO::address)
                .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
        ) 
        assert(list[0]["ADDRESS"] != null || list[0]["address"] != null) 
        list.forEach(System.out::println) 
    }

    /**
     * 原生查询
     */
    @Test
    fun testMP() {
        val dos: MutableList<UserDO?>? = userMapper!!.selectList(
            JoinWrappers.kt(UserDO::class.java)
                .gt(UserDO::id, 3)
                .lt(UserDO::id, 8)
        ) 
        assert(dos!!.size == 4) 

        ThreadLocalUtils.set(
            "SELECT id,pid,`name`,`json`,sex,head_img,create_time,address_id,address_id2,del,create_by,update_by FROM `user` t WHERE t.del=false AND (t.id > ? AND t.id < ?)",
            "SELECT * FROM `user` t WHERE t.del=false AND (t.id > ? AND t.id < ?) "
        ) 
        val dos1: MutableList<UserDO?>? = userMapper.selectList(
            JoinWrappers.kt(UserDO::class.java)
                .gt(UserDO::id, 3)
                .lt(UserDO::id, 8)
        ) 
        assert(dos1!!.size == 4) 
    }

    /**
     * 函数测试
     */
    @Test
    fun testFunc() {
        ThreadLocalUtils.set("SELECT if(t1.user_id < 5,t1.user_id,t1.user_id + 100) AS id FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del=false AND t1.del=false") 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectFunc(
                "if(%s < 5,%s,%s + 100)",
                { arg -> arg.accept(AddressDO::userId, AddressDO::userId, AddressDO::userId) }, UserDO::id
            )
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id) 

        try {
            val dos: List<UserDO> = userMapper!!.selectJoinList(UserDO::class.java, wrapper) 
        } catch (_: BadSqlGrammarException) {
        }
    }

    /**
     * 泛型测试
     */
    @Test
    fun testGeneric() {
        val wrapper: KtLambdaWrapper<AddressDO> = JoinWrappers.kt(AddressDO::class.java)
            .selectAll(AddressDO::class.java)
            .le(AddressDO::id, 10000)
            .orderByDesc(AddressDO::id) 
        val list: List<AddressDTO> = addressMapper!!.selectJoinList(AddressDTO::class.java, wrapper) 
        assert(
            Objects.equals(
                "[AddressDTO(id=22, userId=22, areaId=10022, tel=10000000022, address=朝阳22, del=false, areaList=null, area=null), AddressDTO(id=21, userId=21, areaId=10021, tel=10000000021, address=朝阳21, del=false, areaList=null, area=null), AddressDTO(id=20, userId=20, areaId=10020, tel=10000000020, address=朝阳20, del=false, areaList=null, area=null), AddressDTO(id=19, userId=19, areaId=10019, tel=10000000019, address=朝阳19, del=false, areaList=null, area=null), AddressDTO(id=18, userId=18, areaId=10018, tel=10000000018, address=朝阳18, del=false, areaList=null, area=null), AddressDTO(id=17, userId=17, areaId=10017, tel=10000000017, address=朝阳17, del=false, areaList=null, area=null), AddressDTO(id=16, userId=16, areaId=10016, tel=10000000016, address=朝阳16, del=false, areaList=null, area=null), AddressDTO(id=15, userId=15, areaId=10015, tel=10000000015, address=朝阳15, del=false, areaList=null, area=null), AddressDTO(id=14, userId=14, areaId=10014, tel=10000000014, address=朝阳14, del=false, areaList=null, area=null), AddressDTO(id=13, userId=13, areaId=10013, tel=10000000013, address=朝阳13, del=false, areaList=null, area=null), AddressDTO(id=12, userId=12, areaId=10012, tel=10000000012, address=朝阳12, del=false, areaList=null, area=null), AddressDTO(id=11, userId=11, areaId=10011, tel=10000000011, address=朝阳11, del=false, areaList=null, area=null), AddressDTO(id=10, userId=10, areaId=10010, tel=10000000010, address=朝阳10, del=false, areaList=null, area=null), AddressDTO(id=5, userId=1, areaId=10005, tel=10000000005, address=朝阳05, del=false, areaList=null, area=null), AddressDTO(id=4, userId=1, areaId=10004, tel=10000000004, address=朝阳04, del=false, areaList=null, area=null), AddressDTO(id=3, userId=1, areaId=10003, tel=10000000003, address=朝阳03, del=false, areaList=null, area=null), AddressDTO(id=2, userId=1, areaId=10002, tel=10000000002, address=朝阳02, del=false, areaList=null, area=null), AddressDTO(id=1, userId=1, areaId=10001, tel=10000000001, address=朝阳01, del=false, areaList=null, area=null)]",
                list.toString()
            )
        ) 
    }

    /**
     * count
     */
    @Test
    fun testCount() {
        ThreadLocalUtils.set(
            "SELECT COUNT( 1 ) FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) WHERE t.del=false AND t1.del=false AND t2.del=false",
            "SELECT COUNT( * ) FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) WHERE t.del=false AND t1.del=false AND t2.del=false",
            "SELECT COUNT( * ) AS total FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) WHERE t.del=false AND t1.del=false AND t2.del=false"
        ) 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId) 
        val integer: Long = userMapper!!.selectCount(wrapper) 

        ThreadLocalUtils.set("SELECT COUNT( * ) FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) WHERE t.del=false AND t1.del=false AND t2.del=false") 
        val wrapper1: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId) 
        val aLong1: Long = userMapper.selectJoinCount(wrapper1) 
    }


    /**
     * 动态别名
     */
    @Test
    fun testTable() {
        ThreadLocalUtils.set("SELECT t.id FROM `user`bbbbbbb t LEFT JOIN addressaaaaaaaaaa t1 ON (t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) WHERE t.del=false AND t1.del=false AND t2.del=false AND (t.id <= ?) ORDER BY t.id DESC") 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .select(UserDO::id)
            .leftJoin(AddressDO::class.java) { on ->
                on.eq(AddressDO::userId, UserDO::id)
                    .setTableName { name -> name + "aaaaaaaaaa" }
            }
            .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId)
            .le(UserDO::id, 10000)
            .orderByDesc(UserDO::id)
            .setTableName { name -> name + "bbbbbbb" } 
        try {
            val list = userMapper!!.selectJoinList(UserDTO::class.java, wrapper) 
        } catch (_: BadSqlGrammarException) {
        }
    }


    /**
     * 逻辑删除类型
     */
    @Test
    fun logicDelType() {
        ThreadLocalUtils.set(
            "SELECT t.id,\n" +
                    "       t.pid,\n" +
                    "       t.`name`,\n" +
                    "       t.`json`,\n" +
                    "       t.sex,\n" +
                    "       t.head_img,\n" +
                    "       t.create_time,\n" +
                    "       t.address_id,\n" +
                    "       t.address_id2,\n" +
                    "       t.del,\n" +
                    "       t.create_by,\n" +
                    "       t.update_by,\n" +
                    "       t1.id  AS joina_id,\n" +
                    "       t1.user_id,\n" +
                    "       t1.area_id,\n" +
                    "       t1.tel,\n" +
                    "       t1.address,\n" +
                    "       t1.del AS joina_del,\n" +
                    "       t2.id  AS joinb_id,\n" +
                    "       t2.province,\n" +
                    "       t2.city,\n" +
                    "       t2.area,\n" +
                    "       t2.postcode,\n" +
                    "       t2.del AS joinb_del\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN address t1 ON (t1.user_id = t.id AND t1.del = false)\n" +
                    "         LEFT JOIN area t2 ON (t2.id = t1.area_id AND t2.del = false)\n" +
                    "WHERE t.del = false\n" +
                    "  AND (t.id <= ?)\n" +
                    "ORDER BY t.id DESC\n"
        ) 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .logicDelToOn()
            .selectAll(UserDO::class.java)
            .selectCollection(AddressDO::class.java, UserDTO::addressList) { addr ->
                addr.association(AreaDO::class.java, AddressDTO::area)
            }
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId)
            .le(UserDO::id, 10000)
            .orderByDesc(UserDO::id) 
        println(wrapper.from) 
        val list = userMapper!!.selectJoinList(UserDTO::class.java, wrapper) 

        assert(list[0].addressList != null && list[0].addressList!![0].id != null) 
        list.forEach(System.out::println) 
    }

    /**
     * wrappers 测试
     */
    @Test
    fun joinWrapper() {
        ThreadLocalUtils.set(
            "SELECT t.id,\n" +
                    "       t.pid,\n" +
                    "       t.`name`,\n" +
                    "       t.`json`,\n" +
                    "       t.sex,\n" +
                    "       t.head_img,\n" +
                    "       t.create_time,\n" +
                    "       t.address_id,\n" +
                    "       t.address_id2,\n" +
                    "       t.del,\n" +
                    "       t.create_by,\n" +
                    "       t.update_by,\n" +
                    "       t1.id  AS joina_id,\n" +
                    "       t1.user_id,\n" +
                    "       t1.area_id,\n" +
                    "       t1.tel,\n" +
                    "       t1.address,\n" +
                    "       t1.del AS joina_del,\n" +
                    "       t2.id  AS joinb_id,\n" +
                    "       t2.province,\n" +
                    "       t2.city,\n" +
                    "       t2.area,\n" +
                    "       t2.postcode,\n" +
                    "       t2.del AS joinb_del\n" +
                    "FROM `user` t\n" +
                    "         LEFT JOIN address t1 ON (t1.user_id = t.id AND t1.del = false)\n" +
                    "         LEFT JOIN area t2 ON (t2.id = t1.area_id AND t2.del = false)\n" +
                    "WHERE t.del = false\n" +
                    "  AND (t.id <= ?)\n" +
                    "ORDER BY t.id DESC\n"
        ) 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .logicDelToOn()
            .selectAll(UserDO::class.java)
            .selectCollection(AddressDO::class.java, UserDTO::addressList) { addr ->
                addr.association(AreaDO::class.java, AddressDTO::area)
            }
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId)
            .le(UserDO::id, 10000)
            .orderByDesc(UserDO::id) 

        val list = wrapper.list(UserDTO::class.java) 

        println(list) 
        assert(list[0].addressList != null && list[0].addressList!![0].id != null) 
        list.forEach(System.out::println) 
    }

    @Test
    fun joinRandomMap() {
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .logicDelToOn()
            .selectAll(UserDO::class.java)
            .selectCollection(UserDTO::addressList) { addr ->
                addr.id(AddressDO::id, AddressDTO::id)
                    .result(UserDO::name, AddressDTO::address)
                    .collection(AddressDTO::areaList) { map ->
                        map.id(AreaDO::id)
                            .result(AreaDO::area, AreaDO::area)
                    }
            }
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId)
            .le(UserDO::id, 10000)
            .orderByDesc(UserDO::id) 

        val list: MutableList<UserDTO> = wrapper.list(UserDTO::class.java) 
        println(list) 
        assert(list[0].addressList != null && list[0].addressList!![0].id != null) 
        list.forEach(System.out::println) 
    }

    @Test
    fun joinRandomMap111() {
        ThreadLocalUtils.set("SELECT t.id,t.user_id,t.area_id,t.tel,t.address,t.del FROM address t LEFT JOIN `user` t1 ON (t1.address_id = t.id) LEFT JOIN `user` t2 ON (t2.pid = t1.id) WHERE t.del=false AND t1.del=false AND t2.del=false") 
        val wrapper: KtLambdaWrapper<AddressDO> = JoinWrappers.kt(AddressDO::class.java)
            .selectAll(AddressDO::class.java)
            .leftJoin(UserDO::class.java, UserDO::addressId, AddressDO::id)
            .leftJoin(UserDO::class.java, UserDO::pid, UserDO::id) 

        val addressDOS: List<AddressDO> = wrapper.list() 
    }

    /**
     * 同一个类字段比较
     */
    @Test
    fun joinOwn() {
        ThreadLocalUtils.set("SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del=false AND t1.del=false AND (t1.id = t1.id)") 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .eq(AddressDO::id, AddressDO::id) 

        val addressDOS: List<UserDO> = wrapper.list() 
    }

    /**
     * 同一个类字段比较
     */
    @Test
    fun joinOwn1() {
        ThreadLocalUtils.set("SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t LEFT JOIN address aaa ON (aaa.user_id = t.id) WHERE t.del=false AND aaa.del=false AND (aaa.id = t.id AND aaa.id = aaa.id)") 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectAll(UserDO::class.java)
            .leftJoin(AddressDO::class.java, "aaa", AddressDO::userId, UserDO::id) { ext ->
                ext.eq(AddressDO::id, AddressDO::id)
            }
            .eq(AddressDO::id, AddressDO::id) 
        val addressDOS: List<UserDO> = wrapper.list() 
    }

    /**
     * 同一个类字段比较
     */
    @Test
    fun joinOrder() {
        ThreadLocalUtils.set(
            "SELECT id,user_id,name FROM order_t t ORDER BY t.name DESC",
            "SELECT id,user_id,name FROM order_t t ORDER BY t.name desc"
        ) 
        val wrapper: KtLambdaWrapper<OrderDO> = JoinWrappers.kt(OrderDO::class.java) 
        val list: List<OrderDO> = wrapper.list() 

        ThreadLocalUtils.set(
            "SELECT t.id,t.user_id,t.name,t1.`name` AS userName FROM order_t t LEFT JOIN `user` t1 ON (t1.id = t.user_id) WHERE t1.del=false ORDER BY t.name DESC",
            "SELECT t.id,t.user_id,t.name,t1.`name` AS userName FROM order_t t LEFT JOIN `user` t1 ON (t1.id = t.user_id) WHERE t1.del=false ORDER BY t.name desc"
        ) 
        val w: KtLambdaWrapper<OrderDO> = JoinWrappers.kt(OrderDO::class.java)
            .selectAll(OrderDO::class.java)
            .selectAs(UserDO::name, OrderDO::userName)
            .leftJoin(UserDO::class.java, UserDO::id, OrderDO::userId) 
        println(wrapper.from) 
        val l: List<OrderDO> = w.list() 
    }

    /**
     * 同一个类字段比较
     */
    @Test
    fun delete() {
        //物理删除
        ThreadLocalUtils.set("DELETE t FROM order_t t LEFT JOIN user_dto t1 ON (t1.id = t.user_id) WHERE (t.id = ?)") 
        val w: KtDeleteJoinWrapper<OrderDO> = JoinWrappers.ktDelete(OrderDO::class.java)
            .leftJoin(UserDto::class.java, UserDto::id, OrderDO::userId)
            .eq(OrderDO::id, 1) 
        try {
            orderMapper!!.deleteJoin(w) 
        } catch (_: BadSqlGrammarException) {
            //忽略异常 h2不支持连表删除
        }
        //逻辑删除
        ThreadLocalUtils.set("UPDATE `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) SET t.del=true ,t1.del=true,t2.del=true WHERE t.del=false AND t1.del=false AND t2.del=false AND (t.id = ?)") 
        val wrapper: KtDeleteJoinWrapper<UserDO> = JoinWrappers.ktDelete(UserDO::class.java)
            .deleteAll()
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .leftJoin(AreaDO::class.java, AreaDO::id, AddressDO::areaId)
            .eq(OrderDO::id, 1) 
        try {
            userMapper!!.deleteJoin(wrapper) 
        } catch (_: BadSqlGrammarException) {
            //忽略异常 h2不支持连表删除
        }
    }

    @Test
    fun update() {
        val address = AddressDO()
        address.address = "sadf"
        address.tel = "qqqqqqqq"

        val user = UserDO()
        user.name = "sadf"
        user.updateBy = 1

        val address1 = AddressDO()

        val user1 = UserDO()
        user1.updateBy = 123123

        ThreadLocalUtils.set("UPDATE `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) SET t.update_by=?, t.`name`=?,t1.address=?,t1.tel=?,t1.address=?,t.`name`=?,t.update_by=?,t1.user_id=?,t1.area_id=?,t1.tel=?,t1.address=? WHERE t.del=false AND t1.del=false AND (t.id = ?)") 
        val update: KtUpdateJoinWrapper<UserDO> = JoinWrappers.ktUpdate(UserDO::class.java)
            .set(UserDO::name, "aaaaaa")
            .set(AddressDO::address, "bbbbb")
            .setUpdateEntity(address, user)
            .setUpdateEntityAndNull(address1)
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .eq(OrderDO::id, 1)
        try {
            val join = userMapper!!.updateJoin(user1, update) 
        } catch (_: BadSqlGrammarException) {
            //忽略异常 h2不支持连表删除
        }


        ThreadLocalUtils.set("UPDATE `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) SET t.pid=?, t.`name`=?, t.`json`=?, t.sex=?, t.head_img=?, t.create_time=?, t.address_id=?, t.address_id2=?, t.create_by=?, t.update_by=? WHERE t.del=false AND t1.del=false AND (t.id = ?)") 
        val update1: KtUpdateJoinWrapper<UserDO> = JoinWrappers.ktUpdate(UserDO::class.java)
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .eq(OrderDO::id, 1)
        try {
            userMapper!!.updateJoinAndNull(UserDO(), update1)
        } catch (_: BadSqlGrammarException) {
            //忽略异常 h2不支持连表删除
        }
    }

    /**
     * select 子查询
     */
    @Test
    fun sub() {
        ThreadLocalUtils.set("SELECT ( SELECT st.id FROM `user` st WHERE st.del=false AND (st.id = t.id) limit 1 ) AS id FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del=false AND t1.del=false AND (t.id <= ?)") 
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectSub(
                UserDO::class.java, { w ->
                    w.select(UserDO::id)
                        .eq(UserDO::id, UserDO::id)
                        .last("limit 1")
                }, UserDO::id
            )
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .le(UserDO::id, 100) 
        wrapper.list() 

        ThreadLocalUtils.set("SELECT ( SELECT st.id FROM address st WHERE st.del=false AND (st.id = t.id) limit 1 ) AS id FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del=false AND t1.del=false AND (t.id <= ?)") 
        val wrapper1: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectSub(AddressDO::class.java, { w ->
                w.select(AddressDO::id)
                    .eq(AddressDO::id, UserDO::id)
                    .last("limit 1")
            }, UserDO::id)
            .leftJoin(AddressDO::class.java, AddressDO::userId, UserDO::id)
            .le(UserDO::id, 100) 
        wrapper1.list() 
    }


    /**
     * select 子查询
     */
    @Test
    fun union() {
        ThreadLocalUtils.set("SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t WHERE t.del=false UNION SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t WHERE t.del=false UNION SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t WHERE t.del=false")
        val wrapper: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectAll(UserDO::class.java) 
        val wrapper1: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectAll(UserDO::class.java) 
        val wrapper2: KtLambdaWrapper<UserDO> = JoinWrappers.kt(UserDO::class.java)
            .selectAll(UserDO::class.java) 

        wrapper.union(wrapper1, wrapper2) 
        wrapper.list() 
        println(1) 
    }
}
