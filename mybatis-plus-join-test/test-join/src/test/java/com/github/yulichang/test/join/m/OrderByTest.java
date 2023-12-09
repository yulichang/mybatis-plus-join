package com.github.yulichang.test.join.m;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class OrderByTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }

    @Test
    void orderBy() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by " +
                "FROM `user` t WHERE t.del = false AND (t.id = ?) ORDER BY t.id ASC, t.`name` ASC, t.pid ASC");
        List<SFunction<UserDO, ?>> columList = Arrays.asList(UserDO::getId, UserDO::getName, UserDO::getPid);

        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .eq(UserDO::getId, 1)
                .orderByAsc(columList);


        List<UserDO> list = wrapper.list();
        list.forEach(System.out::println);
    }

    @Test
    void orderBy1() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, t.address_id2, t.del, t.create_by, t.update_by " +
                "FROM `user` t WHERE t.del = false AND (t.id = ?) GROUP BY t.id, t.`name`, t.pid");
        List<SFunction<UserDO, ?>> columList = Arrays.asList(UserDO::getId, UserDO::getName, UserDO::getPid);

        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .eq(UserDO::getId, 1)
                .groupBy(columList);
//                .groupBy(UserDO::getId, UserDO::getName, UserDO::getPid);
//                .orderByAsc(columList);


        List<UserDO> list = wrapper.list();
        list.forEach(System.out::println);
    }

}
