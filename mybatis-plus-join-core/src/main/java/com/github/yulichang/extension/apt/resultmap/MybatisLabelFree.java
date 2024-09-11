package com.github.yulichang.extension.apt.resultmap;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.extension.apt.matedata.BaseColumn;
import com.github.yulichang.extension.apt.matedata.Column;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.MPJReflectionKit;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.toolkit.support.FieldCache;
import com.github.yulichang.wrapper.interfaces.MFunction;
import com.github.yulichang.wrapper.resultmap.IResult;
import com.github.yulichang.wrapper.resultmap.Label;
import com.github.yulichang.wrapper.resultmap.ResultList;
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

        public <E> Builder<T> all(BaseColumn<E> entityClass) {
            allBuild(entityClass);
            return this;
        }


        /**
         * 映射实体字段过滤(含主键)
         */
        public <E> Builder<T> filter(BaseColumn<E> baseColumn, Predicate<SelectCache> predicate) {
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(mybatisLabel.ofType);
            ColumnCache.getListField(baseColumn.getColumnClass()).stream().filter(predicate)
                    .filter(p -> fieldMap.containsKey(p.getColumProperty())).forEach(c ->
                            mybatisLabel.resultList.add(new Result.Builder<T>(false, baseColumn, c).build()));
            return this;
        }

        public Builder<T> id(Column column, SFunction<T, ?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(true, column);
            builder.column(column).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<T> id(Column column) {
            Result.Builder<T> builder = new Result.Builder<>(true, column);
            builder.column(column);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<T> result(Column column, SFunction<T, ?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(false, column);
            builder.column(column).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<T> result(Column column) {
            Result.Builder<T> builder = new Result.Builder<>(false, column);
            builder.column(column);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, R, B extends Collection<R>> Builder<T> collection(BaseColumn<A> baseColumn, SFunction<T, B> func) {
            String dtoFieldName = LambdaUtils.getName(func);
            Class<T> dtoClass = LambdaUtils.getEntityClass(func);
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(dtoClass);
            FieldCache field = fieldMap.get(dtoFieldName);
            Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
            MybatisLabel.Builder<A, R> builder;
            if (genericType == null || genericType.isAssignableFrom(baseColumn.getColumnClass())) {
                //找不到集合泛型 List List<?> List<Object> ， 直接查询数据库实体
                builder = new MybatisLabel.Builder<>(dtoFieldName, baseColumn, field.getType());
            } else {
                Class<R> ofType = (Class<R>) genericType;
                builder = new MybatisLabel.Builder<>(dtoFieldName, baseColumn, field.getType(), ofType, true);
            }
            mybatisLabel.mybatisLabels.add(builder.build());
            return this;
        }

        public <A, R, B extends Collection<R>> Builder<T> collection(SFunction<T, B> func,
                                                                     MFunction<Builder<R>> mFunc) {
            String dtoFieldName = LambdaUtils.getName(func);
            Class<T> dtoClass = LambdaUtils.getEntityClass(func);
            FieldCache field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
            //获取集合泛型
            Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
            Class<R> ofType = (Class<R>) genericType;
            Builder<R> builder = new Builder<>(dtoFieldName, field.getType(), ofType);
            mybatisLabel.mybatisLabels.add(mFunc.apply(builder).build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, R, B extends Collection<R>> Builder<T> collection(BaseColumn<A> entityClass,
                                                                     SFunction<T, B> func,
                                                                     MFunction<MybatisLabel.Builder<A, R>> mFunc) {
            String dtoFieldName = LambdaUtils.getName(func);
            Class<T> dtoClass = LambdaUtils.getEntityClass(func);
            FieldCache field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
            //获取集合泛型
            Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
            Class<R> ofType = (Class<R>) genericType;
            MybatisLabel.Builder<A, R> builder = new MybatisLabel.Builder<>(dtoFieldName, entityClass, field.getType(), ofType, false);
            mybatisLabel.mybatisLabels.add(mFunc.apply(builder).build());
            return this;
        }


        /**
         * 嵌套
         */
        public <A, B> Builder<T> association(BaseColumn<A> child, SFunction<T, B> dtoField) {
            Class<T> dtoClass = LambdaUtils.getEntityClass(dtoField);
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(dtoClass);
            String dtoFieldName = LambdaUtils.getName(dtoField);
            FieldCache field = fieldMap.get(dtoFieldName);
            Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
            MybatisLabel.Builder<A, B> builder;
            builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), (Class<B>) field.getType(), true);
            mybatisLabel.mybatisLabels.add(builder.build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, B> Builder<T> association(BaseColumn<A> child, SFunction<T, B> dtoField,
                                             MFunction<MybatisLabel.Builder<A, B>> collection) {
            String dtoFieldName = LambdaUtils.getName(dtoField);
            Class<T> dtoClass = LambdaUtils.getEntityClass(dtoField);
            FieldCache field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
            Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
            MybatisLabel.Builder<A, B> builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), (Class<B>) field.getType(), false);
            mybatisLabel.mybatisLabels.add(collection.apply(builder).build());
            return this;
        }

        public MybatisLabelFree<T> build() {
            if (CollectionUtils.isEmpty(mybatisLabel.resultList)) {
                throw ExceptionUtils.mpe("无法自动映射, 找不到 <%s> 对应的表, 请使用 .all(xxx.class), id()或者result() 手动映射",
                        mybatisLabel.ofType.getSimpleName());
            }
            return mybatisLabel;
        }

        private void allBuild(BaseColumn<?> entityClass) {
            Map<String, FieldCache> tagMap = MPJReflectionKit.getFieldMap(mybatisLabel.getOfType());
            List<SelectCache> listField = ColumnCache.getListField(entityClass.getColumnClass());
            for (SelectCache s : listField) {
                FieldCache field = tagMap.get(s.getColumProperty());
                if (Objects.nonNull(field)) {
                    Result result = new Result();
                    result.setBaseColumn(entityClass);
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
