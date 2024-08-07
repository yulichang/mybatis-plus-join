package com.github.yulichang.wrapper.interfaces;

import java.io.Serializable;

/**
 * on function
 *
 * @author yulichang
 * @since 1.1.8
 */
@FunctionalInterface
public interface MFunction<T> extends Serializable {

    T apply(T wrapper);
}
