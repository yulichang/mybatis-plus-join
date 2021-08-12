# mybatis-plus-join

* 本页功能只能在1.2.0测试版中使用,最新版本 1.2.0.Beta1

* 点个Star支持一下吧 :)

QQ群:1022221898

## 使用方法

### 安装

- Maven
  ```xml
  <dependency>
      <groupId>com.github.yulichang</groupId>
      <artifactId>mybatis-plus-join</artifactId>
      <version>1.2.0.Beta1</version>
  </dependency>
  ```
- Gradle
  ```
   implementation group: 'com.github.yulichang', name: 'mybatis-plus-join', version: '1.2.0.Beta1'
  ```
  或者clone代码到本地执行 mvn install, 再引入以上依赖  
  <br>
  注意: mybatis plus version >= 3.4.0
  <br>

### 使用

* mapper继承MPJBaseMapper (必选)
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
     *  一对一
     */
    @TableField(exist = false)
    @MPJMapping(tag = UserDO.class, thisField = "pid")
    private UserDO pUser;

    /**
     *  一对多
     */
    @TableField(exist = false)
    @MPJMapping(tag = UserAddressDO.class, joinField = "userId")
    private List<UserAddressDO> addressDOList;
}
```

UserAddressDO.java

```java

@Data
@TableName("user_address")
public class UserAddressDO {

    @TableId
    private Integer id;
    private Integer userId;
    /* 其他属性略 */
}
```

使用

```java

@SpringBootTest
public class MPJDeepTest {
    @Resource
    private UserService userService;

    @Test
    void test1() {
        UserDO deep = userService.getByIdDeep(1);
        System.out.println(deep);
    }

    @Test
    void test2() {
        List<UserDO> list = userService.listByIdsDeep(Arrays.asList(1, 4));
        list.forEach(System.out::println);
    }

    @Test
    void test3() {
        List<UserDO> list = userService.listByMapDeep(new HashMap<String, Object>() {{
            put("id", 1);
        }});
        list.forEach(System.out::println);
    }

    @Test
    void test4() {
        UserDO one = userService.getOneDeep(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getId, 1));
        System.out.println(one);
    }

    @Test
    void test5() {
        Map<String, Object> deep = userService.getMapDeep(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getId, 1));
        System.out.println(deep);
    }
}
```

MPJMapping 说明：

* MPJMapping tag 关联实体类
* MPJMapping thisField 当前类关联对应的字段的属性名，可以不填，默认为当前类的主键
* MPJMapping joinField 关联类对应的字段的属性名，可以不填，默认为关联类的主键
* MPJMapping isThrowExp 一对一查询时,如果查询到多条记录是否抛出异常,true:抛出异常,false:获取列表第一条数据
* 更多功能请看代码注释 [MPJMapping](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/main/java/com/github/yulichang/annotation/MPJMapping.java)




