package com.github.yulichang.test.mapping;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.test.mapping.entity.AddressDO;
import com.github.yulichang.test.mapping.entity.UserDO;
import com.github.yulichang.test.mapping.service.UserService;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
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
    private UserService userService;

    @Test
    public void test() {
        List<UserDO> dos = userService.getRelation(m -> m.selectList(new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getPid, 5)), conf -> conf.prop(UserDO::getPUser).loop(true).maxCount(100));
        System.out.println(1);
    }

    @Test
    public void testJoin() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .leftJoin(AddressDO.class, AddressDO::getId, UserDO::getAddressId);
        List<UserDO> dos = userService.listDeep(wrapper);
        System.out.println(1);
    }
}
