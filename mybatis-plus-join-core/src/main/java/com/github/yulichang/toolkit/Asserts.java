package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.TableInfo;

/**
 * @author yulichang
 * @since 1.4.5
 */
public class Asserts {

    public static void hasTable(TableInfo tableInfo, Class<?> entityClass) {
        if (tableInfo == null) {
            throw new MybatisPlusException(String.format(
                    "mapper not find by class <%s> , add mapper and extends BaseMapper<T> or MPJBaseMapper<T>",
                    entityClass.getSimpleName()));
        }
    }
}
