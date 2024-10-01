package com.github.yulichang.wrapper.interfaces;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Serializable Consumer
 *
 * @auther yulichang
 * @since 1.5.1
 */
public interface MConsumer<T> extends Consumer<T>, Serializable {
}
