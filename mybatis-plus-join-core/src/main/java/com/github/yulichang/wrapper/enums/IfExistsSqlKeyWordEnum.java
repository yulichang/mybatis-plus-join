package com.github.yulichang.wrapper.enums;

import java.io.Serializable;

/**
 * if absent 枚举
 *
 * @author yulichang
 * @since 1.4.9
 */
public enum IfExistsSqlKeyWordEnum implements Serializable {
    EQ,
    NE,
    GT,
    GE,
    LT,
    LE,
    LIKE,
    NOT_LIKE,
    LIKE_RIGHT,
    NOT_LIKE_RIGHT,
    LIKE_LEFT,
    NOT_LIKE_LEFT
}
