package com.github.yulichang.base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.relation.Relation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public interface MPJRelationMapper<T> extends BaseMapper<T> {

    /**
     * 通过注解实现单表多次查询
     *
     * @param function BaseMapper调用方法
     * @param deep     是否深度查询
     * @param prop     属性过滤, 可以只查询需要映射的属性
     * @see com.github.yulichang.annotation.EntityMapping
     * @see com.github.yulichang.annotation.FieldMapping
     */
    @SuppressWarnings("unchecked")
    default <R, M extends BaseMapper<T>> R selectRelation(Function<M, R> function, boolean deep, SFunction<T, ?>... prop) {
        R r = function.apply((M) this);
        if (Objects.isNull(r)) {
            return null;
        }
        if (r instanceof List) {
            List<T> data = (List<T>) r;
            if (CollectionUtils.isEmpty(data)) {
                return r;
            } else {
                T t = data.get(0);
                if (Map.class.isAssignableFrom(t.getClass())) {
                    throw ExceptionUtils.mpe("暂不支持Map类型映射");
                }
                if (Object.class == t.getClass()) {
                    return r;
                }
                return (R) Relation.list(data, Arrays.asList(prop), deep);
            }
        }
        if (r instanceof IPage) {
            IPage<T> data = (IPage<T>) r;
            if (!CollectionUtils.isEmpty(data.getRecords())) {
                T t = data.getRecords().get(0);
                if (Map.class.isAssignableFrom(t.getClass())) {
                    throw ExceptionUtils.mpe("暂不支持Map类型映射");
                }
                if (Object.class == t.getClass()) {
                    return r;
                }
                Relation.list(data.getRecords(), Arrays.asList(prop), deep);
            }
            return r;
        }
        if (r instanceof Integer) {
            return r;
        }
        if (r instanceof Long) {
            return r;
        }
        if (r instanceof Boolean) {
            return r;
        }
        if (Object.class == r.getClass()) {
            return r;
        }
        T data = (T) r;
        return (R) Relation.one(data, Arrays.asList(prop), deep);
    }
}
