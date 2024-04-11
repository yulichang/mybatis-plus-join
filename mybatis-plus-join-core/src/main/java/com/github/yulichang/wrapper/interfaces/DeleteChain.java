package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.github.yulichang.base.MPJBaseMapper;
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
public interface DeleteChain<T> {

    Class<T> getEntityClass();

    /**
     * 链式调用
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错 <br />
     * new DeleteJoinWrapper(User.class)<br />
     * JoinWrappers.delete(User.class)<br />
     */
    @SuppressWarnings({"unused", "unchecked"})
    default int deleteJoin() {
        return SqlHelper.exec(getEntityClass(), mapper -> {
            Assert.isTrue(mapper instanceof MPJBaseMapper, "mapper <%s> is not extends MPJBaseMapper", mapper.getClass().getSimpleName());
            return ((MPJBaseMapper<T>) mapper).deleteJoin((MPJBaseJoin<T>) this);
        });
    }
}
