package com.github.yulichang.wrapper.segments;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Data;

/**
 * 用于selectFunc 和 applyFunc中的参数填充
 * 从原来的 {@link SelectFunc} 里的内部类中提取出来
 *
 * @author yulichang
 * @since 1.4.13
 */
@Data
public class FuncConsumer {

    private SFunction<?, ?>[] args;

    private Object[] values;

    public final <A> FuncConsumer accept(SFunction<A, ?> a) {
        this.args = new SFunction[]{a};
        return this;
    }

    public final <A, B> FuncConsumer accept(SFunction<A, ?> a, SFunction<B, ?> b) {
        this.args = new SFunction[]{a, b};
        return this;
    }

    public final <A, B, C> FuncConsumer accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c) {
        this.args = new SFunction[]{a, b, c};
        return this;
    }

    public final <A, B, C, D> FuncConsumer accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d) {
        this.args = new SFunction[]{a, b, c, d};
        return this;
    }

    public final <A, B, C, D, E> FuncConsumer accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e) {
        this.args = new SFunction[]{a, b, c, d, e};
        return this;
    }

    public final <A, B, C, D, E, F> FuncConsumer accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e, SFunction<F, ?> f) {
        this.args = new SFunction[]{a, b, c, d, e, f};
        return this;
    }

    public final <A, B, C, D, E, F, G> FuncConsumer accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e, SFunction<F, ?> f, SFunction<G, ?> g) {
        this.args = new SFunction[]{a, b, c, d, e, f, g};
        return this;
    }

    public final <A, B, C, D, E, F, G, H> FuncConsumer accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e, SFunction<F, ?> f, SFunction<G, ?> g, SFunction<H, ?> h) {
        this.args = new SFunction[]{a, b, c, d, e, f, g, h};
        return this;
    }

    public final <A, B, C, D, E, F, G, H, I> FuncConsumer accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e, SFunction<F, ?> f, SFunction<G, ?> g, SFunction<H, ?> h, SFunction<I, ?> i) {
        this.args = new SFunction[]{a, b, c, d, e, f, g, h, i};
        return this;
    }

    public final <A, B, C, D, E, F, G, H, I, J> FuncConsumer accept(SFunction<A, ?> a, SFunction<B, ?> b, SFunction<C, ?> c, SFunction<D, ?> d, SFunction<E, ?> e, SFunction<F, ?> f, SFunction<G, ?> g, SFunction<H, ?> h, SFunction<I, ?> i, SFunction<J, ?> j) {
        this.args = new SFunction[]{a, b, c, d, e, f, g, h, i, j};
        return this;
    }


    public final FuncConsumer values(Object... values) {
        this.values = values;
        return this;
    }
}
