package com.github.yulichang.autoconfigure;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置类
 *
 * @author yulichang
 * @since 1.3.7
 */
@Data
@Accessors(chain = true)
@ConfigurationProperties(prefix = "mybatis-plus-join")
public class MybatisPlusJoinProperties {

    /**
     * 打印banner
     */
    private Boolean banner = true;

    /**
     * 表别名
     */
    private String tableAlias = "t";

    /**
     * 连表查询副表是否启用逻辑删除(前提是MP配置了逻辑删除)
     */
    private Boolean subTableLogic = true;

    /**
     * MappedStatement缓存
     */
    private boolean msCache = true;

    /**
     * 连表查询重复字段名前缀
     */
    private String joinPrefix = "join";

    /**
     * 逻辑删除类型 支持 where on
     */
    private String logicDelType = "where";

    /**
     * 映射查询最大深度
     */
    private int mappingMaxCount = 5;

    /**
     * 子查询别名
     */
    private String subQueryAlias = "st";
}
