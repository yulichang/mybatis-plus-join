package com.github.yulichang.test.join.m;

import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AroundTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void around() {
        ThreadLocalUtils.set("SELECT * FROM (SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, " +
                "t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false) tmp");
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .around("select * from (", ") tmp");
        wrapper.list().forEach(System.out::println);
    }

}
