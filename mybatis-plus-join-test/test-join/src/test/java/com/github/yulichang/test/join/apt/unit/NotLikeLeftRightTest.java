package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.extension.apt.AptQueryWrapper;
import com.github.yulichang.extension.apt.toolkit.AptWrappers;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.entity.apt.UserDOCol;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class NotLikeLeftRightTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }

    @Test
    void notLikeLeftRight() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false AND (t.`name` NOT LIKE ?)");
        UserDOCol u = UserDOCol.build();
        AptQueryWrapper<UserDO> wrapper = AptWrappers.query(u)
                .selectAll()
                .notLikeLeft(u.name, "aa");
        List<UserDO> list = wrapper.list();
        list.forEach(System.out::println);

        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false AND (t.`name` NOT LIKE ?)");
        UserDOCol u1 = UserDOCol.build();
        AptQueryWrapper<UserDO> wrapper1 = AptWrappers.query(u1)
                .selectAll()
                .notLikeRight(u1.name, "aa");
        List<UserDO> list1 = wrapper1.list();
        list1.forEach(System.out::println);
    }
}
