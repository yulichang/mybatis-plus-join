package com.github.yulichang.apt;

public interface BaseColumn<T> {

    Class<T> getColumnClass();

    String getAlias();
}