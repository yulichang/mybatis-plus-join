package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.test.join.entity.UserTenantaDO;
import com.github.yulichang.test.join.entity.apt.UserTenantDOCol;
import com.github.yulichang.test.join.entity.apt.UserTenantaDOCol;
import com.github.yulichang.toolkit.JoinWrappers;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ChineseFieldTest {

    @Test
    void chineseField() {
        UserTenantaDOCol ut = new UserTenantaDOCol();
        List<UserTenantaDO> list = JoinWrappers.apt(ut).list();
        assert list.get(0).getDetail() != null;
        list.forEach(System.out::println);
    }
}
