package com.github.yulichang.wrapper.interfaces;

import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.SqlHelper;

/**
 * 链式调用
 * <p>
 * 构造方法必须传 class 或 entity 否则会报错<br />
 * new UpdateJoinWrapper(User.class)<br />
 * JoinWrappers.update(User.class)<br />
 *
 * @author yulichang
 * @since 1.4.10
 */
@SuppressWarnings({"unchecked", "unused"})
public interface UpdateChain<T> {

    Class<T> getEntityClass();

    /**
     * 链式调用
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错 <br />
     * new UpdateJoinWrapper(User.class)<br />
     * JoinWrappers.update(User.class)<br />
     */
    default int update() {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.updateJoin(null, (MPJBaseJoin<T>) this));
    }

    /**
     * 链式调用
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错 <br />
     * new UpdateJoinWrapper(User.class)<br />
     * JoinWrappers.update(User.class)<br />
     */
    default int update(T entity) {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.updateJoin(entity, (MPJBaseJoin<T>) this));
    }

    /**
     * 链式调用
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错 <br />
     * new UpdateJoinWrapper(User.class)<br />
     * JoinWrappers.update(User.class)<br />
     */
    default int updateAndNull() {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.updateJoinAndNull(null, (MPJBaseJoin<T>) this));
    }

    /**
     * 链式调用
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错 <br />
     * new UpdateJoinWrapper(User.class)<br />
     * JoinWrappers.update(User.class)<br />
     */
    default int updateAndNull(T entity) {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.updateJoinAndNull(entity, (MPJBaseJoin<T>) this));
    }
}
