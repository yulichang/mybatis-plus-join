package com.github.yulichang.config.enums;

import com.github.yulichang.toolkit.MPJStringUtils;
import com.github.yulichang.wrapper.enums.IfAbsentSqlKeyWordEnum;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * 条件判断策略
 *
 * @author yulichang
 * @since 1.4.9
 */
public enum IfAbsentEnum implements BiPredicate<Object, IfAbsentSqlKeyWordEnum> {

    /**
     * 非null
     */
    NOT_NULL((val, key) -> Objects.nonNull(val)),
    /**
     * 非空字符串   例： "" -> false, " " -> true ...
     */
    NOT_EMPTY((val, key) -> NOT_NULL.test(val, key) && (!(val instanceof CharSequence) || MPJStringUtils.isNotEmpty((CharSequence) val))),
    /**
     * NOT_BLANK 非空白字符串  例： "" -> false, " " -> false, "\r" -> false, "abc" -> true ...
     */
    NOT_BLANK((val, key) -> NOT_NULL.test(val, key) && (!(val instanceof CharSequence) || MPJStringUtils.isNotBlank((CharSequence) val)));

    private final BiPredicate<Object, IfAbsentSqlKeyWordEnum> predicate;

    IfAbsentEnum(BiPredicate<Object, IfAbsentSqlKeyWordEnum> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(Object obj, IfAbsentSqlKeyWordEnum keyword) {
        return this.predicate.test(obj, keyword);
    }
}
