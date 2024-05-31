package com.github.yulichang.test.join.unit;

import com.github.yulichang.test.join.dto.UserDTO;
import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class StringColumTest {

    @Resource
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    @SneakyThrows
    void stringColum() {
        ThreadLocalUtils.set("SELECT (SELECT id FROM `user` u WHERE u.id = t.id) id, t.`name` AS PName, t.`name` PName, t.`name`," +
                " (SELECT id FROM `user` u WHERE u.id = t.id), t1.id AS joina_id, t1.user_id, t1.area_id, t1.tel, " +
                "t1.address, t1.del FROM `user` t LEFT JOIN address t1 ON (t1.user_id = t.id) WHERE t.del = false AND t1.del = false");
        List<UserDTO> l3 = userMapper.selectJoinList(UserDTO.class, new MPJLambdaWrapper<UserDO>()
                .select("(select id from `user` u where u.id = t.id) id")
                .select("t.`name` as PName")
                .select("t.`name` PName")
                .select("t.`name`")
                .select("(select id from `user` u where u.id = t.id) ")
                .selectAssociation(AddressDO.class, UserDTO::getAddressDTO)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));
        assert l3.get(0).getPName() != null;
        l3.forEach(System.out::println);
    }
}
