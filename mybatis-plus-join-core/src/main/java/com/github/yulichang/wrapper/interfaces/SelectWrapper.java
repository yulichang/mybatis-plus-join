package com.github.yulichang.wrapper.interfaces;

import com.github.yulichang.wrapper.resultmap.Label;
import com.github.yulichang.wrapper.segments.PageInfo;
import com.github.yulichang.wrapper.segments.Select;

import java.util.List;

/**
 * @author yulichang
 * @since 1.4.6
 */
public interface SelectWrapper<Entity, Children> {

    Class<Entity> getEntityClass();

    Children setEntityClass(Class<Entity> clazz);

    List<Select> getSelectColumns();

    Children selectAll();

    boolean isResultMap();

    List<Label<?>> getResultMapMybatisLabel();

    String getFrom();

    String getAlias();

    boolean isResultMapCollection();

    PageInfo getPageInfo();

    boolean isPageByMain();
}
