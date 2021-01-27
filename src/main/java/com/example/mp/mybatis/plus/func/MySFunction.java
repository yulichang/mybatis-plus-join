package com.example.mp.mybatis.plus.func;


import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.example.mp.mybatis.plus.base.MyBaseEntity;

/**
 * 支持别名的 SFunction
 *
 * @author yulichang
 * @see SFunction
 */
@FunctionalInterface
public interface MySFunction<T extends MyBaseEntity, R> extends SFunction<T, R> {

}
