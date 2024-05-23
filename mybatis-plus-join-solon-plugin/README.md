## mybatis-plus-join-solon-plugin

```xml
<dependency>
    <groupId>com.github.yulichang</groupId>
    <artifactId>mybatis-plus-join-solon-plugin</artifactId>
    <version>lastVersion</version>
</dependency>
```

#### Tips:
[solon 2.8.0 需要添加如下配置（此问题会在 MPJ 1.4.13+ 解决)](https://gitee.com/best_handsome/mybatis-plus-join/issues/I9RN5N)
```yml
mybatis.db1:
    globalConfig:
        sqlInjector: com.github.yulichang.injector.MPJSqlInjector
```

### mapper继承MPJBaseMapper

```java
import com.github.yulichang.base.MPJBaseMapper;

@Mapper
public interface UserMapper extends MPJBaseMapper<UserDO> {

}
```

### (可选)service继承MPJBaseService

```java
import com.github.yulichang.mybatisplusjoin.solon.plugin.base.MPJBaseService;

public interface UserService extends MPJBaseService<UserDO> {

}
```

### (可选)serviceImpl继承MPJBaseServiceImpl

```java
import com.github.yulichang.mybatisplusjoin.solon.plugin.base.MPJBaseServiceImpl;
import org.noear.solon.annotation.Component;

@Component
public class UserServiceImpl extends MPJBaseServiceImpl<UserMapper, UserDO> implements UserService {

}
```