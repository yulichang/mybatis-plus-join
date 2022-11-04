package com.github.yulichang.wrapper.resultmap;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * collection 标签 目前先支持这几个属性 后续在扩展
 *
 * @author yulichang
 * @since 1.2.5
 */
@Getter
public class Collection<E, T> {

    private String property;

    private Class<E> entityClass;

    private Class<?> javaType;

    private Class<T> ofType;

    private List<Result> resultList;

    //collection嵌套
//    private List<Collection> collectionList;

    private Collection() {
    }

    public static class Builder<E, T> {

        private final Collection<E, T> collection;

        /**
         * 自动构建
         */
        @SuppressWarnings("unchecked")
        public Builder(String property, Class<E> entityClass, Class<?> javaType) {
            this.collection = new Collection<>();
            collection.property = property;
            collection.entityClass = entityClass;
            collection.javaType = javaType;
            collection.ofType = (Class<T>) entityClass;
            collection.resultList = new ArrayList<>();
            // TODO 构建entityClass所有的字段
        }

        /**
         * 手动构建
         *
         * @param property    property
         * @param entityClass 数据库实体类
         * @param javaType    javaType
         * @param ofType      映射类
         */
        public Builder(String property, Class<E> entityClass, Class<?> javaType, Class<T> ofType) {
            this.collection = new Collection<>();
            collection.property = property;
            collection.entityClass = entityClass;
            collection.javaType = javaType;
            collection.ofType = ofType;
            collection.resultList = new ArrayList<>();
        }

        public Builder<E, T> id(MFunc<Result.Builder<E, T>> result) {
            Result r = result.apply(new Result.Builder<>(true)).build();
            collection.resultList.add(r);
            return this;
        }

        public Builder<E, T> result(MFunc<Result.Builder<E, T>> result) {
            Result r = result.apply(new Result.Builder<>(false)).build();
            collection.resultList.add(r);
            return this;
        }

        public Collection<E, T> build() {
            return collection;
        }
    }
}
