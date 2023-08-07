package com.github.yulichang.kt.segments;

import com.github.yulichang.toolkit.KtUtils;
import com.github.yulichang.wrapper.segments.SelectFunc;
import kotlin.reflect.KProperty;

import java.util.Arrays;

/**
 * 自定义函数列
 *
 * @author yulichang
 * @since 1.4.6
 */
@SuppressWarnings("unused")
public class FuncArgs {

    public SelectFunc.Arg[] accept(KProperty<?>... kProperty) {
        return Arrays.stream(kProperty).map(i -> new SelectFunc.Arg(KtUtils.ref(i), i.getName())).toArray(SelectFunc.Arg[]::new);
    }

}
