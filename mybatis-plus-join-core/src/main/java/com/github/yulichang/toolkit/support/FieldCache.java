package com.github.yulichang.toolkit.support;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import lombok.Data;

import java.lang.reflect.Field;

/**
 * 反射字段缓存
 *
 * @author yulichang
 * @since 1.4.5
 */
@Data
public class FieldCache {

    public Field field;

    public Class<?> type;

    public boolean isAccessible = false;

    public Object getFieldValue(Object object) {
        try {
            if (!isAccessible) {
                isAccessible = true;
                field.setAccessible(true);
            }
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw ExceptionUtils.mpe(e);
        }
    }

    public void setFieldValue(Object source, Object value) {
        try {
            if (!isAccessible) {
                isAccessible = true;
                field.setAccessible(true);
            }
            field.set(source, value);
        } catch (IllegalAccessException e) {
            throw ExceptionUtils.mpe(e);
        }
    }
}
