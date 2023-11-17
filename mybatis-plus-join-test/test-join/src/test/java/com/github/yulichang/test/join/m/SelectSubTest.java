package com.github.yulichang.test.join.m;

import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.AreaDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SelectSubTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    /**
     * select 子查询
     */
    @Test
    void sub() {
        ThreadLocalUtils.set("SELECT (SELECT st.id FROM `user` st WHERE st.del = false AND (st.id = t.id AND st.id = ?) LIMIT 1) AS id, (SELECT st.id FROM `user` st WHERE st.del = false AND (st.id = t.id AND st.id = ?) LIMIT 1) AS name FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del = false AND t1.del = false AND (t.id <= ?)");
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectSub(UserDO.class, w -> w.select(UserDO::getId)
                        .eq(UserDO::getId, UserDO::getId)
                        .eq(UserDO::getId, 2)
                        .last("limit 1"), UserDO::getId)
                .selectSub(UserDO.class, w -> w.select(UserDO::getId)
                        .eq(UserDO::getId, UserDO::getId)
                        .eq(UserDO::getId, 3)
                        .last("limit 1"), UserDO::getName)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .le(UserDO::getId, 100);
        wrapper.list();

        ThreadLocalUtils.set("SELECT (SELECT st.id FROM area st WHERE st.del = false AND (st.id = t1.id) LIMIT 1) AS id FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del = false AND t1.del = false AND (t.id <= ?)");
        MPJLambdaWrapper<UserDO> wrapper1 = JoinWrappers.lambda(UserDO.class)
                .selectSub(AreaDO.class, w -> w.select(AreaDO::getId)
                        .eq(AreaDO::getId, AddressDO::getId)
                        .last("limit 1"), UserDO::getId)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .le(UserDO::getId, 100);
        wrapper1.list();
    }
}
