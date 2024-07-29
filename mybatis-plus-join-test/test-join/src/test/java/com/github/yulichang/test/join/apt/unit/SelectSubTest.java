package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.entity.apt.AddressDOCol;
import com.github.yulichang.test.join.entity.apt.AreaDOCol;
import com.github.yulichang.test.join.entity.apt.UserDOCol;
import com.github.yulichang.test.util.EnabledIfConfig;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.apt.AptQueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.EnabledIf;

@SpringBootTest
@EnabledIf(value = EnabledIfConfig.runWithExcludingOracle, loadContext = true)
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
        UserDOCol u = UserDOCol.build();
        UserDOCol sb = UserDOCol.build();
        UserDOCol sb2 = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();

        AptQueryWrapper<UserDO> wrapper = JoinWrappers.apt(u)
                .selectSub(sb, w -> w.select(sb.id)
                        .eq(sb.id, u.id)
                        .eq(sb.id, 2)
                        .last("limit 1"), UserDO::getId)
                .selectSub(sb2, w -> w.select(sb2.id)
                        .eq(sb2.id, u.id)
                        .eq(sb2.id, 3)
                        .last("limit 1"), UserDO::getName)
                .leftJoin(addr, addr.userId, u.id)
                .le(u.id, 100);
        wrapper.list();

        UserDOCol u1 = UserDOCol.build();
        AddressDOCol addr1 = AddressDOCol.build();
        AreaDOCol area = AreaDOCol.build();

        ThreadLocalUtils.set("SELECT (SELECT st.id FROM area st WHERE st.del = false AND (st.id = t1.id) LIMIT 1) AS id FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del = false AND t1.del = false AND (t.id <= ?)");
        AptQueryWrapper<UserDO> wrapper1 = JoinWrappers.apt(u1)
                .selectSub(area, w -> w.select(area.id)
                        .eq(area.id, addr1.id)
                        .last("limit 1"), UserDO::getId)
                .leftJoin(addr1, addr1.userId, u1.id)
                .le(u1.id, 100);
        wrapper1.list();
    }

    @Test
    void sub1() {
        ThreadLocalUtils.set("SELECT (SELECT st.id FROM `area` st WHERE st.del = false AND (st.id = t1.id) LIMIT 1) AS id FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del = false AND t1.del = false AND (t.id <= ?)");
        UserDOCol u = UserDOCol.build();
        AddressDOCol addr = AddressDOCol.build();
        AreaDOCol ar = AreaDOCol.build();

        AptQueryWrapper<UserDO> wrapper1 = JoinWrappers.apt(u)
                .selectSub(ar, w -> w.select(ar.id)
                        .eq(ar.id, addr.id)
                        .setTableName(t -> "`" + t + "`")
                        .last("limit 1"), UserDO::getId)
                .leftJoin(addr, addr.userId, u.id)
                .le(u.id, 100);
        wrapper1.list();
    }
}
