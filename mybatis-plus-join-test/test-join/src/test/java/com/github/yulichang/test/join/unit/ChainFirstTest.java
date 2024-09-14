package com.github.yulichang.test.join.unit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.UpdateJoinWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ChainFirstTest {


    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void chainFirst() {
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .select(UserDO::getName);

        String first = wrapper.first(String.class);

        System.out.println(first);
    }

    @Test
    void chainFirst1() {
        UpdateJoinWrapper<UserDO> update = JoinWrappers.update(UserDO.class)
                .set(UserDO::getName, null);
        update.update();

        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .select(UserDO::getName);

        String first = wrapper.first(String.class);

        System.out.println(first);
    }

    @Test
    void chainFirst2() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false AND (t.id = ?)");
        System.out.println(JoinWrappers.lambda(UserDO.class).eq(UserDO::getId,1).mapOne());
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false LIMIT ?");
        System.out.println(JoinWrappers.lambda(UserDO.class).mapFirst());
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false");
        System.out.println(JoinWrappers.lambda(UserDO.class).mapList());
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false LIMIT ?");
        System.out.println(JoinWrappers.lambda(UserDO.class).mapPage(new Page<>(1, 3)));
    }

}
