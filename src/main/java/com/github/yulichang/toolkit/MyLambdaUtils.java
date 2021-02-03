package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.ibatis.reflection.property.PropertyNamer;

/**
 * @author yulichang
 * @see LambdaUtils
 * @see PropertyNamer
 */
@SuppressWarnings("all")
public final class MyLambdaUtils {


    public static <T> String getName(SFunction<T, ?> fn) {
        return PropertyNamer.methodToProperty(LambdaUtils.resolve(fn).getImplMethodName());
    }

    public static <T> String getColumn(SFunction<T, ?> fn) {
        return StringUtils.camelToUnderline(getName(fn));
    }

    public static <T> Class<T> getEntityClass(SFunction<T, ?> fn) {
        return (Class<T>) LambdaUtils.resolve(fn).getInstantiatedType();
    }

}
