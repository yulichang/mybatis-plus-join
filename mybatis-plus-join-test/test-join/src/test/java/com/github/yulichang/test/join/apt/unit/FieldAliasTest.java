package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.entity.UserTenantDO;
import com.github.yulichang.test.join.entity.apt.AddressDOCol;
import com.github.yulichang.test.join.entity.apt.UserDOCol;
import com.github.yulichang.test.join.entity.apt.UserTenantDOCol;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.join.mapper.UserTenantMapper;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.apt.AptQueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FieldAliasTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTenantMapper userTenantMapper;

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void fieldAlias() {
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        List<UserDO> list = userMapper.selectList(JoinWrappers.apt(u)
                .selectAll()
                .leftJoin(addr, addr.userId, u.id));

        list.forEach(System.out::println);

        assert list.get(0).getImg() != null;

    }

    @Test
    void fieldAlias1() {
        UserTenantDOCol ut = UserTenantDOCol.build();
        UserDOCol u = UserDOCol.build();
        AptQueryWrapper<UserTenantDO> wrapper = JoinWrappers.apt(ut)
                .selectAll()
                .leftJoin(u, u.id, ut.uuid);
        List<UserTenantDO> list = userTenantMapper.selectList(wrapper);

        assert list.get(0).getIdea() != null;
        assert list.get(0).getUuid() != null;
    }
}
