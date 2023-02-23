package com.github.yulichang.test.collection;

import com.github.yulichang.test.collection.dto.*;
import com.github.yulichang.test.collection.entity.*;
import com.github.yulichang.test.collection.mapper.TableAMapper;
import com.github.yulichang.test.collection.mapper.TableTMapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.session.SqlSessionFactory;
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
    @Resource
    private TableTMapper tableMapper;
    @Resource
    private SqlSessionFactory sqlSessionFactory;

    /**
     * 覆盖测试
     */
    @Test
    void testJoinCollection() {
        testAA();
        //4层嵌套  a对多b  b对多c  c对多d  d对多e
        MPJLambdaWrapper<TableA> wrapper1 = new MPJLambdaWrapper<TableA>()
                .selectAll(TableA.class)
                .selectCollection(TableB.class, TableADTO::getBList, b -> b
                        .collection(TableC.class, TableBDTO::getCList, c -> c
                                .collection(TableD.class, TableCDTO::getDList, d -> d
                                        .collection(TableE.class, TableDDTO::getEList, e -> e
                                                .id(TableE::getId)))))
                .leftJoin(TableB.class, TableB::getAid, TableA::getId)
                .leftJoin(TableC.class, TableC::getBid, TableB::getId)
                .leftJoin(TableD.class, TableD::getCid, TableC::getId)
                .leftJoin(TableE.class, TableE::getDid, TableD::getId);
        List<TableADTO> dtos1 = tableAMapper.selectJoinList(TableADTO.class, wrapper1);

        MPJLambdaWrapper<TableA> wrapper = new MPJLambdaWrapper<TableA>()
                .selectAll(TableA.class)
                .selectCollection(TableB.class, TableADTO::getBList, b -> b
                        .collection(TableC.class, TableBDTO::getCList, c -> c
                                .collection(TableD.class, TableCDTO::getDList, d -> d
                                        .collection(TableE.class, TableDDTO::getEList))))
                .leftJoin(TableB.class, TableB::getAid, TableA::getId)
                .leftJoin(TableC.class, TableC::getBid, TableB::getId)
                .leftJoin(TableD.class, TableD::getCid, TableC::getId)
                .leftJoin(TableE.class, TableE::getDid, TableD::getId);
        List<TableADTO> dtos = tableAMapper.selectJoinList(TableADTO.class, wrapper);

        assert dtos.get(0).getBList().get(0).getCList().get(0).getDList().get(0).getEList().get(0).getName() != null;
    }

    @Test
    void testAA() {
        MPJLambdaWrapper<TableA> wrapper1 = new MPJLambdaWrapper<TableA>()
                .selectAll(TableA.class)
                .selectAssociation(TableB.class, TableADTO::getB, b -> b
                        .association(TableC.class, TableBDTO::getC, c -> c
                                .association(TableD.class, TableCDTO::getD, d -> d
                                        .association(TableE.class, TableDDTO::getE, e -> e
                                                .id(TableE::getId)))))
                .leftJoin(TableB.class, TableB::getAid, TableA::getId)
                .leftJoin(TableC.class, TableC::getBid, TableB::getId)
                .leftJoin(TableD.class, TableD::getCid, TableC::getId)
                .leftJoin(TableE.class, TableE::getDid, TableD::getId)
                .last("LIMIT 1");
        List<TableADTO> dtos1 = tableAMapper.selectJoinList(TableADTO.class, wrapper1);
        System.out.println(1);
    }

    /**
     * 映射同一张表多次
     */
    @Test
    void testRepeat() {
        MPJLambdaWrapper<TableT> wrapper = new MPJLambdaWrapper<TableT>()
                .selectAll(TableT.class)
                .selectAssociation("t1", TableA.class, TableDTO::getTable1)
                .selectAssociation("t2", TableA.class, TableDTO::getTable2)
                .leftJoin(TableA.class, TableA::getId, TableT::getAid1)
                .leftJoin(TableA.class, TableA::getId, TableT::getAid2);
        List<TableDTO> dtos = tableMapper.selectJoinList(TableDTO.class, wrapper);
        System.out.println(1);
    }
}
