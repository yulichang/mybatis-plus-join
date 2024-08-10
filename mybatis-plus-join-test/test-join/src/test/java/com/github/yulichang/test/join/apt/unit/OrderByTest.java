package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.apt.Column;
import com.github.yulichang.extension.apt.AptQueryWrapper;
import com.github.yulichang.extension.apt.toolkit.AptWrappers;
import com.github.yulichang.test.join.dto.UserDTO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.entity.apt.AddressDOCol;
import com.github.yulichang.test.join.entity.apt.UserDOCol;
import com.github.yulichang.test.util.EnabledIfConfig;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@EnabledIf(value = EnabledIfConfig.runWithExcludingOracle,loadContext = true)
public class OrderByTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }

    @Test
    void orderBy() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by " +
                "FROM `user` t WHERE t.del = false AND (t.id = ?) ORDER BY t.id ASC, t.`name` ASC, t.pid ASC");
        UserDOCol u = UserDOCol.build();
        List<Column> columList = Arrays.asList(u.id, u.name, u.pid);

        AptQueryWrapper<UserDO> wrapper = AptWrappers.query(u)
                .selectAll()
                .eq(u.id, 1)
                .orderByAsc(columList);


        List<UserDO> list = wrapper.list();
        list.forEach(System.out::println);
    }

    @Test
    void orderBy1() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by " +
                "FROM `user` t WHERE t.del = false AND (t.id = ?) GROUP BY t.id, t.`name`, t.pid");
        UserDOCol u = UserDOCol.build();
        List<Column> columList = Arrays.asList(u.id, u.name, u.pid);

        AptQueryWrapper<UserDO> wrapper = AptWrappers.query(u)
                .selectAll()
                .eq(u.id, 1)
                .groupBy(columList);


        List<UserDO> list = wrapper.list();
        list.forEach(System.out::println);
    }


    @Test
    void orderBy2() {
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr =  AddressDOCol.build();

        AptQueryWrapper<UserDO> wrapper = AptWrappers.query(u)
                .selectAll()
                .selectAs(addr.id, UserDTO::getAddress)
                .leftJoin(addr, addr.userId, u.id)
                .orderByAsc(UserDTO::getAddress);


        List<UserDO> list = wrapper.list();
        list.forEach(System.out::println);
    }

}
