<!--suppress HtmlDeprecatedAttribute -->
<p align="center">
  <a href="https://github.com/yulichang/mybatis-plus-join" target="_blank">
   <img alt="Mybatis-Plus-Join-Logo" src="https://github.com/yulichang/mybatis-plus-join/assets/33247410/f61a92d0-dc6e-463d-b431-61f469bd35f8">
  </a>
</p>
<h1 align="center">MyBatis-Plus-Join</h1>
<p align="center">
  为简化开发工作、提高生产率而生
</p>
<p align="center">
  <a href="https://central.sonatype.com/artifact/com.github.yulichang/mybatis-plus-join-boot-starter">
    <img alt="maven" src="https://img.shields.io/maven-central/v/com.github.yulichang/mybatis-plus-join-boot-starter.svg?style=flat-square">
  </a>
  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
  <a href="https://github.com/yulichang/mybatis-plus-join" target="_blank">
    <img src="https://img.shields.io/github/stars/yulichang/mybatis-plus-join.svg?style=social&label=Stars" alt=""/>
  </a>
  <a href="https://gitee.com/best_handsome/mybatis-plus-join" target="_blank">
    <img src="https://gitee.com/best_handsome/mybatis-plus-join/badge/star.svg?theme=dark" alt=""/>
  </a>
</p>
<p align="center">
对 <a href="https://github.com/baomidou/mybatis-plus" target="_blank">MyBatis-Plus</a> 多表查询的扩展 |
<a href="https://github.com/yulichang/mybatis-plus-join-demo" target="_blank">演示工程</a> |
<a href="https://mybatis-plus-join.github.io" target="_blank">使用文档</a> |
点个Star支持一下吧 (☆▽☆)
</p>

<p align="center">
QQ群:680016987  或者 
<a href="https://gitee.com/best_handsome/mybatis-plus-join/issues/I65N2M" target="_blank">添加作者微信，备注MPJ，加入微信群</a>
<br/>
<img width="200px" height="200px" src="https://github.com/yulichang/mybatis-plus-join/assets/33247410/6a2aedfa-c67d-4691-9441-204f28b6c73d"  alt="添加作者微信，备注MPJ，加入微信群"/>
</p>

### <a href="https://mybatis-plus-join.github.io" target="_blank">使用文档WIKI</a>

## 使用方法

### 安装

- Maven
  ```xml
  <dependency>
      <groupId>com.github.yulichang</groupId>
      <artifactId>mybatis-plus-join-boot-starter</artifactId>
      <version>1.5.0</version>
  </dependency>
  ```
- Gradle
  ```
   implementation 'com.github.yulichang:mybatis-plus-join-boot-starter:1.5.0'
  ```
  或者clone代码到本地执行 mvn install, 再引入以上依赖  
  <br>
  注意: MyBatis Plus版本需要3.3.0+
  <br>

### 使用

* mapper继承MPJBaseMapper 

### Lambda形式用法（MPJLambdaWrapper）

#### 简单的连表查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    void testJoin() {
        //和Mybatis plus一致，MPJLambdaWrapper的泛型必须是主表的泛型，并且要用主表的Mapper来调用
        MPJLambdaWrapper<UserDO> wrapper = JoinWrappers.lambda(UserDO.class)
                .selectAll(UserDO.class)//查询user表全部字段
                .select(UserAddressDO::getTel)//查询user_address tel 字段
                .selectAs(UserAddressDO::getAddress, UserDTO::getUserAddress)//别名
                .select(AreaDO::getProvince, AreaDO::getCity)
                .leftJoin(UserAddressDO.class, UserAddressDO::getUserId, UserDO::getId)
                .leftJoin(AreaDO.class, AreaDO::getId, UserAddressDO::getAreaId)
                .eq(UserDO::getId, 1)
                .like(UserAddressDO::getTel, "1")
                .gt(UserDO::getId, 5);

        //连表查询 返回自定义ResultType
        List<UserDTO> list = userMapper.selectJoinList(UserDTO.class, wrapper);

        //分页查询 （需要启用 mybatis plus 分页插件）
        Page<UserDTO> listPage = userMapper.selectJoinPage(new Page<>(2, 10), UserDTO.class, wrapper);
    }
}
```

对应sql

```
SELECT  
    t.id, t.name, t.sex, t.head_img, 
    t1.tel, t1.address AS userAddress,
    t2.province, t2.city 
FROM 
    user t 
    LEFT JOIN user_address t1 ON t1.user_id = t.id 
    LEFT JOIN area t2 ON t2.id = t1.area_id 
WHERE (
    t.id = ? 
    AND t1.tel LIKE ? 
    AND t.id > ?)
```


MPJLambdaWrapper其他功能

* <a href="https://mybatis-plus-join.github.io/pages/core/lambda/select/selectCollection.html" target="_blank">
  一对一，一对多使用</a>
* <a href="https://mybatis-plus-join.github.io/pages/core/lambda/select/selectFunc.html" target="_blank">简单的SQL函数使用</a>
* <a href="https://mybatis-plus-join.github.io/pages/core/lambda/join/leftJoin.html" target="_blank">ON语句多条件支持</a>
* <a href="https://mybatis-plus-join.github.io" target="_blank">其他全部功能请参考使用文档</a>

# <a href="https://mybatis-plus-join.github.io" target="_blank">使用文档 wiki</a>
# 用爱发电
<a href="https://mybatis-plus-join.github.io/pages/quickstart/support.html" target="_blank">
  <img alt="支持一下mybatis-plus-join" src="https://mybatis-plus-join.github.io/support.png">
</a>
