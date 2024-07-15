package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.entity.apt.AddressDOCol;
import com.github.yulichang.test.join.entity.apt.AreaDOCol;
import com.github.yulichang.test.join.entity.apt.UserDOCol;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.util.EnabledIfConfig;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.apt.AptQueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.util.List;

@SpringBootTest
@EnabledIf(value = EnabledIfConfig.runWithExcludingOracle, loadContext = true)
public class TableAliasTest {

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        Reset.reset();
    }

    @Test
    void tableAlias() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                "LEFT JOIN address addr1 ON (addr1.id = t.address_id) " +
                "LEFT JOIN address addr2 ON (addr2.id = t.address_id2) " +
                "LEFT JOIN area area1 ON (area1.id = addr1.area_id) " +
                "WHERE t.del = false AND addr1.del = false AND addr2.del = false AND area1.del = false " +
                "GROUP BY t.id");
        UserDOCol u = new UserDOCol();
        AddressDOCol addr1 = new AddressDOCol("addr1");
        AddressDOCol addr2 = new AddressDOCol("addr2");
        AreaDOCol area1 = new AreaDOCol("area1");

        AptQueryWrapper<UserDO> wrapper = JoinWrappers.apt(u)
                .selectAll()
                .leftJoin(addr1, addr1.id, u.addressId)
                .leftJoin(addr2, addr2.id, u.addressId2)
                .leftJoin(area1, area1.id, addr1.areaId)
                .groupBy(u.id);

        List<UserDO> dos = userMapper.selectJoinList(UserDO.class, wrapper);
        dos.forEach(System.out::println);
    }

    @Test
    void tableAlias2() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                "LEFT JOIN address addr1 ON (addr1.id = t.address_id) " +
                "LEFT JOIN address addr2 ON (addr2.id = t.address_id2) " +
                "LEFT JOIN area area1 ON (area1.id = addr2.area_id) " +
                "WHERE t.del = false AND addr1.del = false AND addr2.del = false AND area1.del = false " +
                "GROUP BY t.id,addr1.id ORDER BY addr1.id DESC");

        UserDOCol u = new UserDOCol();
        AddressDOCol addr1 = new AddressDOCol("addr1");
        AddressDOCol addr2 = new AddressDOCol("addr2");
        AreaDOCol area1 = new AreaDOCol("area1");

        AptQueryWrapper<UserDO> wrapper = JoinWrappers.apt(u)
                .selectAll()
                .leftJoin(addr1, addr1.id, u.addressId)
                .leftJoin(addr2, addr2.id, u.addressId2)
                .leftJoin(area1, area1.id, addr2.areaId)
                .groupBy(u.id)
                .groupBy(addr1.id)
                .orderByDesc(addr1.id);

        List<UserDO> dos = userMapper.selectJoinList(UserDO.class, wrapper);
        dos.forEach(System.out::println);
    }


    @Test
    void tableAliasEQ() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                "LEFT JOIN address addr1 ON (addr1.id = t.address_id) " +
                "LEFT JOIN address addr2 ON (addr2.id = t.address_id2) " +
                "LEFT JOIN area area1 ON (area1.id = addr2.area_id) WHERE t.del = false AND addr1.del = false AND addr2.del = false AND area1.del = false " +
                "AND (addr1.id = ? AND addr2.id = ? AND addr1.id = ?)");

        UserDOCol u = new UserDOCol();
        AddressDOCol addr1 = new AddressDOCol("addr1");
        AddressDOCol addr2 = new AddressDOCol("addr2");
        AreaDOCol area1 = new AreaDOCol("area1");

        AptQueryWrapper<UserDO> wrapper = JoinWrappers.apt(u)
                .selectAll()
                .leftJoin(addr1, addr1.id, u.addressId)
                .leftJoin(addr2, addr2.id, u.addressId2)
                .leftJoin(area1, area1.id, addr2.areaId)
                .eq(addr1.id, 1)
                .eq(addr2.id, 2)
                .eq(addr1.id, 3);

        List<UserDO> dos = userMapper.selectJoinList(UserDO.class, wrapper);
        dos.forEach(System.out::println);
    }

    @Test
    void tableAlias3() {
        ThreadLocalUtils.set("SELECT aaa.id, aaa.pid, aaa.`name`, aaa.`json`, aaa.sex, aaa.head_img, aaa.create_time, " +
                "aaa.address_id, aaa.address_id2, aaa.del, aaa.create_by, aaa.update_by FROM `user` aaa WHERE aaa.`name` = ? AND aaa.del = false");

        UserDOCol u = new UserDOCol("aaa");

        UserDO userDO = new UserDO();
        userDO.setName("aaa");
        AptQueryWrapper<UserDO> wrapper = JoinWrappers.apt(u, userDO);
        wrapper.list();
    }
}
