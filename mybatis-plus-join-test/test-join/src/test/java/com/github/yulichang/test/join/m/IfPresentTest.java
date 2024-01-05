package com.github.yulichang.test.join.m;

import com.github.yulichang.config.enums.IfPresentEnum;
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
public class IfPresentTest {

    @BeforeEach
    void setUp() {
        Reset.reset();
    }

    @Test
    void ifPresent() {
        assert IfPresentEnum.NOT_EMPTY.test("\t");
        assert !IfPresentEnum.NOT_EMPTY.test("");
        assert IfPresentEnum.NOT_EMPTY.test(" ");
        assert IfPresentEnum.NOT_EMPTY.test("\r");
        assert IfPresentEnum.NOT_EMPTY.test("a");
        assert IfPresentEnum.NOT_EMPTY.test(1);
        assert IfPresentEnum.NOT_EMPTY.test(true);
        assert IfPresentEnum.NOT_EMPTY.test('A');

        assert !IfPresentEnum.NOT_BLANK.test("\t");
        assert !IfPresentEnum.NOT_BLANK.test("");
        assert !IfPresentEnum.NOT_BLANK.test(" ");
        assert !IfPresentEnum.NOT_BLANK.test("\r");
        assert IfPresentEnum.NOT_BLANK.test("a");
        assert IfPresentEnum.NOT_EMPTY.test(1);
        assert IfPresentEnum.NOT_EMPTY.test(true);
        assert IfPresentEnum.NOT_EMPTY.test('A');

        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                "WHERE t.del = false AND (t.id = ? AND t.head_img = ? AND t.`name` = ?)");
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .eqIfPresent(UserDO::getId, 1)
                .eqIfPresent(UserDO::getPid, null)
                .eqIfPresent(UserDO::getAddressId, "")
                .eqIfPresent(UserDO::getImg, "\t")
                .eqIfPresent(UserDO::getName, "张三 1");
        List<UserDO> list = wrapper.list();
        list.forEach(System.out::println);

        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                "WHERE t.del = false AND (t.id = ? AND t.`name` = ?)");
        MPJLambdaWrapper<UserDO> wrapper1 = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .setIfPresent(IfPresentEnum.NOT_BLANK)
                .eqIfPresent(UserDO::getId, 1)
                .eqIfPresent(UserDO::getPid, null)
                .eqIfPresent(UserDO::getAddressId, "")
                .eqIfPresent(UserDO::getImg, "\t")
                .eqIfPresent(UserDO::getName, "张三 1");
        List<UserDO> list1 = wrapper1.list();
        list1.forEach(System.out::println);

        ThreadLocalUtils.set("SELECT t.id, t.pid, t.`name`, t.`json`, t.sex, t.head_img, t.create_time, t.address_id, " +
                "t.address_id2, t.del, t.create_by, t.update_by FROM `user` t " +
                "WHERE t.del = false AND (t.id = ? AND t.pid = ? AND t.`name` = ? AND t.head_img = ? AND t.`name` = ?)");
        MPJLambdaWrapper<UserDO> wrapper2 = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .setIfPresent(o -> true)
                .eqIfPresent(UserDO::getId, 1)
                .eqIfPresent(UserDO::getPid, null)
                .eqIfPresent(UserDO::getName, "")
                .eqIfPresent(UserDO::getImg, "\t")
                .eqIfPresent(UserDO::getName, "张三 1");
        List<UserDO> list2 = wrapper2.list();
        list2.forEach(System.out::println);
    }
}
