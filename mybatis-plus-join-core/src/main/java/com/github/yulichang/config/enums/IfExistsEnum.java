package com.github.yulichang.config.enums;

import com.github.yulichang.toolkit.MPJStringUtils;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 条件判断策略
 *
 * @author yulichang
 * @since 1.4.9
 */
public enum IfExistsEnum implements Predicate<Object> {

    /**
     * 非null
     */
    NOT_NULL(Objects::nonNull),
    /**
     * 非空字符串   例： "" -> false, " " -> true ...
     */
    NOT_EMPTY(val -> NOT_NULL.and(v -> !(v instanceof CharSequence) || MPJStringUtils.isNotEmpty((CharSequence) v)).test(val)),
    /**
     * NOT_BLANK 非空白字符串  例： "" -> false, " " -> false, "\r" -> false, "abc" -> true ...
     */
    NOT_BLANK(val -> NOT_NULL.and(v -> !(v instanceof CharSequence) || MPJStringUtils.isNotBlank((CharSequence) v)).test(val));

    private final Predicate<Object> predicate;

    IfExistsEnum(Predicate<Object> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(Object obj) {
        return this.predicate.test(obj);
    }
}
