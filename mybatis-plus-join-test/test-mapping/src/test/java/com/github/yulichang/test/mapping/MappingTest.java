package com.github.yulichang.test.mapping;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.test.mapping.entity.AddressDO;
import com.github.yulichang.test.mapping.entity.UserDO;
import com.github.yulichang.test.mapping.mapper.UserMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 注解一对一,一对多查询
 */
@SpringBootTest
@SuppressWarnings("unused")
class MappingTest {

    @Resource
    private UserMapper userMapper;

    @Test
    public void test() {
        List<UserDO> dos = userMapper.selectRelation(e -> e.selectList(new QueryWrapper<>()), Collections.singletonList(UserDO::getAddressId));
        System.out.println(1);
    }

    @Test
    public void testJoin() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .leftJoin(AddressDO.class, AddressDO::getId, UserDO::getAddressId);
        List<UserDO> dos = userMapper.selectRelation(e -> e.selectList(wrapper));
        System.out.println(1);
    }
}
