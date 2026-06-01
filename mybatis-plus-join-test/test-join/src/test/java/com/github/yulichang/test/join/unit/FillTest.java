package com.github.yulichang.test.join.unit;

import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@SpringBootTest
public class FillTest {


    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void fill() {
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class).selectAll(UserDO.class);

        wrapper.fill(UserDO::getAddressId, UserDO::getAddressList);

        List<UserDO> list = wrapper.clone().list();
        list.forEach(System.out::println);

        assert !list.stream().findFirst().get().getAddressList().isEmpty();
    }

    @Test
    void fill1() {
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class).selectAll(UserDO.class)
                .eq(UserDO::getId, 1);


        wrapper.fill(UserDO::getAddressId, UserDO::getAddress1, w -> {
            System.out.println(1);
//            w.last("limit 1");
        });

        UserDO list = wrapper.clone().one();
        System.out.println(list);

        assert list.getAddress1() != null;
    }


}
