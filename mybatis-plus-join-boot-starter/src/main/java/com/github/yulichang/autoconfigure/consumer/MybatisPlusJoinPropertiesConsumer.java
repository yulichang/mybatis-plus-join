package com.github.yulichang.autoconfigure.consumer;

import com.github.yulichang.autoconfigure.MybatisPlusJoinProperties;

/**
 * 自定义配置
 *
 * @author yulichang
 * @since 1.4.9
 */
public interface MybatisPlusJoinPropertiesConsumer {

    /**
     * 自定义配置，此方法会覆盖配置文件中的MPJ配置
     *
     * @param properties 配置文件里的配置
     * @return 修改后的配置
     */
    MybatisPlusJoinProperties config(MybatisPlusJoinProperties properties);
}
