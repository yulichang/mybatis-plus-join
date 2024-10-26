package com.github.yulichang.test.join.unit;

import com.github.yulichang.test.join.dto.UserDTO;
import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.segments.Fun;
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

    @Test
    void applyFunc1() {
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
                    t.update_by,
                    (SELECT st.id FROM `user` st WHERE st.del = false AND (st.id = t.id)) AS address
                FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id)
                WHERE t.del = false
                    AND t1.del = false
                    AND (t.id = (SELECT sst.id FROM `user` sst WHERE sst.del = false AND (sst.id = t.id AND sst.id >= ? AND sst.id <= ?)))
                """);
        MPJLambdaWrapper<UserDO> wrapper1 = JoinWrappers.lambda(UserDO.class)
                .selectAll()
                .selectFunc("%s", arg -> arg.accept(
                                Fun.f(UserDO.class, u -> u
                                        .select(UserDO::getId)
                                        .eq(UserDO::getId, UserDO::getId))),
                        UserDTO::getAddress)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .eq(UserDO::getId, UserDO.class, w -> w
                        .setAlias("sst")
                        .select(UserDO::getId)
                        .eq(UserDO::getId, UserDO::getId)
                        .ge(UserDO::getId, 0)
                        .le(UserDO::getId, 100));
        wrapper1.list(UserDTO.class);
    }

}
