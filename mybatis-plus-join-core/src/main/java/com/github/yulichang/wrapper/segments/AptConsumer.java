package com.github.yulichang.wrapper.segments;

import com.github.yulichang.extension.apt.matedata.Column;
import lombok.Data;

import java.io.Serializable;

/**
 * 用于selectFunc 和 applyFunc中的参数填充
 * 从原来的 {@link SelectFunc} 里的内部类中提取出来
 *
 * @author yulichang
 * @since 1.4.13
 */
@Data
public class AptConsumer implements Serializable {

    private Column[] columns;

    private Object[] values;


    public final AptConsumer accept(Column... a) {
        this.columns = a;
        return this;
    }

    public final AptConsumer accept(Object... a) {
        this.values = a;
        return this;
    }

}
