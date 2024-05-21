package com.github.yulichang.test.join.m;

import com.github.yulichang.test.join.entity.UserTenantaDO;
import com.github.yulichang.toolkit.JoinWrappers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ChineseFieldTest {

    @Test
    void chineseField() {
        List<UserTenantaDO> list = JoinWrappers.lambda(UserTenantaDO.class).list();
        assert list.get(0).getDetail() != null;
        list.forEach(System.out::println);
    }
}
