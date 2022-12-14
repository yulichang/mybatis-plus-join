package com.github.yulichang.test.join;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.test.join.dto.AddressDTO;
import com.github.yulichang.test.join.dto.UserDTO;
import com.github.yulichang.test.join.entity.AddressDO;
import com.github.yulichang.test.join.entity.AreaDO;
import com.github.yulichang.test.join.entity.UserDO;
import com.github.yulichang.test.join.mapper.UserMapper;
import com.github.yulichang.toolkit.MPJWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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
class LambdaWrapperTest {
    @Resource
    private UserMapper userMapper;

    @Test
    void testJoin() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .selectCollection(AddressDO.class, UserDTO::getAddressList, addr -> addr
                        .association(AreaDO.class, AddressDTO::getArea))
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .leftJoin(AreaDO.class, AreaDO::getId, AddressDO::getAreaId)
                .orderByDesc(UserDO::getId);
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        assert list.get(0).getAddressList() != null && list.get(0).getAddressList().get(0).getId() != null;
        list.forEach(System.out::println);
    }

    @Test
    void testJoin1() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .selectCollection(AddressDO.class, UserDTO::getAddressList, addr -> addr
                        .association(AreaDO.class, AddressDTO::getArea))
                .leftJoin(AddressDO::getUserId, UserDO::getId)
                .leftJoin(AreaDO::getId, AddressDO::getAreaId)
                .orderByDesc(UserDO::getId);
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        assert list.get(0).getAddressList().get(0).getId() != null;
        list.forEach(System.out::println);
    }

    /**
     * 基本数据类型测试
     */
    @Test
    void testWrapper() {
        //基本数据类型 和 String
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .select(UserDO::getId)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .leftJoin(AreaDO.class, AreaDO::getId, AddressDO::getAreaId);
        List<Integer> list = userMapper.selectJoinList(Integer.class, wrapper);

        assert list.get(0) != null;
        System.out.println(list);

        //java.sql包下的类
        MPJLambdaWrapper<UserDO> wrapper1 = new MPJLambdaWrapper<UserDO>()
                .select(UserDO::getCreateTime)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .leftJoin(AreaDO.class, AreaDO::getId, AddressDO::getAreaId);
        List<Timestamp> list1 = userMapper.selectJoinList(Timestamp.class, wrapper1);

        assert list1.get(0) != null;
        System.out.println(list);
    }


    @Test
    void testMSCache() {
//        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
//                .selectAll(UserDO.class)
//                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
//                .leftJoin(AreaDO.class, AreaDO::getId, AddressDO::getAreaId);
//        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        MPJLambdaWrapper<UserDO> wrapper1 = new MPJLambdaWrapper<UserDO>()
                .select(UserDO::getId)
                .selectAs(UserDO::getJson, UserDTO::getArea)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .leftJoin(AreaDO.class, AreaDO::getId, AddressDO::getAreaId);
        List<UserDTO> list1 = userMapper.selectJoinList(UserDTO.class, wrapper1);

        assert list1.get(0).getArea() != null;
    }

    /**
     * 自连接测试
     */
    @Test
    void testInner() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
//                .disableSubLogicDel()//关闭副表逻辑删除
                .disableLogicDel()//关闭主表逻辑删除
                .selectAll(UserDO.class)
                .selectCollection(UserDO.class, UserDO::getChildren)
                .leftJoin(UserDO.class, UserDO::getPid, UserDO::getId);
        List<UserDO> list = userMapper.selectJoinList(UserDO.class, wrapper);
        System.out.println(list);

        MPJLambdaWrapper<UserDO> wrapper1 = new MPJLambdaWrapper<UserDO>()
                .disableSubLogicDel()
                .selectAll(UserDO.class)
                .selectCollection(1, UserDO.class, UserDO::getChildren, c -> c
                        .collection(2, UserDO.class, UserDO::getChildren))
                .leftJoin(UserDO.class, UserDO::getPid, UserDO::getId, ext -> ext
                        .selectAs(UserDO::getName, UserDO::getAlias)
                        .leftJoin(UserDO.class, UserDO::getPid, UserDO::getId)
                        .le(UserDO::getId, 5))
                .le(UserDO::getId, 4);
        List<UserDO> list1 = userMapper.selectJoinList(UserDO.class, wrapper1);
        System.out.println(wrapper1.getSqlSegment());
        assert "(t1.id <= #{ew.paramNameValuePairs.MPGENVAL1} AND t.id <= #{ew.paramNameValuePairs.MPGENVAL2})".equals(wrapper1.getSqlSegment());
        System.out.println(list1);
    }

    /**
     * 逻辑删除测试
     */
    @Test
    void testLogicDel() {
        List<UserDTO> l1 = userMapper.selectJoinList(UserDTO.class, new MPJLambdaWrapper<>());
        assert l1.size() == 14;

        List<UserDTO> l2 = userMapper.selectJoinList(UserDTO.class, new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .select(AddressDO::getAddress)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));
        assert l2.size() == 10;

        List<UserDTO> l3 = userMapper.selectJoinList(UserDTO.class, new MPJLambdaWrapper<UserDO>()
                .disableSubLogicDel()
                .selectAll(UserDO.class)
                .selectCollection(AddressDO.class, UserDTO::getAddressList)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));
        assert l3.size() == 14 && l3.get(0).getAddressList().size() == 9;

        List<UserDTO> l4 = userMapper.selectJoinList(UserDTO.class, new MPJLambdaWrapper<UserDO>()
                .disableSubLogicDel()
                .selectAll(UserDO.class)
                .selectCollection(AddressDO.class, UserDTO::getAddressList)
                .leftJoin(AddressDO.class, on -> on
                        .eq(AddressDO::getUserId, UserDO::getId)
                        .eq(AddressDO::getDel, false)));
        assert l4.size() == 14 && l4.get(0).getAddressList().size() == 5;
    }


    /**
     * 别名测试
     */
    @Test
    void testAlias() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
//                .disableSubLogicDel()//关闭副表逻辑删除
//                .disableLogicDel()//关闭主表逻辑删除
                .selectAll(UserDO.class)
                .selectCollection(UserDO.class, UserDO::getChildren)
                .leftJoin(UserDO.class, UserDO::getPid, UserDO::getId);
        List<UserDO> list = userMapper.selectJoinList(UserDO.class, wrapper);
        assert list.get(0).getName() != null && list.get(0).getChildren().get(0).getName() != null;
        assert list.get(0).getImg() != null && list.get(0).getChildren().get(0).getImg() != null;
        System.out.println(list);
    }


    /**
     * 简单的分页关联查询 lambda
     */
    @Test
    void test1() {
        IPage<UserDTO> iPage = userMapper.selectJoinPage(new Page<>(1, 10), UserDTO.class,
                MPJWrappers.<UserDO>lambdaJoin()
                        .selectAll(UserDO.class)
                        .select(AddressDO::getAddress)
                        .select(AreaDO::getProvince)
                        .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                        .leftJoin(AreaDO.class, AreaDO::getId, AddressDO::getAreaId));
        iPage.getRecords().forEach(System.out::println);
    }

    /**
     * 简单的分页关联查询 lambda
     * ON语句多条件
     */
    @Test
    void test3() {
        IPage<UserDTO> page = userMapper.selectJoinPage(new Page<>(1, 10), UserDTO.class,
                MPJWrappers.<UserDO>lambdaJoin()
                        .selectAll(UserDO.class)
                        .select(AddressDO::getAddress)
                        .leftJoin(AddressDO.class, on -> on
                                .eq(AddressDO::getUserId, UserDO::getId)
                                .eq(AddressDO::getUserId, UserDO::getId))
                        .eq(UserDO::getId, 1)
                        .and(i -> i.eq(UserDO::getImg, "er")
                                .or()
                                .eq(AddressDO::getUserId, 1))
                        .eq(UserDO::getId, 1));
        page.getRecords().forEach(System.out::println);
    }

    /**
     * 简单的函数使用
     */
    @Test
    void test4() {
        UserDTO one = userMapper.selectJoinOne(UserDTO.class, MPJWrappers.<UserDO>lambdaJoin()
                .selectSum(UserDO::getId)
                .selectMax(UserDO::getId, UserDTO::getHeadImg)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));
        System.out.println(one);
    }


    /**
     * 忽略个别查询字段
     */
    @Test
    void test6() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .select(AddressDO.class, p -> true)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .eq(UserDO::getId, 1);
        IPage<UserDTO> page = userMapper.selectJoinPage(new Page<>(1, 10), UserDTO.class, wrapper);
        assert page.getRecords().get(0).getAddress() != null;
        page.getRecords().forEach(System.out::println);
    }


    /**
     * 关联查询返回map
     */
    @Test
    void test7() {
        List<Map<String, Object>> list = userMapper.selectJoinMaps(MPJWrappers.<UserDO>lambdaJoin()
                .selectAll(UserDO.class)
                .select(AddressDO::getAddress)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));
        assert list.get(0).get("ADDRESS") != null;
        list.forEach(System.out::println);
    }

    /**
     * 关联查询返回map
     */
    @Test
    void testMP() {
        List<UserDO> dos = userMapper.selectList(new LambdaQueryWrapper<UserDO>()
                .gt(UserDO::getId, 3)
                .lt(UserDO::getId, 8));
        assert dos.size() == 4;
    }
}
