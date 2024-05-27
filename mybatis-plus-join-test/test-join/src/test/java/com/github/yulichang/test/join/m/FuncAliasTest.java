package com.github.yulichang.test.join.m;

import com.baomidou.mybatisplus.annotation.DbType;
import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.test.util.Throw;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.segments.Fun;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FuncAliasTest {
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        Reset.reset();
    }

    @Test
    void funcAlias() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by, count(ad.id) AS id, count(addr.id) AS id, " +
                "if(ad.user_id < 5, addr.user_id, ad.user_id + 100) AS id FROM `user` t " +
                "LEFT JOIN address ad ON (ad.user_id = t.id) LEFT JOIN address addr ON (addr.user_id = t.id) " +
                "WHERE t.del = false AND ad.del = false AND addr.del = false GROUP BY t.id");
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .selectFunc(() -> "count(%s)", "ad", AddressDO::getId)
                .selectFunc(() -> "count(%s)", "addr", AddressDO::getId)
                .selectFunc("if(%s < 5,%s,%s + 100)", arg -> arg.accept(
                        Fun.f("ad", AddressDO::getUserId),
                        Fun.f("addr", AddressDO::getUserId),
                        Fun.f("ad", AddressDO::getUserId)), UserDO::getId)
                .leftJoin(AddressDO.class, "ad", AddressDO::getUserId, UserDO::getId)
                .leftJoin(AddressDO.class, "addr", AddressDO::getUserId, UserDO::getId)
                .groupBy(UserDO::getId);

        Throw.tryDo(() -> {
            userMapper.selectJoinList(UserDO.class, wrapper);
        }, DbType.POSTGRE_SQL, DbType.ORACLE);
    }
}
