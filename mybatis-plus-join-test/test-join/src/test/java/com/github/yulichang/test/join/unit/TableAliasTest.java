package com.github.yulichang.test.join.unit;

import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.AreaDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@EnabledIf("com.github.yulichang.test.util.EnabledIf#runWithExcludingOracle")
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
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .leftJoin(AddressDO.class, "addr1", AddressDO::getId, UserDO::getAddressId)
                .leftJoin(AddressDO.class, "addr2", AddressDO::getId, UserDO::getAddressId2)
                .leftJoin(AreaDO.class, "area1", AreaDO::getId, "addr1", AddressDO::getAreaId)
                .groupBy(UserDO::getId);

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
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .leftJoin(AddressDO.class, "addr1", AddressDO::getId, UserDO::getAddressId)
                .leftJoin(AddressDO.class, "addr2", AddressDO::getId, UserDO::getAddressId2)
                .leftJoin(AreaDO.class, "area1", AreaDO::getId, "addr2", AddressDO::getAreaId)
                .groupBy(UserDO::getId)
                .groupBy("addr1", AddressDO::getId)
                .orderByDesc("addr1", AddressDO::getId);

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
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .leftJoin(AddressDO.class, "addr1", AddressDO::getId, UserDO::getAddressId)
                .leftJoin(AddressDO.class, "addr2", AddressDO::getId, UserDO::getAddressId2)
                .leftJoin(AreaDO.class, "area1", AreaDO::getId, "addr2", AddressDO::getAreaId)
                .eq("addr1", AddressDO::getId, 1)
                .eq("addr2", AddressDO::getId, 2)
                .eq("addr1", AddressDO::getId, 3);

        List<UserDO> dos = userMapper.selectJoinList(UserDO.class, wrapper);
        dos.forEach(System.out::println);
    }

    @Test
    void tableAlias3() {
        ThreadLocalUtils.set("SELECT aaa.id, aaa.pid, aaa.`name`, aaa.`json`, aaa.sex, aaa.head_img, aaa.create_time, " +
                "aaa.address_id, aaa.address_id2, aaa.del, aaa.create_by, aaa.update_by FROM `user` aaa WHERE aaa.`name` = ? AND aaa.del = false");
        UserDO userDO = new UserDO();
        userDO.setName("aaa");
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda("aaa", userDO);
        wrapper.list();
    }
}
