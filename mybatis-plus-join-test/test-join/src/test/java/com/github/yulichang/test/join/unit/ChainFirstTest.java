package com.github.yulichang.test.join.unit;

import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
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

}
