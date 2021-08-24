# mybatis-plus-join

* 本页功能只能在1.2.0测试版中使用,最新版本 1.2.0.Beta5

* 点个Star支持一下吧 :)

QQ群:1022221898

## 使用方法

### 安装

- Maven
  ```xml
  <dependency>
      <groupId>com.github.yulichang</groupId>
      <artifactId>mybatis-plus-join</artifactId>
      <version>1.2.0.Beta5</version>
  </dependency>
  ```
- Gradle
  ```
   implementation group: 'com.github.yulichang', name: 'mybatis-plus-join', version: '1.2.0.Beta5'
  ```
  或者clone代码到本地执行 mvn install, 再引入以上依赖  
  <br>
  注意: mybatis plus version >= 3.4.0
  <br>

### 使用

* mapper继承MPJBaseMapper
* service继承MPJBaseService (可选)
* serviceImpl继承MPJBaseServiceImpl (可选)

#### @MPJMapping注解

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
 * 映射只对MPJBaseDeepService中的方法有效果 ，一般以Deep结尾，比如 getByIdDeep listByIdsDeep 等
 * 如果不需要关系映射就使用mybatis plus原生方法即可，比如 getById listByIds 等
 *
 * @see com.github.yulichang.base.service.MPJDeepService
 */
@SpringBootTest
class MappingTest {
    @Resource
    private UserMapper userMapper;

    /**
     * 根据id查询
     * <p>
     * 查询过程：
     * 一共查询了3次
     * 第一次查询目标UserDO
     * 第二次根据pid查询上级用户
     * 第三次根据自身id查询下级用户
     */
    @Test
    void test1() {
        UserDO deep = userMapper.selectByIdDeep(2);
        System.out.println(deep);
    }

    /**
     * 查询全部
     * <p>
     * 查询过程：
     * 一共查询了3次
     * 第一次查询目标UserDO集合
     * 第二次根据pid查询上级用户（不会一条记录一条记录的去查询，对pid进行汇总，用in语句一次性查出来，然后进行匹配）
     * 第三次根据自身id查询下级用户（不会一条记录一条记录的去查询，对id进行汇总，用in语句一次性查出来，然后进行匹配）
     */
    @Test
    void test2() {
        List<UserDO> list = userMapper.selectListDeep(Wrappers.emptyWrapper());
        list.forEach(System.out::println);
    }

    /**
     * 分页查询
     * <p>
     * 查询过程与上面一致
     */
    @Test
    void test3() {
        Page<UserDO> page = userMapper.selectPageDeep(new Page<>(2, 2), Wrappers.emptyWrapper());
        page.getRecords().forEach(System.out::println);
    }

```

MPJMapping 说明：

* MPJMapping tag 关联实体类
* MPJMapping thisField 当前类关联对应的字段的属性名，可以不填，默认为当前类的主键
* MPJMapping joinField 关联类对应的字段的属性名，可以不填，默认为关联类的主键
* MPJMapping isThrowExp 一对一查询时,如果查询到多条记录是否抛出异常,true:抛出异常,false:获取列表第一条数据
*

更多功能请看代码注释 [MPJMapping](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/main/java/com/github/yulichang/annotation/MPJMapping.java)




