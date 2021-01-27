package com.example.mp.mybatis.plus.toolkit;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * 程序连表工具类
 *
 * @author yulichang
 * @since 2021/01/13
 */
@Slf4j
public class ListJoinUtils {


    /**
     * List关联
     * example:
     * join(leftList, rightList, Left::getRightId, Right::getId, Left::setRight);
     * <p>
     *
     * @param leftData       主表
     * @param rightData      数据表
     * @param leftCondition  主表参与比较的属性
     * @param rightCondition 数据表参与比较的属性
     * @param action         主表数据关联操作
     * @param <L>            主表泛型
     * @param <R>            数据表泛型
     * @param <LE>           主表参与比较的属性泛型
     * @param <RE>           数据表参与比较的属性泛型
     */
    public static <L, R, LE, RE> void join(List<L> leftData, List<R> rightData,
                                           Function<L, LE> leftCondition,
                                           Function<R, RE> rightCondition,
                                           BiConsumer<L, R> action) {
        if (CollectionUtils.isNotEmpty(leftData) && CollectionUtils.isNotEmpty(rightData)) {
            for (L l : leftData) {
                LE le = leftCondition.apply(l);
                if (Objects.nonNull(le)) {
                    for (R r : rightData) {
                        RE re = rightCondition.apply(r);
                        if (Objects.nonNull(re) && le.equals(re)) {
                            action.accept(l, r);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * List关联
     * example:
     * join(lefts, rights, (l, r) -> l.getRightId().intValue() == r.getId(), Left::setRight);
     * <p>
     *
     * @param leftData  主表
     * @param rightData 数据表
     * @param condition 对比提条件
     * @param action    关联操作
     * @param <L>       主表泛型
     * @param <R>       数据表泛型
     */
    public static <L, R> void join(List<L> leftData, List<R> rightData,
                                   BiPredicate<L, R> condition,
                                   BiConsumer<L, R> action) {
        if (CollectionUtils.isNotEmpty(leftData) && CollectionUtils.isNotEmpty(rightData)) {
            for (L l : leftData) {
                for (R r : rightData) {
                    if (Objects.nonNull(l) && Objects.nonNull(r)) {
                        if (condition.test(l, r)) {
                            action.accept(l, r);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * List关联
     * example:
     * <p>
     * join(leftList, rightList, (l, r) -> {
     * ----if (l.getRightId().intValue() == r.getId()) {
     * --------l.setRight(r);
     * ----}
     * });
     */
    public static <L, R> void join(List<L> leftData, List<R> rightData,
                                   BiConsumer<L, R> action) {
        if (CollectionUtils.isNotEmpty(leftData) && CollectionUtils.isNotEmpty(rightData)) {
            for (L l : leftData) {
                for (R r : rightData) {
                    if (Objects.nonNull(l) && Objects.nonNull(r)) {
                        action.accept(l, r);
                    }
                }
            }
        }
    }

    /**
     * 获取集合中 命中src 的第一条记录的值
     * example
     * <p>
     * leftList.forEach(i -> i.setRightName(findFirst(rightList, i.getRightId(), Right::getId, Right::getName)));
     *
     * @param collection 数据集合
     * @param src        目标值
     * @param tag        获取目标值的方法
     * @param result     命中结果
     */
    public static <R, T, F> R findFirst(Collection<T> collection, Object src, Function<T, F> tag, Function<T, R> result) {
        if (CollectionUtils.isNotEmpty(collection)) {
            for (T t : collection) {
                F f = tag.apply(t);
                if (Objects.nonNull(f)) {
                    if (f.equals(src)) {
                        return result.apply(t);
                    }
                }
            }
        }
        return null;
    }

    public static <T> T findFirst(Collection<T> collection, T src) {
        if (CollectionUtils.isNotEmpty(collection)) {
            for (T t : collection) {
                if (t.equals(src)) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * 获取集合中 命中src 的所有记录的值
     */
    public static <R, T, F> List<R> findSubList(Collection<T> collection, Object src, Function<T, F> tag, Function<T, R> result) {
        List<R> r = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(collection)) {
            for (T t : collection) {
                F f = tag.apply(t);
                if (Objects.nonNull(f)) {
                    if (f.equals(src)) {
                        r.add(result.apply(t));
                    }
                }
            }
        }
        return r;
    }
}
