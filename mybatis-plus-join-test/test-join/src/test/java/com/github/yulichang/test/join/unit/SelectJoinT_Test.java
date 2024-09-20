package com.github.yulichang.test.join.unit;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.toolkit.JoinWrappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@SuppressWarnings("NewClassNamingConvention")
public class SelectJoinT_Test {

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void selectJoinT() {
        UserDO userDO = userMapper.selectJoinOne(JoinWrappers.<UserDO>lambda().eq(UserDO::getId, 1));
        assert userDO != null;
        System.out.println(userDO);

        List<UserDO> list = userMapper.selectJoinList(JoinWrappers.lambda());
        assert !list.isEmpty();
        list.forEach(i -> {
            assert i != null;
            System.out.println(i);
        });

        Page<UserDO> page = userMapper.selectJoinPage(new Page<>(1, 10), JoinWrappers.lambda());
        assert !page.getRecords().isEmpty();
        page.getRecords().forEach(i -> {
            assert i != null;
            System.out.println(i);
        });
    }
}
