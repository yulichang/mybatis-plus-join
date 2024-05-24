package com.github.yulichang.test.join.m;

import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;

@SpringBootTest
public class UpdateIncTest {

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
        try {
            JoinWrappers.update(UserDO.class).set(AddressDO::getAddress, UserDO::getImg)
                    .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId).update();
        } catch (BadSqlGrammarException ignore) {
        }
        JoinWrappers.lambda(UserDO.class).list().forEach(System.out::println);
    }

}
