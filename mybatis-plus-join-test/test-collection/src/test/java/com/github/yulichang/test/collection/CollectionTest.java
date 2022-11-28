package com.github.yulichang.test.collection;

import com.github.yulichang.test.collection.dto.TableADTO;
import com.github.yulichang.test.collection.dto.TableBDTO;
import com.github.yulichang.test.collection.dto.TableCDTO;
import com.github.yulichang.test.collection.dto.TableDDTO;
import com.github.yulichang.test.collection.entity.*;
import com.github.yulichang.test.collection.mapper.TableAMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 连表测试类
 * <p>
 * 支持mybatis-plus 查询枚举字段
 * 支持mybatis-plus typeHandle功能
 * <p>
 * 移除了mybatis-plus 逻辑删除支持，逻辑删除需要在连表查询时自己添加对应的条件
 */
@SuppressWarnings("unused")
@SpringBootTest
class CollectionTest {
    @Resource
    private TableAMapper tableAMapper;

    @Test
    void testJoinCollection() {
        //4层嵌套  a对多b  b对多c  c对多d  d对多e
        MPJLambdaWrapper<TableA> wrapper = new MPJLambdaWrapper<TableA>()
                .selectAll(TableA.class)
                .selectCollection(TableB.class, TableADTO::getBList, b -> b
                        .collection(TableC.class, TableBDTO::getCcList, c -> c
                                .collection(TableD.class, TableCDTO::getDList, d -> d
                                        .collection(TableE.class, TableDDTO::getEList))))
                .leftJoin(TableB.class, TableB::getAid, TableA::getId)
                .leftJoin(TableC.class, TableC::getBid, TableB::getId)
                .leftJoin(TableD.class, TableD::getCid, TableC::getId)
                .leftJoin(TableE.class, TableE::getDid, TableD::getId);

        List<TableADTO> dtos = tableAMapper.selectJoinList(TableADTO.class, wrapper);
        System.out.println(dtos);
    }
}
