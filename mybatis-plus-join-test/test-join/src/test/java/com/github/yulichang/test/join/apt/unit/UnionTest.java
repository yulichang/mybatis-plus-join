package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.extension.apt.AptQueryWrapper;
import com.github.yulichang.extension.apt.toolkit.AptWrappers;
import com.github.yulichang.test.join.entity.AreaDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.entity.apt.AddressDOCol;
import com.github.yulichang.test.join.entity.apt.AreaDOCol;
import com.github.yulichang.test.join.entity.apt.UserDOCol;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
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
    void unionAll1() {
        ThreadLocalUtils.set("SELECT t.id FROM `user` t WHERE t.del = false AND (t.id = ?) UNION ALL SELECT t.id FROM address t WHERE (t.id = ?) UNION ALL SELECT (SELECT st.id FROM area st WHERE st.del = false AND (st.id = ? AND (st.id = ?))) AS id FROM area t WHERE t.del = false AND (t.id = ? AND (t.id = ?))");

        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AreaDOCol area = AreaDOCol.build();
        AreaDOCol areaSb = AreaDOCol.build();

        AptQueryWrapper<UserDO> wrapper = AptWrappers.query(u)
                .select(u.id)
                .eq(u.id, 1)
                .unionAll(addr, union -> union
                        .select(addr.id)
                        .disableLogicDel()
                        .eq(addr.id, 2))
                .unionAll(area, union -> union
                        .selectSub(areaSb, sub -> sub
                                .select(areaSb.id)
                                .eq(areaSb.id, 3)
                                .and(and -> and.eq(areaSb.id, 4)), AreaDO::getId)
                        .eq(area.id, 5)
                        .and(and -> and.eq(area.id, 6)));
        List<UserDO> list = wrapper.list();
        assert list.size() == 2 && list.get(0).getId() != null;
    }
}
