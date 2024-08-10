package com.github.yulichang.test.join.apt.unit;

import com.github.yulichang.extension.apt.toolkit.AptWrappers;
import com.github.yulichang.test.join.dto.AreaDTO;
import com.github.yulichang.test.join.entity.AreaDO;
import com.github.yulichang.test.join.entity.apt.AreaDOCol;
import com.github.yulichang.test.join.entity.apt.UserDtoCol;
import com.github.yulichang.test.join.mapper.AreaMapper;
import com.github.yulichang.test.util.Reset;
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
        AreaDOCol ar = AreaDOCol.build();
        UserDtoCol ud = UserDtoCol.build();
        List<AreaDO> list = areaMapper.selectJoinList(AreaDO.class, AptWrappers.query(ar)
                .select(ar.Postcode)
                .leftJoin(ud, ud.id, ar.id));

        assert list.get(0).getPostcode() != null;
    }

    @Test
    void testFieldName1() {
        AreaDOCol ar = AreaDOCol.build();
        UserDtoCol ud = UserDtoCol.build();
        List<AreaDTO> list = areaMapper.selectJoinList(AreaDTO.class, AptWrappers.query(ar)
                .selectAs(ar.Postcode, AreaDTO::getPostcode)
                .leftJoin(ud, ud.id, ar.id));

        assert list.get(0).getPostcode() != null;
    }
}
