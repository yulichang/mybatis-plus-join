package com.github.yulichang.test.join.mysql;

import com.github.yulichang.test.join.entity.*;
import com.github.yulichang.test.join.mapper.OrderMapper;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.DeleteJoinWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 连表删除没有同意语法语法，不同数据库差别较大
 * MPJ 连表更新 目前只支持 mysql
 */
@SpringBootTest
@EnabledIf("com.github.yulichang.test.util.EnabledIf#runWithMysql")
public class DeleteJoinTest {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderMapper orderMapper;

    @BeforeEach
    void setUp() {
        Reset.reset();
    }

    /**
     * 同一个类字段比较
     */
    @Test
    void delete() {
        //物理删除
        ThreadLocalUtils.set("DELETE t FROM order_t t LEFT JOIN user_dto t1 ON (t1.id = t.user_id) WHERE (t.id = ?)");
        DeleteJoinWrapper<OrderDO> w = JoinWrappers.delete(OrderDO.class)
                .leftJoin(UserDto.class, UserDto::getId, OrderDO::getUserId)
                .eq(OrderDO::getId, 1);

        orderMapper.deleteJoin(w);

        //逻辑删除
        ThreadLocalUtils.set("UPDATE `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) LEFT JOIN area t2 ON (t2.id = t1.area_id) SET t.del=true ,t1.del=true,t2.del=true WHERE t.del=false AND t1.del=false AND t2.del=false AND (t.id = ?)");
        DeleteJoinWrapper<UserDO> wrapper = JoinWrappers.delete(UserDO.class)
                .deleteAll()
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .leftJoin(AreaDO.class, AreaDO::getId, AddressDO::getAreaId)
                .eq(OrderDO::getId, 1);
        userMapper.deleteJoin(wrapper);
    }
}
