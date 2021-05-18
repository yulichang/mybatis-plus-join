package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.support.ColumnCache;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.exception.MPJException;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Map;

/**
 * @author yulichang
 * @see com.baomidou.mybatisplus.core.toolkit.LambdaUtils
 * @see org.apache.ibatis.reflection.property.PropertyNamer
 */
public final class LambdaUtils {

    /**
     * 获取属性名
     */
    public static <T> String getName(SFunction<T, ?> fn) {
        return PropertyNamer.methodToProperty(com.baomidou.mybatisplus.core.toolkit.LambdaUtils.resolve(fn).getImplMethodName());
    }


    @SuppressWarnings("unchecked")
    public static <T> Class<T> getEntityClass(SFunction<T, ?> fn) {
        return (Class<T>) com.baomidou.mybatisplus.core.toolkit.LambdaUtils.resolve(fn).getInstantiatedType();
    }
}
