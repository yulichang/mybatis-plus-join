package com.github.yulichang.toolkit.support;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import lombok.Getter;

/**
 * 缓存添加tableInfo信息
 *
 * @author yulichang
 * @see com.baomidou.mybatisplus.core.toolkit.support.ColumnCache
 * @since 1.3.0
 */
public class ColumnCache extends com.baomidou.mybatisplus.core.toolkit.support.ColumnCache {

    @Getter
    private TableFieldInfo tableFieldInfo;

    @Getter
    private String keyProperty;

    @Getter
    private boolean isPK;

    @Getter
    private Class<?> keyType;


    @Deprecated
    public ColumnCache(String column, String columnSelect) {
        super(column, columnSelect);
    }

    @Deprecated
    public ColumnCache(String column, String columnSelect, String mapping) {
        super(column, columnSelect, mapping);
    }

    public ColumnCache(String column, String columnSelect, TableFieldInfo tableFieldInfo, String keyProperty, boolean isPK, Class<?> keyType) {
        super(column, columnSelect);
        this.tableFieldInfo = tableFieldInfo;
        this.keyProperty = keyProperty;
        this.isPK = isPK;
        this.keyType = keyType;
    }
}
