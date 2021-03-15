package com.github.yulichang.common.support.alias;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 * 字段添加别名
 * 使用方法:
 * <p>
 * select t.* from table t ${ew.customSqlSegmentAlias("t")}
 * <p>
 * 注意:
 * 官方的自定义sql是ew.customSqlSegment,不带括号,是属性
 * 带别名的是 ew.customSqlSegment("t") 带括号,是方法
 * 括号中的别名必须带双引号
 *
 * @author yulichang
 */
public class AliasLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {
    /**
     * 字段别名
     */
    private String alias;

    /**
     * 重写字段序列化方法
     */
    @Override
    protected String columnToString(SFunction<T, ?> column, boolean onlyColumn) {
        String as = super.columnToString(column, onlyColumn);
        return StringUtils.isBlank(alias) ? as : (alias + StringPool.DOT + as);
    }

    public String customSqlSegment(String alias) {
        this.alias = alias;
        return super.getCustomSqlSegment();
    }
}
