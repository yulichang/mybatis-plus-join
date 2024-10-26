package com.github.yulichang.extension.kt.segments;

import com.github.yulichang.toolkit.KtUtils;
import com.github.yulichang.wrapper.segments.SelectFunc;
import kotlin.reflect.KProperty;
import lombok.Data;

import java.util.Arrays;

/**
 * 自定义函数列
 *
 * @author yulichang
 * @since 1.4.6
 */
@Data
@SuppressWarnings("unused")
public class FuncArgs {

    private KProperty<?>[] args;

    private Object[] values;

    public FuncArgs accept(KProperty<?>... kProperty) {
        this.args = kProperty;
        return this;
    }

    public FuncArgs values(Object... values) {
        this.values = values;
        return this;
    }

    public SelectFunc.Arg[] getFuncArg() {
        return Arrays.stream(args).map(i ->
                        new SelectFunc.Arg(KtUtils.ref(i), i.getName(), false, null, i,false,null))
                .toArray(SelectFunc.Arg[]::new);
    }

}
