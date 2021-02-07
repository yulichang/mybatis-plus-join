# mybatis-plus-join

* 支持连表查询的[mybatis-plus](https://gitee.com/baomidou/mybatis-plus)  
  只做增强,不做修改,可以使用原生mybatis-plus全部的功能
* [演示示例](https://gitee.com/best_handsome/mybatis-plus-join-demo)

## 使用方法

[goto wiki](https://gitee.com/best_handsome/mybatis-plus-join/wikis)

### 安装

1. 在项目中添加依赖,依赖已经包含了mybatis-plus-boot-starter<3.4.2><br>
   依赖后无需再次引入mybatis-plus

   ```xml
   <dependency>
       <groupId>com.github.yulichang</groupId>
       <artifactId>mybatis-plus-join</artifactId>
       <version>1.0.7</version>
   </dependency>
   ```
   或者clone代码到本地,执行mvn install,再引入以上依赖
   <br><br>

2. 配置插件,添加MPJInterceptor

    ```java
    @Configuration
    public class MybatisPlusConfig {
        /**
         * 启用连表拦截器
         */
        @Bean
        public MybatisPlusInterceptor paginationInterceptor() {
            MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
            //分页插件
            interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
            //连表插件
            interceptor.addInnerInterceptor(new MPJInterceptor());
            //多租户,垃圾sql拦截插件......
            return interceptor;
        }
    }
    ```

### 使用

* mapper继承MPJBaseMapper (必选)
* service继承MPJBaseService (可选)
* serviceImpl继承MPJBaseServiceImpl (可选)

1. MPJBaseMapper继承BaseMapper,在原有的方法基础上又添加了以下方法:
    * selectJoinOne 连表查询一条记录对象
    * selectJoinList 连表查询返回命中记录对象集合
    * selectJoinPage 连表分页查询对象集合
    * selectJoinMap 连表查询一条记录返回Map
    * selectJoinMaps 连表查询返回命中记录Map集合
    * selectJoinMapsPage 连表分页查询返回Map集合

2. MPJBaseService 继承了IService,同样添加以上方法

3. MPJBaseServiceImpl 继承了ServiceImpl,同样添加了以上方法

## 核心类 MPJQueryWrapper,MPJLambdaQueryWrapper和MPJJoinLambdaQueryWrapper

|-|MPJQueryWrapper|MPJLambdaQueryWrapper|MPJJoinLambdaQueryWrapper|
|---|---|---|---|
|select(String)|支持|<font color=red>**支持**|不支持|
|select(lambda)|不支持|仅支持主表lambda|所有表lambda|
|join(String)|支持|支持|不支持|
|join(lambda)|不支持|不支持|支持|
|条件String|支持|不支持|不支持|
|条件lambda|不支持|仅支持主表lambda|所有表lambda|

MPJQueryWrapper相当于mp的QueryWrapper  
MPJLambdaQueryWrapper相当于mp的LambdaQueryWrapper

两者可以无缝切换  
MPJQueryWrapper.lambda() ===> MPJLambdaQueryWrapper  
MPJLambdaQueryWrapper.stringQuery() ===> MPJQueryWrapper

## MPJQueryWrapper和MPJLambdaQueryWrapper

### 简单的3表查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class,
                new MPJLambdaQueryWrapper<UserDO>()
                        .selectAll(UserDO.class)
                        .select("addr.tel", "addr.address", "a.province")
                        .leftJoin("user_address addr on t.id = addr.user_id")
                        .rightJoin("area a on addr.area_id = a.id")
                        .gt(true, UserDO::getId, 1)
                        .eq(true, UserDO::getSex, "男")
                        .stringQuery()
                        .like(true, "addr.tel", "1")
                        .le(true, "a.province", "1"));
    }
}
```

对应sql

```
SELECT 
    t.id,
    t.name,
    t.sex,
    t.head_img,
    addr.tel,
    addr.address,
    a.province
FROM 
    user t
    LEFT JOIN user_address addr on t.id = addr.user_id
    RIGHT JOIN area a on addr.area_id = a.id
WHERE (
    t.id > ?
    AND t.sex = ?
    AND addr.tel LIKE ?
    AND a.province <= ?)
```

说明:

* UserDTO.class 查询结果类(resultType)
* selectAll(UserDO.class) 查询主表全部字段(主表实体类)
* select() mp的select策略是覆盖,以最后一次为准,这里的策略是追加,可以一直select  
  主表字段可以用lambda,会自动添加表别名,主表别名默认是 t ,非主表字段必须带别名查询
* leftJoin() rightJoin() innerJoin() 传sql片段 格式 (表 + 别名 + 关联条件)
* stringQuery() lambda查询转string查询
* lambda() string查询转lambda查询
* 条件查询,可以查询主表以及参与连接的所有表的字段,全部调用mp原生的方法,正常使用没有sql注入风险

### 分页查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        IPage<UserDTO> page = userMapper.selectJoinPage(new Page<>(1, 10), UserDTO.class,
                new MPJLambdaQueryWrapper<UserDO>()
                        .selectAll(UserDO.class)
                        .select("addr.tel", "addr.address")
                        .select("a.province")
                        .leftJoin("user_address addr on t.id = addr.user_id")
                        .rightJoin("area a on addr.area_id = a.id"));
    }
}
```

对应sql

```
SELECT 
    t.id,
    t.name,
    t.sex,
    t.head_img,
    addr.tel,
    addr.address,
    a.province
FROM 
    user t
    LEFT JOIN user_address addr on t.id = addr.user_id
    RIGHT JOIN area a on addr.area_id = a.id 
LIMIT ?,?
```

### 还可以这么操作,但不建议

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class,
                new MPJLambdaQueryWrapper<UserDO>()
                        .selectAll(UserDO.class)
                        .select("addr.tel", "addr.address")
                        //行列转换
                        .select("CASE t.sex WHEN '男' THEN '1' ELSE '0' END AS sex")
                        //求和函数
                        .select("sum(a.province) AS province")
                        //自定义数据集
                        .leftJoin("(select * from user_address) addr on t.id = addr.user_id")
                        .rightJoin("area a on addr.area_id = a.id")
                        .eq(true, UserDO::getId, 1)
                        .stringQuery()
                        .like(true, "addr.tel", "1")
                        .le(true, "a.province", "1")
                        .orderByDesc("addr.id"));
    }
}
```

对应sql

```
SELECT 
    t.id,
    t.name,
    t.sex,
    t.head_img,
    addr.tel,
    addr.address,
    CASE t.sex WHEN '男' THEN '1' ELSE '0' END AS sex,
    sum(a.province) AS province
FROM 
    user t
    LEFT JOIN (select * from user_address) addr on t.id = addr.user_id
    RIGHT JOIN area a on addr.area_id = a.id
WHERE (
    t.id = ?
    AND addr.tel LIKE ?
    AND a.province <= ?)
ORDER BY
    addr.id DESC
```

## MPJJoinLambdaQueryWrapper用法

MPJJoinLambdaQueryWrapper与上面连个Wrapper不同,是一套新的支持多表的wrapper   
MPJQueryWrapper是基于QueryWrapper扩展的MPJLambdaQueryWrapper是基于LambdaQueryWrapper扩展的
而LambdaQueryWrapper由于泛型约束,不支持扩展成多表的lambdaWrapper

#### MPJJoinLambdaQueryWrapper示例

#### 简单的3表查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class,
                new MPJJoinLambdaQueryWrapper<UserDO>()
                        .selectAll(UserDO.class)
                        .select(UserAddressDO::getTel)
                        .selectAs(UserAddressDO::getAddress, UserDTO::getUserAddress)
                        .select(AreaDO::getProvince, AreaDO::getCity)
                        .leftJoin(UserAddressDO.class, UserAddressDO::getUserId, UserDO::getId)
                        .leftJoin(AreaDO.class, AreaDO::getId, UserAddressDO::getAreaId)
                        .eq(true, UserDO::getId, 1)
                        .like(true, UserAddressDO::getTel, "1")
                        .gt(true, UserDO::getId, 5));
    }
}
```

对应sql

```
SELECT 
    user.id,
    user.name,
    user.sex,
    user.head_img,
    user_address.tel,
    user_address.address AS userAddress,
    area.province,
    area.city
FROM 
    user
    LEFT JOIN user_address ON user_address.user_id = user.id
    LEFT JOIN area ON area.id = user_address.area_id
WHERE (
    user.id = ?
    AND user_address.tel LIKE ?
    AND user.id > ?)
```

说明:

* UserDTO.class 查询结果返回类(resultType)
* selectAll() 查询指定实体类的全部字段
* select() 查询指定的字段,支持可变参数,同一个select只能查询相同表的字段  
  故将UserAddressDO和AreaDO分开为两个select()
* selectAs() 字段别名查询,用于数据库字段与业务实体类属性名不一致时使用
* leftJoin() 参数说明  
  第一个参数:  参与连表的实体类class  
  第二个参数:  连表的ON字段,这个属性必须是第一个参数实体类的属性  
  第三个参数:  参与连表的ON的另一个实体类属性
* 条件查询,可以查询主表以及参与连接的所有表的字段,全部调用mp原生的方法,正常使用没有sql注入风险

#### 分页查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        IPage<UserDTO> iPage = userMapper.selectJoinPage(new Page<>(2, 10), UserDTO.class,
                new MPJJoinLambdaQueryWrapper<UserDO>()
                        .selectAll(UserDO.class)
                        .select(UserAddressDO::getTel)
                        .selectAs(UserAddressDO::getAddress, UserDTO::getUserAddress)
                        .select(AreaDO::getProvince, AreaDO::getCity)
                        .leftJoin(UserAddressDO.class, UserAddressDO::getUserId, UserDO::getId)
                        .leftJoin(AreaDO.class, AreaDO::getId, UserAddressDO::getAreaId));
    }
}
```

对应sql

```
SELECT 
    t.id,
    t.name,
    t.sex,
    t.head_img,
    t1.tel,
    t1.address AS userAddress,
    t2.province,
    t2.city
FROM 
    user t
    LEFT JOIN user_address t1 ON t1.user_id = t.id
    LEFT JOIN area t2 ON t2.id = t1.area_id
LIMIT ?,?
```

# [wiki](https://gitee.com/best_handsome/mybatis-plus-join/wikis)


