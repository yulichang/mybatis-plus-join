package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @author yulichang
 * @see LambdaUtils
 * @see PropertyNamer
 */

public final class MyLambdaUtils {

    /**
     * 获取lambda属性名 UserDO::getId -> id
     */
    public static <T> String getName(SFunction<T, ?> fn) {
        return PropertyNamer.methodToProperty(LambdaUtils.resolve(fn).getImplMethodName());
    }

    /**
     * 获取列明
     * 优先获取tableField中的值
     */
    public static <T> String getColumn(SFunction<T, ?> fn) {

        SerializedLambda lambda = LambdaUtils.resolve(fn);
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        try {
            Field field = lambda.getImplClass().getDeclaredField(fieldName);
            TableField annotation = field.getAnnotation(TableField.class);
            if (Objects.nonNull(annotation)) {
                return annotation.value();
            }
        } catch (NoSuchFieldException ignored) {
        }
        return StringUtils.camelToUnderline(fieldName);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getEntityClass(SFunction<T, ?> fn) {
        return (Class<T>) LambdaUtils.resolve(fn).getInstantiatedType();
    }
}
