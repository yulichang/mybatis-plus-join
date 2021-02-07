package com.github.yulichang.toolkit;

import org.springframework.beans.BeansException;

/**
 * 用于简化lambda中对象属性拷贝操作
 *
 * @author yulichang
 * @see org.springframework.beans.BeanUtils
 */
public class BeanUtils {

    public static <T> T copyProperties(Object source, T target) throws BeansException {
        org.springframework.beans.BeanUtils.copyProperties(source, target);
        return target;
    }

    public static <T> T copyProperties(Object source, T target, Class<?> editable) throws BeansException {
        org.springframework.beans.BeanUtils.copyProperties(source, target, editable);
        return target;
    }

    public static <T> T copyProperties(Object source, T target, String... ignoreProperties) throws BeansException {
        org.springframework.beans.BeanUtils.copyProperties(source, target, ignoreProperties);
        return target;
    }

}
