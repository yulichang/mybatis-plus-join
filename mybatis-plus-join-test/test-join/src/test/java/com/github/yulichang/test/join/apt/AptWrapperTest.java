package com.github.yulichang.test.join.apt;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.adapter.base.tookit.VersionUtils;
import com.github.yulichang.extension.apt.AptQueryWrapper;
import com.github.yulichang.extension.apt.toolkit.AptWrappers;
import com.github.yulichang.test.join.dto.AddressDTO;
import com.github.yulichang.test.join.dto.UserDTO;
import com.github.yulichang.test.join.dto.UserTenantDTO;
import com.github.yulichang.test.join.dto.UserTenantDescDTO;
import com.github.yulichang.test.join.entity.*;
import com.github.yulichang.test.join.entity.apt.*;
import com.github.yulichang.test.join.mapper.*;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.StrUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.yulichang.test.join.entity.tables.Tables.*;

/**
 * 连表测试类
 * <p>
 * 支持mybatis-plus 查询枚举字段
 * 支持mybatis-plus typeHandle功能
 * <p>
 * 移除了mybatis-plus 逻辑删除支持，逻辑删除需要在连表查询时自己添加对应的条件
 */
@SuppressWarnings("unused")
@SpringBootTest
class AptWrapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDTOMapper userDTOMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserTenantMapper userTenantMapper;

    @BeforeEach
    void setUp() {
        Reset.reset();
    }

    @Test
    void testSelectSort() {
        ThreadLocalUtils.set("SELECT t.id, t.user_id, t.tenant_id FROM user_tenant t WHERE t.tenant_id = 1");

        UserTenantDOCol ut = USERTENANTDO;

        AptQueryWrapper<UserTenantDO> lambda = AptWrappers.query(ut)
                .selectAsClass(ut, UserTenantDTO.class);
        List<UserTenantDO> list = userTenantMapper.selectJoinList(UserTenantDO.class, lambda);
        assert list.size() == 5 && list.get(0).getIdea() != null;


        ThreadLocalUtils.set("SELECT t.tenant_id, t.user_id, t.id FROM user_tenant t WHERE t.tenant_id = 1");
        AptQueryWrapper<UserTenantDO> lambda1 = AptWrappers.query(ut)
                .selectAsClass(ut, UserTenantDescDTO.class);
        List<UserTenantDO> list1 = userTenantMapper.selectJoinList(UserTenantDO.class, lambda1);
        assert list1.size() == 5 && list1.get(0).getIdea() != null;
    }

    @Test
    void testSimple() {
        UserTenantDOCol ut = USERTENANTDO;
        AptQueryWrapper<UserTenantDO> lambda = AptWrappers.query(ut);
        lambda.selectAs(ut.idea, UserTenantDO::getIdea);
        List<UserTenantDO> list = userTenantMapper.selectList(lambda);

        assert list.size() == 5 && list.get(0).getIdea() != null;
    }

    @Test
    void testJoin() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.id  AS joina_id,
                       t1.user_id,
                       t1.area_id,
                       t1.tel,
                       t1.address,
                       t1.del AS joina_del,
                       t2.id  AS joinb_id,
                       t2.province,
                       t2.city,
                       t2.area,
                       t2.postcode,
                       t2.del AS joinb_del
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                         LEFT JOIN area t2 ON (t2.id = t1.area_id)
                WHERE t.del = false
                  AND t1.del = false
                  AND t2.del = false
                  AND (t.id <= ?)
                ORDER BY t.id DESC""");

        UserDOCol u = USERDO;
        AddressDOCol addr = ADDRESSDO;
        AreaDOCol ar = AREADO;

        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
                .selectAll()
                .selectCollection(addr, UserDTO::getAddressList, ad -> ad
                        .association(ar, AddressDTO::getArea))
                .leftJoin(addr, addr.userId, u.id)
                .leftJoin(ar, ar.id, addr.areaId)
                .le(u.id, 10000)
                .orderByDesc(u.id);

        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        assert wrapper.checkJoinTable(AddressDO.class);
        assert wrapper.checkJoinTable(AreaDO.class);
        assert !wrapper.checkJoinTable(UserDO.class);
        assert !wrapper.checkJoinTable(UserDto.class);

        assert list.get(0).getAddressList() != null && list.get(0).getAddressList().get(0).getId() != null;
        list.forEach(System.out::println);
    }

    @Test
    void testJoinField() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.id AS joina_id
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                WHERE t.del = false
                  AND t1.del = false
                  AND (t.id <= ?)
                ORDER BY t.id DESC""");

        UserDOCol u = USERDO;
        AddressDOCol addr = ADDRESSDO;
        AreaDOCol ar = AREADO;

        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
                .selectAll()
                .selectCollection(addr, UserDTO::getAddressIds, e -> e
                        .id(addr.id))
                .leftJoin(addr, addr.userId, u.id)
                .le(u.id, 10000)
                .orderByDesc(u.id);
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        assert list.get(0).getAddressIds() != null;
        list.forEach(System.out::println);
    }


    @Test
    void testJoin1() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.id  AS joina_id,
                       t1.user_id,
                       t1.area_id,
                       t1.tel,
                       t1.address,
                       t1.del AS joina_del,
                       t2.id  AS joinb_id,
                       t2.province,
                       t2.city,
                       t2.area,
                       t2.postcode,
                       t2.del AS joinb_del
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                         LEFT JOIN area t2 ON (t2.id = t1.area_id)
                WHERE t.del = false
                  AND t1.del = false
                  AND t2.del = false
                ORDER BY t.id DESC""");

        UserDOCol u = USERDO;
        AddressDOCol addr = ADDRESSDO;
        AreaDOCol ar = AREADO;

        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
                .selectAll()
                .selectCollection(addr, UserDTO::getAddressList, ad -> ad
                        .association(ar, AddressDTO::getArea))
                .leftJoin(addr, addr.userId, u.id)
                .leftJoin(ar, ar.id, addr.areaId)
                .orderByDesc(u.id);
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        assert list.get(0).getAddressList().get(0).getId() != null;
        list.forEach(System.out::println);
    }

    /**
     * 基本数据类型测试
     */
    @Test
    void testWrapper() {
        ThreadLocalUtils.set("""
                SELECT t.id
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                         LEFT JOIN area t2 ON (t2.id = t1.area_id)
                WHERE t.del = false
                  AND t1.del = false
                  AND t2.del = false""");

        UserDOCol u = USERDO;
        AddressDOCol addr = ADDRESSDO;
        AreaDOCol ar = AREADO;

        //基本数据类型 和 String
        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
                .select(u.id)
                .leftJoin(addr, addr.userId, u.id)
                .leftJoin(ar, ar.id, addr.areaId);
        List<Integer> list = userMapper.selectJoinList(Integer.class, wrapper);

        assert list.get(0) != null;
        System.out.println(list);


        ThreadLocalUtils.set("""
                SELECT t.create_time
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                         LEFT JOIN area t2 ON (t2.id = t1.area_id)
                WHERE t.del = false
                  AND t1.del = false
                  AND t2.del = false""");
        //java.sql包下的类
        AptQueryWrapper<UserDO> wrapper1 = new AptQueryWrapper<>(u)
                .select(u.createTime)
                .leftJoin(addr, addr.userId, u.id)
                .leftJoin(ar, ar.id, addr.areaId);
        List<Timestamp> list1 = userMapper.selectJoinList(Timestamp.class, wrapper1);

        assert list1.get(0) != null;
        System.out.println(list);
    }


    @Test
    void testMSCache() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by
                FROM `user` t
                WHERE t.id = ?
                  AND t.del = false
                  AND (t.id <= ?)
                ORDER BY t.id ASC, t.`name` ASC""");

        UserDOCol u = USERDO;
        AddressDOCol addr = ADDRESSDO;
        AreaDOCol ar = AREADO;

        UserDO userDO = new UserDO();
        userDO.setId(1);
        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u, userDO)
                .selectAll()
                .le(u.id, 100)
                .orderByAsc(u.id, u.name);

        List<UserDO> list = userMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    @Test
    void testTableAliasR() {
        ThreadLocalUtils.set("""
                SELECT tt.id,
                       tt.user_id,
                       tt.create_by,
                       tt.update_by,
                       ua.`name` AS userName,
                       ub.`name` AS createName,
                       uc.`name` AS updateName
                FROM user_dto tt
                         LEFT JOIN `user` ua ON (ua.id = tt.user_id)
                         LEFT JOIN `user` ub ON (ub.id = tt.create_by)
                         LEFT JOIN `user` uc ON (uc.id = tt.update_by)
                WHERE ua.del = false
                  AND ub.del = false
                  AND uc.del = false
                  AND (ua.id <= ? AND ub.id >= ?)""");

        UserDtoCol tt = UserDtoCol.build("tt");
        UserDOCol ua = UserDOCol.build("ua");
        UserDOCol ub = UserDOCol.build("ub");
        UserDOCol uc = UserDOCol.build("uc");

        AptQueryWrapper<UserDto> wrapper = new AptQueryWrapper<>(tt)
                .selectAll()
                .selectAs(ua.name, UserDto::getUserName)
                .selectAs(ub.name, UserDto::getCreateName)
                .selectAs(uc.name, UserDto::getUpdateName)
                .leftJoin(ua, ua.id, tt.userId)
                .leftJoin(ub, ub.id, tt.createBy)
                .leftJoin(uc, uc.id, tt.updateBy)
                .le(ua.id, 100)
                .ge(ub.id, 0);

        List<UserDto> userDtos = userDTOMapper.selectJoinList(UserDto.class, wrapper);
        assert StrUtils.isNotBlank(userDtos.get(0).getUserName());
        assert StrUtils.isNotBlank(userDtos.get(0).getCreateName());
        assert StrUtils.isNotBlank(userDtos.get(0).getUpdateName());


        ThreadLocalUtils.set("""
                SELECT tt.id,
                       tt.pid,
                       tt.`name`,
                       tt.`json`,
                       tt.sex,
                       tt.head_img,
                       tt.create_time,
                       tt.address_id,
                       tt.address_id2,
                       tt.del,
                       tt.create_by,
                       tt.update_by,
                       ua.id,
                       ub.head_img
                FROM `user` tt
                         LEFT JOIN `user` ua ON (ua.id = tt.pid)
                         LEFT JOIN `user` ub ON (ub.id = tt.create_by)
                         LEFT JOIN `user` uc ON (uc.id = tt.update_by)
                WHERE tt.del = false
                  AND ua.del = false
                  AND ub.del = false
                  AND uc.del = false
                  AND (ua.head_img = tt.`name` AND tt.id = ua.id)""");

        UserDOCol ut = UserDOCol.build("tt");
        UserDOCol ua1 = UserDOCol.build("ua");
        UserDOCol ub1 = UserDOCol.build("ub");
        UserDOCol uc1 = UserDOCol.build("uc");

        AptQueryWrapper<UserDO> w = new AptQueryWrapper<>(ut)
                .selectAll()
                .select(ua1.id)
                .select(ub1.img)
                .leftJoin(ua1, ua1.id, ut.pid)
                .leftJoin(ub1, ub1.id, ut.createBy)
                .leftJoin(uc1, uc1.id, ut.updateBy)
                .eq(ua1.img, ut.name)
                .eq(ut.id, ua1.id);

        userMapper.selectJoinList(UserDO.class, w);
        System.out.println(1);
    }

    /**
     * 自连接测试
     */
    @Test
    void testInner() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.id          AS joina_id,
                       t1.pid         AS joina_pid,
                       t1.`name`      AS joina_name,
                       t1.`json`      AS joina_json,
                       t1.sex         AS joina_sex,
                       t1.head_img    AS joina_head_img,
                       t1.create_time AS joina_create_time,
                       t1.address_id  AS joina_address_id,
                       t1.address_id2 AS joina_address_id2,
                       t1.del         AS joina_del,
                       t1.create_by   AS joina_create_by,
                       t1.update_by   AS joina_update_by
                FROM `user` t
                         LEFT JOIN `user` t1 ON (t1.pid = t.id)
                WHERE t.del = false
                  AND (t.id > ?)""");

        UserDOCol u = USERDO;
        UserDOCol ua = UserDOCol.build();

        //自连接
        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
                .disableSubLogicDel()//关闭副表逻辑删除
                .selectAll()
                .selectCollection(ua, UserDO::getChildren)
                .leftJoin(ua, ua.pid, u.id)
                .gt(u.id, 0);
        List<UserDO> list = userMapper.selectJoinList(UserDO.class, wrapper);
        System.out.println(list);

        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.`name` AS createName,
                       t2.`name` AS updateName
                FROM `user` t
                         LEFT JOIN `user` t1 ON (t1.id = t.create_by)
                         LEFT JOIN `user` t2 ON (t2.id = t.update_by)
                WHERE (t2.id = t.update_by AND t.id = t1.id)""");

        UserDOCol uu = UserDOCol.build();
        UserDOCol uua = UserDOCol.build();
        UserDOCol uub = UserDOCol.build();

        //关联一张表多次
        AptQueryWrapper<UserDO> w = new AptQueryWrapper<>(uu)
                .disableLogicDel()
                .disableSubLogicDel()
                .selectAll(uu)
                .selectAs(uua.name, UserDO::getCreateName)
                .selectAs(uub.name, UserDO::getUpdateName)
                .leftJoin(uua, uua.id, uu.createBy)
                .leftJoin(uub, uub.id, uu.updateBy)
                .eq(uub.id, uu.updateBy)
                .eq(uu.id, uua.id);
        List<UserDO> dos = userMapper.selectJoinList(UserDO.class, w);
        assert dos.get(0).getCreateName() != null && dos.get(0).getUpdateName() != null;


        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.`name`      AS alias,
                       t1.id          AS joina_id,
                       t1.pid         AS joina_pid,
                       t1.`name`      AS joina_name,
                       t1.`json`      AS joina_json,
                       t1.sex         AS joina_sex,
                       t1.head_img    AS joina_head_img,
                       t1.create_time AS joina_create_time,
                       t1.address_id  AS joina_address_id,
                       t1.address_id2 AS joina_address_id2,
                       t1.del         AS joina_del,
                       t1.create_by   AS joina_create_by,
                       t1.update_by   AS joina_update_by,
                       t2.id          AS joinb_id,
                       t2.pid         AS joinb_pid,
                       t2.`name`      AS joinb_name,
                       t2.`json`      AS joinb_json,
                       t2.sex         AS joinb_sex,
                       t2.head_img    AS joinb_head_img,
                       t2.create_time AS joinb_create_time,
                       t2.address_id  AS joinb_address_id,
                       t2.address_id2 AS joinb_address_id2,
                       t2.del         AS joinb_del,
                       t2.create_by   AS joinb_create_by,
                       t2.update_by   AS joinb_update_by
                FROM `user` t
                         LEFT JOIN `user` t1 ON (t1.pid = t.id)
                         LEFT JOIN `user` t2 ON (t2.pid = t1.id)
                WHERE t.del = false
                  AND (t1.id <= ? AND t.id <= ?)""");

        UserDOCol uuu = UserDOCol.build();
        UserDOCol uuua = UserDOCol.build();
        UserDOCol uuub = UserDOCol.build();

        AptQueryWrapper<UserDO> wrapper1 = new AptQueryWrapper<>(uuu)
                .disableSubLogicDel()
                .selectAll()
                .selectAs(uuua.name, UserDO::getAlias)
                .selectCollection(uuua, UserDO::getChildren, c -> c
                        .collection(uuub, UserDO::getChildren))
                .leftJoin(uuua, uuua.pid, uuu.id)
                .leftJoin(uuub, uuub.pid, uuua.id)
                .le(uuua.id, 5)
                .le(uuu.id, 4);
        List<UserDO> list1 = userMapper.selectJoinList(UserDO.class, wrapper1);
        System.out.println(list1);
    }

    /**
     * 逻辑删除测试
     */
    @Test
    void testLogicDel() {
        UserDOCol u1 = UserDOCol.build();
        List<UserDTO> l1 = userMapper.selectJoinList(UserDTO.class, new AptQueryWrapper<>(u1));
        assert l1.size() == 14;

        UserDOCol u2 = UserDOCol.build();
        AddressDOCol addr2 = AddressDOCol.build();
        List<UserDTO> l2 = userMapper.selectJoinList(UserDTO.class, new AptQueryWrapper<>(u2)
                .selectAll()
                .select(addr2.address)
                .leftJoin(addr2, addr2.userId, u2.id));
        assert l2.size() == 10;

        UserDOCol u3 = UserDOCol.build();
        AddressDOCol addr3 = AddressDOCol.build();
        List<UserDTO> l3 = userMapper.selectJoinList(UserDTO.class, new AptQueryWrapper<>(u3)
                .disableSubLogicDel()
                .selectAll()
                .selectCollection(addr3, UserDTO::getAddressList)
                .leftJoin(addr3, addr3.userId, u3.id));
        assert l3.size() == 14 && l3.get(0).getAddressList().size() == 9;

        UserDOCol u4 = UserDOCol.build();
        AddressDOCol addr4 = AddressDOCol.build();
        List<UserDTO> l4 = userMapper.selectJoinList(UserDTO.class, new AptQueryWrapper<>(u4)
                .disableSubLogicDel()
                .selectAll()
                .selectCollection(addr4, UserDTO::getAddressList)
                .leftJoin(addr4, on -> on
                        .eq(addr4.userId, u4.id)
                        .eq(addr4.del, false)));
        assert l4.size() == 14 && l4.get(0).getAddressList().size() == 5;
    }

    /**
     * 别名测试
     */
    @Test
    void testAlias() {
        UserDOCol u = UserDOCol.build();
        UserDOCol uu = UserDOCol.build();
        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
//                .disableSubLogicDel()//关闭副表逻辑删除
//                .disableLogicDel()//关闭主表逻辑删除
                .selectAll()
                .selectCollection(uu, UserDO::getChildren)
                .leftJoin(uu, uu.pid, u.id);
        List<UserDO> list = userMapper.selectJoinList(UserDO.class, wrapper);
        assert list.get(0).getName() != null && list.get(0).getChildren().get(0).getName() != null;
        assert list.get(0).getImg() != null && list.get(0).getChildren().get(0).getImg() != null;
        System.out.println(list);
    }

    /**
     * 别名测试
     */
    @Test
    void testObj() {
        ThreadLocalUtils.set("SELECT DISTINCT t.id FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del=false AND t1.del=false ORDER BY t.id DESC");

        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
                .distinct()
                .select(u.id)
                .leftJoin(addr, addr.userId, u.id)
                .orderByDesc(u.id);
        List<Object> list = userMapper.selectObjs(wrapper);
    }

    @Test
    void testTableAlias() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       aa.id,
                       aa.user_id,
                       aa.area_id,
                       aa.tel,
                       aa.address,
                       aa.del
                FROM `user` t
                         LEFT JOIN address aa ON (aa.user_id = t.id)
                WHERE t.del = false
                  AND aa.del = false""");

        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build("aa");
        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
//                .disableLogicDel()//关闭主表逻辑删除
                .selectAll()
                .selectAll(addr)
//                .selectCollection(UserDO.class, UserDO::getChildren)
                .leftJoin(addr, addr.userId, u.id);
        List<UserDO> list = userMapper.selectJoinList(UserDO.class, wrapper);

        System.out.println(list);
    }

    @Test
    void testLabel() {
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr1 = AddressDOCol.build("t1");
        AddressDOCol addr2 = AddressDOCol.build("t2");

        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
                .disableSubLogicDel()
                .selectAll()
                .selectCollection(addr1, UserDO::getAddressList)
                .selectCollection(addr2, UserDO::getAddressList2)
                .leftJoin(addr1, addr1.id, u.addressId)
                .leftJoin(addr2, addr2.id, u.addressId2);
        List<UserDO> list = userMapper.selectJoinList(UserDO.class, wrapper);

        assert list.get(0).getAddressList().get(0).getAddress() != null;
        assert list.get(0).getAddressList2().get(0).getAddress() != null;
        System.out.println(list);
    }


    /**
     * 简单的分页关联查询 lambda
     */
    @Test
    void test1() {
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AreaDOCol ar = AreaDOCol.build();

        Page<UserDTO> page = new Page<>(1, 10);
        page.setSearchCount(false);
        IPage<UserDTO> iPage = userMapper.selectJoinPage(page, UserDTO.class, AptWrappers.query(u)
                .selectAll()
                .select(addr.address)
                .select(ar.province)
                .leftJoin(addr, addr.userId, u.id)
                .leftJoin(ar, ar.id, addr.areaId));
        iPage.getRecords().forEach(System.out::println);
    }

    /**
     * 简单的分页关联查询 lambda
     * ON语句多条件
     */
    @Test
    void test3() {
        ThreadLocalUtils.set("""
                        SELECT t.id,
                               t.pid,
                               t.`name`,
                               t.`json`,
                               t.sex,
                               t.head_img,
                               t.create_time,
                               t.address_id,
                               t.address_id2,
                               t.del,
                               t.create_by,
                               t.update_by,
                               t1.address
                        FROM `user` t
                                 LEFT JOIN address t1 ON (t.id = t1.user_id AND t.id = t1.user_id)
                        WHERE t.del = false
                          AND t1.del = false
                          AND (t.id = ? AND (t.head_img = ? OR t1.user_id = ?) AND t.id = ?)
                        LIMIT ?""",
                "SELECT * FROM ( SELECT TMP.*, ROWNUM ROW_ID FROM ( SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, " +
                        "t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by, t1.address FROM `user` t " +
                        "LEFT JOIN address t1 ON (t.id = t1.user_id AND t.id = t1.user_id) WHERE t.del = false AND t1.del = false AND " +
                        "(t.id = ? AND (t.head_img = ? OR t1.user_id = ?) AND t.id = ?) ) TMP WHERE ROWNUM <=?) WHERE ROW_ID > ?");

        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();

        IPage<UserDTO> page = userMapper.selectJoinPage(new Page<>(1, 10), UserDTO.class, AptWrappers.query(u)
                .selectAll()
                .select(addr.address)
                .leftJoin(addr, on -> on
                        .eq(u.id, addr.userId)
                        .eq(u.id, addr.userId))
                .eq(u.id, 1)
                .and(i -> i
                        .eq(u.img, "er")
                        .or()
                        .eq(addr.userId, 1))
                .eq(u.id, 1));
        page.getRecords().forEach(System.out::println);
    }

    /**
     * 简单的函数使用
     */
    @Test
    void test4() {
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();

        AptQueryWrapper<UserDO> wrapper = AptWrappers.query(u)
                .selectSum(u.id)
                .selectMax(u.id, UserDTO::getHeadImg)
                .leftJoin(addr, addr.userId, u.id);

        UserDTO one = userMapper.selectJoinOne(UserDTO.class, wrapper);
        System.out.println(one);
    }

    /**
     * 忽略个别查询字段
     */
    @Test
    @SneakyThrows
    void test8() throws BadSqlGrammarException {
        ThreadLocalUtils.set("SELECT t.`name` FROM `user` t WHERE t.del=false AND (t.`name` = ?)");
        UserDOCol u = UserDOCol.build();
        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
                .select(u.name)
                .eq(u.name, "ref");
        userMapper.selectList(wrapper);
    }


    /**
     * 关联查询返回map
     */
    @Test
    void test7() {
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        List<Map<String, Object>> list = userMapper.selectJoinMaps(AptWrappers.query(u)
                .selectAll()
                .select(addr.address)
                .leftJoin(addr, addr.userId, u.id));
        assert list.get(0).get("ADDRESS") != null || list.get(0).get("address") != null;
        list.forEach(System.out::println);
    }

    /**
     * 原生查询
     */
    @Test
    void testMP() {
        List<UserDO> dos = userMapper.selectList(new LambdaQueryWrapper<UserDO>()
                .gt(UserDO::getId, 3)
                .lt(UserDO::getId, 8));
        assert dos.size() == 4;

        ThreadLocalUtils.set(
                "SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false AND (t.id > ? AND t.id < ?)",
                "SELECT id, pid, `name`, `json`, sex, head_img, create_time, address_id, address_id2, del, create_by, update_by FROM `user` t WHERE t.del = false AND (t.id > ? AND t.id < ?)",
                "SELECT * FROM `user` t WHERE t.del=false AND (t.id > ? AND t.id < ?) ");
        UserDOCol u = UserDOCol.build();
        List<UserDO> dos1 = userMapper.selectList(new AptQueryWrapper<>(u)
                .gt(u.id, 3)
                .lt(u.id, 8));
        assert dos1.size() == 4;
    }

    /**
     * 泛型测试
     */
    @Test
    void testGeneric() {
        AddressDOCol addr = AddressDOCol.build();
        AptQueryWrapper<AddressDO> wrapper = new AptQueryWrapper<>(addr)
                .selectAll()
                .le(addr.id, 10000)
                .orderByDesc(addr.id);
        List<AddressDTO> list = addressMapper.selectJoinList(AddressDTO.class, wrapper);
        assert Objects.equals("[AddressDTO(id=22, userId=22, areaId=10022, tel=10000000022, address=朝阳22, del=false, areaList=null, area=null), AddressDTO(id=21, userId=21, areaId=10021, tel=10000000021, address=朝阳21, del=false, areaList=null, area=null), AddressDTO(id=20, userId=20, areaId=10020, tel=10000000020, address=朝阳20, del=false, areaList=null, area=null), AddressDTO(id=19, userId=19, areaId=10019, tel=10000000019, address=朝阳19, del=false, areaList=null, area=null), AddressDTO(id=18, userId=18, areaId=10018, tel=10000000018, address=朝阳18, del=false, areaList=null, area=null), AddressDTO(id=17, userId=17, areaId=10017, tel=10000000017, address=朝阳17, del=false, areaList=null, area=null), AddressDTO(id=16, userId=16, areaId=10016, tel=10000000016, address=朝阳16, del=false, areaList=null, area=null), AddressDTO(id=15, userId=15, areaId=10015, tel=10000000015, address=朝阳15, del=false, areaList=null, area=null), AddressDTO(id=14, userId=14, areaId=10014, tel=10000000014, address=朝阳14, del=false, areaList=null, area=null), AddressDTO(id=13, userId=13, areaId=10013, tel=10000000013, address=朝阳13, del=false, areaList=null, area=null), AddressDTO(id=12, userId=12, areaId=10012, tel=10000000012, address=朝阳12, del=false, areaList=null, area=null), AddressDTO(id=11, userId=11, areaId=10011, tel=10000000011, address=朝阳11, del=false, areaList=null, area=null), AddressDTO(id=10, userId=10, areaId=10010, tel=10000000010, address=朝阳10, del=false, areaList=null, area=null), AddressDTO(id=5, userId=1, areaId=10005, tel=10000000005, address=朝阳05, del=false, areaList=null, area=null), AddressDTO(id=4, userId=1, areaId=10004, tel=10000000004, address=朝阳04, del=false, areaList=null, area=null), AddressDTO(id=3, userId=1, areaId=10003, tel=10000000003, address=朝阳03, del=false, areaList=null, area=null), AddressDTO(id=2, userId=1, areaId=10002, tel=10000000002, address=朝阳02, del=false, areaList=null, area=null), AddressDTO(id=1, userId=1, areaId=10001, tel=10000000001, address=朝阳01, del=false, areaList=null, area=null)]"
                , list.toString());
    }

    /**
     * count
     */
    @Test
    void testCount() {
        ThreadLocalUtils.set(
                "SELECT COUNT( 1 ) FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) WHERE t.del=false AND t1.del=false AND t2.del=false",
                "SELECT COUNT( * ) FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) WHERE t.del=false AND t1.del=false AND t2.del=false",
                "SELECT COUNT( * ) AS total FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) WHERE t.del=false AND t1.del=false AND t2.del=false");
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AreaDOCol ar = AreaDOCol.build();
        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
                .leftJoin(addr, addr.userId, u.id)
                .leftJoin(ar, ar.id, addr.areaId);
        Object integer = userMapper.selectCount(wrapper);

        ThreadLocalUtils.set("SELECT COUNT( * ) FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) WHERE t.del=false AND t1.del=false AND t2.del=false");
        UserDOCol u1 = UserDOCol.build();
        AddressDOCol addr1 = AddressDOCol.build();
        AreaDOCol ar1 = AreaDOCol.build();
        AptQueryWrapper<UserDO> wrapper1 = new AptQueryWrapper<>(u1)
                .leftJoin(addr1, addr1.userId, u1.id)
                .leftJoin(ar1, ar1.id, addr1.areaId);
        Long aLong1 = userMapper.selectJoinCount(wrapper1);
    }


    /**
     * 动态别名
     */
    @Test
    void testTable() {
        ThreadLocalUtils.set("SELECT t.id FROM (SELECT * FROM `user`) t LEFT JOIN (SELECT * FROM address) t1 ON " +
                "(t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) WHERE t.del = false AND t1.del = false " +
                "AND t2.del = false AND (t.id <= ?) ORDER BY t.id DESC");
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AreaDOCol ar = AreaDOCol.build();

        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
                .select(u.id)
                .leftJoin(addr, on -> on
                        .eq(addr.userId, u.id)
                        .setTableName(name -> String.format("(select * from %s)", name)))
                .leftJoin(ar, ar.id, addr.areaId)
                .le(u.id, 10000)
                .orderByDesc(u.id)
                .setTableName(name -> String.format("(select * from %s)", name));

        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);
    }


    /**
     * 逻辑删除类型
     */
    @Test
    void logicDelType() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.id  AS joina_id,
                       t1.user_id,
                       t1.area_id,
                       t1.tel,
                       t1.address,
                       t1.del AS joina_del,
                       t2.id  AS joinb_id,
                       t2.province,
                       t2.city,
                       t2.area,
                       t2.postcode,
                       t2.del AS joinb_del
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id AND t1.del = false)
                         LEFT JOIN area t2 ON (t2.id = t1.area_id AND t2.del = false)
                WHERE t.del = false
                  AND (t.id <= ?)
                ORDER BY t.id DESC
                """);
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AreaDOCol ar = AreaDOCol.build();
        AptQueryWrapper<UserDO> wrapper = new AptQueryWrapper<>(u)
                .logicDelToOn()
                .selectAll()
                .selectCollection(addr, UserDTO::getAddressList, add -> add
                        .association(ar, AddressDTO::getArea))
                .leftJoin(addr, addr.userId, u.id)
                .leftJoin(ar, ar.id, addr.areaId)
                .le(u.id, 10000)
                .orderByDesc(u.id);
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        assert list.get(0).getAddressList() != null && list.get(0).getAddressList().get(0).getId() != null;
        list.forEach(System.out::println);
    }

    /**
     * wrappers 测试
     */
    @Test
    void joinWrapper() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.id  AS joina_id,
                       t1.user_id,
                       t1.area_id,
                       t1.tel,
                       t1.address,
                       t1.del AS joina_del,
                       t2.id  AS joinb_id,
                       t2.province,
                       t2.city,
                       t2.area,
                       t2.postcode,
                       t2.del AS joinb_del
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id AND t1.del = false)
                         LEFT JOIN area t2 ON (t2.id = t1.area_id AND t2.del = false)
                WHERE t.del = false
                  AND (t.id <= ?)
                ORDER BY t.id DESC
                """);
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AreaDOCol ar = AreaDOCol.build();

        AptQueryWrapper<UserDO> wrapper = AptWrappers.query(u)
                .logicDelToOn()
                .selectAll()
                .selectCollection(addr, UserDTO::getAddressList, ad -> ad
                        .association(ar, AddressDTO::getArea))
                .leftJoin(addr, addr.userId, u.id)
                .leftJoin(ar, ar.id, addr.areaId)
                .le(u.id, 10000)
                .orderByDesc(u.id);

        List<UserDTO> list = wrapper.list(UserDTO.class);

        System.out.println(list);
        assert list.get(0).getAddressList() != null && list.get(0).getAddressList().get(0).getId() != null;
        list.forEach(System.out::println);
    }

    @Test
    void joinRandomMap() {
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AreaDOCol ar = AreaDOCol.build();
        AptQueryWrapper<UserDO> wrapper = AptWrappers.query(u)
                .logicDelToOn()
                .selectAll()
                .selectCollection(UserDTO::getAddressList, ad -> ad
                        .id(addr.id, AddressDTO::getId)
                        .result(u.name, AddressDTO::getAddress)
                        .collection(AddressDTO::getAreaList, map -> map
                                .id(ar.id)
                                .result(ar.area, AreaDO::getArea)))
                .leftJoin(addr, addr.userId, u.id)
                .leftJoin(ar, ar.id, addr.areaId)
                .le(u.id, 10000)
                .orderByDesc(u.id);

        List<UserDTO> list = wrapper.list(UserDTO.class);

        System.out.println(list);
        assert list.get(0).getAddressList() != null && list.get(0).getAddressList().get(0).getId() != null;
        list.forEach(System.out::println);
    }

    @Test
    void joinRandomMap111() {
        ThreadLocalUtils.set("SELECT t.id,t.user_id,t.area_id,t.tel,t.address,t.del FROM address t LEFT JOIN `user` t1 ON (t1.address_id = t.id) LEFT JOIN `user` t2 ON (t2.pid = t1.id) WHERE t.del=false AND t1.del=false AND t2.del=false");
        UserDOCol u = UserDOCol.build();
        UserDOCol u1 = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AptQueryWrapper<AddressDO> wrapper = AptWrappers.query(addr)
                .selectAll()
                .leftJoin(u, u.addressId, addr.id)
                .leftJoin(u1, u1.pid, u.id);

        List<AddressDO> addressDOS = wrapper.list();
    }

    /**
     * 同一个类字段比较
     */
    @Test
    void joinOwn() {
        ThreadLocalUtils.set("SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del=false AND t1.del=false AND (t1.id = t1.id)");
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AptQueryWrapper<UserDO> wrapper = AptWrappers.query(u)
                .selectAll()
                .leftJoin(addr, addr.userId, u.id)
                .eq(addr.id, addr.id);

        List<UserDO> addressDOS = wrapper.list();
    }

    /**
     * 同一个类字段比较
     */
    @Test
    void joinOwn1() {
        ThreadLocalUtils.set("SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t " +
                "LEFT JOIN address aaa ON (aaa.user_id = t.id) WHERE t.del=false AND aaa.del=false AND (aaa.id = t.id AND aaa.id = aaa.id)");
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build("aaa");
        AptQueryWrapper<UserDO> wrapper = AptWrappers.query(u)
                .selectAll()
                .leftJoin(addr, addr.userId, u.id)
                .eq(addr.id, u.id)
                .eq(addr.id, addr.id);
        List<UserDO> addressDOS = wrapper.list();
    }

    /**
     * 同一个类字段比较
     */
    @Test
    void joinOrder() {
        if (VersionUtils.compare(VersionUtils.getVersion(), "3.4.3") >= 0) {
            ThreadLocalUtils.set("SELECT id,user_id,name FROM order_t t ORDER BY t.name DESC",
                    "SELECT t.id, t.user_id, t.name FROM order_t t ORDER BY t.name DESC",
                    "SELECT id,user_id,name FROM order_t t ORDER BY t.name desc");
        } else {
            ThreadLocalUtils.set("SELECT id,user_id,name FROM order_t t",
                    "SELECT id,user_id,name FROM order_t t");
        }
        OrderDOCol o = OrderDOCol.build();
        AptQueryWrapper<OrderDO> wrapper = AptWrappers.query(o);
        List<OrderDO> list = wrapper.list();

        if (VersionUtils.compare(VersionUtils.getVersion(), "3.4.3") >= 0) {
            ThreadLocalUtils.set("SELECT t.id,t.user_id,t.name,t1.`name` AS userName FROM order_t t LEFT JOIN `user` t1 ON (t1.id = t.user_id) WHERE t1.del=false ORDER BY t.name DESC",
                    "SELECT t.id,t.user_id,t.name,t1.`name` AS userName FROM order_t t LEFT JOIN `user` t1 ON (t1.id = t.user_id) WHERE t1.del=false ORDER BY t.name desc");
        } else {
            ThreadLocalUtils.set("SELECT t.id,t.user_id,t.name,t1.`name` AS userName FROM order_t t LEFT JOIN `user` t1 ON (t1.id = t.user_id) WHERE t1.del=false",
                    "SELECT t.id,t.user_id,t.name,t1.`name` AS userName FROM order_t t LEFT JOIN `user` t1 ON (t1.id = t.user_id) WHERE t1.del=false");
        }

        UserDOCol u = UserDOCol.build();
        OrderDOCol o1 = OrderDOCol.build();
        AptQueryWrapper<OrderDO> w = AptWrappers.query(o1)
                .selectAll()
                .selectAs(u.name, OrderDO::getUserName)
                .leftJoin(u, u.id, o1.userId);
        List<OrderDO> l = w.list();
    }

    /**
     * select 子查询
     */
    @Test
    void checkOrderBy() {
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AptQueryWrapper<UserDO> wrapper = AptWrappers.query(u)
                .selectAll()
                .leftJoin(addr, addr.userId, u.id)
                .le(u.id, 100)
                .checkSqlInjection()
                .orderByDesc("t.id");
        wrapper.list();
    }
}
