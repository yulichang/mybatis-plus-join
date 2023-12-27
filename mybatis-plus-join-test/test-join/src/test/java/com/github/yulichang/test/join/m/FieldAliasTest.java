package com.github.yulichang.test.join.m;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.entity.UserTenantDO;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.join.mapper.UserTenantMapper;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
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
        List<UserDO> list = userMapper.selectList(JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));

        list.forEach(System.out::println);

        assert list.get(0).getImg() != null;

    }

    @Test
    void fieldAlias1() {
        MPJLambdaWrapper<UserTenantDO> wrapper = JoinWrappers.lambda(UserTenantDO.class)
                .selectAll(UserTenantDO.class)
                .leftJoin(UserDO.class, UserDO::getId, UserTenantDO::getUuid);
        List<UserTenantDO> list = userTenantMapper.selectList(wrapper);

        new LambdaQueryWrapper<UserDO>().getSqlSelect();
        list.forEach(System.out::println);
        System.out.println(wrapper.getSqlSelect());
        assert list.get(0).getIdea() != null;
        assert list.get(0).getUuid() != null;
    }
}
