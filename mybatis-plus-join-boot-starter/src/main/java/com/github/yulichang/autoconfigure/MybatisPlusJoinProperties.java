package com.github.yulichang.autoconfigure;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for MyBatis.
 *
 * @author Eddú Meléndez
 * @author Kazuki Shimizu
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "mybatis-plus-join")
public class MybatisPlusJoinProperties {

    private Boolean banner = false;
}
