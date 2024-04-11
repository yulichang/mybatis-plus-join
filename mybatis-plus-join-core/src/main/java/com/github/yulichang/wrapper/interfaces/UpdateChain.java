package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.github.yulichang.base.MPJBaseMapper;
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
        return SqlHelper.exec(getEntityClass(), mapper -> {
            Assert.isTrue(mapper instanceof MPJBaseMapper, "mapper <%s> is not extends MPJBaseMapper", mapper.getClass().getSimpleName());
            return ((MPJBaseMapper<T>) mapper).updateJoin(null, (MPJBaseJoin<T>) this);
        });
    }

    /**
     * 链式调用
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错 <br />
     * new UpdateJoinWrapper(User.class)<br />
     * JoinWrappers.update(User.class)<br />
     */
    default int update(T entity) {
        return SqlHelper.exec(getEntityClass(), mapper -> {
            Assert.isTrue(mapper instanceof MPJBaseMapper, "mapper <%s> is not extends MPJBaseMapper", mapper.getClass().getSimpleName());
            return ((MPJBaseMapper<T>) mapper).updateJoin(entity, (MPJBaseJoin<T>) this);
        });
    }

    /**
     * 链式调用
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错 <br />
     * new UpdateJoinWrapper(User.class)<br />
     * JoinWrappers.update(User.class)<br />
     */
    default int updateAndNull() {
        return SqlHelper.exec(getEntityClass(), mapper -> {
            Assert.isTrue(mapper instanceof MPJBaseMapper, "mapper <%s> is not extends MPJBaseMapper", mapper.getClass().getSimpleName());
            return ((MPJBaseMapper<T>) mapper).updateJoinAndNull(null, (MPJBaseJoin<T>) this);
        });
    }

    /**
     * 链式调用
     * <p>
     * 构造方法必须传 class 或 entity 否则会报错 <br />
     * new UpdateJoinWrapper(User.class)<br />
     * JoinWrappers.update(User.class)<br />
     */
    default int updateAndNull(T entity) {
        return SqlHelper.exec(getEntityClass(), mapper -> {
            Assert.isTrue(mapper instanceof MPJBaseMapper, "mapper <%s> is not extends MPJBaseMapper", mapper.getClass().getSimpleName());
            return ((MPJBaseMapper<T>) mapper).updateJoinAndNull(entity, (MPJBaseJoin<T>) this);
        });
    }
}
