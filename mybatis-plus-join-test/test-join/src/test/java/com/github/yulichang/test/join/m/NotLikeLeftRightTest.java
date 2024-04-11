package com.github.yulichang.test.join.m;

import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
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
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .notLikeLeft(UserDO::getName, "aa");
        List<UserDO> list = wrapper.list();
        list.forEach(System.out::println);

        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false AND (t.`name` NOT LIKE ?)");
        MPJLambdaWrapper<UserDO> wrapper1 = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .notLikeRight(UserDO::getName, "aa");
        List<UserDO> list1 = wrapper1.list();
        list1.forEach(System.out::println);
    }
}
