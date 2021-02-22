package com.github.yulichang.common.support;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;

/**
 * 字段添加别名
 *
 * @author yulichang
 */
public class AliasQueryWrapper<T> extends QueryWrapper<T> {
    /**
     * 字段别名
     */
    private String alias;

    public AliasQueryWrapper<T> setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * 重写字段序列化方法
     */
    @Override
    protected String columnToString(String column) {
        return StringUtils.isBlank(alias) ? column : (alias + StringPool.DOT + column);
    }
}
