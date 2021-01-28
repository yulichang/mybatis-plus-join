package com.github.mybatisplus.func;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.mybatisplus.base.MyBaseEntity;

/**
 * 支持别名的 SFunction
 *
 * @author yulichang
 * @see SFunction
 */
@FunctionalInterface
public interface MySFunction<T extends MyBaseEntity, R> extends SFunction<T, R> {

}
