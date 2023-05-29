<p align="center">
  <a href="https://github.com/yulichang/mybatis-plus-join" target="_blank">
   <img alt="Mybatis-Plus-Join-Logo" src="https://mybatisplusjoin.com/lg.png">
  </a>
</p>
<p align="center">
  为简化开发工作、提高生产率而生
</p>

<p align="center">
  <a href="https://github.com/yulichang/mybatis-plus-join" target="_blank">
    <img src="https://img.shields.io/github/stars/yulichang/mybatis-plus-join.svg?style=social&label=Stars" alt=""/>
  </a>
  <a href="https://gitee.com/best_handsome/mybatis-plus-join" target="_blank">
    <img src="https://gitee.com/best_handsome/mybatis-plus-join/badge/star.svg?theme=dark" alt=""/>
  </a>
  <br/>
  <a href="https://central.sonatype.com/artifact/com.github.yulichang/mybatis-plus-join-boot-starter">
    <img alt="maven" src="https://img.shields.io/maven-central/v/com.github.yulichang/mybatis-plus-join-boot-starter.svg?style=flat-square">
  </a>

  <a href="https://www.apache.org/licenses/LICENSE-2.0">
    <img alt="code style" src="https://img.shields.io/badge/license-Apache%202-4EB1BA.svg?style=flat-square">
  </a>
</p>
<h1 align="center">mybatis-plus-join</h1>
<p align="center">
对 <a href="https://gitee.com/baomidou/mybatis-plus" target="_blank">mybatis-plus</a> 多表查询的扩展 |
<a href="https://gitee.com/best_handsome/mybatis-plus-join-demo" target="_blank">演示工程</a> |
<a href="https://www.baidu.com/link?url=wdmhssysW-Mj19Gkcc2CBOzNVoimHat57mlnH78SEU_6y0awYgDKTBy7es9BXnAH&wd=&eqid=908484020001866e000000056440b5e3" target="_blank">使用文档</a> |
点个Star支持一下吧 (☆▽☆)
</p>

<p align="center">
QQ群:1022221898  或者 
<a href="https://gitee.com/best_handsome/mybatis-plus-join/issues/I65N2M" target="_blank">添加作者微信，备注MPJ，加入微信群</a>  
<br/>
<img width="200px" src="https://mybatisplusjoin.com/qr.png"  alt="添加作者微信，备注MPJ，加入微信群"/>
</p>

### <a href="https://www.baidu.com/link?url=6NtKqbGKUIIdwoBUaqNkQnLiG3d5y03L6Pfg6ODId0VKfPifpB1xqdQzsBprm-0h&wd=&eqid=d26e7f1600004777000000056440ade6" target="_blank">使用文档WIKI</a>

## 使用方法

### 安装

- Maven
  ```xml
  <dependency>
      <groupId>com.github.yulichang</groupId>
      <artifactId>mybatis-plus-join-boot-starter</artifactId>
      <version>1.4.5</version>
  </dependency>
  ```
- Gradle
  ```
   implementation 'com.github.yulichang:mybatis-plus-join-boot-starter:1.4.5'
  ```
  或者clone代码到本地执行 mvn install, 再引入以上依赖  
  <br>
  注意: mybatis plus version >= 3.3.0
  <br>

### 使用

* mapper继承MPJBaseMapper (必选)
* service继承MPJBaseService (可选)
* serviceImpl继承MPJBaseServiceImpl (可选)

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

说明:

* UserDTO.class 查询结果返回类(resultType)
* selectAll() 查询指定实体类的全部字段
* select() 查询指定的字段,支持可变参数,同一个select只能查询相同表的字段
* selectAs() 字段别名查询,用于数据库字段与业务实体类属性名不一致时使用
* leftJoin() 参数说明  
  第一个参数:  参与连表的实体类class  
  第二个参数:  连表的ON字段,这个属性必须是第一个参数实体类的属性  
  第三个参数:  参与连表的ON的另一个实体类属性
* 默认主表别名是t,其他的表别名以先后调用的顺序使用t1,t2,t3....
* 条件查询,可以查询主表以及参与连接的所有表的字段,全部调用mp原生的方法,正常使用没有sql注入风险

#### 一对多查询

```java
class test {
    @Resource
    private UserMapper userMapper;

    @Test
    void testResultMap() {
        MPJLambdaWrapper<UserDO> wrapper = new MPJLambdaWrapper<>(User.class)
                .selectAll(UserDO.class)
                //对多查询
                .selectCollection(AddressDO.class, UesrDTO::getAddressList)
                //对一查询
                .selectAssociation(AddressDO.class, UesrDTO::getAddress)
                .leftJoin(AddressDO.class, AddressDO::getUserId, UserDO::getId);

        List<UserDTO> dtoList = userMapper.selectJoinList(UserDTO.class, wrapper);

        //关于对多分页查询
        //由于嵌套结果方式会导致结果集被折叠，因此分页查询的结果在折叠后总数会减少，所以无法保证分页结果数量正确。
    }
}
```

MPJLambdaWrapper其他功能

* <a href="https://mybatisplusjoin.com/pages/core/lambda/select/selectCollection.html" target="_blank">
  一对一，一对多使用</a>
* <a href="https://mybatisplusjoin.com/pages/core/lambda/select/selectFunc.html" target="_blank">简单的SQL函数使用</a>
* <a href="https://mybatisplusjoin.com/pages/core/lambda/join/leftJoin.html" target="_blank">ON语句多条件支持</a>
* <a href="https://www.baidu.com/link?url=wdmhssysW-Mj19Gkcc2CBOzNVoimHat57mlnH78SEU_6y0awYgDKTBy7es9BXnAH&wd=&eqid=908484020001866e000000056440b5e3" target="_blank">其他全部功能请参考使用文档</a>

# <a href="https://www.baidu.com/link?url=wdmhssysW-Mj19Gkcc2CBOzNVoimHat57mlnH78SEU_6y0awYgDKTBy7es9BXnAH&wd=&eqid=908484020001866e000000056440b5e3" target="_blank">使用文档 wiki</a>
