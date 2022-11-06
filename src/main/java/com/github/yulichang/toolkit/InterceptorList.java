package com.github.yulichang.toolkit;

import com.github.yulichang.interceptor.MPJInterceptor;
import org.apache.ibatis.plugin.Interceptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

/**
 * mybatis 拦截器列表
 * 用于替换 interceptorChain 中的拦截器列表
 * 保证 MPJInterceptor 再最后一个（第一个执行）
 *
 * @author yulichang
 * @since 1.2.5
 */
public class InterceptorList<E extends Interceptor> extends ArrayList<E> {

    public InterceptorList() {
        super();
    }

    public InterceptorList(Collection<? extends E> c) {
        super(c);
        Predicate<E> predicate = i -> i instanceof MPJInterceptor;
        if (this.stream().anyMatch(predicate)) {
            E mpjInterceptor = super.stream().filter(predicate).findFirst().orElse(null);
            super.removeIf(predicate);
            super.add(mpjInterceptor);
        }
    }

    @Override
    public boolean add(E e) {
        if (this.isEmpty()) {
            return super.add(e);
        }
        Predicate<E> predicate = i -> i instanceof MPJInterceptor;
        if (this.stream().anyMatch(predicate)) {
            E mpjInterceptor = super.stream().filter(predicate).findFirst().orElse(null);
            super.removeIf(predicate);
            boolean a = super.add(e);
            boolean b = super.add(mpjInterceptor);
            return a && b;
        }
        return super.add(e);
    }
}
