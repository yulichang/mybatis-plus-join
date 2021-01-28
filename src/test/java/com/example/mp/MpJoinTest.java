package com.example.mp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mp.business.dto.UserDTO;
import com.example.mp.business.entity.UserAddressEntity;
import com.example.mp.business.entity.UserEntity;
import com.example.mp.business.mapper.UserMapper;
import com.example.mp.business.service.UserService;
import com.example.mp.mybatis.plus.wrapper.MyLambdaQueryWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * 连表
 */
@SuppressWarnings("all")
@SpringBootTest
class MpJoinTest {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userMapper;

    /**
     * 快速开始
     * 简单的连表查询
     */
    @Test
    void quickStart() {
        //简单的连表查询 -->  user left join user_address
        //mapper
        List<UserDTO> dtoList1 = userMapper.selectJoinList(new MyLambdaQueryWrapper<UserEntity>()
                        .selectAll(UserEntity.class)//查询全部字段,mp中默认全部字段,但是这默认为空,class按道理可以不传,从泛型中获取,目前还没有想到解决方案
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId,// left join -> sql中join后面on的两个比骄傲字段
                                right -> right.selectAll(UserAddressEntity.class)),
                UserDTO.class);
        log(dtoList1.get(0).getTel());

        //service实现
        List<UserDTO> dtoList2 = userService.selectJoinList(new MyLambdaQueryWrapper<UserEntity>()
                        .selectAll(UserEntity.class)
                        //同样支持条件判断 false不会进行连表 right join(还有 inner join) -> sql中join后面on的两个比较字段
                        .rightJoin(true, UserEntity::getId, UserAddressEntity::getUserId,
                                right -> right.selectAll(UserAddressEntity.class)),
                UserDTO.class);
        log(dtoList2.get(0).getHeadImg());
    }

    /**
     * 别名用法(as)
     * 简单的连表查询
     */
    @Test
    void test1() {
        //连表查询 -->  user left join user_address
        //mapper
        List<UserDTO> dtoList1 = userMapper.selectJoinList(new MyLambdaQueryWrapper<UserEntity>()
                        .selectAll(UserEntity.class)
                        .as(UserEntity::getId, UserDTO::getId)//sql as 当数据库字段与属性不匹配是使用, user.id as UserDTO.id
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId, right -> right
                                .asSum(UserAddressEntity::getAddress, UserDTO::getAddress)//as 的拓展 sum() 还有 count avg max min 等...
                                .selectAll(UserAddressEntity.class)),
                UserDTO.class);
        log(dtoList1.get(0).getTel());
        //service 略...
    }

    /**
     * 待条件的连表查询
     */
    @Test
    void test2() {
        //连表查询 -->  user left join user_address
        //mapper
        List<UserDTO> dtoList1 = userMapper.selectJoinList(new MyLambdaQueryWrapper<UserEntity>()
                        .selectAll(UserEntity.class)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId, right -> right
                                .selectAll(UserAddressEntity.class))
                        .eq(true, UserEntity::getId, 1)//支持条件判断
                        .like(UserAddressEntity::getTel, "1")//支持所有join的表字段 UserEntity 或 UserAddressEntity都可以
                        .eq(UserEntity::getId, UserAddressEntity::getUserId)//甚至可以字段与字段比较 UserEntity.id = UserAddressEntity.id
                /* 其他 .ne() .gt() .ge() ....  请参考 mybatis-plus */
                , UserDTO.class);
        log(dtoList1.get(0).getTel());
        //service 略...
    }

    /**
     * 连表分页查询 (请配置mybatis-plus 分页插件)
     */
    @Test
    void test3() {
        //连表查询 -->  user left join user_address
        //mapper
        IPage<UserDTO> page = userMapper.selectJoinPage(new Page<>(1, 10), new MyLambdaQueryWrapper<UserEntity>()
                        .selectAll(UserEntity.class)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId, right -> right
                                .selectAll(UserAddressEntity.class))
                        .eq(true, UserEntity::getId, 1)//支持条件判断
                        .like(UserAddressEntity::getTel, "1")//支持所有join的表字段 UserEntity 或 UserAddressEntity都可以
                        .eq(UserEntity::getId, UserAddressEntity::getUserId)//甚至可以字段与字段比较 UserEntity.id = UserAddressEntity.id
                /* 其他 .ne() .gt() .ge() ....  请参考 mybatis-plus */
                , UserDTO.class);
        log(page.getRecords().get(0).getTel());
        //service 略...
    }


    @Test
    void test4() {
        userMapper.selectJoinList(new MyLambdaQueryWrapper<UserEntity>()
                        .as(UserEntity::getHeadImg, UserDTO::getUserHeadImg)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId,
                                right -> right.select(UserAddressEntity::getAddress, UserAddressEntity::getTel))
                , UserDTO.class);
    }


    private void log(String format, String... args) {
        System.err.println(String.format(format, args));
    }

    private void log(String msg) {
        System.err.println(msg);
    }
}
