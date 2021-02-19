## 连表通用wrapper

### 使用方法

#### 如需单独使用 请拷贝以下5个类

[com.github.yulichang.common.JoinLambdaWrapper](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/main/java/com/github/yulichang/common/JoinLambdaWrapper.java)
[com.github.yulichang.common.JoinAbstractWrapper](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/main/java/com/github/yulichang/common/JoinAbstractLambdaWrapper.java)
[com.github.yulichang.common.JoinAbstractLambdaWrapper](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/main/java/com/github/yulichang/common/JoinAbstractLambdaWrapper.java)
[com.github.yulichang.wrapper.interfaces.Compare](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/main/java/com/github/yulichang/wrapper/interfaces/Compare.java)
[com.github.yulichang.wrapper.interfaces.Func](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/main/java/com/github/yulichang/wrapper/interfaces/Func.java)

#### 不使用表别名

注解:

```java

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    @Select("select user.*,user_address.tel from user left join user_address on user.id = user_address.user_id ${ew.customSqlSegment}")
    UserDTO userLeftJoin(@Param(Constants.WRAPPER) Wrapper<?> queryWrapper);
}
```

或者xml

```

<select id="userLeftJoin" resultType="UserDTO">
    select 
        user.*, 
        user_address.tel
    from 
        user 
        left join user_address on user.id = user_address.user_id
    ${ew.customSqlSegment}
</select>
```

使用wrapper:

```java
class MpJoinTest {
    @Resource
    private UserMapper userMapper;

    @Test
    void test() {
        UserDTO userDTO = userMapper.userLeftJoin(new JoinLambdaWrapper<>()
                .eq(UserDO::getId, "1")
                .eq(UserAddressDO::getUserId, "1"));
    }
}
```

对应sql:

```
select 
    user.*,
    user_address.tel 
from 
    user 
    left join user_address on user.id = user_address.user_id 
WHERE (
    user.id = ? 
    AND user_address.user_id = ?)
```

#### 使用表别名

注解:

```java

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    @Select("select u.*,ua.tel from user u left join user_address ua on u.id = ua.user_id ${ew.customSqlSegment}")
    UserDTO userLeftJoin(@Param(Constants.WRAPPER) Wrapper<?> queryWrapper);
}
```

或者xml

```
<select id="userLeftJoin" resultType="UserDTO">
    select 
        u.*, 
        ua.tel
    from 
        user u
        left join user_address ua on u.id = ua.user_id
    ${ew.customSqlSegment}
</select>
```

使用wrapper:

```java
class MpJoinTest {
    @Resource
    private UserMapper userMapper;

    @Test
    void test() {
        UserDTO userDTO = userMapper.userLeftJoin(new JoinLambdaWrapper<>()
                .alias(UserDO.class, "u")         //如果使用别名需要再此声明别名与实体的对应关系
                .alias(UserAddressDO.class, "ua") //如果使用别名需要再此声明别名与实体的对应关系
                .eq(UserDO::getId, "1")
                .eq(UserAddressDO::getUserId, "1"));
    }
}
```

对应sql:

```
select 
    u.*,
    ua.tel 
from 
    user u
    left join user_address ua on u.id = ua.user_id 
WHERE (
    u.id = ? 
    AND ua.user_id = ?)
```