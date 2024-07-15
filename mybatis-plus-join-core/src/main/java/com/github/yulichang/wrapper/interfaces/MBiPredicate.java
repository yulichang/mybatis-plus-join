package com.github.yulichang.wrapper.interfaces;

import java.io.Serializable;
import java.util.function.BiPredicate;

/**
 * on function
 *
 * @author yulichang
 * @since 1.4.14
 */
@FunctionalInterface
public interface MBiPredicate<T, U> extends BiPredicate<T, U>, Serializable {

}
