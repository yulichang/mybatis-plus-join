package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.SqlHelper;

import java.util.List;

/**
 * 链式调用
 * 构造方法必须传 class 或 entity 否则会报错
 * new MPJLambdaWrapper(User.class)
 * new MPJQueryWrapper(User.class)
 * JoinWrappers.\<User\>lambdaJoin(User.class)
 * JoinWrappers.\<User\>queryJoin(User.class)
 *
 * @author yulichang
 * @since 1.4.4
 */
@SuppressWarnings({"unchecked", "unused"})
public interface Chain<T> {

    Class<T> getEntityClass();

    /**
     * 链式调用 等效于 selectOne
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错
     * new MPJLambdaWrapper(User.class)
     * new MPJQueryWrapper(User.class)
     * JoinWrappers.\<User\>lambdaJoin(User.class)
     * JoinWrappers.\<User\>queryJoin(User.class)
     */
    default T one() {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectOne((Wrapper<T>) this));
    }

    /**
     * 链式调用 等效于 selectJoinOne
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错
     * new MPJLambdaWrapper(User.class)
     * new MPJQueryWrapper(User.class)
     * JoinWrappers.\<User\>lambdaJoin(User.class)
     * JoinWrappers.\<User\>queryJoin(User.class)
     */
    default <R> R one(Class<R> resultType) {
        return SqlHelper.execJoin(getEntityClass(), mapper -> mapper.selectJoinOne(resultType, (MPJBaseJoin<T>) this));
    }

    /**
     * 链式调用 查询列表第一个 匹配多个不会抛异常
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错
     * new MPJLambdaWrapper(User.class)
     * new MPJQueryWrapper(User.class)
     * JoinWrappers.\<User\>lambdaJoin(User.class)
     * JoinWrappers.\<User\>queryJoin(User.class)
     */
    default T first() {
        List<T> list = list();
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    /**
     * 链式调用 查询列表第一个 匹配多个不会抛异常
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错
     * new MPJLambdaWrapper(User.class)
     * new MPJQueryWrapper(User.class)
     * JoinWrappers.\<User\>lambdaJoin(User.class)
     * JoinWrappers.\<User\>queryJoin(User.class)
     */
    default <R> R first(Class<R> resultType) {
        List<R> list = list(resultType);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错
     * new MPJLambdaWrapper(User.class)
     * new MPJQueryWrapper(User.class)
     * JoinWrappers.\<User\>lambdaJoin(User.class)
     * JoinWrappers.\<User\>queryJoin(User.class)
     */
    default List<T> list() {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectList((Wrapper<T>) this));
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错
     * new MPJLambdaWrapper(User.class)
     * new MPJQueryWrapper(User.class)
     * JoinWrappers.\<User\>lambdaJoin(User.class)
     * JoinWrappers.\<User\>queryJoin(User.class)
     */
    default <R> List<R> list(Class<R> resultType) {
        return SqlHelper.execJoin(getEntityClass(), mapper -> mapper.selectJoinList(resultType, (MPJBaseJoin<T>) this));
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错
     * new MPJLambdaWrapper(User.class)
     * new MPJQueryWrapper(User.class)
     * JoinWrappers.\<User\>lambdaJoin(User.class)
     * JoinWrappers.\<User\>queryJoin(User.class)
     */
    default <P extends IPage<T>> P page(P page) {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectPage(page, (Wrapper<T>) this));
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错
     * new MPJLambdaWrapper(User.class)
     * new MPJQueryWrapper(User.class)
     * JoinWrappers.\<User\>lambdaJoin(User.class)
     * JoinWrappers.\<User\>queryJoin(User.class)
     */
    default <R, P extends IPage<R>> P page(P page, Class<R> resultType) {
        return SqlHelper.execJoin(getEntityClass(), mapper -> mapper.selectJoinPage(page, resultType, (MPJBaseJoin<T>) this));
    }
}
