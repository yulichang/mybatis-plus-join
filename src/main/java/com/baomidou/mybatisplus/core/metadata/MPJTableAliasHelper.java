package com.baomidou.mybatisplus.core.metadata;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.annotation.MPJTableAlias;
import com.github.yulichang.toolkit.Constant;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 全局表别名控制,默认表别名为 t t1 t2 ... <br>
 * 可以通过@MPJTableAlias注解指定表别名<br/>
 * 仅对MPJLambdaWrapper有效
 *
 * @author yulichang
 * @see com.github.yulichang.wrapper.MPJLambdaWrapper
 * @see MPJTableAlias
 * @since 1.2.0
 */
public class MPJTableAliasHelper {

    private static final Map<Class<?>, TableAlias> CACHE = new ConcurrentHashMap<>();

    /**
     * 用于生成别名的序号
     */
    private static final AtomicInteger index = new AtomicInteger(1);

    public static void init(Class<?> clazz) {
        TableAlias alias = new TableAlias();
        MPJTableAlias tableAlias = clazz.getAnnotation(MPJTableAlias.class);
        if (tableAlias != null && StringUtils.isNotBlank(tableAlias.value())) {
            alias.setAlias(tableAlias.value());
        } else {
            alias.setAlias(Constant.TABLE_ALIAS + index.getAndIncrement());
        }
        alias.setAliasDOT(alias.getAlias() + StringPool.DOT);
        CACHE.put(clazz, alias);
    }

    public static TableAlias get(Class<?> clazz) {
        return CACHE.get(clazz);
    }

    @Data
    public static class TableAlias {
        /**
         * 表别名
         */
        private String alias;

        /**
         * 前缀
         */
        private String aliasDOT;
    }

}
