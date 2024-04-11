package com.github.yulichang.adapter.base.metadata;

import lombok.Data;

/**
 * 兼容MP 3.5.4
 * copy {@link com.baomidou.mybatisplus.core.metadata.OrderFieldInfo}
 *
 * @since 1.4.7
 */
@Data
public class OrderFieldInfo {

    /**
     * 字段
     */
    private String column;

    /**
     * 排序类型
     */
    private String type;

    /**
     * 排序顺序
     */
    private short sort;


    public OrderFieldInfo(String column, String type, short orderBySort) {
        this.column = column;
        this.type = type;
        this.sort = orderBySort;
    }
}
