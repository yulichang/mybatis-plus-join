package com.github.yulichang.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.repository.AbstractRepository;

/**
 * {@link AbstractRepository}
 * {@link JoinRepository}
 *
 * @auther yulichang
 * @since 1.5.2
 */
@SuppressWarnings("unused")
public abstract class JoinAbstractRepository<M extends BaseMapper<T>, T> extends AbstractRepository<M, T> implements JoinRepository<T> {
}
