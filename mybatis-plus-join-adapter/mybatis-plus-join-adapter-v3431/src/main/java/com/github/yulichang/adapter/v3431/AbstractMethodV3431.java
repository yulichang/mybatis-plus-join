package com.github.yulichang.adapter.v3431;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;

import java.util.List;

/**
 * @author yulichang
 * @since 1.4.5
 */
public class AbstractMethodV3431 {

    public static List<AbstractMethod> getMethod(AbstractSqlInjector sqlInjector, Class<?> clazz) {
        return sqlInjector.getMethodList(clazz);
    }
}
