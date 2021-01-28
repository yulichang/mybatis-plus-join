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
![image](https://gitee.com/best_handsome/mybatis-plus-join/raw/master/doc/select.png)
对应sql  
![image](https://gitee.com/best_handsome/mybatis-plus-join/raw/master/doc/selectSql.png)

#### selectAll(UserEntity.class) 查询UserEntity全部字段

查询user全部字段和user_address表中的address,tel
![image](https://gitee.com/best_handsome/mybatis-plus-join/raw/master/doc/selectAll.png)
对应sql  
![image](https://gitee.com/best_handsome/mybatis-plus-join/raw/master/doc/selectAllSql.png)

#### as(UserEntity::getHeadImg,UserDTO::getUserHeadImg)

查询字段head_img as userHeadImg
![image](https://gitee.com/best_handsome/mybatis-plus-join/raw/master/doc/selectAs.png)
对应sql  
![image](https://gitee.com/best_handsome/mybatis-plus-join/raw/master/doc/selectAsSql.png)

#### 左连接 leftJoin(UserEntity::getId,UserAddressEntity::getUserId,right -> right)

前连个参数是两个表的连接条件 -> user left join user_address on user.id = User_address.user_id  
第三个参数是右表wrapper对象,可以继续使用,以上方法.

#### 条件查询eq()

![image](https://gitee.com/best_handsome/mybatis-plus-join/raw/master/doc/selectEq.png)
对应sql  
![image](https://gitee.com/best_handsome/mybatis-plus-join/raw/master/doc/selectEqSql.png)

#### [参考测试类](https://gitee.com/best_handsome/mybatis-plus-join/blob/master/src/test/java/com/example/mp/MpJoinTest.java)

