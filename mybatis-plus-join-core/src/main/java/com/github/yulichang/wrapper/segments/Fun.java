package com.github.yulichang.wrapper.segments;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.interfaces.MFunction;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 别名func
 * <p>
 * 仅对selectFunc有效
 *
 * @author yulichang
 * @since 1.4.11
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Fun<T, R> implements SFunction<T, R> {

    private final String alias;
    private final SFunction<T, R> func;

    private final boolean isSub;
    private final Class<T> clazz;
    private final MFunction<MPJLambdaWrapper<T>> sub;

    @Override

    public R apply(T t) {
        throw new UnsupportedOperationException();
    }

    /**
     * 别名func
     * <p>
     * 仅对selectFunc有效
     */
    public static <T, R> Fun<T, R> f(String alias, SFunction<T, R> func) {
        return new Fun<>(alias, func, false, null, null);
    }

    /**
     * 别名func
     * <p>
     * 仅对selectFunc有效
     */
    public static <T, R> Fun<T, R> f(Class<T> clazz, MFunction<MPJLambdaWrapper<T>> func) {
        return new Fun<>(null, null, true, clazz, func);
    }
}
