package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.FillUtils;
import com.github.yulichang.wrapper.JoinQueryWrapper;


/**
 * 填充
 *
 * @author yulichang
 * @since 1.5.1
 */
@SuppressWarnings({"unchecked", "unused", "UnusedReturnValue"})
public interface Fill<Children> {

    Children addFill(MConsumer<Object> consumer);

    default <R> Children fill(SFunction<R, ?> field, SFunction<R, ?> tagField) {
        return addFill(c -> FillUtils.fill(c, field, tagField));
    }

    default <T, X> Children fill(SFunction<T, ?> field, SFunction<T, X> tagField, MConsumer<JoinQueryWrapper<X>> consumer) {
        return addFill(c -> FillUtils.fill(c, field, tagField, (MConsumer<JoinQueryWrapper<?>>) ((Object) consumer)));
    }

    default <T, X> Children fill(SFunction<T, ?> field, Class<X> oneClass, SFunction<T, ?> tagField) {
        return addFill(c -> FillUtils.fill(c, field, oneClass, tagField));
    }

    default <T, X> Children fill(SFunction<T, ?> field, Class<X> oneClass, SFunction<T, ?> tagField, MConsumer<JoinQueryWrapper<X>> consumer) {
        return addFill(c -> FillUtils.fill(c, field, oneClass, tagField, (MConsumer<JoinQueryWrapper<?>>) ((Object) consumer)));
    }

    default <T, X> Children fill(SFunction<T, ?> field, SFunction<X, ?> oneField, SFunction<T, ?> tagField) {
        return addFill(c -> FillUtils.fill(c, field, oneField, tagField));
    }

    default <T, X> Children fill(SFunction<T, ?> field, SFunction<X, ?> oneField, SFunction<T, ?> tagField, MConsumer<JoinQueryWrapper<X>> consumer) {
        return addFill(c -> FillUtils.fill(c, field, oneField, tagField, (MConsumer<JoinQueryWrapper<?>>) ((Object) consumer)));
    }
}
