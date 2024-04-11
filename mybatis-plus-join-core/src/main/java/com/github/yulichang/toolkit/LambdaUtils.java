package com.github.yulichang.toolkit;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.support.IdeaProxyLambdaMeta;
import com.github.yulichang.toolkit.support.LambdaMeta;
import com.github.yulichang.toolkit.support.ReflectLambdaMeta;
import com.github.yulichang.toolkit.support.ShadowLambdaMeta;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * copy {@link com.baomidou.mybatisplus.core.toolkit.LambdaUtils}
 */
public final class LambdaUtils {

    public static <T> String getName(SFunction<T, ?> fn) {
        LambdaMeta extract = extract(fn);
        return PropertyNamer.methodToProperty(extract.getImplMethodName());
    }


    @SuppressWarnings("unchecked")
    public static <T> Class<T> getEntityClass(SFunction<T, ?> fn) {
        return (Class<T>) extract(fn).getInstantiatedClass();
    }

    /**
     * 该缓存可能会在任意不定的时间被清除
     *
     * @param func 需要解析的 lambda 对象
     * @param <T>  类型，被调用的 Function 对象的目标类型
     * @return 返回解析后的结果
     */
    public static <T> LambdaMeta extract(SFunction<T, ?> func) {
        // 1. IDEA 调试模式下 lambda 表达式是一个代理
        if (func instanceof Proxy) {
            return new IdeaProxyLambdaMeta((Proxy) func);
        }
        // 2. 反射读取
        try {
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            return new ReflectLambdaMeta((SerializedLambda) method.invoke(func), func.getClass().getClassLoader());
        } catch (Throwable e) {
            // 3. 反射失败使用序列化的方式读取
            return new ShadowLambdaMeta(com.github.yulichang.toolkit.support.SerializedLambda.extract(func));
        }
    }
}
