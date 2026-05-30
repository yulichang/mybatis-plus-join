package com.github.yulichang.wrapper.ext;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.wrapper.JoinQueryWrapper;
import com.github.yulichang.wrapper.interfaces.IExt;

@SuppressWarnings("unused")
public interface Ext<Children extends JoinQueryWrapper<?>> extends IExt<Children> {

    default <T, R> Children cccEq(SFunction<T, ?> c, Object val) {
        getChildren().eq(c, val);
        return getChildren();
    }
}
