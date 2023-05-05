package com.yulichang.test.springboot3jdk17;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.test.util.ThreadLocalUtils;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yulichang.test.springboot3jdk17.dto.AddressDTO;
import com.yulichang.test.springboot3jdk17.dto.UserDTO;
import com.yulichang.test.springboot3jdk17.entity.AddressDO;
import com.yulichang.test.springboot3jdk17.entity.AreaDO;
import com.yulichang.test.springboot3jdk17.entity.UserDO;
import com.yulichang.test.springboot3jdk17.entity.UserDto;
import com.yulichang.test.springboot3jdk17.mapper.UserDTOMapper;
import com.yulichang.test.springboot3jdk17.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserDTOMapper userDTOMapper;

    @Test
    void testJoin() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.id  AS joina_id,
                       t1.user_id,
                       t1.area_id,
                       t1.tel,
                       t1.address,
                       t1.del AS joina_del,
                       t2.id  AS joinb_id,
                       t2.province,
                       t2.city,
                       t2.area,
                       t2.postcode,
                       t2.del AS joinb_del
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                         LEFT JOIN area t2 ON (t2.id = t1.area_id)
                WHERE t.del = false
                  AND t1.del = false
                  AND t2.del = false
                  AND (t.id <= ?)
                ORDER BY t.id DESC""");
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .selectCollection(AddressDO.class, UserDTO::getAddressList, addr -> addr
                        .association(AreaDO.class, AddressDTO::getArea))
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .leftJoin(AreaDO.class, AreaDO::getId, AddressDO::getAreaId)
                .le(UserDO::getId, 10000)
                .orderByDesc(UserDO::getId);
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        assert list.get(0).getAddressList() != null && list.get(0).getAddressList().get(0).getId() != null;
        list.forEach(System.out::println);
    }

    @Test
    void testJoinField() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.id AS joina_id
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                WHERE t.del = false
                  AND t1.del = false
                  AND (t.id <= ?)
                ORDER BY t.id DESC""");
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)

                .selectCollection(AddressDO.class, UserDTO::getAddressIds, e -> e
                        .id(AddressDO::getId))

                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .le(UserDO::getId, 10000)
                .orderByDesc(UserDO::getId);
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        assert list.get(0).getAddressIds() != null;
        list.forEach(System.out::println);
    }


    @Test
    void testJoin1() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.id  AS joina_id,
                       t1.user_id,
                       t1.area_id,
                       t1.tel,
                       t1.address,
                       t1.del AS joina_del,
                       t2.id  AS joinb_id,
                       t2.province,
                       t2.city,
                       t2.area,
                       t2.postcode,
                       t2.del AS joinb_del
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                         LEFT JOIN area t2 ON (t2.id = t1.area_id)
                WHERE t.del = false
                  AND t1.del = false
                  AND t2.del = false
                ORDER BY t.id DESC""");
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .selectCollection(AddressDO.class, UserDTO::getAddressList, addr -> addr
                        .association(AreaDO.class, AddressDTO::getArea))
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .leftJoin(AreaDO.class, AreaDO::getId, AddressDO::getAreaId)
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
        ThreadLocalUtils.set("""
                SELECT t.id
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                         LEFT JOIN area t2 ON (t2.id = t1.area_id)
                WHERE t.del = false
                  AND t1.del = false
                  AND t2.del = false""");
        //基本数据类型 和 String
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .select(UserDO::getId)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .leftJoin(AreaDO.class, AreaDO::getId, AddressDO::getAreaId);
        List<Integer> list = userMapper.selectJoinList(Integer.class, wrapper);

        assert list.get(0) != null;
        System.out.println(list);


        ThreadLocalUtils.set("""
                SELECT t.create_time
                FROM `user` t
                         LEFT JOIN address t1 ON (t1.user_id = t.id)
                         LEFT JOIN area t2 ON (t2.id = t1.area_id)
                WHERE t.del = false
                  AND t1.del = false
                  AND t2.del = false""");
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
    @SuppressWarnings("unchecked")
    void testMSCache() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by
                FROM `user` t
                WHERE t.id = ?
                  AND t.del = false
                  AND (t.id <= ?)
                ORDER BY t.id ASC, t.`name` ASC""");
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>(new UserDO() {{
            setId(1);
        }})
                .selectAll(UserDO.class)
//                .setEntity(new UserDO() {{
//                    setId(1);
//                }})
                .le(UserDO::getId, 100)
                .orderByAsc(UserDO::getId, UserDO::getName);

        List<UserDO> list = userMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    @Test
    void testTableAliasR() {
        ThreadLocalUtils.set("""
                SELECT tt.id,
                       tt.user_id,
                       tt.create_by,
                       tt.update_by,
                       ua.`name` AS userName,
                       ub.`name` AS createName,
                       uc.`name` AS updateName
                FROM user_dto tt
                         LEFT JOIN `user` ua ON (ua.id = tt.user_id)
                         LEFT JOIN `user` ub ON (ub.id = tt.create_by)
                         LEFT JOIN `user` uc ON (uc.id = tt.update_by)
                WHERE ua.del = false
                  AND ub.del = false
                  AND uc.del = false
                  AND (ua.id <= ? AND ub.id >= ?)""");
        MPJLambdaWrapper<UserDto> wrapper = new MPJLambdaWrapper<UserDto>("tt")
                .selectAll(UserDto.class)
                .leftJoin(UserDO.class, "ua", UserDO::getId, UserDto::getUserId, ext -> ext
                        .selectAs(UserDO::getName, UserDto::getUserName)
                        .le(UserDO::getId, 100))
                .leftJoin(UserDO.class, "ub", UserDO::getId, UserDto::getCreateBy, ext -> ext
                        .selectAs(UserDO::getName, UserDto::getCreateName)
                        .ge(UserDO::getId, 0))
                .leftJoin(UserDO.class, "uc", UserDO::getId, UserDto::getUpdateBy, ext -> ext
                        .selectAs(UserDO::getName, UserDto::getUpdateName));
        List<UserDto> userDtos = userDTOMapper.selectJoinList(UserDto.class, wrapper);
        assert StringUtils.isNotBlank(userDtos.get(0).getUserName());
        assert StringUtils.isNotBlank(userDtos.get(0).getCreateName());
        assert StringUtils.isNotBlank(userDtos.get(0).getUpdateName());


        ThreadLocalUtils.set("""
                SELECT tt.id,
                       tt.pid,
                       tt.`name`,
                       tt.`json`,
                       tt.sex,
                       tt.head_img,
                       tt.create_time,
                       tt.address_id,
                       tt.address_id2,
                       tt.del,
                       tt.create_by,
                       tt.update_by,
                       ua.id,
                       ub.head_img
                FROM `user` tt
                         LEFT JOIN `user` ua ON (ua.id = tt.pid)
                         LEFT JOIN `user` ub ON (ub.id = tt.create_by)
                         LEFT JOIN `user` uc ON (uc.id = tt.update_by)
                WHERE tt.del = false
                  AND ua.del = false
                  AND ub.del = false
                  AND uc.del = false
                  AND (ua.head_img = tt.`name` AND tt.id = ua.id)""");
        MPJLambdaWrapper<UserDO> w = new MPJLambdaWrapper<UserDO>("tt")
                .selectAll(UserDO.class)
                .leftJoin(UserDO.class, "ua", UserDO::getId, UserDO::getPid, ext -> ext
                        .select(UserDO::getId)
                        .eq(UserDO::getImg, UserDO::getName))
                .leftJoin(UserDO.class, "ub", UserDO::getId, UserDO::getCreateBy, ext -> ext
                        .select(UserDO::getImg))
                .leftJoin(UserDO.class, "uc", UserDO::getId, UserDO::getUpdateBy)
                .eq(UserDO::getId, UserDO::getId);
        userMapper.selectJoinList(UserDO.class, w);
        System.out.println(1);
    }

    /**
     * 自连接测试
     */
    @Test
    void testInner() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.id          AS joina_id,
                       t1.pid         AS joina_pid,
                       t1.`name`      AS joina_name,
                       t1.`json`      AS joina_json,
                       t1.sex         AS joina_sex,
                       t1.head_img    AS joina_head_img,
                       t1.create_time AS joina_create_time,
                       t1.address_id  AS joina_address_id,
                       t1.address_id2 AS joina_address_id2,
                       t1.del         AS joina_del,
                       t1.create_by   AS joina_create_by,
                       t1.update_by   AS joina_update_by
                FROM `user` t
                         LEFT JOIN `user` t1 ON (t1.pid = t.id)
                WHERE t.del = false
                  AND (t.id > ?)""");
        //自连接
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .disableSubLogicDel()//关闭副表逻辑删除
                .selectAll(UserDO.class)
                .selectCollection(UserDO.class, UserDO::getChildren)
                .leftJoin(UserDO.class, UserDO::getPid, UserDO::getId)
                .gt(UserDO::getId, 0);
        List<UserDO> list = userMapper.selectJoinList(UserDO.class, wrapper);
        System.out.println(list);

        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.`name` AS createName,
                       t2.`name` AS updateName
                FROM `user` t
                         LEFT JOIN `user` t1 ON (t1.id = t.create_by)
                         LEFT JOIN `user` t2 ON (t2.id = t.update_by)
                WHERE (t2.id = t.update_by AND t.id = t1.id)""");
        //关联一张表多次
        MPJLambdaWrapper<UserDO> w = new MPJLambdaWrapper<UserDO>()
                .disableLogicDel()
                .disableSubLogicDel()
                .selectAll(UserDO.class)
                .leftJoin(UserDO.class, UserDO::getId, UserDO::getCreateBy, ext -> ext
                        .selectAs(UserDO::getName, UserDO::getCreateName))
                .leftJoin(UserDO.class, (on, ext) -> {
                    on.eq(UserDO::getId, UserDO::getUpdateBy);
                    ext.selectAs(UserDO::getName, UserDO::getUpdateName)
                            .eq(UserDO::getId, UserDO::getUpdateBy);
                })
                .eq(UserDO::getId, UserDO::getId);
        List<UserDO> dos = userMapper.selectJoinList(UserDO.class, w);
        assert dos.get(0).getCreateName() != null && dos.get(0).getUpdateName() != null;


        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.`name`      AS alias,
                       t1.id          AS joina_id,
                       t1.pid         AS joina_pid,
                       t1.`name`      AS joina_name,
                       t1.`json`      AS joina_json,
                       t1.sex         AS joina_sex,
                       t1.head_img    AS joina_head_img,
                       t1.create_time AS joina_create_time,
                       t1.address_id  AS joina_address_id,
                       t1.address_id2 AS joina_address_id2,
                       t1.del         AS joina_del,
                       t1.create_by   AS joina_create_by,
                       t1.update_by   AS joina_update_by,
                       t2.id          AS joinb_id,
                       t2.pid         AS joinb_pid,
                       t2.`name`      AS joinb_name,
                       t2.`json`      AS joinb_json,
                       t2.sex         AS joinb_sex,
                       t2.head_img    AS joinb_head_img,
                       t2.create_time AS joinb_create_time,
                       t2.address_id  AS joinb_address_id,
                       t2.address_id2 AS joinb_address_id2,
                       t2.del         AS joinb_del,
                       t2.create_by   AS joinb_create_by,
                       t2.update_by   AS joinb_update_by
                FROM `user` t
                         LEFT JOIN `user` t1 ON (t1.pid = t.id)
                         LEFT JOIN `user` t2 ON (t2.pid = t1.id)
                WHERE t.del = false
                  AND (t1.id <= ? AND t.id <= ?)""");
        MPJLambdaWrapper<UserDO> wrapper1 = new MPJLambdaWrapper<UserDO>()
                .disableSubLogicDel()
                .selectAll(UserDO.class)
                .selectCollection("t1", UserDO.class, UserDO::getChildren, c -> c
                        .collection("t2", UserDO.class, UserDO::getChildren))
                .leftJoin(UserDO.class, UserDO::getPid, UserDO::getId, ext -> ext
                        .selectAs(UserDO::getName, UserDO::getAlias)
                        .leftJoin(UserDO.class, UserDO::getPid, UserDO::getId)
                        .le(UserDO::getId, 5))
                .le(UserDO::getId, 4);
        List<UserDO> list1 = userMapper.selectJoinList(UserDO.class, wrapper1);
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

    @Test
    void testTableAlias() {
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       aa.id,
                       aa.user_id,
                       aa.area_id,
                       aa.tel,
                       aa.address,
                       aa.del
                FROM `user` t
                         LEFT JOIN address aa ON (aa.user_id = t.id)
                WHERE t.del = false
                  AND aa.del = false""");
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
//                .disableLogicDel()//关闭主表逻辑删除
                .selectAll(UserDO.class)
                .selectAll(AddressDO.class, "aa")
//                .selectCollection(UserDO.class, UserDO::getChildren)
                .leftJoin(AddressDO.class, "aa", AddressDO::getUserId, UserDO::getId);
        List<UserDO> list = userMapper.selectJoinList(UserDO.class, wrapper);

        System.out.println(list);
    }

    @Test
    void testLabel() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .disableSubLogicDel()
                .selectAll(UserDO.class)
                .selectCollection("t1", AddressDO.class, UserDO::getAddressList)
                .selectCollection("t2", AddressDO.class, UserDO::getAddressList2)
                .leftJoin(AddressDO.class, AddressDO::getId, UserDO::getAddressId)
                .leftJoin(AddressDO.class, AddressDO::getId, UserDO::getAddressId2);
        List<UserDO> list = userMapper.selectJoinList(UserDO.class, wrapper);

        assert list.get(0).getAddressList().get(0).getAddress() != null;
        assert list.get(0).getAddressList2().get(0).getAddress() != null;
        System.out.println(list);
    }


    /**
     * 简单的分页关联查询 lambda
     */
    @Test
    void test1() {
        Page<UserDTO> page = new Page<>(1, 10);
        page.setSearchCount(false);
        IPage<UserDTO> iPage = userMapper.selectJoinPage(page, UserDTO.class,
                JoinWrappers.lambda(UserDO.class)
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
        ThreadLocalUtils.set("""
                SELECT t.id,
                       t.pid,
                       t.`name`,
                       t.`json`,
                       t.sex,
                       t.head_img,
                       t.create_time,
                       t.address_id,
                       t.address_id2,
                       t.del,
                       t.create_by,
                       t.update_by,
                       t1.address
                FROM `user` t
                         LEFT JOIN address t1 ON (t.id = t1.user_id AND t.id = t1.user_id)
                WHERE t.del = false
                  AND t1.del = false
                  AND (t.id = ? AND (t.head_img = ? OR t1.user_id = ?) AND t.id = ?)
                LIMIT ?""");
        IPage<UserDTO> page = userMapper.selectJoinPage(new Page<>(1, 10), UserDTO.class,
                JoinWrappers.lambda(UserDO.class)
                        .selectAll(UserDO.class)
                        .select(AddressDO::getAddress)
                        .leftJoin(AddressDO.class, on -> on
                                .eq(UserDO::getId, AddressDO::getUserId)
                                .eq(UserDO::getId, AddressDO::getUserId))
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
        UserDTO one = userMapper.selectJoinOne(UserDTO.class, JoinWrappers.lambda(UserDO.class)
                .selectSum(UserDO::getId)
                .selectMax(UserDO::getId, UserDTO::getHeadImg)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));
        System.out.println(one);
    }


    /**
     * 忽略个别查询字段
     */
    @Test
    @SuppressWarnings("deprecation")
    void test6() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                .select(AddressDO.class, p -> true)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId)
                .eq(UserDO::getId, 1);
        Page<UserDTO> page = userMapper.selectJoinPage(new Page<>(1, 10), UserDTO.class, wrapper);
        assert page.getRecords().get(0).getAddress() != null;
        page.getRecords().forEach(System.out::println);
    }

    /**
     * 忽略个别查询字段
     */
    @Test
    void test8() {
        ThreadLocalUtils.set("SELECT t.`name` FROM `user` t WHERE t.del=false AND (t.`name` = ?)");
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .select(UserDO::getName)
                .eq(UserDO::getName, "ref");
        userMapper.selectList(wrapper);
    }


    /**
     * 关联查询返回map
     */
    @Test
    void test7() {
        List<Map<String, Object>> list = userMapper.selectJoinMaps(JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)
                .select(AddressDO::getAddress)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId));
        assert list.get(0).get("ADDRESS") != null;
        list.forEach(System.out::println);
    }

    /**
     * 原生查询
     */
    @Test
    void testMP() {
        List<UserDO> dos = userMapper.selectList(new LambdaQueryWrapper<UserDO>()
                .gt(UserDO::getId, 3)
                .lt(UserDO::getId, 8));
        assert dos.size() == 4;
    }

    /**
     * 函数测试
     */
    @Test
    void testFunc() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class);

        List<UserDO> dos = userMapper.selectJoinList(UserDO.class, wrapper);
        System.out.println(1);
    }
}
