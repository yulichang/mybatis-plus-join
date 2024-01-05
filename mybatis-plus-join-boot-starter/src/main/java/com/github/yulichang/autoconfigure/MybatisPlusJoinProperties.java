package com.github.yulichang.autoconfigure;

import com.github.yulichang.config.enums.IfPresentEnum;
import com.github.yulichang.config.enums.LogicDelTypeEnum;
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
    private LogicDelTypeEnum logicDelType = LogicDelTypeEnum.ON;

    /**
     * 映射查询最大深度
     */
    private int mappingMaxCount = 5;

    /**
     * 子查询别名
     */
    private String subQueryAlias = "st";

    /**
     * Wrapper ifPresent 判断策略
     * <p>
     * NOT_NULL 非null
     * <p>
     * NOT_EMPTY 非空字符串   例： "" -> false, " " -> true ...
     * <p>
     * NOT_BLANK 非空白字符串  例： "" -> false, " " -> false, "\r" -> false, "abc" -> true ...
     */
    private IfPresentEnum ifPresent = IfPresentEnum.NOT_EMPTY;
}
