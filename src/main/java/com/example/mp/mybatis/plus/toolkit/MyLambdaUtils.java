package com.example.mp.mybatis.plus.toolkit;

import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.example.mp.mybatis.plus.base.MyBaseEntity;
import com.example.mp.mybatis.plus.func.MySFunction;
import org.apache.ibatis.reflection.property.PropertyNamer;

/**
 * @author yulichang
 * @see LambdaUtils
 * @see PropertyNamer
 */
public class MyLambdaUtils {

    public static <T> String getName(SFunction<T, ?> fn) {
        return PropertyNamer.methodToProperty(LambdaUtils.resolve(fn).getImplMethodName());
    }

    public static <T extends MyBaseEntity> String getColumn(MySFunction<T, ?> fn) {
        return StringUtils.camelToUnderline(getName(fn));
    }

    public static <T extends MyBaseEntity> Class<T> getEntityClass(MySFunction<T, ?> fn) {
        return (Class<T>) LambdaUtils.resolve(fn).getImplClass();
    }
}
