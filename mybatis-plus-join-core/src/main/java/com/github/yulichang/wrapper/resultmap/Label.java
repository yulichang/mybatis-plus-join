package com.github.yulichang.wrapper.resultmap;

import java.util.List;

public interface Label<T> {

    String getProperty();

    Class<?> getJavaType();

    Class<T> getOfType();

    List<Result> getResultList();

    List<?> getMybatisLabels();
}
