# mybatis-plus-join

支持连表查询的[mybatis-plus](https://gitee.com/baomidou/mybatis-plus)

## 运行环境

* mysql8
* jdk8
* mybatis-plus 3.4.2

## 使用方法

### 方法一

1. 将代码down到本地，使用maven install

2. 在自己的项目中替换mybatisplus依赖(框架依赖mybatisplus<3.4.2>)

   ```xml
   <dependency>
       <groupId>com.github.mybatisplus</groupId>
       <artifactId>join</artifactId>
       <version>1.0-SNAPSHOT</version>
   </dependency>
   ```

### 方法二

1. 将mybatisplus目录复制到你的工程中

2. 如果mybatisplus目录不在springboot扫描路径下,  
   将MybatisPlusConfiguration移动到springboot扫描路径下或添加springboot扫描路径

3. 添加cglib依赖

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

### MyLambdaQueryWrapper用法

#### select(UserEntity::getId)  查询指定的字段,支持可变参数

查询user表中的head_img,name和user_address表中的address,tel

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(new MyLambdaQueryWrapper<UserEntity>()
                        .selectAll(UserEntity.class)
                        .leftJoin(UserEntity::getId, UserAddressEntity::getUserId, r1 -> r1
                                .select(UserAddressEntity::getAddress)
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

伪代码

```java
class test {
    void testJoin() {
        List<UserDTO> list = userMapper.selectJoinList(new MyLambdaQueryWrapper<主表实体类>()
                        .selectAll(主表实体类.class)
                        .leftJoin(主表实体类on属性, 子表实体类on属性, 子表对象 -> 子表对象
                                .select(子表查询字段)
                                .leftJoin(UserAddressEntity::getAreaId, AreaEntity::getId,
                                        r2 -> r2.select(AreaEntity::getProvince)))
                , UserDTO.class);//返回对象class
    }
}
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
SELECT t0.name,
       t0.sex,
       t0.head_img,
       t0.id,
       t1.address,
       t1.tel
FROM user t0
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

#### [参考测试类](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/test/java/com/example/mp/MpJoinTest.java)

