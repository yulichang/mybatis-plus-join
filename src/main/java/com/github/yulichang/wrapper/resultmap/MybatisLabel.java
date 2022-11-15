package com.github.yulichang.wrapper.resultmap;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.exception.MPJException;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.ReflectionKit;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * collection 标签 目前先支持这几个属性 后续在扩展
 *
 * @author yulichang
 * @since 1.2.5
 */
@Getter
public class MybatisLabel<E, T> {

    private String property;

    private Class<E> entityClass;

    private Class<?> javaType;

    private Class<T> ofType;

    private List<Result> resultList;

    /**
     * wrapper里面的引用
     */
    private List<MybatisLabel<?, ?>> mybatisLabels;

    private MybatisLabel() {
    }

    @SuppressWarnings({"unused", "unchecked"})
    public static class Builder<E, T> {

        private final MybatisLabel<E, T> mybatisLabel;

        /**
         * 自动构建
         */
        @SuppressWarnings("unchecked")
        public Builder(String property, Class<E> entityClass, Class<?> javaType) {
            this.mybatisLabel = new MybatisLabel<>();
            mybatisLabel.property = property;
            mybatisLabel.entityClass = entityClass;
            mybatisLabel.javaType = javaType;
            mybatisLabel.ofType = (Class<T>) entityClass;
            mybatisLabel.resultList = new ArrayList<>();
            mybatisLabel.mybatisLabels = new ArrayList<>();
            autoBuild(true, entityClass, (Class<T>) entityClass);
        }

        /**
         * 手动构建
         *
         * @param property    property
         * @param entityClass 数据库实体类
         * @param javaType    javaType
         * @param ofType      映射类
         * @param auto        自动映射数据库实体对应的字段
         */
        public Builder(String property, Class<E> entityClass, Class<?> javaType, Class<T> ofType, boolean auto) {
            this.mybatisLabel = new MybatisLabel<>();
            mybatisLabel.property = property;
            mybatisLabel.entityClass = entityClass;
            mybatisLabel.javaType = javaType;
            mybatisLabel.ofType = ofType;
            mybatisLabel.resultList = new ArrayList<>();
            mybatisLabel.mybatisLabels = new ArrayList<>();
            autoBuild(auto, entityClass, ofType);
        }

        public Builder<E, T> id(SFunction<E, ?> entity, SFunction<T, ?> tag) {
            Result.Builder<E, T> builder = new Result.Builder<>(true);
            builder.column(entity).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<E, T> id(SFunction<E, ?> entity) {
            Result.Builder<E, T> builder = new Result.Builder<>(true);
            builder.column(entity);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<E, T> result(SFunction<E, ?> entity, SFunction<T, ?> tag) {
            Result.Builder<E, T> builder = new Result.Builder<>(false);
            builder.column(entity).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<E, T> result(SFunction<E, ?> entity) {
            Result.Builder<E, T> builder = new Result.Builder<>(false);
            builder.column(entity);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, R, B extends Collection<R>> Builder<E, T> collection(Class<A> entityClass, SFunction<T, B> func) {
            String dtoFieldName = LambdaUtils.getName(func);
            Class<T> dtoClass = LambdaUtils.getEntityClass(func);
            Map<String, Field> fieldMap = ReflectionKit.getFieldMap(dtoClass);
            Field field = fieldMap.get(dtoFieldName);
            Class<?> genericType = ReflectionKit.getGenericType(field);
            MybatisLabel.Builder<A, R> builder;
            if (genericType == null || genericType.isAssignableFrom(entityClass)) {
                //找不到集合泛型 List List<?> List<Object> ， 直接查询数据库实体
                builder = new Builder<>(dtoFieldName, entityClass, field.getType());
            } else {
                Class<R> ofType = (Class<R>) genericType;
                if (ReflectionKit.isPrimitiveOrWrapper(ofType)) {
                    throw new MPJException("collection 不支持基本数据类型");
                }
                builder = new Builder<>(dtoFieldName, entityClass, field.getType(), ofType, true);
            }
            mybatisLabel.mybatisLabels.add(builder.build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, R, B extends Collection<R>> Builder<E, T> collection(Class<A> entityClass, SFunction<T, B> func, MFunc<Builder<A, R>> mFunc) {
            String dtoFieldName = LambdaUtils.getName(func);
            Class<T> dtoClass = LambdaUtils.getEntityClass(func);
            Field field = ReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
            //获取集合泛型
            Class<?> genericType = ReflectionKit.getGenericType(field);
            Class<R> ofType = (Class<R>) genericType;
            MybatisLabel.Builder<A, R> builder = new MybatisLabel.Builder<>(dtoFieldName, entityClass, field.getType(), ofType, false);
            mybatisLabel.mybatisLabels.add(mFunc.apply(builder).build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, B> Builder<E, T> association(Class<A> child, SFunction<T, B> dtoField) {
            Class<T> dtoClass = LambdaUtils.getEntityClass(dtoField);
            Map<String, Field> fieldMap = ReflectionKit.getFieldMap(dtoClass);
            String dtoFieldName = LambdaUtils.getName(dtoField);
            Field field = fieldMap.get(dtoFieldName);
            Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
            if (ReflectionKit.isPrimitiveOrWrapper(field.getType())) {
                throw new MPJException("association 不支持基本数据类型");
            }
            MybatisLabel.Builder<A, B> builder;
            builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), (Class<B>) field.getType(), true);
            mybatisLabel.mybatisLabels.add(builder.build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, B> Builder<E, T> selectAssociation(Class<A> child, SFunction<T, B> dtoField,
                                                      MFunc<MybatisLabel.Builder<A, B>> collection) {
            String dtoFieldName = LambdaUtils.getName(dtoField);
            Class<T> dtoClass = LambdaUtils.getEntityClass(dtoField);
            Field field = ReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
            Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
            if (ReflectionKit.isPrimitiveOrWrapper(field.getType())) {
                throw new MPJException("association 不支持基本数据类型");
            }
            MybatisLabel.Builder<A, B> builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), (Class<B>) child, false);
            mybatisLabel.mybatisLabels.add(collection.apply(builder).build());
            return this;
        }


        public MybatisLabel<E, T> build() {
            if (CollectionUtils.isEmpty(mybatisLabel.resultList)) {
                autoBuild(true, mybatisLabel.entityClass, mybatisLabel.ofType);
            }
            return mybatisLabel;
        }

        private void autoBuild(boolean auto, Class<E> entityClass, Class<T> tagClass) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
            Map<String, Field> tagMap = ReflectionKit.getFieldMap(tagClass);
            if (auto && !tagMap.isEmpty()) {
                Function<TableFieldInfo, Result> build = field -> {
                    Result result = new Result();
                    result.setId(false);
                    result.setTableFieldInfo(field);
                    result.setColumn(field.getColumn());
                    result.setProperty(field.getProperty());
                    result.setJavaType(field.getField().getType());
                    result.setJdbcType(field.getJdbcType());
                    result.setTypeHandle(field.getTypeHandler());
                    return result;
                };
                if (entityClass == tagClass) {
                    if (tableInfo.havePK()) {
                        mybatisLabel.resultList.add(pkBuild(tableInfo));
                    }
                    mybatisLabel.resultList.addAll(tableInfo.getFieldList().stream().map(build).collect(Collectors.toList()));
                } else {
                    if (tableInfo.havePK() && tagMap.containsKey(tableInfo.getKeyProperty())) {
                        mybatisLabel.resultList.add(pkBuild(tableInfo));
                    }
                    mybatisLabel.resultList.addAll(tableInfo.getFieldList().stream().filter(i ->
                            tagMap.containsKey(i.getProperty())).map(build).collect(Collectors.toList()));
                }
            }
        }

        private Result pkBuild(TableInfo tableInfo) {
            Result result = new Result();
            result.setId(true);
            result.setColumn(tableInfo.getKeyColumn());
            result.setProperty(tableInfo.getKeyProperty());
            result.setJavaType(tableInfo.getKeyType());
            return result;
        }
    }
}