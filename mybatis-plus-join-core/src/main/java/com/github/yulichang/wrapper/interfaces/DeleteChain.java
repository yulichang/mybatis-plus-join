package com.github.yulichang.wrapper.interfaces;

import com.github.yulichang.interfaces.MPJBaseJoin;
import com.github.yulichang.toolkit.SqlHelper;

/**
 * 链式调用
 * <p>
 * 构造方法必须传 class 或 entity 否则会报错<br />
 * new DeleteJoinWrapper(User.class)<br />
 * JoinWrappers.delete(User.class)<br />
 *
 * @author yulichang
 * @since 1.4.10
 */
public interface DeleteChain<T> extends MPJBaseJoin<T> {

    Class<T> getEntityClass();

    /**
     * 链式调用
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错 <br />
     * new DeleteJoinWrapper(User.class)<br />
     * JoinWrappers.delete(User.class)<br />
     */
    @SuppressWarnings("unused")
    default int deleteJoin() {
        return SqlHelper.exec(getEntityClass(), mapper -> mapper.deleteJoin(this));
    }
}
