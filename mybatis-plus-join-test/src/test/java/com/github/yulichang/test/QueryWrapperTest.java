package com.github.yulichang.test;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.test.dto.UserDTO;
import com.github.yulichang.test.entity.UserDO;
import com.github.yulichang.test.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SpringBootTest
class QueryWrapperTest {
    @Resource
    private UserMapper userMapper;

    /**
     * 链表查询
     */
    @Test
    void test1() {
        UserDO userDO = userMapper.selectJoinOne(UserDO.class, new MPJQueryWrapper<UserDO>()
                .selectAll(UserDO.class).last("LIMIT 1"));
        System.out.println(userDO);

        UserDTO dto = userMapper.selectJoinOne(UserDTO.class, new MPJQueryWrapper<UserDO>()
                .selectAll(UserDO.class)
                .select("name AS nameName")
                .last("LIMIT 1"));
        System.out.println(dto);
    }

    @Test
    void test2() {
        List<UserDO> userDO = userMapper.selectJoinList(UserDO.class, new MPJQueryWrapper<UserDO>()
                .selectAll(UserDO.class)
                .leftJoin("address t2 on t2.user_id = t.id")
                .le("t.id ", 10));
        System.out.println(userDO);

        List<UserDTO> dto = userMapper.selectJoinList(UserDTO.class, new MPJQueryWrapper<UserDO>()
                .selectAll(UserDO.class)
                .select("t2.address AS userAddress")
                .leftJoin("address t2 on t2.user_id = t.id")
                .le("t.id ", 10));
        System.out.println(dto);
    }

    @Test
    void test3() {
        IPage<UserDO> userDO = userMapper.selectJoinPage(new Page<>(1, 10), UserDO.class, new MPJQueryWrapper<UserDO>()
                .selectAll(UserDO.class)
                .leftJoin("address t2 on t2.user_id = t.id")
                .lt("t.id ", 5));
        System.out.println(userDO);

        IPage<UserDTO> dto = userMapper.selectJoinPage(new Page<>(1, 10), UserDTO.class, new MPJQueryWrapper<UserDO>()
                .selectAll(UserDO.class)
                .select("t2.address AS userAddress")
                .leftJoin("address t2 on t2.user_id = t.id")
                .lt("t.id ", 5));
        System.out.println(dto);
    }

    @Test
    void test4() {
        List<Map<String, Object>> maps = userMapper.selectJoinMaps(new MPJQueryWrapper<UserDO>()
                .selectAll(UserDO.class)
                .leftJoin("address t2 on t2.user_id = t.id")
                .lt("t.id ", 5));
        System.out.println(maps);

        List<Map<String, Object>> joinMaps = userMapper.selectJoinMaps(new MPJQueryWrapper<UserDO>()
                .selectAll(UserDO.class)
                .select("t2.address AS userAddress")
                .leftJoin("address t2 on t2.user_id = t.id")
                .lt("t.id ", 5));
        System.out.println(joinMaps);
    }

}
