# mybatis-plus-join

支持连表查询的[mybatis-plus](https://gitee.com/baomidou/mybatis-plus)

## 使用方法

[goto wiki](https://gitee.com/best_handsome/mybatis-plus-join/wikis)

### 方法一

1. 将代码down到本地，使用maven install

2. 在自己的项目中添加依赖

   ```xml
   <dependency>
       <groupId>com.github.mybatisplus</groupId>
       <artifactId>join</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

### 方法二

1. 将mybatisplus目录复制到你的工程中的springboot扫描路径下

2. 添加cglib依赖

   ```xml
   <dependency>
       <groupId>cglib</groupId>
       <artifactId>cglib</artifactId>
       <version>3.3.0</version>
   </dependency>
   ```

### 使用

* mapper继承MyBaseMapper (必选)
* service继承MyBaseService (可选)
* serviceImpl继承MyBaseServiceImpl (可选)

# 核心类 MyLambdaQuery 和 MyLambdaQueryWrapper

## MyLambdaQuery用法

### 简单的3表查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class,
                new MyLambdaQuery<UserDO>()
                        .selectAll(UserDO.class)
                        .select("addr.tel", "addr.address")
                        .select("a.province")
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

```sql
SELECT t.id,
       t.name,
       t.sex,
       t.head_img,
       addr.tel,
       addr.address,
       a.province
FROM user t
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
                new MyLambdaQuery<UserDO>()
                        .selectAll(UserDO.class)
                        .select("addr.tel", "addr.address")
                        .select("a.province")
                        .leftJoin("user_address addr on t.id = addr.user_id")
                        .rightJoin("area a on addr.area_id = a.id"));
    }
}
```

对应sql

```sql
SELECT t.id,
       t.name,
       t.sex,
       t.head_img,
       addr.tel,
       addr.address,
       a.province
FROM user t
         LEFT JOIN user_address addr on t.id = addr.user_id
         RIGHT JOIN area a on addr.area_id = a.id LIMIT ?,?
```

### 还可以这么操作,但不建议

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class,
                new MyLambdaQuery<UserDO>()
                        .selectAll(UserDO.class)
                        .select("addr.tel", "addr.address")
                        //行列转换
                        .select("CASE t.sex WHEN '男' THEN '1' ELSE '0' END AS sex")
                        //求和函数
                        .select("sum(a.province) as province")
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

```sql
SELECT t.id,
       t.name,
       t.sex,
       t.head_img,
       addr.tel,
       addr.address,
       CASE t.sex WHEN '男' THEN '1' ELSE '0' END AS sex,
       sum(a.province)                           as province
FROM user t
         LEFT JOIN (select * from user_address) addr on t.id = addr.user_id
         RIGHT JOIN area a on addr.area_id = a.id
WHERE (
              t.id = ?
              AND addr.tel LIKE ?
              AND a.province <= ?)
ORDER BY addr.id DESC
```

## MyLambdaQueryWrapper用法

#### MyLambdaQueryWrapper更符合面向对象(OOP),没有难以理解的常量(魔术值),全部基于lambda,但灵活性不如上面的

#### 简单的3表查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class,
                new MyLambdaQueryWrapper<UserDO>()
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

```sql
SELECT user.id,
       user.name,
       user.sex,
       user.head_img,
       user_address.tel,
       user_address.address AS userAddress,
       area.province,
       area.city
FROM user
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
                new MyLambdaQueryWrapper<UserDO>()
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

```mysql
SELECT user.id,
       user.name,
       user.sex,
       user.head_img,
       user_address.tel,
       user_address.address AS userAddress,
       area.province,
       area.city
FROM user
         LEFT JOIN user_address ON user_address.user_id = user.id
         LEFT JOIN area ON area.id = user_address.area_id
LIMIT ?,?
```

# [wiki](https://gitee.com/best_handsome/mybatis-plus-join/wikis)


