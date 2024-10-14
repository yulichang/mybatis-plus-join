package com.github.yulichang.test.join.unit;

import com.github.yulichang.test.join.dto.UserDTO;
import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SelectFuncTest {


    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void applyFunc() {
        ThreadLocalUtils.set("""
                SELECT t.id, t.pid, t.`name`, t.`json`,
                    t.sex, t.head_img, t.create_time, t.address_id,
                    t.address_id2, t.del, t.create_by, t.update_by,
                    concat(t.id, t1.address, ?, ?) AS address
                FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id)
                WHERE t.del = false AND t1.del = false
                    AND (concat(t.id, t1.user_id, ?) IS NOT NULL AND t.id >= ?)
                """);
        List<UserDTO> list = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .selectFunc("concat(%s,%s,{0},{1})",
                        arg -> arg
                                .accept(UserDO::getId, AddressDO::getAddress)
                                .values("aaa", 123),
                        UserDTO::getAddress)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .applyFunc("concat(%s,%s,{0}) is not null", arg -> arg.accept(UserDO::getId, AddressDO::getUserId), "12")
                .ge(UserDO::getId, 0)
                .list(UserDTO.class);
        list.forEach(System.out::println);
    }

}
