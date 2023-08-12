package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.github.yulichang.wrapper.segments.Select;

import java.util.List;
import java.util.Map;

/**
 * @author yulichang
 * @since 1.4.6
 */
public interface SelectWrapper<Entity, Children> {

    Class<Entity> getEntityClass();

    Children setEntityClass(Class<Entity> clazz);

    List<Select> getSelectColumns();

    Children selectAll(Class<?> clazz);

    boolean isResultMap();

    List<?> getResultMapMybatisLabel();

    Map<String, Wrapper<?>> getWrapperMap();
}
