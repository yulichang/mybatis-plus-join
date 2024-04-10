package com.github.yulichang.test.join.m;

import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UpdateIncTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void eqSql() {
        ThreadLocalUtils.set("UPDATE `user` t SET t.id = t.id + 100 WHERE t.del = false");
        JoinWrappers.update(UserDO.class).setIncrBy(UserDO::getId,100).update();

        JoinWrappers.lambda(UserDO.class).list().forEach(System.out::println);

        ThreadLocalUtils.set("UPDATE `user` t SET t.id = t.id - 100 WHERE t.del = false");
        JoinWrappers.update(UserDO.class).setDecrBy(UserDO::getId,100).update();

        JoinWrappers.lambda(UserDO.class).list().forEach(System.out::println);
    }

}
