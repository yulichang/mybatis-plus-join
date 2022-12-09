# mybatis-plus-join

* 对 [mybatis-plus](https://gitee.com/baomidou/mybatis-plus) 多表查询的扩展
* [演示工程](https://gitee.com/best_handsome/mybatis-plus-join-demo)
* 点个Star支持一下吧 :)

QQ群:1022221898

## 使用方法

### 安装

- Maven
  ```xml
  <dependency>
      <groupId>com.github.yulichang</groupId>
      <artifactId>mybatis-plus-join-boot-starter</artifactId>
      <version>1.3.8</version>
  </dependency>
  ```
- Gradle
  ```
   implementation 'com.github.yulichang:mybatis-plus-join-boot-starter:1.3.8'
  ```
  或者clone代码到本地执行 mvn install, 再引入以上依赖  
  <br>
  注意: mybatis plus version >= 3.4.0
  <br>

### 使用

* mapper继承MPJBaseMapper (必选)
* service继承MPJBaseService (可选)
* serviceImpl继承MPJBaseServiceImpl (可选)

### Lambda形式用法（MPJLambdaWrapper）

#### 简单的连表查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        //和Mybatis plus一致，MPJLambdaWrapper的泛型必须是主表的泛型，并且要用主表的Mapper来调用
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)//查询user表全部字段
                .select(UserAddressDO::getTel)//查询user_address tel 字段
                .selectAs(UserAddressDO::getAddress, UserDTO::getUserAddress)//别名 t.address AS userAddress
                .select(AreaDO::getProvince, AreaDO::getCity)
                .leftJoin(UserAddressDO.class, UserAddressDO::getUserId, UserDO::getId)
                .leftJoin(AreaDO.class, AreaDO::getId, UserAddressDO::getAreaId)
                .eq(UserDO::getId, 1)
                .like(UserAddressDO::getTel, "1")
                .gt(UserDO::getId, 5);

        //连表查询 返回自定义ResultType
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        //分页查询 （需要启用 mybatis plus 分页插件）
        Page<UserDTO> listPage = userMapper.selectJoinPage(new Page<>(2, 10), UserDTO.class, wrapper);
    }
}
```

对应sql

```
SELECT  
    t.id, t.name, t.sex, t.head_img, 
    t1.tel, t1.address AS userAddress,
    t2.province, t2.city 
FROM 
    user t 
    LEFT JOIN user_address t1 ON t1.user_id = t.id 
    LEFT JOIN area t2 ON t2.id = t1.area_id 
WHERE (
    t.id = ? 
    AND t1.tel LIKE ? 
    AND t.id > ?)
```

说明:

* UserDTO.class 查询结果返回类(resultType)
* selectAll() 查询指定实体类的全部字段
* select() 查询指定的字段,支持可变参数,同一个select只能查询相同表的字段
* selectAs() 字段别名查询,用于数据库字段与业务实体类属性名不一致时使用
* leftJoin() 参数说明  
  第一个参数:  参与连表的实体类class  
  第二个参数:  连表的ON字段,这个属性必须是第一个参数实体类的属性  
  第三个参数:  参与连表的ON的另一个实体类属性
* 默认主表别名是t,其他的表别名以先后调用的顺序使用t1,t2,t3....
* 条件查询,可以查询主表以及参与连接的所有表的字段,全部调用mp原生的方法,正常使用没有sql注入风险

#### 一对多查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    @Test
    void testResultMap() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<UserDO>()
                .selectAll(UserDO.class)
                //对多查询
                .selectCollection(AddressDO.class, UesrDTO::getAddressList)
                //对一查询
                .selectAssociation(AddressDO.class, UesrDTO::getAddress)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId);

        List<UserDTO> dtoList = userMapper.selectJoinList(UserDTO.class, wrapper);

        //关于对多分页查询
        //由于嵌套结果方式会导致结果集被折叠，因此分页查询的结果在折叠后总数会减少，所以无法保证分页结果数量正确。
    }
}
```

等效于ResultMap

```xml

<resultMap id="xxxxxxxx" type="com.github.yulichang.join.dto.UserDTO">
    <result property="id" column="id"/>
    <result property="name" column="name"/>
    <!--其他属性省略-->
    <collection property="addressList" javaType="java.util.List"
                ofType="com.github.yulichang.join.entity.UserAddressDO">
        <id property="id" column="mpj_id"/>
        <result property="address" column="address"/>
        <result property="userId" column="user_id"/>
        <!--其他属性省略-->
    </collection>
</resultMap>
```

MPJLambdaWrapper其他功能

* [一对一，一对多使用](https://gitee.com/best_handsome/mybatis-plus-join/wikis/MyLambdaWrapper/selectCollection()?sort_id=6509590)
* [简单的SQL函数使用](https://gitee.com/best_handsome/mybatis-plus-join/wikis/selectFunc()?sort_id=4082479)
* [ON语句多条件支持](https://gitee.com/best_handsome/mybatis-plus-join/wikis/leftJoin?sort_id=3496671)

### String形式用法（MPJQueryWrapper）

#### 简单的连表查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        MPJQueryWrapper wrapper = new MPJQueryWrapper<UserDO>()
                .selectAll(UserDO.class)
                .select("addr.tel", "addr.address", "a.province")
                .leftJoin("user_address addr on t.id = addr.user_id")
                .rightJoin("area a on addr.area_id = a.id")
                .like("addr.tel", "1")
                .le("a.province", "1");

        //列表查询
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        //分页查询 （需要启用 mybatis plus 分页插件）
        Page<UserDTO> listPage = userMapper.selectJoinPage(new Page<>(1, 10), UserDTO.class, wrapper);
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
    addr.tel LIKE ?
    AND a.province <= ?)
```

说明:

* UserDTO.class 查询结果类(resultType)
* selectAll(UserDO.class) 查询主表全部字段(主表实体类)默认主表别名 "t"
* select() mp的select策略是覆盖,以最后一次为准,这里的策略是追加,可以一直select  
  主表字段可以用lambda,会自动添加表别名,主表别名默认是 t ,非主表字段必须带别名查询
* leftJoin() rightJoin() innerJoin() 传sql片段 格式 (表 + 别名 + 关联条件)
* 条件查询,可以查询主表以及参与连接的所有表的字段,全部调用mp原生的方法,正常使用没有sql注入风险

#### 还可以这么操作,但不建议

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class,
                new MPJQueryWrapper<UserDO>()
                        .selectAll(UserDO.class)
                        .select("addr.tel", "addr.address")
                        //行列转换
                        .select("CASE t.sex WHEN '男' THEN '1' ELSE '0' END AS sex")
                        //求和函数
                        .select("sum(a.province) AS province")
                        //自定义数据集
                        .leftJoin("(select * from user_address) addr on t.id = addr.user_id")
                        .rightJoin("area a on addr.area_id = a.id")
                        .like("addr.tel", "1")
                        .le("a.province", "1")
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
    addr.tel LIKE ?
    AND a.province <= ?)
ORDER BY
    addr.id DESC
```

# [wiki](https://gitee.com/best_handsome/mybatis-plus-join/wikis)




