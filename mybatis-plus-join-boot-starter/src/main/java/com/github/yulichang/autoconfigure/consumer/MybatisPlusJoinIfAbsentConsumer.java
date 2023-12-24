package com.github.yulichang.autoconfigure.consumer;

import com.github.yulichang.wrapper.enums.IfAbsentSqlKeyWordEnum;

import java.util.function.BiPredicate;

/**
 * 自定义IfAbsent策略
 *
 * @author yulichang
 * @since 1.4.9
 */
public interface MybatisPlusJoinIfAbsentConsumer extends BiPredicate<Object, IfAbsentSqlKeyWordEnum> {
}
