package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.config.enums.IfExistsEnum;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.entity.apt.UserDOCol;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.apt.AptQueryWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class IfExistsTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }

    @Test
    void IfExists() {
        assert IfExistsEnum.NOT_EMPTY.test("\t");
        assert !IfExistsEnum.NOT_EMPTY.test("");
        assert IfExistsEnum.NOT_EMPTY.test(" ");
        assert IfExistsEnum.NOT_EMPTY.test("\r");
        assert IfExistsEnum.NOT_EMPTY.test("a");
        assert IfExistsEnum.NOT_EMPTY.test(1);
        assert IfExistsEnum.NOT_EMPTY.test(true);
        assert IfExistsEnum.NOT_EMPTY.test('A');

        assert !IfExistsEnum.NOT_BLANK.test("\t");
        assert !IfExistsEnum.NOT_BLANK.test("");
        assert !IfExistsEnum.NOT_BLANK.test(" ");
        assert !IfExistsEnum.NOT_BLANK.test("\r");
        assert IfExistsEnum.NOT_BLANK.test("a");
        assert IfExistsEnum.NOT_EMPTY.test(1);
        assert IfExistsEnum.NOT_EMPTY.test(true);
        assert IfExistsEnum.NOT_EMPTY.test('A');

        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                "WHERE t.del = false AND (t.id = ? AND t.head_img = ? AND t.`name` = ?)");
        UserDOCol u = new UserDOCol();
        AptQueryWrapper<UserDO> wrapper = JoinWrappers.apt(u)
                .selectAll()
                .eqIfExists(u.id, 1)
                .eqIfExists(u.pid, null)
                .eqIfExists(u.addressId, "")
                .eqIfExists(u.img, "\t")
                .eqIfExists(u.name, "张三 1");
        List<UserDO> list = wrapper.list();
        list.forEach(System.out::println);

        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                "WHERE t.del = false AND (t.id = ? AND t.`name` = ?)");
        UserDOCol u1 = new UserDOCol();
        AptQueryWrapper<UserDO> wrapper1 = JoinWrappers.apt(u1)
                .selectAll()
                .setIfExists(IfExistsEnum.NOT_BLANK)
                .eqIfExists(u1.id, 1)
                .eqIfExists(u1.pid, null)
                .eqIfExists(u1.addressId, "")
                .eqIfExists(u1.img, "\t")
                .eqIfExists(u1.name, "张三 1");
        List<UserDO> list1 = wrapper1.list();
        list1.forEach(System.out::println);

        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                "WHERE t.del = false AND (t.id = ? AND t.`name` = ? AND t.head_img = ? AND t.`name` = ?)");
        UserDOCol u2 = new UserDOCol();
        AptQueryWrapper<UserDO> wrapper2 = JoinWrappers.apt(u2)
                .selectAll()
                .setIfExists(o -> true)
                .eqIfExists(u2.id, 1)
                .eqIfExists(u2.name, "")
                .eqIfExists(u2.img, "\t")
                .eqIfExists(u2.name, "张三 1");
        List<UserDO> list2 = wrapper2.list();
        list2.forEach(System.out::println);
    }
}
