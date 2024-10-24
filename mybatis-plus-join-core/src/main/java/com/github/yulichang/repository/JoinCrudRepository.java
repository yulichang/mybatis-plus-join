package com.github.yulichang.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.repository.CrudRepository;

/**
 * {@link CrudRepository}
 * {@link JoinRepository}
 *
 * @auther yulichang
 * @since 1.5.2
 */
@SuppressWarnings("unused")
public abstract class JoinCrudRepository<M extends BaseMapper<T>, T> extends CrudRepository<M, T> implements JoinRepository<T> {
}
