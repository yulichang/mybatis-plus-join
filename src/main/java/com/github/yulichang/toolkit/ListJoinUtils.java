package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

/**
 * 程序连表工具类
 * 通常用于将两个单表查询的结果合并
 *
 * @author yulichang
 */
public class ListJoinUtils {

    /**
     * example:
     * <pre>
     * leftJoin(left,right,(l,r) ->{
     *     if(l.getId.equals(r.getLeftId())){
     *         l.setXXX(r.getXXX());
     *         ....
     *     }
     * });
     * </pre>
     *
     * @param left     主表list
     * @param right    附表list
     * @param consumer 执行内容
     */
    public static <L, R> void leftJoin(List<L> left, List<R> right, BiConsumer<L, R> consumer) {
        if (CollectionUtils.isNotEmpty(left) && CollectionUtils.isNotEmpty(right)) {
            for (L l : left) {
                if (Objects.nonNull(l)) {
                    for (R r : right) {
                        if (Objects.nonNull(r)) {
                            consumer.accept(l, r);
                        }
                    }
                }
            }
        }
    }


    /**
     * example:
     * <pre>
     * leftJoin(left, right,
     *         (l, r) -> l.getId.equals(r.getLeftId()),
     *         (l, r) -> {
     *             l.setXXX(r.getXXX());
     *             ....
     *         }
     * );
     * </pre>
     *
     * @param left      主表list
     * @param right     附表list
     * @param predicate 条件
     * @param consumer  执行内容
     */
    public static <L, R> void leftJoin(List<L> left, List<R> right, BiPredicate<L, R> predicate, BiConsumer<L, R> consumer) {
        if (CollectionUtils.isNotEmpty(left) && CollectionUtils.isNotEmpty(right)) {
            for (L l : left) {
                if (Objects.nonNull(l)) {
                    for (R r : right) {
                        if (Objects.nonNull(r)) {
                            if (predicate.test(l, r)) {
                                consumer.accept(l, r);
                            }
                        }
                    }
                }
            }
        }
    }
}
