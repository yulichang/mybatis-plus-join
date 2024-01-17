package com.github.yulichang.autoconfigure.consumer;

import com.github.yulichang.wrapper.enums.IfExistsSqlKeyWordEnum;

import java.util.function.BiPredicate;

/**
 * 自定义IfExists策略
 *
 * @author yulichang
 * @since 1.4.9
 */
public interface MybatisPlusJoinIfExistsConsumer extends BiPredicate<Object, IfExistsSqlKeyWordEnum> {
}
