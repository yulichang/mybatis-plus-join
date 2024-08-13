package com.github.yulichang.wrapper.segments;

import com.github.yulichang.apt.Column;

import java.io.Serializable;

/**
 * 用于selectFunc 和 applyFunc中的参数填充
 * 从原来的 {@link SelectFunc} 里的内部类中提取出来
 *
 * @author yulichang
 * @since 1.4.13
 */
public class AptConsumer implements Serializable {

    public static final AptConsumer func = new AptConsumer();

    public final Column[] accept(Column... a) {
        return a;
    }

}
