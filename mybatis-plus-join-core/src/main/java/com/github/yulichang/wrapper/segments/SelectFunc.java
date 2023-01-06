package com.github.yulichang.wrapper.segments;


import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import lombok.Getter;
import org.apache.ibatis.type.TypeHandler;

import java.util.Objects;

/**
 * 自定义函数列
 *
 * @author yulichang
 * @since 1.3.10
 */
@Getter
public class SelectFunc implements Select {

    private final Integer index;

    private final SelectCache cache;

    private final String column;

    private final SFunction<?, ?>[] args;

    private final boolean hasAlias;

    private final String alias;

    private final boolean isFunc;

    private final BaseFuncEnum func;

    private final boolean hasTableAlias;

    private final String tableAlias;


    public SelectFunc(SelectCache cache, Integer index, String alias, BaseFuncEnum func, boolean hasTableAlias, String tableAlias) {
        this.index = index;
        this.cache = cache;
        this.column = cache.getColumn();
        this.hasAlias = true;
        this.args = null;
        this.alias = alias;
        this.isFunc = true;
        this.func = func;
        this.hasTableAlias = hasTableAlias;
        this.tableAlias = tableAlias;
    }

    public SelectFunc(String alias, Integer index, BaseFuncEnum func, String column, boolean hasTableAlias, String tableAlias) {
        this.index = index;
        this.column = column;
        this.args = null;
        this.cache = null;
        this.hasAlias = true;
        this.alias = alias;
        this.isFunc = true;
        this.func = func;
        this.hasTableAlias = hasTableAlias;
        this.tableAlias = tableAlias;
    }

    public SelectFunc(String alias, Integer index, BaseFuncEnum func, SFunction<?, ?>[] args, boolean hasTableAlias, String tableAlias) {
        this.index = index;
        this.column = null;
        this.args = args;
        this.cache = null;
        this.hasAlias = true;
        this.alias = alias;
        this.isFunc = true;
        this.func = func;
        this.hasTableAlias = hasTableAlias;
        this.tableAlias = tableAlias;
    }

    @Override
    public Class<?> getClazz() {
        return Objects.isNull(cache) ? null : cache.getClazz();
    }


    @Override
    public boolean isPk() {
        return false;
    }


    @Override
    public Class<?> getColumnType() {
        return Objects.isNull(cache) ? null : cache.getColumnType();
    }

    @Override
    public String getTagColumn() {
        return Objects.isNull(cache) ? null : cache.getTagColumn();
    }

    @Override
    public String getColumProperty() {
        return Objects.isNull(cache) ? null : cache.getColumProperty();
    }

    @Override
    public boolean hasTypeHandle() {
        return !Objects.isNull(cache) && cache.isHasTypeHandle();
    }

    @Override
    public TypeHandler<?> getTypeHandle() {
        return Objects.isNull(cache) ? null : cache.getTypeHandler();
    }

    @Override
    public TableFieldInfo getTableFieldInfo() {
        return Objects.isNull(cache) ? null : cache.getTableFieldInfo();
    }

    @Override
    public boolean isLabel() {
        return false;
    }

    @Override
    public boolean isStr() {
        return false;
    }


    /**
     * 泛型不同不能使用可变参数
     * 我想10个参数应该够了吧...
     */
    @SuppressWarnings("unused")
    public static class Func {

        public final <A> SFunction<?, ?>[] accept(SFunction<A, ?> a) {
            return new SFunction[]{a};
        }

        public final <A, B> SFunction<?, ?>[] accept(SFunction<A, ?> a, SFunction<B, ?> b) {
            return new SFunction[]{a, b};
        }

        public final <A, B, C> SFunction<?, ?>[] accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c) {
            return new SFunction[]{a, b, c};
        }

        public final <A, B, C, D> SFunction<?, ?>[] accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d) {
            return new SFunction[]{a, b, c, d};
        }

        public final <A, B, C, D, E> SFunction<?, ?>[] accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e) {
            return new SFunction[]{a, b, c, d, e};
        }

        public final <A, B, C, D, E, F> SFunction<?, ?>[] accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e, SFunction<F, ?> f) {
            return new SFunction[]{a, b, c, d, e, f};
        }

        public final <A, B, C, D, E, F, G> SFunction<?, ?>[] accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e, SFunction<F, ?> f, SFunction<G, ?> g) {
            return new SFunction[]{a, b, c, d, e, f, g};
        }

        public final <A, B, C, D, E, F, G, H> SFunction<?, ?>[] accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e, SFunction<F, ?> f, SFunction<G, ?> g, SFunction<H, ?> h) {
            return new SFunction[]{a, b, c, d, e, f, g, h};
        }

        public final <A, B, C, D, E, F, G, H, I> SFunction<?, ?>[] accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e, SFunction<F, ?> f, SFunction<G, ?> g, SFunction<H, ?> h, SFunction<I, ?> i) {
            return new SFunction[]{a, b, c, d, e, f, g, h, i};
        }

        public final <A, B, C, D, E, F, G, H, I, J> SFunction<?, ?>[] accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e, SFunction<F, ?> f, SFunction<G, ?> g, SFunction<H, ?> h, SFunction<I, ?> i, SFunction<J, ?> j) {
            return new SFunction[]{a, b, c, d, e, f, g, h, j};
        }

    }
}
