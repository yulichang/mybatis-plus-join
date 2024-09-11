package com.github.yulichang.extension.apt.toolkit;

import com.github.yulichang.extension.apt.matedata.BaseColumn;
import com.github.yulichang.extension.apt.AptQueryWrapper;

/**
 * @author yulichang
 * @since 1.5.0
 */
@SuppressWarnings("unused")
public class AptWrappers {

    /**
     * AptWrappers.query(User.class)
     */
    public static <T> AptQueryWrapper<T> query(BaseColumn<T> baseColumn) {
        return new AptQueryWrapper<>(baseColumn);
    }

    /**
     * AptWrappers.query("t", User.class)
     */
    public static <T> AptQueryWrapper<T> query(BaseColumn<T> baseColumn, T entity) {
        return new AptQueryWrapper<>(baseColumn, entity);
    }
}
