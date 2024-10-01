package com.github.yulichang.wrapper.interfaces;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * on function
 *
 * @author yulichang
 * @since 1.5.0
 */
@FunctionalInterface
public interface MPredicate<T> extends Predicate<T>, Serializable {

}
