## 支持lambda的QueryWrapper

让QueryWrapper也能使用lambda  
单表lambda请使用mybatis-plus提供的LambdaQueryWrapper  
本示例主要在连表的情况下使用!

### 使用方法

拷贝以下类即可  
[com.github.yulichang.common.support.func.F](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/main/java/com/github/yulichang/common/support/func/F.java)  
[com.github.yulichang.common.support.func.S](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/main/java/com/github/yulichang/common/support/func/S.java)

原理:  
F类以表名+列明的形式序列化 (@TableName与@TableField注解是有效的,会优先使用注解的值)

* F.s(UserDO::getId)  --> user.id
* F.s(UserAddrDO::getId)  --> user_addr.id

S类以预定义表名+列明的形式序列化  
忽略表名,以预定义别名替代(@TableField注解是有效的,会优先使用注解的值)

* S.a(UserDO::getId)       --> a.id
* S.b(UserDO::getId)       --> b.id
* S.c(UserDO::getId)       --> c.id
* S.d(UserDO::getId)       --> d.id
* S.e(UserDO::getId)       --> e.id
* S.f(UserDO::getId)       --> f.id

注解:

```java

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {
    /**
     * 使用 S
     */
    @Select("select a.*,b.tel from user a " +
            "left join user_address b on a.id = b.user_id ${ew.customSqlSegment}")
    UserDTO userLeftJoin(@Param(Constants.WRAPPER) Wrapper<?> queryWrapper);

    /**
     * 使用 F
     */
    @Select("select user.*,user_address.tel from user " +
            "left join user_address on user.id = user_address.user_id ${ew.customSqlSegment}")
    UserDTO userLeftJoin(@Param(Constants.WRAPPER) Wrapper<?> queryWrapper);
}
```

或者xml

```
<!-- 使用 S -->
<select id="userLeftJoin" resultType="UserDTO">
    select 
        a.*, 
        b.tel
    from 
        user a
        left join user_address b on a.id = b.user_id
    ${ew.customSqlSegment}
</select>

<!-- 使用 F -->
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

    /**
     * 使用S
     */
    @Test
    void testS() {
        UserDTO dto = userMapper.userLeftJoin(new QueryWrapper<UserDO>()
                .eq(S.a(UserDO::getId), "1")
                .gt(S.a(UserDO::getSex), "3")
                .eq(S.b(UserAddressDO::getTel), "10086")
                .like(S.b(UserAddressDO::getAddress), "北京"));
    }

    /**
     * 使用F
     */
    @Test
    void testF() {
        UserDTO dto = userMapper.userLeftJoin(new QueryWrapper<UserDO>()
                .eq(F.s(UserDO::getId), "1")
                .gt(F.s(UserDO::getSex), "3")
                .eq(F.s(UserAddressDO::getTel), "10086")
                .like(F.s(UserAddressDO::getAddress), "北京"));
    }
}
```

对应sql:

```
# 使用S
select 
    a.*,
    b.tel 
from user a 
    left join user_address b on a.id = b.user_id 
WHERE (
    a.id = ? 
    AND a.sex > ? 
    AND b.tel = ? 
    AND b.address LIKE ?)
    
# 使用F
select 
    user.*,
    user_address.tel 
from user 
    left join user_address on user.id = user_address.user_id 
WHERE (
    user.id = ? 
    AND user.sex > ? 
    AND user_address.tel = ? 
    AND user_address.address LIKE ?)
```

QQ群:1022221898