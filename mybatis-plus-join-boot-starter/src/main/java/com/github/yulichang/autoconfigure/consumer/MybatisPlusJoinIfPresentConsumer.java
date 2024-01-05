package com.github.yulichang.autoconfigure.consumer;

import com.github.yulichang.wrapper.enums.IfPresentSqlKeyWordEnum;

import java.util.function.BiPredicate;

/**
 * 自定义IfPresent策略
 *
 * @author yulichang
 * @since 1.4.9
 */
public interface MybatisPlusJoinIfPresentConsumer extends BiPredicate<Object, IfPresentSqlKeyWordEnum> {
}
