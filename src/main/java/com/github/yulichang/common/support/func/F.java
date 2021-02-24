package com.github.yulichang.common.support.func;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.Objects;

/**
 * 使用表全名,不适用别名
 * UserDO::getId       --> user.id
 * UserAddrDO::getTel  --> user_addr.tel
 *
 * @author yulichang
 */
public class F {

    public static <T, R> String s(SFunction<T, R> sFunction) {
        Assert.notNull(sFunction, "function is null");
        return TableInfoHelper.getTableInfo(getEntityClass(sFunction)).getTableName()
                + StringPool.DOT + getColumn(sFunction);
    }

    /**
     * 与S的getColumn一致,两个都用,保留一个就行了
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

    @SuppressWarnings("unchecked")
    public static <T> Class<T> getEntityClass(SFunction<T, ?> fn) {
        return (Class<T>) com.baomidou.mybatisplus.core.toolkit.LambdaUtils.resolve(fn).getInstantiatedType();
    }
}
