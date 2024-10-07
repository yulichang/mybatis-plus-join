package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.github.yulichang.base.JoinMapper;

import java.util.function.Function;

/**
 * @author yulichang
 * @see com.baomidou.mybatisplus.extension.toolkit.SqlHelper
 * @since 1.4.4
 */
@SuppressWarnings("unchecked")
public final class SqlHelper {

    public static <R, T> R exec(Class<T> entityClass, Function<JoinMapper<T>, R> function) {
        Assert.notNull(entityClass, "请使用 new MPJLambdaWrapper(主表.class) 或 JoinWrappers.lambda(主表.class) 构造方法");
        Object mapper = SpringContentUtils.getMapper(entityClass);
        Assert.notNull(mapper, "mapper not init <%s>", entityClass.getSimpleName());
        Assert.isTrue(mapper instanceof JoinMapper, "mapper <%s> not extends MPJBaseMapper ", entityClass.getSimpleName());
        return function.apply((JoinMapper<T>) mapper);
    }
}
