package com.github.yulichang.wrapper.resultmap;

@FunctionalInterface
public interface MFunc<T> {

    T apply(T t);
}
