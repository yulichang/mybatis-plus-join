package com.yulichang.test.springboot3jdk17;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yulichang.test.springboot3jdk17.entity.UserDO;
import com.yulichang.test.springboot3jdk17.entity.UserDto;
import com.yulichang.test.springboot3jdk17.mapper.UserDTOMapper;
import com.yulichang.test.springboot3jdk17.mapper.UserMapper;
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
