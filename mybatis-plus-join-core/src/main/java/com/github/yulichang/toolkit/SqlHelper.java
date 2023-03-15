package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.mapper.MPJTableMapperHelper;

import java.util.function.Function;

/**
 * @author yulichang
 * @see com.baomidou.mybatisplus.extension.toolkit.SqlHelper
 * @since 1.4.4
 */
@SuppressWarnings("unchecked")
public class SqlHelper {

    public static <R, T> R exec(Class<T> entityClass, Function<BaseMapper<T>, R> function) {
        Assert.notNull(entityClass,"请使用 new MPJLambdaWrapper(主表.class) 或 JoinWrappers.lambda(主表.class) 构造方法");
        Class<?> mapperClass = MPJTableMapperHelper.getMapper(entityClass);
        Assert.notNull(mapperClass, "mapper not find by class <%s>", entityClass.getSimpleName());
        Object mapper = SpringContentUtils.getBean(mapperClass);
        Assert.notNull(mapper, "mapper not init <%s>", entityClass.getSimpleName());
        return function.apply((BaseMapper<T>) mapper);
    }

    public static <R, T> R execJoin(Class<T> entityClass, Function<MPJBaseMapper<T>, R> function) {
        Assert.notNull(entityClass,"请使用 new MPJLambdaWrapper(主表.class) 或 JoinWrappers.lambda(主表.class) 构造方法");
        Class<?> mapperClass = MPJTableMapperHelper.getMapper(entityClass);
        Assert.notNull(mapperClass, "mapper not find by class <%s>", entityClass.getSimpleName());
        Object mapper = SpringContentUtils.getBean(mapperClass);
        Assert.notNull(mapper, "mapper not init <%s>", entityClass.getSimpleName());
        Assert.isTrue(mapper instanceof MPJBaseMapper, "mapper not extends MPJBaseMapper <%s>", entityClass.getSimpleName());
        return function.apply((MPJBaseMapper<T>) mapper);
    }
}
