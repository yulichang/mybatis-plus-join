package com.github.yulichang.apt;

import java.io.Serializable;

public interface BaseColumn<T> extends Serializable {

    Class<T> getColumnClass();

    String getAlias();
}