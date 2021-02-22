## 别名wrapper用法

[官方自定义sql](https://mp.baomidou.com/guide/wrapper.html#%E4%BD%BF%E7%94%A8-wrapper-%E8%87%AA%E5%AE%9A%E4%B9%89sql)

官方提供的自定义sql不支持表别名和多实体泛型,扩展能力有限,对此就行了优化

原理:  
AliasQueryWrapper继承QueryWrapper  
AliasLambdaQueryWrapper继承LambdaQueryWrapper  
这两个类重写了父类字段序列化方法columnToString,添加别名

### 使用方法

#### AliasLambdaQueryWrapper

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
        UserDTO userDTO = userMapper.userLeftJoin(new AliasLambdaQueryWrapper<UserDO>()
                .setAlias("u")                //指定别名,要与注解或xml中的别名保持一致
                .eq(UserDO::getId, "1")
                .like(UserDO::getSex, "3"));
    }
}
```

对应sql:

```
select 
    u.*,
    ua.tel 
from user u 
    left join user_address ua on u.id = ua.user_id 
WHERE (
    u.id = ? 
    AND u.sex LIKE ?)
```

#### AliasQueryWrapper

mybatis-plus原生的QueryWrapper也是可以实现别名的,但是没有统一的转换方法,开发时容易忽略

AliasQueryWrapper.setAlias("u").eq("id", 1);  
等效与  
QueryWrapper.eq("u.id", 1);

AliasQueryWrapper别名一次设置全局通用,不需要每个字段都加别名

说明:  
对于非主表字段查询也是支持的

```java
class MpJoinTest {
    @Resource
    private UserMapper userMapper;

    @Test
    void test() {
        UserDTO userDTO = userMapper.userLeftJoin(new AliasQueryWrapper<>()
                .setAlias("u")
                .eq("id", "1")
                .like("sex", "3")
                .eq("ua.tel", "10086")           //如果查询字段中有点(.)则不会添加别名
                .like("ua.address", "北京"));
    }
}
```
对应sql:
```
select 
    u.*,
    ua.tel 
from user u 
    left join user_address ua on u.id = ua.user_id 
WHERE (
    u.id = ? 
    AND u.sex LIKE ? 
    AND ua.tel = ? 
    AND ua.address LIKE ?)
```

注意:如果你喜欢骚操作,请使用原生QueryWrapper  
举例:
* .eq("(select sex from user where id = u.id)", "男")
* .eq("count(u.id)", 1)

不建议在QueryWrapper中使用sql或函数  
除非你不考虑后续的维护和sql调优


QQ群:1022221898