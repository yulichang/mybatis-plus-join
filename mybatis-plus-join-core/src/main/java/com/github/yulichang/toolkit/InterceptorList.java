package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.interceptor.MPJInterceptor;
import com.github.yulichang.interceptor.pagination.PageInnerInterceptor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.plugin.Interceptor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * mybatis 拦截器列表
 * 用于替换 interceptorChain 中的拦截器列表
 * 保证 MPJInterceptor 再最后一个（第一个执行）
 *
 * @author yulichang
 * @since 1.3.0
 */
public class InterceptorList<E extends Interceptor> extends ArrayList<E> {

    private static final Log log = LogFactory.getLog(InterceptorList.class);

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
        try {
            AdapterHelper.getAdapter().checkCollectionPage();
        } catch (Exception e) {
            return;
        }
        if (this.stream().anyMatch(i -> i instanceof MybatisPlusInterceptor)) {
            MybatisPlusInterceptor mybatisPlusInterceptor = super.stream().filter(i -> i instanceof MybatisPlusInterceptor)
                    .map(i -> (MybatisPlusInterceptor) i).findFirst().orElse(null);
            if (mybatisPlusInterceptor != null) {
                try {
                    Field field = MybatisPlusInterceptor.class.getDeclaredField("interceptors");
                    field.setAccessible(true);
                    @SuppressWarnings("unchecked")
                    List<InnerInterceptor> interceptors = (List<InnerInterceptor>) field.get(mybatisPlusInterceptor);

                    Integer index = null;
                    PaginationInnerInterceptor paginationInnerInterceptor = null;
                    if (interceptors.stream().noneMatch(i -> i instanceof PageInnerInterceptor)) {
                        for (int i = 0; i < interceptors.size(); i++) {
                            InnerInterceptor innerInterceptor = interceptors.get(i);
                            if (innerInterceptor instanceof PaginationInnerInterceptor) {
                                paginationInnerInterceptor = (PaginationInnerInterceptor) innerInterceptor;
                                index = i;
                                break;
                            }
                        }
                    }
                    if (index != null) {
                        interceptors.add(index + 1, new PageInnerInterceptor(paginationInnerInterceptor));
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public boolean add(E e) {
        if (this.isEmpty()) {
            return super.add(e);
        }
        boolean add = super.add(e);
        Predicate<E> predicate = i -> i instanceof MPJInterceptor;
        if (this.stream().anyMatch(predicate)) {
            E mpjInterceptor = super.stream().filter(predicate).findFirst().orElse(null);
            super.removeIf(predicate);
            return super.add(mpjInterceptor);
        }
        return add;
    }
}
