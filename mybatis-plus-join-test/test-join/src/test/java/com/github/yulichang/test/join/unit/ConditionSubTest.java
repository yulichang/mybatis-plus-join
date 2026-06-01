package com.github.yulichang.test.join.unit;

import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.EnabledIfConfig;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@SpringBootTest
@EnabledIf(value = EnabledIfConfig.runWithExcludingOracle, loadContext = true)
public class ConditionSubTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    /**
     * select 子查询
     */
    @Test
    void conditionSub() {
        ThreadLocalUtils.set("""
                SELECT
                    t.id,
                    t.pid,
                    t.`name`,
                    t.`json`,
                    t.sex,
                    t.head_img,
                    t.create_time,
                    t.address_id,
                    t.address_id2,
                    t.del,
                    t.create_by,
                    t.update_by
                FROM `user` t
                    LEFT JOIN address t1 ON (t1.user_id = t.id)
                WHERE t.del = false
                    AND t1.del = false
                    AND (t.id >= ? AND t.id <= ? AND
                         t.id = (SELECT st.id FROM `user` st WHERE st.del = false AND (st.id = t.id AND st.id >= ? AND st.id <= ?))
                    AND t.id >= ? AND t.id <= ?)
                """);
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .ge(UserDO::getId, -2)
                .le(UserDO::getId, 102)
                .eq(UserDO::getId, UserDO.class, w -> w
                        .select(UserDO::getId)
                        .eq(UserDO::getId, UserDO::getId)
                        .ge(UserDO::getId, 0)
                        .le(UserDO::getId, 100))
                .ge(UserDO::getId, -1)
                .le(UserDO::getId, 101);

        wrapper.list();

        ThreadLocalUtils.set("""
                SELECT
                    t.id,
                    t.pid,
                    t.`name`,
                    t.`json`,
                    t.sex,
                    t.head_img,
                    t.create_time,
                    t.address_id,
                    t.address_id2,
                    t.del,
                    t.create_by,
                    t.update_by
                FROM `user` t
                    LEFT JOIN address t1 ON (t1.user_id = t.id)
                WHERE t.del = false
                    AND t1.del = false
                    AND (t.id = (SELECT sst.id FROM `user` sst WHERE sst.del = false AND (sst.id = t.id AND sst.id >= ? AND sst.id <= ?)))
                """);
        MPJLambdaWrapper<UserDO> wrapper1 = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .eq(UserDO::getId, UserDO.class, w -> w
                        .setAlias("sst")
                        .select(UserDO::getId)
                        .eq(UserDO::getId, UserDO::getId)
                        .ge(UserDO::getId, 0)
                        .le(UserDO::getId, 100));
        wrapper1.list();
    }

    @Test
    void conditionSubIn() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                WHERE t.del = false
                  AND t1.del = false
                  AND (t.id >= ? AND t.id <= ? AND
                       t.id IN (SELECT t.id FROM `user` t WHERE t.del = false AND (t.id >= ? AND t.id <= ?)) AND t.id >= ? AND
                       t.id <= ?)
                """);
        MPJLambdaWrapper<UserDO> wrapper1 = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .ge(UserDO::getId, -2)
                .le(UserDO::getId, 102)
                .in(UserDO::getId, UserDO.class, u -> u
                        .select(UserDO::getId)
                        .ge(UserDO::getId, 0)
                        .le(UserDO::getId, 100))
                .ge(UserDO::getId, -1)
                .le(UserDO::getId, 101);
        wrapper1.list();
    }

    @Test
    void conditionSubExists() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                WHERE t.del = false
                  AND t1.del = false
                  AND (t.id >= ? AND t.id <= ? AND
                       EXISTS (SELECT st.id FROM `user` st WHERE st.del = false AND (st.id >= ? AND st.id <= ?)) AND t.id >= ? AND
                       t.id <= ?)
                """);
        MPJLambdaWrapper<UserDO> wrapper1 = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .ge(UserDO::getId, -2)
                .le(UserDO::getId, 102)
                .exists(UserDO.class, u -> u
                        .select(UserDO::getId)
                        .ge(UserDO::getId, 0)
                        .le(UserDO::getId, 100))
                .ge(UserDO::getId, -1)
                .le(UserDO::getId, 101);
        wrapper1.list();
    }

    @Test
    void conditionSubHaving() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                WHERE t.del = false
                  AND t1.del = false
                  AND (t.id >= ? AND t.id <= ? AND
                       NOT EXISTS (SELECT st.id FROM `user` st WHERE st.del = false AND (st.id >= ? AND st.id <= ?)) AND t.id >= ? AND
                       t.id <= ?)
                """);
        MPJLambdaWrapper<UserDO> wrapper1 = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .ge(UserDO::getId, -2)
                .le(UserDO::getId, 102)
                .notExists(UserDO.class, h -> h
                        .select(UserDO::getId)
                        .ge(UserDO::getId, 0)
                        .le(UserDO::getId, 100))
                .ge(UserDO::getId, -1)
                .le(UserDO::getId, 101);
        wrapper1.list();
    }

    @Test
    void conditionSubEQ() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                WHERE t.del = false
                  AND t1.del = false
                  AND (t.id >= ? AND t.id <= ? AND
                       t.id = (SELECT st.id FROM `user` st WHERE st.del = false AND (st.id = t1.id AND st.id >= ? AND st.id <= ?)) AND
                       t.id >= ? AND t.id <= ?)
                """);
        MPJLambdaWrapper<UserDO> wrapper1 = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .ge(UserDO::getId, -2)
                .le(UserDO::getId, 102)
                .eq(UserDO::getId, UserDO.class, u -> u
                        .select(UserDO::getId)
                        .eq(UserDO::getId, AddressDO::getId)
                        .ge(UserDO::getId, 0)
                        .le(UserDO::getId, 100))
                .ge(UserDO::getId, -1)
                .le(UserDO::getId, 101);
        wrapper1.list();
    }
}
