package com.github.yulichang.test.join.unit;

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
public class FromTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    /**
     * select 子查询
     */
    @Test
    void from() {
        ThreadLocalUtils.set("""
                SELECT
                    t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time,
                    t.address_id, t.address_id2, t.del, t.create_by, t.update_by
                FROM (SELECT
                        t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time,
                        t.address_id, t.address_id2, t.del, t.create_by, t.update_by
                    FROM `user` t WHERE t.del = false AND (t.id >= ?)) t
                WHERE t.del = false
                """);
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .from(from -> from
                        .selectAll()
                        .ge(UserDO::getId, 0));
        wrapper.list();
    }

    @Test
    void from1() {
        ThreadLocalUtils.set("""
                SELECT
                    t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time,
                    t.address_id, t.address_id2, t.del, t.create_by, t.update_by
                FROM (SELECT
                        t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time,
                        t.address_id, t.address_id2, t.del, t.create_by, t.update_by
                    FROM `user` t
                    WHERE t.del = false AND (t.id >= ?)
                    UNION ALL
                    (SELECT
                        t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time,
                        t.address_id, t.address_id2, t.del, t.create_by, t.update_by
                    FROM `user` t
                    WHERE t.del = false)) t
                WHERE t.del = false
                """);
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .from(from -> from
                        .selectAll()
                        .ge(UserDO::getId, 0)
                        .unionAll(UserDO.class, MPJLambdaWrapper::selectAll));
        wrapper.list();
    }
}
