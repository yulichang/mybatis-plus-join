package com.github.yulichang.test.join.unit;

import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.segments.Fun;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CloneTest {


    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void cloneTest() {
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .applyFunc("concat(%s,%s,{0}) is not null", arg -> arg.accept(UserDO::getId, AddressDO::getUserId), "12")
                .applyFunc("concat(%s,%s,{0}) is not null", arg -> arg.accept(
                        Fun.f("t", UserDO::getId),
                        Fun.f("t1", AddressDO::getUserId)), "12");

        wrapper.list().forEach(System.out::println);

        MPJLambdaWrapper<UserDO> clone = wrapper.clone();

        clone.list().forEach(System.out::println);
    }

}
