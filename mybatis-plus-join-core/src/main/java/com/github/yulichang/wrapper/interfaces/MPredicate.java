package com.github.yulichang.wrapper.interfaces;

import java.io.Serializable;
import java.util.function.Predicate;

/**
 * on function
 *
 * @author yulichang
 * @since 1.4.14
 */
@FunctionalInterface
public interface MPredicate<T> extends Predicate<T>, Serializable {

}
