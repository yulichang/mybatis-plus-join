package com.github.yulichang.common.support.alisa;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * 字段添加别名
 *
 * @author yulichang
 */
public class AliasLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {
    /**
     * 字段别名
     */
    private String alias;

    public AliasLambdaQueryWrapper<T> setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    /**
     * 重写字段序列化方法
     */
    @Override
    protected String columnToString(SFunction<T, ?> column, boolean onlyColumn) {
        String as = super.columnToString(column, onlyColumn);
        return StringUtils.isBlank(alias) ? as : (alias + StringPool.DOT + as);
    }
}
