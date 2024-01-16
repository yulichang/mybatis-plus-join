## mybatis-plus-join-solon-plugin

```xml
<dependency>
    <groupId>com.github.yulichang</groupId>
    <artifactId>mybatis-plus-join-solon-plugin</artifactId>
    <version>lastVersion</version>
</dependency>
```

### mapper继承MPJBaseMapper

```java
import com.github.yulichang.base.MPJBaseMapper;

@Mapper
public interface UserMapper extends MPJBaseMapper<UserDO> {

}
```

### (可选)service继承JoinService

```java
import com.github.yulichang.mybatisplusjoin.solon.plugin.base.JoinService;

public interface UserService extends JoinService<UserDO> {

}
```

### (可选)serviceImpl继承JoinServiceImpl

```java
import com.github.yulichang.mybatisplusjoin.solon.plugin.base.JoinServiceImpl;
import org.noear.solon.annotation.Component;

@Component
public class UserServiceImpl extends JoinServiceImpl<UserMapper, UserDO> implements UserService {

}
```