# mybatis-plus-join

#### @EntityMapping 和 @FieldMapping 注解

UserDO.java

```java

@Data
@TableName("user")
public class UserDO {

    @TableId
    private Integer id;
    private Integer pid;//父id
    /* 其他属性略 */

    /**
     * 查询上级 一对一
     */
    @TableField(exist = false)
    @EntityMapping(thisField = "pid", joinField = "id")
    private UserDO pUser;

    /**
     * 查询下级 一对多
     */
    @TableField(exist = false)
    @EntityMapping(thisField = "id", joinField = "pid")
    private List<UserDO> childUser;

    /**
     * 带条件的查询下级 一对多
     */
    @TableField(exist = false)
    @EntityMapping(thisField = "id", joinField = "pid",
            condition = {
                    @MPJMappingCondition(column = "sex", value = "0"),//sex = '0' 默认条件是等于
                    @MPJMappingCondition(column = "name", value = "张三", keyWord = SqlKeyword.LIKE)//name like '%a%'
            },
            apply = @MPJMappingApply(value = "id between 1 and 20"))//拼接sql 同 wrapper.apply()
    private List<UserDO> childUserCondition;

    /**
     * 查询地址 (一对多)
     */
    @TableField(exist = false)
    @EntityMapping(thisField = "id", joinField = "userId")
    private List<UserAddressDO> addressList;

    /**
     * 绑定字段 （一对多）
     */
    @TableField(exist = false)
    @FieldMapping(tag = UserDO.class, thisField = "id", joinField = "pid", select = "id")
    private List<Integer> childIds;
}
```

使用

```java
/**
 * 一对一，一对多关系映射查询
 * 映射只对以Deep结尾有效，比如 getByIdDeep listByIdsDeep 等
 * 如果不需要关系映射就使用mybatis plus原生方法即可，比如 getById listByIds 等
 *
 * 注意：关系映射不会去关联查询，而是执行多次单表查询（对结果汇总后使用in语句查询,再对结果进行匹配）
 */
@SpringBootTest
class MappingTest {
    @Resource
    private UserMapper userMapper;

    @Test
    void test1() {
        UserDO deep = userMapper.selectByIdDeep(2);
        System.out.println(deep);
    }

    @Test
    void test2() {
        List<UserDO> list = userMapper.selectListDeep(Wrappers.emptyWrapper());
        list.forEach(System.out::println);
    }

    @Test
    void test3() {
        Page<UserDO> page = userMapper.selectPageDeep(new Page<>(2, 2), Wrappers.emptyWrapper());
        page.getRecords().forEach(System.out::println);
    }

    /**
     * 更多方法请查阅 MPJDeepMapper 或者 MPJDeepService
     * 使用方式与 mybatis plus 一致
     */
}
```

MPJMapping 说明：

* @EntityMapping / @FieldMapping tag 关联实体类
* @EntityMapping / @FieldMapping thisField 当前类关联对应的字段的属性名，可以不填，默认为当前类的主键
* @EntityMapping / @FieldMapping joinField 关联类对应的字段的属性名，可以不填，默认为关联类的主键
* @EntityMapping / @FieldMapping isThrowExp 一对一查询时,如果查询到多条记录是否抛出异常,true:抛出异常,false:获取列表第一条数据
*

更多功能请看代码注释 [EntityMapping](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/main/java/com/github/yulichang/annotation/EntityMapping.java)
[FieldMapping](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/main/java/com/github/yulichang/annotation/FieldMapping.java)




