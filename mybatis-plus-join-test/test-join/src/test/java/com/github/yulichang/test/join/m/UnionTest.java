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

import java.util.List;

@SuppressWarnings("deprecation")
@SpringBootTest
public class UnionTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void union() {
        ThreadLocalUtils.set("SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t WHERE t.del=false AND (t.id = ?) UNION SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t WHERE (t.`name` = ? AND (t.`name` = ?)) UNION SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by FROM `user` t WHERE t.del=false AND (t.pid = ?)");
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .eq(UserDO::getId, 1);
        MPJLambdaWrapper<UserDO> wrapper1 = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .disableLogicDel()
                .eq(UserDO::getName, "张三 2")
                .and(w -> w.eq(UserDO::getName, "张三 2"));
        MPJLambdaWrapper<UserDO> wrapper2 = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .eq(UserDO::getPid, 2);
        wrapper.union(wrapper1, wrapper2);
        List<UserDO> list = wrapper.list();

        System.out.println(wrapper.getUnionSql());
        assert list.size() == 7 && list.get(0).getId() != null;
    }

    @Test
    void unionAll() {
        ThreadLocalUtils.set("SELECT t.id FROM `user` t WHERE t.del = false AND (t.id = ?) UNION ALL SELECT t.id FROM address t UNION ALL SELECT t.id FROM area t WHERE t.del = false AND (t.id = ?)");
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .select(UserDO::getId)
                .eq(UserDO::getId, 1);
        MPJLambdaWrapper<AddressDO> wrapper1 = JoinWrappers.lambda(AddressDO.class)
                .select(AddressDO::getId)
                .disableLogicDel();
        MPJLambdaWrapper<AreaDO> wrapper2 = JoinWrappers.lambda(AreaDO.class)
                .select(AreaDO::getId)
                .eq(AreaDO::getId, 2);
        wrapper.unionAll(wrapper1, wrapper2);
        List<UserDO> list = wrapper.list();

        assert list.size() == 23 && list.get(0).getId() != null;
    }

    @Test
    void unionAll1() {
        ThreadLocalUtils.set("SELECT t.id FROM `user` t WHERE t.del = false AND (t.id = ?) UNION ALL SELECT t.id FROM address t WHERE (t.id = ?) UNION ALL SELECT (SELECT st.id FROM area st WHERE st.del = false AND (st.id = ? AND (st.id = ?))) AS id FROM area t WHERE t.del = false AND (t.id = ? AND (t.id = ?))");
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .select(UserDO::getId)
                .eq(UserDO::getId, 1)
                .unionAll(AddressDO.class, union -> union
                        .select(AddressDO::getId)
                        .disableLogicDel()
                        .eq(UserDO::getId, 2))
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
}
