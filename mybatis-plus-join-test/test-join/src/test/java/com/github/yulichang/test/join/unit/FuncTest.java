package com.github.yulichang.test.join.unit;

import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FuncTest {


    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void func() {
        ThreadLocalUtils.set("SELECT t.`json` FROM `user` t WHERE t.del = false");
        List<UserDO> list = JoinWrappers.lambda(UserDO.class)
                .func(false, w -> w.select(UserDO::getId))
                .select(UserDO::getJson)
                .list();
        assert list.get(0).getId() == null && list.get(0).getJson() != null;
        list.forEach(System.out::println);

        ThreadLocalUtils.set("SELECT t.id FROM `user` t WHERE t.del = false");
        List<UserDO> list1 = JoinWrappers.lambda(UserDO.class)
                .func(true, w -> w.select(UserDO::getId), w -> w.select(UserDO::getName))
                .list();
        assert list1.get(0).getId() != null && list1.get(0).getName() == null;
        list1.forEach(System.out::println);

        ThreadLocalUtils.set("SELECT t.`name` FROM `user` t WHERE t.del = false");
        List<UserDO> list2 = JoinWrappers.lambda(UserDO.class)
                .func(false, w -> w.select(UserDO::getId), w -> w.select(UserDO::getName))
                .list();
        assert list2.get(0).getName() != null && list2.get(0).getId() == null;
        list2.forEach(System.out::println);
    }

}
