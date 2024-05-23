package com.github.yulichang.wrapper.segments;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * 用于selectFunc 和 applyFunc中的参数填充
 * 从原来的 {@link SelectFunc} 里的内部类中提取出来
 *
 * @author yulichang
 * @since 1.4.13
 */
public class FuncConsumer {

    public static final FuncConsumer func = new FuncConsumer();

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
        return new SFunction[]{a, b, c, d, e, f, g, h, i, j};
    }

}
