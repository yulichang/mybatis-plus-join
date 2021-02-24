package com.github.yulichang.common.support.alisa;

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
     * 可以自定义过滤策略可以是空格或其他(以下方法是只为查询字段没有带点 . 的加别名,带点的则不会加)
     * setAlias("u")
     * <p>
     * .eq("id")     --> u.id
     * .eq("ee.id")  --> ee.id
     */
    @Override
    protected String columnToString(String column) {
        if (column.lastIndexOf(StringPool.DOT) < 0) {
            return StringUtils.isBlank(alias) ? column : (alias + StringPool.DOT + column);
        } else {
            return column;
        }
    }
}
