package com.github.yulichang.test.join.mysql;

import com.baomidou.mybatisplus.core.plugins.IgnoreStrategy;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.github.yulichang.test.join.dto.UserJson;
import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.OrderDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.util.EnabledIfConfig;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.UpdateJoinWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 连表更新没有同意语法语法，不同数据库差别较大
 * MPJ 连表更新 目前只支持 mysql
 */
@SpringBootTest("spring.profiles.active=mysql")
@EnabledIf(value = EnabledIfConfig.runWithMysql,loadContext = true)
public class UpdateJoinTest {

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        Reset.reset();
    }

    @Test
    void updateInc() {
        ThreadLocalUtils.set("UPDATE `user` t SET t.id = t.id + 100 WHERE t.del = false");
        JoinWrappers.update(UserDO.class).setIncrBy(UserDO::getId, 100).update();

        JoinWrappers.lambda(UserDO.class).list().forEach(System.out::println);

        ThreadLocalUtils.set("UPDATE `user` t SET t.id = t.id - 100 WHERE t.del = false");
        JoinWrappers.update(UserDO.class).setDecrBy(UserDO::getId, 100).update();

        JoinWrappers.lambda(UserDO.class).list().forEach(System.out::println);
    }


    @Test
    void updateInc1() {
        ThreadLocalUtils.set("UPDATE `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) SET t1.address = t.head_img WHERE t.del = false AND t1.del = false");
        JoinWrappers.update(UserDO.class).set(AddressDO::getAddress, UserDO::getImg)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId).update();
        JoinWrappers.lambda(UserDO.class).list().forEach(System.out::println);
    }

    @Test
    void updateInc2() {
        UserJson json = new UserJson();
        json.setId(1111L);
        json.setName("11111111111");

        List<UserJson> jsonList = new ArrayList<>();
        jsonList.add(json);

        UserDO userDO = new UserDO();
        userDO.setJson(jsonList);

        InterceptorIgnoreHelper.handle(IgnoreStrategy.builder().tenantLine(true).build());

        UpdateJoinWrapper<UserDO> wrapper = JoinWrappers.update(UserDO.class)
                .setUpdateEntity(userDO);

        userMapper.updateJoin(null, wrapper);

        InterceptorIgnoreHelper.clearIgnoreStrategy();

        List<UserDO> list = JoinWrappers.lambda(UserDO.class).list();
        list.forEach(System.out::println);
        list.forEach(c -> {
            assert Objects.equals(c.getJson().get(0).getName(), "11111111111");
        });
    }


    @Test
    void update() {
        ThreadLocalUtils.set("UPDATE `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) SET t.update_by=?, t.`name`=?,t1.address=?,t1.tel=?,t1.address=?,t.`name`=?,t.update_by=?,t1.user_id=?,t1.area_id=?,t1.tel=?,t1.address=? WHERE t.del=false AND t1.del=false AND (t.id = ?)");
        UpdateJoinWrapper<UserDO> update = JoinWrappers.update(UserDO.class)
                .set(UserDO::getName, "aaaaaa")
                .set(AddressDO::getAddress, "bbbbb")
                .setUpdateEntity(new AddressDO().setAddress("sadf").setTel("qqqqqqqq"),
                        new UserDO().setName("nnnnnnnnnnnn").setUpdateBy(1))
                .setUpdateEntityAndNull(new AddressDO())
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .eq(OrderDO::getId, 1);
        System.out.println(update.getSqlSet());
        userMapper.updateJoin(new UserDO().setUpdateBy(123123), update);

        ThreadLocalUtils.set("UPDATE `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) SET t.pid=?, " +
                "t.`name`=?, t.`json`=?, t.sex=?, t.head_img=?, t.create_time=?, t.address_id=?, t.address_id2=?, " +
                "t.create_by=?, t.update_by=? WHERE t.del=false AND t1.del=false AND (t.id = ?)");

        UpdateJoinWrapper<UserDO> up = JoinWrappers.update(UserDO.class)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .eq(OrderDO::getId, 1);
        userMapper.updateJoinAndNull(new UserDO(), up);
    }
}
