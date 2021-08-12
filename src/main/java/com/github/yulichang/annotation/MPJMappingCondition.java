package com.github.yulichang.annotation;

import com.baomidou.mybatisplus.core.enums.SqlKeyword;

/**
 * 映射表条件
 *
 * @author yulichang
 * @since 1.2.0
 */
public @interface MPJMappingCondition {

    /**
     * 条件枚举
     * 目前只实现了 = != > >= < <=
     * 其他的暂未实现 TODO
     *
     * @see SqlKeyword
     */
    SqlKeyword keyWord() default SqlKeyword.EQ;

    /**
     * 数据库列名
     */
    String column();

    /**
     * 对应的值
     * 一般是一个值
     * 如果条件是 between 会取前两个
     * 如果条件是 in 就会取全部
     */
    String[] value();
}
