# mybatis-plus-join

支持连表查询的[mybatis-plus](https://gitee.com/baomidou/mybatis-plus)

## 运行环境

* mysql8
* jdk8
* mybatis-plus 3.4.2

## 使用方法

### 使用

* entity继承MyBaseEntity
* mapper继承MyBaseMapper
* service继承MyBaseService
* serviceImpl继承MyBaseServiceImpl

### MyLambdaQueryWrapper用法

#### select(UserEntity::getId)  查询指定的字段,支持可变参数

查询user表中的head_img,name和user_address表中的address,tel

```java
class test {
    @Resource
    private UserMapper userMapper;
    
    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(new MyLambdaQueryWrapper<UserEntity>()
                        .select(UserEntity::getHeadImg, UserEntity::getName)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId,
                                right -> right.select(UserAddressEntity::getAddress, UserAddressEntity::getTel))
                , UserDTO.class);
    }
}
```

对应sql

```sql
SELECT 
    t0.head_img,
    t0.name,
    t1.address,
    t1.tel
FROM 
user t0
LEFT JOIN user_address t1 ON t0.id = t1.user_id
```

#### selectAll(UserEntity.class) 查询UserEntity全部字段

查询user全部字段和user_address表中的address,tel

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(new MyLambdaQueryWrapper<UserEntity>()
                        .selectAll(UserEntity.class)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId,
                                right -> right.select(UserAddressEntity::getAddress, UserAddressEntity::getTel))
                , UserDTO.class);
    }
}
```

对应sql

```sql
SELECT 
    t0.name,
    t0.sex, 
    t0.head_img, 
    t0.id, 
    t1.address, 
    t1.tel
FROM 
user t0
LEFT JOIN user_address t1 ON t0.id = t1.user_id
```

#### as(UserEntity::getHeadImg,UserDTO::getUserHeadImg)

查询字段head_img as userHeadImg

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(new MyLambdaQueryWrapper<UserEntity>()
                        .as(UserEntity::getHeadImg, UserDTO::getUserHeadImg)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId,
                                right -> right.select(UserAddressEntity::getAddress, UserAddressEntity::getTel))
                , UserDTO.class);
    }
}
```

对应sql

```sql
SELECT 
    t0.head_img AS userHeadImg, 
    t1.address,
    t1.tel
FROM 
user t0
LEFT JOIN user_address t1 ON t0.id = t1.user_id
```

#### 左连接 leftJoin(UserEntity::getId,UserAddressEntity::getUserId,right -> right)

前连个参数是两个表的连接条件:  
user left join user_address on user.id = User_address.user_id  
第三个参数是右表wrapper对象,可以继续使用,以上方法.

#### 条件查询eq()

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(new MyLambdaQueryWrapper<UserEntity>()
                        .selectAll(UserEntity.class)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId,
                                right -> right.select(UserAddressEntity::getAddress, UserAddressEntity::getTel))
                        .eq(true, UserEntity::getId, 1)
                        .like(UserAddressEntity::getTel, "1")
                        .eq(UserEntity::getId, UserAddressEntity::getUserId)
                , UserDTO.class);
    }
}
```

对应sql

```sql
SELECT 
    t0.name,
    t0.sex, 
    t0.head_img,
    t0.id, 
    t1.address,
    t1.tel
FROM 
user t0
LEFT JOIN user_address t1 ON t0.id = t1.user_id
WHERE (
    t0.id = ? 
    AND t1.tel LIKE ? 
    AND t0.id = t1.user_id)
```

#### [参考测试类](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/test/java/com/example/mp/MpJoinTest.java)

