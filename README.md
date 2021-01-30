# mybatis-plus-join

支持连表查询的[mybatis-plus](https://gitee.com/baomidou/mybatis-plus)

## 运行环境

* mysql8
* jdk8
* mybatis-plus 3.4.2

## 使用方法

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

## MyQueryWrapper用法

简单的3表查询
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
                                .eq(true, UserDO::getId, 1)
                                .stringQuery()
                                .like(true, "addr.tel", "1")
                                .le(true, "a.province", "1"));
    }
}
```

对应sql
  
```sql
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
    t.id = ? 
    AND addr.tel LIKE ? 
    AND a.province <= ?)
```

说明:
* UserDTO.class 查询结果类(resultType)
* selectAll(UserDO.class) 查询主表全部字段(主表实体类)
* select() mp的select策略是覆盖,这里的策略是追加,可以一直select  
主表字段可以用lambda,会自动添加表别名,主表别名默认是 t ,非主表字段必须带别名查询
* leftJoin() rightJoin() innerJoin() 传sql片段 格式 (表 + 别名 + 关联条件)
* stringQuery() lambda查询转string查询
* lambda() string查询转lambda查询
* sql组装由mp完成,正常使用没有sql注入风险

分页查询
  
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

还可以怎么操作,但不建议

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
SELECT 
    t.id,
    t.name,
    t.sex,
    t.head_img,
    addr.tel,
    addr.address,
    CASE t.sex WHEN '男' THEN '1' ELSE '0' END AS sex,
    sum(a.province) as province 
FROM 
user t 
LEFT JOIN (select * from user_address) addr on t.id = addr.user_id 
RIGHT JOIN area a on addr.area_id = a.id 
WHERE (
    t.id = ? 
    AND addr.tel LIKE ? 
    AND a.province <= ?) 
ORDER BY addr.id DESC
```

# MyLambdaQueryWrapper用法

### MyLambdaQueryWrapper更符合面向对象(OOP),没有难以理解的常量(魔术值),全部基于lambda,但好像不那么好理解

简单的3表查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(new MyLambdaQueryWrapper<UserEntity>()
                        .selectAll(UserEntity.class)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId,
                                r1 -> r1.select(UserAddressEntity::getAddress)
                                        .leftJoin(UserAddressEntity::getAreaId, AreaEntity::getId,
                                                r2 -> r2.select(AreaEntity::getProvince)))
                , UserDTO.class);
    }
}
```

对应sql

```sql
SELECT t0.name,
       t0.sex,
       t0.head_img,
       t0.id,
       t1.address,
       t2.province
FROM user t0
         LEFT JOIN user_address t1 ON t0.id = t1.user_id
         LEFT JOIN area t2 ON t1.area_id = t2.id
```

sql -> 伪代码

```java
class test {
    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(new MyLambdaQueryWrapper<user表>()
                        .selectAll(user表实体类.class)//查询user表全部字段
                        .leftJoin(user表on字段, user_address表on字段,
                                user_address表对象 -> user_address表对象
                                        .select(user_address表address字段)
                                        .leftJoin(user_address表表的on字段, area表的on字段,
                                                area表对象 -> area表对象.select(area表的province字段)))
                , UserDTO.class);//返回对象class
    }
}
```

查询user全部字段和user_address表中的address,tel

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, new MyLambdaQueryWrapper<UserEntity>()
                        .selectAll(UserEntity.class)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId,
                                right -> right.select(UserAddressEntity::getAddress, UserAddressEntity::getTel)));
    }
}
```

对应sql

```sql
SELECT t0.name,
       t0.sex,
       t0.head_img,
       t0.id,
       t1.address,
       t1.tel
FROM user t0
         LEFT JOIN user_address t1 ON t0.id = t1.user_id
```

查询字段别名 head_img as userHeadImg

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, new MyLambdaQueryWrapper<UserEntity>()
                        .as(UserEntity::getHeadImg, UserDTO::getUserHeadImg)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId,
                                right -> right.select(UserAddressEntity::getAddress, UserAddressEntity::getTel)));
    }
}
```

对应sql

```sql
SELECT t0.head_img AS userHeadImg,
       t1.address,
       t1.tel
FROM user t0
         LEFT JOIN user_address t1 ON t0.id = t1.user_id
```

#### 左连接 leftJoin(UserEntity::getId,UserAddressEntity::getUserId,right -> right)

前连个参数是两个表的连接条件:  
user left join user_address on user.id = User_address.user_id  
第三个参数是右表wrapper对象,可以继续使用,以上方法.

连表条件查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, new MyLambdaQueryWrapper<UserEntity>()
                        .selectAll(UserEntity.class)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId,
                                right -> right.select(UserAddressEntity::getAddress, UserAddressEntity::getTel))
                        .eq(true, UserEntity::getId, 1)
                        .like(UserAddressEntity::getTel, "1")
                        .eq(UserEntity::getId, UserAddressEntity::getUserId));
    }
}
```

对应sql

```sql
SELECT t0.name,
       t0.sex,
       t0.head_img,
       t0.id,
       t1.address,
       t1.tel
FROM user t0
         LEFT JOIN user_address t1 ON t0.id = t1.user_id
WHERE (
              t0.id = ?
              AND t1.tel LIKE ?
              AND t0.id = t1.user_id)
```

