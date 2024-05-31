package com.github.yulichang.test.join.unit;

import com.github.yulichang.test.join.dto.AreaDTO;
import com.github.yulichang.test.join.entity.AreaDO;
import com.github.yulichang.test.join.entity.UserDto;
import com.github.yulichang.test.join.mapper.AreaMapper;
import com.github.yulichang.test.util.Reset;
import com.github.yulichang.toolkit.JoinWrappers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class FieldNameTest {

    @Autowired
    private AreaMapper areaMapper;

    @BeforeEach
    void setUp() {
        Reset.reset();
    }


    @Test
    void testFieldName() {
        List<AreaDO> list = areaMapper.selectJoinList(AreaDO.class, JoinWrappers.lambda(AreaDO.class)
                .select(AreaDO::getPostcode)
                .leftJoin(UserDto.class,UserDto::getId,AreaDO::getId));

        list.forEach(System.out::println);
        assert list.get(0).getPostcode() != null;
    }

    @Test
    void testFieldName1() {
        List<AreaDTO> list = areaMapper.selectJoinList(AreaDTO.class, JoinWrappers.lambda(AreaDO.class)
                .selectAs(AreaDO::getPostcode,AreaDTO::getPostcode)
                .leftJoin(UserDto.class,UserDto::getId,AreaDO::getId));

        list.forEach(System.out::println);
        assert list.get(0).getPostcode() != null;
    }
}
