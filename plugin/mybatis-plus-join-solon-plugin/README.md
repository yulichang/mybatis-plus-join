## mybatis-plus-join-solon-plugin

```xml
<dependency>
    <groupId>com.github.yulichang</groupId>
    <artifactId>mybatis-plus-join-solon-plugin</artifactId>
    <version>lastVersion</version>
</dependency>
```

#### Tips:
如果出现 `Invalid bound statement (not found)` 添加如下配置
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