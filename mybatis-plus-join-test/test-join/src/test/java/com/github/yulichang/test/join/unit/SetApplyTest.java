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
public class SetApplyTest {


    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void applyFunc() {
        ThreadLocalUtils.set("UPDATE `user` t SET t.id = t.id + ? + ? WHERE t.del = false");

        int update = JoinWrappers.update(UserDO.class)
                .setApply("%s = %s + {0} + {1}", arg -> arg.accept(UserDO::getId, UserDO::getId), "100", "1000")
                .update();

        assert update > 0;

        List<UserDO> list = JoinWrappers.lambda(UserDO.class).list();
        System.out.println(list);
    }

}
