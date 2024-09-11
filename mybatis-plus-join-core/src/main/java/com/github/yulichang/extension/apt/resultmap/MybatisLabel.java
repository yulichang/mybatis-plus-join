package com.github.yulichang.extension.apt.resultmap;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.extension.apt.matedata.BaseColumn;
import com.github.yulichang.extension.apt.matedata.Column;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.MPJReflectionKit;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.toolkit.support.FieldCache;
import com.github.yulichang.wrapper.interfaces.MFunction;
import com.github.yulichang.wrapper.resultmap.IResult;
import com.github.yulichang.wrapper.resultmap.Label;
import com.github.yulichang.wrapper.segments.SelectCache;
import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * collection 标签 目前先支持这几个属性 后续在扩展
 *
 * @author yulichang
 * @since 1.3.0
 */
@Getter
public class MybatisLabel<E, T> implements Label<T> {

    private String property;

    private BaseColumn<E> baseColumn;

    private Class<?> javaType;

    private Class<T> ofType;

    private List<IResult> resultList;

    /**
     * wrapper里面的引用
     */
    private List<Label<?>> mybatisLabels;

    private MybatisLabel() {
    }

    @SuppressWarnings({"unused", "unchecked", "DuplicatedCode"})
    public static class Builder<E, T> {

        private final MybatisLabel<E, T> mybatisLabel;

        /**
         * 自动构建
         */
        public Builder(String property, BaseColumn<E> baseColumn, Class<?> javaType) {
            this.mybatisLabel = new MybatisLabel<>();
            mybatisLabel.property = property;
            mybatisLabel.baseColumn = baseColumn;
            mybatisLabel.javaType = javaType;
            mybatisLabel.ofType = (Class<T>) baseColumn.getColumnClass();
            mybatisLabel.resultList = new ArrayList<>();
            mybatisLabel.mybatisLabels = new ArrayList<>();
            autoBuild(true, baseColumn.getColumnClass(), (Class<T>) baseColumn.getColumnClass());
        }

        /**
         * 手动构建
         *
         * @param property   property
         * @param baseColumn 数据库实体类
         * @param javaType   javaType
         * @param ofType     映射类
         * @param auto       自动映射数据库实体对应的字段
         */
        public Builder(String property, BaseColumn<E> baseColumn, Class<?> javaType, Class<T> ofType, boolean auto) {
            this.mybatisLabel = new MybatisLabel<>();
            mybatisLabel.property = property;
            mybatisLabel.baseColumn = baseColumn;
            mybatisLabel.javaType = javaType;
            mybatisLabel.ofType = ofType;
            mybatisLabel.resultList = new ArrayList<>();
            mybatisLabel.mybatisLabels = new ArrayList<>();
            autoBuild(auto, baseColumn.getColumnClass(), ofType);
        }

        /**
         * 映射实体全部字段
         */
        public Builder<E, T> all() {
            autoBuild(true, mybatisLabel.baseColumn.getColumnClass(), mybatisLabel.ofType);
            return this;
        }

        /**
         * 映射实体字段过滤(含主键)
         */
        public Builder<E, T> filter(Predicate<SelectCache> predicate) {
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(mybatisLabel.ofType);
            ColumnCache.getListField(mybatisLabel.baseColumn.getColumnClass()).stream().filter(predicate)
                    .filter(p -> fieldMap.containsKey(p.getColumProperty())).forEach(c ->
                            mybatisLabel.resultList.add(new Result.Builder<T>(c.isPk(), mybatisLabel.baseColumn, c).build()));
            return this;
        }

        public Builder<E, T> id(Column column, SFunction<T, ?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(true, column);
            builder.column(column).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<E, T> id(Column column) {
            Result.Builder<T> builder = new Result.Builder<>(true, column);
            builder.column(column);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<E, T> result(Column column, SFunction<T, ?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(false, column);
            builder.column(column).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<E, T> result(Column column) {
            Result.Builder<T> builder = new Result.Builder<>(false, column);
            builder.column(column);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, R, B extends Collection<R>> Builder<E, T> collection(BaseColumn<A> entityClass, SFunction<T, B> func) {
            String dtoFieldName = LambdaUtils.getName(func);
            Class<T> dtoClass = LambdaUtils.getEntityClass(func);
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(dtoClass);
            FieldCache field = fieldMap.get(dtoFieldName);
            Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
            Builder<A, R> builder;
            if (genericType == null || genericType.isAssignableFrom(entityClass.getColumnClass())) {
                //找不到集合泛型 List List<?> List<Object> ， 直接查询数据库实体
                builder = new Builder<>(dtoFieldName, entityClass, field.getType());
            } else {
                Class<R> ofType = (Class<R>) genericType;
                builder = new Builder<>(dtoFieldName, entityClass, field.getType(), ofType, true);
            }
            mybatisLabel.mybatisLabels.add(builder.build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, R, B extends Collection<R>> Builder<E, T> collection(SFunction<T, B> func,
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
        public <A, R, B extends Collection<R>> Builder<E, T> collection(BaseColumn<A> entityClass,
                                                                        SFunction<T, B> func,
                                                                        MFunction<Builder<A, R>> mFunc) {
            String dtoFieldName = LambdaUtils.getName(func);
            Class<T> dtoClass = LambdaUtils.getEntityClass(func);
            FieldCache field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
            //获取集合泛型
            Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
            Class<R> ofType = (Class<R>) genericType;
            Builder<A, R> builder = new Builder<>(dtoFieldName, entityClass, field.getType(), ofType, false);
            mybatisLabel.mybatisLabels.add(mFunc.apply(builder).build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, B> Builder<E, T> association(BaseColumn<A> child, SFunction<T, B> dtoField) {
            Class<T> dtoClass = LambdaUtils.getEntityClass(dtoField);
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(dtoClass);
            String dtoFieldName = LambdaUtils.getName(dtoField);
            FieldCache field = fieldMap.get(dtoFieldName);
            Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
            Builder<A, B> builder;
            builder = new Builder<>(dtoFieldName, child, field.getType(), (Class<B>) field.getType(), true);
            mybatisLabel.mybatisLabels.add(builder.build());
            return this;
        }


        /**
         * 嵌套
         */
        public <A, B> Builder<E, T> association(SFunction<T, B> dtoField,
                                                MFunction<MybatisLabelFree.Builder<B>> collection) {
            String dtoFieldName = LambdaUtils.getName(dtoField);
            Class<T> dtoClass = LambdaUtils.getEntityClass(dtoField);
            FieldCache field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
            Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
            MybatisLabelFree.Builder<B> builder = new MybatisLabelFree.Builder<>(dtoFieldName, field.getType(), (Class<B>) field.getType());
            mybatisLabel.mybatisLabels.add(collection.apply(builder).build());
            return this;
        }

        /**
         * 嵌套
         */
        public <A, B> Builder<E, T> association(BaseColumn<A> child, SFunction<T, B> dtoField,
                                                MFunction<Builder<A, B>> collection) {
            String dtoFieldName = LambdaUtils.getName(dtoField);
            Class<T> dtoClass = LambdaUtils.getEntityClass(dtoField);
            FieldCache field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
            Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
            Builder<A, B> builder = new Builder<>(dtoFieldName, child, field.getType(), (Class<B>) field.getType(), false);
            mybatisLabel.mybatisLabels.add(collection.apply(builder).build());
            return this;
        }

        public MybatisLabel<E, T> build() {
            if (CollectionUtils.isEmpty(mybatisLabel.resultList)) {
                autoBuild(true, mybatisLabel.baseColumn.getColumnClass(), mybatisLabel.ofType);
            }
            return mybatisLabel;
        }

        private void autoBuild(boolean auto, Class<E> entityClass, Class<T> tagClass) {
            TableInfo tableInfo = TableHelper.getAssert(entityClass);
            Map<String, FieldCache> tagMap = MPJReflectionKit.getFieldMap(tagClass);
            if (auto && !tagMap.isEmpty()) {
                List<SelectCache> listField = ColumnCache.getListField(entityClass);
                if (entityClass.isAssignableFrom(tagClass)) {
                    mybatisLabel.resultList.addAll(listField.stream().map(i -> {
                        Result result = new Result();
                        result.setId(i.isPk());
                        result.setBaseColumn(mybatisLabel.baseColumn);
                        result.setProperty(i.getColumProperty());
                        result.setJavaType(i.getColumnType());
                        result.setJdbcType(i.getJdbcType());
                        result.setSelectNormal(i);
                        return result;
                    }).collect(Collectors.toList()));
                } else {
                    for (SelectCache s : listField) {
                        FieldCache field = tagMap.get(s.getColumProperty());
                        if (Objects.nonNull(field)) {
                            Result result = new Result();
                            result.setId(s.isPk());
                            result.setBaseColumn(mybatisLabel.baseColumn);
                            result.setProperty(s.getColumProperty());
                            result.setJavaType(field.getType());
                            result.setSelectNormal(s);
                            mybatisLabel.resultList.add(result);
                        }
                    }
                }
            }
        }
    }
}
