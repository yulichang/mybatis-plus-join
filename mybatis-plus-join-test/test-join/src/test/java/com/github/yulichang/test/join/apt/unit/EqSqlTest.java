package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.extension.apt.toolkit.AptWrappers;
import com.github.yulichang.test.join.entity.apt.UserDOCol;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EqSqlTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void eqSql() {
        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false AND (t.id = (SELECT id FROM `user` WHERE id = 1))");
        UserDOCol u = UserDOCol.build();
        AptWrappers.query(u).eqSql(u.id, "select id from `user` where id = 1").list();

        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t WHERE t.del = false AND (t.id = (SELECT id FROM `user` WHERE id = 1))");
        UserDOCol u1 = UserDOCol.build();
        AptWrappers.query(u1).eqSql("t.id", "select id from `user` where id = 1").list();
    }

}
