package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.extension.apt.toolkit.AptWrappers;
import com.github.yulichang.test.join.entity.UserTenantaDO;
import com.github.yulichang.test.join.entity.apt.UserTenantaDOCol;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ChineseFieldTest {

    @Test
    void chineseField() {
        UserTenantaDOCol ut = UserTenantaDOCol.build();
        List<UserTenantaDO> list = AptWrappers.query(ut).list();
        assert list.get(0).getDetail() != null;
        list.forEach(System.out::println);
    }
}
