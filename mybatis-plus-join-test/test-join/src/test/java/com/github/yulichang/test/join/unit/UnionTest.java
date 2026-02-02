package com.github.yulichang.test.join.unit;

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

import java.util.List;

@SpringBootTest
public class UnionTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void unionAll() {
        ThreadLocalUtils.set("""
                SELECT
                    t.id
                FROM `user` t
                WHERE t.del = false
                    AND (t.id = ?)
                UNION ALL
                (SELECT
                    t.id
                FROM address t
                WHERE (t.id = ?))
                UNION ALL
                (SELECT
                    (SELECT
                        st.id
                    FROM area st
                    WHERE st.del = false
                        AND (st.id = ? AND (st.id = ?))) AS id
                FROM area t
                WHERE t.del = false
                    AND (t.id = ? AND (t.id = ?)))
                """);
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .select(UserDO::getId)
                .eq(UserDO::getId, 1)
                .unionAll(AddressDO.class, union -> union
                        .select(AddressDO::getId)
                        .disableLogicDel()
                        .eq(AddressDO::getId, 2))
                .unionAll(AreaDO.class, union -> union
                        .selectSub(AreaDO.class, sub -> sub
                                .select(AreaDO::getId)
                                .eq(AreaDO::getId, 3)
                                .and(and -> and.eq(AreaDO::getId, 4)), AreaDO::getId)
                        .eq(AreaDO::getId, 5)
                        .and(and -> and.eq(AreaDO::getId, 6)));
        List<UserDO> list = wrapper.list();
        assert list.size() == 2 && list.get(0).getId() != null;
    }

    @Test
    void unionAll1() {
        ThreadLocalUtils.set("""
                SELECT
                    t.id
                FROM `user` t
                WHERE t.del = false
                    AND (t.id = ?)
                UNION
                (SELECT
                    t.id
                FROM address t
                WHERE (t.id = ?))
                UNION
                (SELECT
                    (SELECT
                        st.id
                    FROM area st
                    WHERE st.del = false
                        AND (st.id = ? AND (st.id = ?))) AS id
                FROM area t
                WHERE t.del = false
                    AND (t.id = ? AND (t.id = ?)))
                """);
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .select(UserDO::getId)
                .eq(UserDO::getId, 1)
                .union(AddressDO.class, union -> union
                        .select(AddressDO::getId)
                        .disableLogicDel()
                        .eq(AddressDO::getId, 2))
                .union(AreaDO.class, union -> union
                        .selectSub(AreaDO.class, sub -> sub
                                .select(AreaDO::getId)
                                .eq(AreaDO::getId, 3)
                                .and(and -> and.eq(AreaDO::getId, 4)), AreaDO::getId)
                        .eq(AreaDO::getId, 5)
                        .and(and -> and.eq(AreaDO::getId, 6)));
        List<UserDO> list = wrapper.list();
        assert list.size() == 2 && list.get(0).getId() != null;
    }
}
