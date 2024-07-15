package com.github.yulichang.config.enums;

import com.github.yulichang.toolkit.MPJStringUtils;
import com.github.yulichang.wrapper.interfaces.MPredicate;

import java.io.Serializable;
import java.util.Objects;

/**
 * 条件判断策略
 *
 * @author yulichang
 * @since 1.4.9
 */
public enum IfExistsEnum implements MPredicate<Object>, Serializable {

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

    private final MPredicate<Object> predicate;

    IfExistsEnum(MPredicate<Object> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(Object obj) {
        return this.predicate.test(obj);
    }
}
