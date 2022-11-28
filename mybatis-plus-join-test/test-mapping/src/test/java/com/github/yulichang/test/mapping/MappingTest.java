package com.github.yulichang.test.mapping;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.test.mapping.entity.UserDO;
import com.github.yulichang.test.mapping.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
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
        List<UserDO> dos = userMapper.selectListDeep(new QueryWrapper<>());
        System.out.println(1);
    }
}
