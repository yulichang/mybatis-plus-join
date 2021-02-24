package com.github.yulichang.common.support.func;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Objects;

/**
 * 预定义以下别名 可以自行设置别名
 * S.a(UserDO::getId)       --> a.id
 * S.b(UserDO::getId)       --> b.id
 * S.c(UserDO::getId)       --> c.id
 * S.d(UserDO::getId)       --> d.id
 * S.e(UserDO::getId)       --> e.id
 * S.f(UserDO::getId)       --> f.id
 *
 * @author yulichang
 */
public class S {

    public static <T, R> String a(SFunction<T, R> sFunction) {
        Assert.notNull(sFunction, "function is null");
        return "a" + StringPool.DOT + getColumn(sFunction);
    }

    public static <T, R> String b(SFunction<T, R> sFunction) {
        Assert.notNull(sFunction, "function is null");
        return "b" + StringPool.DOT + getColumn(sFunction);
    }

    public static <T, R> String c(SFunction<T, R> sFunction) {
        Assert.notNull(sFunction, "function is null");
        return "c" + StringPool.DOT + getColumn(sFunction);
    }

    public static <T, R> String d(SFunction<T, R> sFunction) {
        Assert.notNull(sFunction, "function is null");
        return "d" + StringPool.DOT + getColumn(sFunction);
    }

    public static <T, R> String e(SFunction<T, R> sFunction) {
        Assert.notNull(sFunction, "function is null");
        return "e" + StringPool.DOT + getColumn(sFunction);
    }

    public static <T, R> String f(SFunction<T, R> sFunction) {
        Assert.notNull(sFunction, "function is null");
        return "f" + StringPool.DOT + getColumn(sFunction);
    }

    /**
     * 与F的getColumn一致,两个都用,保留一个就行了
     */
    public static <T> String getColumn(SFunction<T, ?> fn) {
        SerializedLambda lambda = LambdaUtils.resolve(fn);
        String fieldName = PropertyNamer.methodToProperty(lambda.getImplMethodName());
        try {
            TableField annotation = lambda.getImplClass().getDeclaredField(fieldName).getAnnotation(TableField.class);
            if (Objects.nonNull(annotation) && StringUtils.isNotBlank(annotation.value())) {
                return annotation.value();
            }
        } catch (NoSuchFieldException ignored) {
        }
        return StringUtils.camelToUnderline(fieldName);
    }
}
