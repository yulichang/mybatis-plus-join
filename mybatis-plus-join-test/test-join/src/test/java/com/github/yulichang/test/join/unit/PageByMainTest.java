package com.github.yulichang.test.join.unit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.test.join.dto.UserDTO;
import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.mapper.AreaMapper;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.geom.Area;
import java.util.List;

@SpringBootTest
public class PageByMainTest {

    @Autowired
    private AreaMapper areaMapper;


    @Test
    void pageByMain() {
        List<Area> all = areaMapper.all();
        System.out.println(all);
        assert !all.isEmpty();

        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by, t1.id AS joina_id, t1.user_id, t1.area_id, t1.tel, " +
                "t1.address, t1.del AS joina_del FROM (SELECT * FROM `user` t WHERE t.del = false AND (t.id <= ?) LIMIT ?) t " +
                "LEFT JOIN address t1 ON (t1.user_id = t.id AND t1.user_id >= ? AND t1.del = false)");
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .selectCollection(AddressDO.class, UserDTO::getAddressList)
                .leftJoin(AddressDO.class, on -> on
                        .eq(AddressDO::getUserId, UserDO::getId)
                        .ge(AddressDO::getUserId, 0))
                .le(UserDO::getId, 15)
                .logicDelToOn()
                .pageByMain();

        Page<UserDTO> page = wrapper.page(new Page<>(1, 8), UserDTO.class);
        page.getRecords().forEach(System.out::println);
        assert page.getRecords().size() == 8;
    }

    @Test
    void pageByMain1() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2," +
                " t.del, t.create_by, t.update_by, concat('?', t.id) AS sdafsdfsdfsd, t1.id AS joina_id, t1.user_id, " +
                "t1.area_id, t1.tel, t1.address, t1.del AS joina_del FROM (SELECT * FROM `user` t WHERE t.del = false " +
                "AND (t.id <= ?) LIMIT ?) t LEFT JOIN address t1 ON (t1.user_id = t.id AND t1.user_id >= ? AND t1.del = false)");
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .selectFunc(() -> "concat('?',%s)", UserDO::getId, "sdafsdfsdfsd")
                .selectCollection(AddressDO.class, UserDTO::getAddressList)
                .leftJoin(AddressDO.class, on -> on
                        .eq(AddressDO::getUserId, UserDO::getId)
                        .ge(AddressDO::getUserId, 0))
                .le(UserDO::getId, 15)
                .logicDelToOn()
                .pageByMain();

        Page<UserDTO> page = wrapper.page(new Page<>(1, 8), UserDTO.class);
        page.getRecords().forEach(System.out::println);
        assert page.getRecords().size() == 8;

    }

    @Test
    void test11() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by, concat('?', t.id) AS sdafsdfsdfsd, t1.id AS joina_id, " +
                "t1.user_id, t1.area_id, t1.tel, t1.address, t1.del AS joina_del FROM (SELECT * FROM `user` t " +
                "WHERE t.del = false AND (t.id <= ?) GROUP BY t.id ORDER BY t.id DESC LIMIT ?) t LEFT JOIN address t1 ON " +
                "(t1.user_id = t.id AND t1.user_id >= ? AND t1.del = false)");
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .selectFunc(() -> "concat('?',%s)", UserDO::getId, "sdafsdfsdfsd")
                .selectCollection(AddressDO.class, UserDTO::getAddressList)
                .leftJoin(AddressDO.class, on -> on
                        .eq(AddressDO::getUserId, UserDO::getId)
                        .ge(AddressDO::getUserId, 0))
                .le(UserDO::getId, 15)
                .orderByDesc(UserDO::getId)
                .groupBy(UserDO::getId)
                .logicDelToOn()
                .pageByMain(f -> f.setCountSelectSql("1"));

        Page<UserDTO> page = wrapper.page(new Page<>(1, 8), UserDTO.class);
        page.getRecords().forEach(System.out::println);
        assert page.getRecords().size() == 8;
    }

    @Test
    void test13() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by, concat('?', t.id) AS sdafsdfsdfsd, t1.id AS joina_id, " +
                "t1.user_id, t1.area_id, t1.tel, t1.address, t1.del AS joina_del FROM (SELECT * FROM `user` t " +
                "WHERE t.del = false AND (t.id <= ?) GROUP BY t.id ORDER BY t.id DESC LIMIT ?) t LEFT JOIN address t1 ON " +
                "(t1.user_id = t.id AND t1.user_id >= ? AND t1.del = false)");
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .selectFunc(() -> "concat('?',%s)", UserDO::getId, "sdafsdfsdfsd")
                .selectCollection(AddressDO.class, UserDTO::getAddressList)
                .leftJoin(AddressDO.class, on -> on
                        .eq(AddressDO::getUserId, UserDO::getId)
                        .ge(AddressDO::getUserId, 0))
                .le(UserDO::getId, 15)
                .orderByDesc(UserDO::getId)
                .groupBy(UserDO::getId)
                .logicDelToOn()
                .pageByMain();

        Page<UserDTO> page = wrapper.page(new Page<>(1, 8), UserDTO.class);
        page.getRecords().forEach(System.out::println);
        assert page.getRecords().size() == 8;
    }
}
