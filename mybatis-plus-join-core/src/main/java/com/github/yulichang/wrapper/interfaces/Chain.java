package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.SqlHelper;

import java.util.List;

/**
 * 链式调用
 * <p>
 * 构造方法必须传 class 或 entity 否则会报错<br />
 * new MPJLambdaWrapper(User.class)<br />
 * JoinWrappers.lambda(User.class)<br />
 *
 * @author yulichang
 * @since 1.4.4
 */
@SuppressWarnings({"unchecked", "unused"})
public interface Chain<T> {

    Class<T> getEntityClass();

    /**
     * 链式调用 等效于MP mapper的 selectCount()
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错 <br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default Long count() {
        return SqlHelper.exec(getEntityClass(), mapper -> {
            Assert.isTrue(mapper instanceof MPJBaseMapper, "mapper <%s> is not extends MPJBaseMapper", mapper.getClass().getSimpleName());
            return ((MPJBaseMapper<T>) mapper).selectJoinCount((MPJBaseJoin<T>) this);
        });
    }

    /**
     * 链式调用 等效于 selectOne
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default T one() {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectOne((Wrapper<T>) this));
    }

    /**
     * 链式调用 等效于 selectJoinOne
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default <R> R one(Class<R> resultType) {
        return SqlHelper.execJoin(getEntityClass(), mapper -> mapper.selectJoinOne(resultType, (MPJBaseJoin<T>) this));
    }

    /**
     * 链式调用 查询列表第一个 匹配多个不会抛异常
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default T first() {
        List<T> list = list();
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    /**
     * 链式调用 查询列表第一个 匹配多个不会抛异常
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default <R> R first(Class<R> resultType) {
        List<R> list = list(resultType);
        return CollectionUtils.isEmpty(list) ? null : list.get(0);
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default List<T> list() {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectList((Wrapper<T>) this));
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default <R> List<R> list(Class<R> resultType) {
        return SqlHelper.execJoin(getEntityClass(), mapper -> mapper.selectJoinList(resultType, (MPJBaseJoin<T>) this));
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default <P extends IPage<T>> P page(P page) {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectPage(page, (Wrapper<T>) this));
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default <R, P extends IPage<R>> P page(P page, Class<R> resultType) {
        return SqlHelper.execJoin(getEntityClass(), mapper -> mapper.selectJoinPage(page, resultType, (MPJBaseJoin<T>) this));
    }
}
