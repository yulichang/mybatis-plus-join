package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.test.join.dto.AreaDTO;
import com.github.yulichang.test.join.entity.AreaDO;
import com.github.yulichang.test.join.entity.apt.AreaDOCol;
import com.github.yulichang.test.join.entity.apt.UserDtoCol;
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
        AreaDOCol ar = new AreaDOCol();
        UserDtoCol ud = new UserDtoCol();
        List<AreaDO> list = areaMapper.selectJoinList(AreaDO.class, JoinWrappers.apt(ar)
                .select(ar.Postcode)
                .leftJoin(ud, ud.id, ar.id));

        assert list.get(0).getPostcode() != null;
    }

    @Test
    void testFieldName1() {
        AreaDOCol ar = new AreaDOCol();
        UserDtoCol ud = new UserDtoCol();
        List<AreaDTO> list = areaMapper.selectJoinList(AreaDTO.class, JoinWrappers.apt(ar)
                .selectAs(ar.Postcode, AreaDTO::getPostcode)
                .leftJoin(ud, ud.id, ar.id));

        assert list.get(0).getPostcode() != null;
    }
}
