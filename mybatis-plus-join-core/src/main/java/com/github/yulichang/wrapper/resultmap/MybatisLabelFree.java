package com.github.yulichang.wrapper.resultmap;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.MPJReflectionKit;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.toolkit.support.FieldCache;
import com.github.yulichang.wrapper.interfaces.MFunction;
import com.github.yulichang.wrapper.segments.SelectCache;
import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;

/**
 * 无泛型约束 实现自由映射
 *
 * @author yulichang
 * @since 1.4.4
 */
@Getter
public class MybatisLabelFree<T> implements Label<T> {

    private String property;

    private Class<?> javaType;

    private Class<T> ofType;

    private List<IResult> resultList;

    /**
     * wrapper里面的引用
     */
    private List<Label<?>> mybatisLabels;

    private MybatisLabelFree() {
    }

    @SuppressWarnings({"unused", "unchecked", "DuplicatedCode"})
    public static class Builder<T> {

        private final MybatisLabelFree<T> mybatisLabel;

        /**
         * 手动构建
         *
         * @param property property
         * @param javaType javaType
         * @param ofType   映射类
         */
        public Builder(String property, Class<?> javaType, Class<T> ofType) {
            this.mybatisLabel = new MybatisLabelFree<>();
            mybatisLabel.property = property;
            mybatisLabel.javaType = javaType;
            mybatisLabel.ofType = ofType;
            mybatisLabel.resultList = new ResultList();
            mybatisLabel.mybatisLabels = new ArrayList<>();
        }

        public <E> Builder<T> all(Class<E> entityClass) {
            allBuild(null, entityClass);
            return this;
        }

        public <E> Builder<T> all(String prefix, Class<E> entityClass) {
            allBuild(prefix, entityClass);
            return this;
        }

        /**
         * 映射实体字段过滤(含主键)
         */
        public <E> Builder<T> filter(Class<E> entityClass, Predicate<SelectCache> predicate) {
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(mybatisLabel.ofType);
            ColumnCache.getListField(entityClass).stream().filter(predicate)
                    .filter(p -> fieldMap.containsKey(p.getColumProperty())).forEach(c ->
                            mybatisLabel.resultList.add(new Result.Builder<T>(false, null, c).build()));
            return this;
        }

        /**
         * 映射实体字段过滤(含主键)
         */
        public <E> Builder<T> filter(String prefix, Class<E> entityClass, Predicate<SelectCache> predicate) {
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(mybatisLabel.ofType);
            ColumnCache.getListField(entityClass).stream().filter(predicate)
                    .filter(p -> fieldMap.containsKey(p.getColumProperty())).forEach(c ->
                            mybatisLabel.resultList.add(new Result.Builder<T>(false, prefix, c).build()));
            return this;
        }

        public <E> Builder<T> id(SFunction<E, ?> entity, SFunction<T, ?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(true, null);
            builder.column(entity).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public <E> Builder<T> id(SFunction<E, ?> entity) {
            Result.Builder<T> builder = new Result.Builder<>(true, null);
            builder.column(entity);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public <E> Builder<T> id(String index, SFunction<E, ?> entity, SFunction<T, ?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(true, index);
            builder.column(entity).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public <E> Builder<T> id(String index, SFunction<E, ?> entity) {
            Result.Builder<T> builder = new Result.Builder<>(true, index);
            builder.column(entity);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public <E> Builder<T> result(SFunction<E, ?> entity, SFunction<T, ?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(false, null);
            builder.column(entity).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public <E> Builder<T> result(SFunction<E, ?> entity) {
            Result.Builder<T> builder = new Result.Builder<>(false, null);
            builder.column(entity);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public <E> Builder<T> result(String index, SFunction<E, ?> entity, SFunction<T, ?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(false, index);
            builder.column(entity).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public <E> Builder<T> result(String index, SFunction<E, ?> entity) {
            Result.Builder<T> builder = new Result.Builder<>(false, index);
            builder.column(entity);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public <A, R, B extends Collection<R>> Builder<T> collection(Class<A> entityClass, SFunction<T, B> func) {
            return collection(null, entityClass, func);
        }

        /**
         * 嵌套
         */
        public <A, R, B extends Collection<R>> Builder<T> collection(String prefix, Class<A> entityClass, SFunction<T, B> func) {
            String dtoFieldName = LambdaUtils.getName(func);
            Class<T> dtoClass = LambdaUtils.getEntityClass(func);
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(dtoClass);
            FieldCache field = fieldMap.get(dtoFieldName);
            Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
            MybatisLabel.Builder<A, R> builder;
            if (genericType == null || genericType.isAssignableFrom(entityClass)) {
                //找不到集合泛型 List List<?> List<Object> ， 直接查询数据库实体
                builder = new MybatisLabel.Builder<>(prefix, dtoFieldName, entityClass, field.getType());
            } else {
                Class<R> ofType = (Class<R>) genericType;
                builder = new MybatisLabel.Builder<>(prefix, dtoFieldName, entityClass, field.getType(), ofType, true);
            }
            mybatisLabel.mybatisLabels.add(builder.build());
            return this;
        }

        public <A, R, B extends Collection<R>> Builder<T> collection(Class<A> entityClass, SFunction<T, B> func, MFunction<MybatisLabel.Builder<A, R>> mFunc) {
            return collection(null, entityClass, func, mFunc);
        }

        public <A, R, B extends Collection<R>> Builder<T> collection(SFunction<T, B> func,
                                                                     MFunction<MybatisLabelFree.Builder<R>> mFunc) {
            String dtoFieldName = LambdaUtils.getName(func);
            Class<T> dtoClass = LambdaUtils.getEntityClass(func);
            FieldCache field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
            //获取集合泛型
            Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
            Class<R> ofType = (Class<R>) genericType;
            MybatisLabelFree.Builder<R> builder = new MybatisLabelFree.Builder<>(dtoFieldName, field.getType(), ofType);
            mybatisLabel.mybatisLabels.add(mFunc.apply(builder).build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, R, B extends Collection<R>> Builder<T> collection(String prefix,
                                                                     Class<A> entityClass,
                                                                     SFunction<T, B> func,
                                                                     MFunction<MybatisLabel.Builder<A, R>> mFunc) {
            String dtoFieldName = LambdaUtils.getName(func);
            Class<T> dtoClass = LambdaUtils.getEntityClass(func);
            FieldCache field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
            //获取集合泛型
            Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
            Class<R> ofType = (Class<R>) genericType;
            MybatisLabel.Builder<A, R> builder = new MybatisLabel.Builder<>(prefix, dtoFieldName, entityClass, field.getType(), ofType, false);
            mybatisLabel.mybatisLabels.add(mFunc.apply(builder).build());
            return this;
        }

        public <A, B> Builder<T> association(Class<A> child, SFunction<T, B> dtoField) {
            return association(null, child, dtoField);
        }

        /**
         * 嵌套
         */
        public <A, B> Builder<T> association(String index, Class<A> child, SFunction<T, B> dtoField) {
            Class<T> dtoClass = LambdaUtils.getEntityClass(dtoField);
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(dtoClass);
            String dtoFieldName = LambdaUtils.getName(dtoField);
            FieldCache field = fieldMap.get(dtoFieldName);
            Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
            MybatisLabel.Builder<A, B> builder;
            builder = new MybatisLabel.Builder<>(index, dtoFieldName, child, field.getType(), (Class<B>) field.getType(), true);
            mybatisLabel.mybatisLabels.add(builder.build());
            return this;
        }

        public <A, B> Builder<T> association(Class<A> child, SFunction<T, B> dtoField,
                                             MFunction<MybatisLabel.Builder<A, B>> collection) {
            return association(null, child, dtoField, collection);
        }

        /**
         * 嵌套
         */
        public <A, B> Builder<T> association(String index, Class<A> child, SFunction<T, B> dtoField,
                                             MFunction<MybatisLabel.Builder<A, B>> collection) {
            String dtoFieldName = LambdaUtils.getName(dtoField);
            Class<T> dtoClass = LambdaUtils.getEntityClass(dtoField);
            FieldCache field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
            Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
            MybatisLabel.Builder<A, B> builder = new MybatisLabel.Builder<>(index, dtoFieldName, child, field.getType(), (Class<B>) field.getType(), false);
            mybatisLabel.mybatisLabels.add(collection.apply(builder).build());
            return this;
        }

        public MybatisLabelFree<T> build() {
            if (CollectionUtils.isEmpty(mybatisLabel.resultList)) {
                TableInfo tableInfo = TableHelper.get(mybatisLabel.ofType);
                Assert.notNull(tableInfo,
                        "无法自动映射, 找不到 <%s> 对应的表, 请使用 .all(xxx.class), id()或者result() 手动映射",
                        mybatisLabel.ofType.getSimpleName());
                all(mybatisLabel.ofType);
            }
            return mybatisLabel;
        }

        private void allBuild(String prefix, Class<?> entityClass) {
            Map<String, FieldCache> tagMap = MPJReflectionKit.getFieldMap(mybatisLabel.getOfType());
            List<SelectCache> listField = ColumnCache.getListField(entityClass);
            for (SelectCache s : listField) {
                FieldCache field = tagMap.get(s.getColumProperty());
                if (Objects.nonNull(field)) {
                    Result result = new Result();
                    result.setIndex(prefix);
                    result.setId(s.isPk());
                    result.setJavaType(field.getType());
                    result.setProperty(s.getColumProperty());
                    result.setSelectNormal(s);
                    mybatisLabel.resultList.add(result);
                }
            }
        }
    }
}
