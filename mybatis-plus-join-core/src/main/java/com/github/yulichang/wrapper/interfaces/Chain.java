package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.SqlHelper;

import java.util.List;
import java.util.Optional;

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
@SuppressWarnings("unused")
public interface Chain<T> extends MPJBaseJoin<T> {

    Class<T> getEntityClass();

    boolean isResultMapCollection();

    /**
     * 链式调用 等效于MP mapper的 selectCount()
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错 <br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default Long count() {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectJoinCount(this));
    }

    /**
     * 链式调用 等效于 selectOne
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default T one() {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectJoinOne(getEntityClass(), this));
    }

    /**
     * 链式调用 等效于 selectJoinOne
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default <R> R one(Class<R> resultType) {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectJoinOne(resultType, this));
    }

    /**
     * 链式调用 查询列表第一个 匹配多个不会抛异常
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default T first() {
        List<T> list = this.isResultMapCollection() ? list() :
                page(new Page<T>(1, 1).setSearchCount(false)).getRecords();
        return Optional.of(list).filter(CollectionUtils::isNotEmpty).map(m -> m.get(0)).orElse(null);
    }

    /**
     * 链式调用 查询列表第一个 匹配多个不会抛异常
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default <R> R first(Class<R> resultType) {
        List<R> list = this.isResultMapCollection() ? list(resultType) :
                page(new Page<R>(1, 1).setSearchCount(false), resultType).getRecords();
        return Optional.of(list).filter(CollectionUtils::isNotEmpty).map(m -> m.get(0)).orElse(null);
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default List<T> list() {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectJoinList(getEntityClass(), this));
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default <R> List<R> list(Class<R> resultType) {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectJoinList(resultType, this));
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default <P extends IPage<T>> P page(P page) {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectJoinPage(page, getEntityClass(), this));
    }

    /**
     * 链式调用
     * 构造方法必须传 class 或 entity 否则会报错<br />
     * new MPJLambdaWrapper(User.class)<br />
     * JoinWrappers.lambda(User.class)<br />
     */
    default <R, P extends IPage<R>> P page(P page, Class<R> resultType) {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.selectJoinPage(page, resultType, this));
    }
}
