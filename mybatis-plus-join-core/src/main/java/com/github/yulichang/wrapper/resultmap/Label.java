package com.github.yulichang.wrapper.resultmap;

import java.util.List;

/**
 * 映射标签
 *
 * @author yulichang
 * @since 2023/3/17 11:35
 */
public interface Label<T> {

    String getProperty();

    Class<?> getJavaType();

    Class<T> getOfType();

    List<IResult> getResultList();

    List<?> getMybatisLabels();
}
