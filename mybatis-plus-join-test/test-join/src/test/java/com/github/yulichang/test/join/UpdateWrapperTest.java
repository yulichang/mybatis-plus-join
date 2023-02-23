package com.github.yulichang.test.join;

import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.entity.UserDto;
import com.github.yulichang.test.join.mapper.UserDTOMapper;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * springboot3 & jdk17+
 */
@SuppressWarnings("unused")
@SpringBootTest
class UpdateWrapperTest {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDTOMapper userDTOMapper;


    /**
     * 逻辑删除
     */
    @Test
    public void testUpdate() {
        ThreadLocalUtils.set("UPDATE `user` t SET del=true WHERE t.del=false AND (t.id = ?)");
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .eq(UserDO::getId, 1);

        userMapper.delete(wrapper);

        assert userMapper.selectById(1) == null;
    }

    /**
     * 非逻辑删除
     */
    @Test
    public void testUpdate1() {
        MPJLambdaWrapper<UserDto> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(UserDto::getId, 1);
        userDTOMapper.delete(wrapper);

        assert userDTOMapper.selectById(1) == null;
    }


    /**
     * 修改
     */
    @Test
    public void testUpdate2() {
//        MPJLambdaWrapper<UserDto> wrapper = new MPJLambdaWrapper<UserDto>()
//                .leftJoin(UserDO.class, UserDO::getId, UserDto::getUserId)
//                .eq(UserDto::getId, 1);
        MPJLambdaWrapper<UserDto> wrapper = new MPJLambdaWrapper<>();
        wrapper.eq(UserDto::getId, 1);
        userDTOMapper.update(new UserDto() {{
            setUserId(2222);
        }}, wrapper);
    }


}
