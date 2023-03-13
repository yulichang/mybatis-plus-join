package com.github.yulichang.test.join;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.test.join.dto.UserDTO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.util.ThreadLocalUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;

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
        UserDTO dto = userMapper.selectJoinOne(UserDTO.class, new MPJQueryWrapper<UserDO>()
                .selectAll(UserDO.class)
                .select("name AS nameName")
                .last("LIMIT 1"));
        System.out.println(dto);
    }

    /**
     * 链表查询
     */
    @Test
    void table() {
        ThreadLocalUtils.set("SELECT t.id,t.pid,t.`name`,t.`json`,t.sex,t.head_img,t.create_time,t.address_id,t.address_id2,t.del,t.create_by,t.update_by,name AS nameName FROM `user`fwear t WHERE t.del=false LIMIT 1");
        MPJQueryWrapper<UserDO> wrapper = new MPJQueryWrapper<UserDO>()
                .selectAll(UserDO.class)
                .setTableName(name -> name + "fwear")
                .select("name AS nameName")
                .last("LIMIT 1");
        try {
            userMapper.selectJoinOne(UserDTO.class, wrapper);
        } catch (BadSqlGrammarException ignored) {
        }
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
