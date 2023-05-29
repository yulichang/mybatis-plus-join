package com.github.yulichang.kt.resultmap;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.github.yulichang.toolkit.KtUtils;
import com.github.yulichang.toolkit.MPJReflectionKit;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.toolkit.support.FieldCache;
import com.github.yulichang.wrapper.resultmap.IResult;
import com.github.yulichang.wrapper.resultmap.Label;
import com.github.yulichang.wrapper.resultmap.MFunc;
import com.github.yulichang.wrapper.resultmap.ResultList;
import com.github.yulichang.wrapper.segments.SelectCache;
import kotlin.reflect.KProperty;
import lombok.Getter;

import java.util.*;
import java.util.function.Predicate;

/**
 * 无泛型约束 实现自由映射
 *
 * @author yulichang
 * @since 1.4.6
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

    @SuppressWarnings({"unused", "DuplicatedCode"})
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

        public Builder<T> all(Class<?> entityClass) {
            allBuild(null, entityClass);
            return this;
        }

        public Builder<T> all(String prefix, Class<?> entityClass) {
            allBuild(prefix, entityClass);
            return this;
        }

        /**
         * 映射实体字段过滤(含主键)
         */
        public Builder<T> filter(Class<?> entityClass, Predicate<SelectCache> predicate) {
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(mybatisLabel.ofType);
            ColumnCache.getListField(entityClass).stream().filter(predicate)
                    .filter(p -> fieldMap.containsKey(p.getColumProperty())).forEach(c ->
                            mybatisLabel.resultList.add(new Result.Builder<T>(false, null, c).build()));
            return this;
        }

        /**
         * 映射实体字段过滤(含主键)
         */
        public Builder<T> filter(String prefix, Class<?> entityClass, Predicate<SelectCache> predicate) {
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(mybatisLabel.ofType);
            ColumnCache.getListField(entityClass).stream().filter(predicate)
                    .filter(p -> fieldMap.containsKey(p.getColumProperty())).forEach(c ->
                            mybatisLabel.resultList.add(new Result.Builder<T>(false, prefix, c).build()));
            return this;
        }

        public Builder<T> id(KProperty<?> entity, KProperty<?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(true, null);
            builder.column(entity).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<T> id(KProperty<?> entity) {
            Result.Builder<T> builder = new Result.Builder<>(true, null);
            builder.column(entity);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<T> id(String index, KProperty<?> entity, KProperty<?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(true, index);
            builder.column(entity).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<T> id(String index, KProperty<?> entity) {
            Result.Builder<T> builder = new Result.Builder<>(true, index);
            builder.column(entity);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<T> result(KProperty<?> entity, KProperty<?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(false, null);
            builder.column(entity).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<T> result(KProperty<?> entity) {
            Result.Builder<T> builder = new Result.Builder<>(false, null);
            builder.column(entity);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<T> result(String index, KProperty<?> entity, KProperty<?> tag) {
            Result.Builder<T> builder = new Result.Builder<>(false, index);
            builder.column(entity).property(tag);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<T> result(String index, KProperty<?> entity) {
            Result.Builder<T> builder = new Result.Builder<>(false, index);
            builder.column(entity);
            mybatisLabel.resultList.add(builder.build());
            return this;
        }

        public Builder<T> collection(Class<?> entityClass, KProperty<?> func) {
            return collection(null, entityClass, func);
        }

        /**
         * 嵌套
         */
        public Builder<T> collection(String prefix, Class<?> entityClass, KProperty<?> func) {
            String dtoFieldName = func.getName();
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(KtUtils.ref(func));
            FieldCache field = fieldMap.get(dtoFieldName);
            Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
            MybatisLabel.Builder<?, ?> builder;
            if (genericType == null || genericType.isAssignableFrom(entityClass)) {
                //找不到集合泛型 List List<?> List<Object> ， 直接查询数据库实体
                builder = new MybatisLabel.Builder<>(prefix, dtoFieldName, entityClass, field.getType());
            } else {
                builder = new MybatisLabel.Builder<>(prefix, dtoFieldName, entityClass, field.getType(), genericType, true);
            }
            mybatisLabel.mybatisLabels.add(builder.build());
            return this;
        }

        public Builder<T> collection(Class<?> entityClass, KProperty<?> func, MFunc<MybatisLabel.Builder<?, ?>> mFunc) {
            return collection(null, entityClass, func, mFunc);
        }

        public Builder<T> collection(KProperty<?> func, MFunc<Builder<?>> mFunc) {
            String dtoFieldName = func.getName();
            FieldCache field = MPJReflectionKit.getFieldMap(KtUtils.ref(func)).get(dtoFieldName);
            //获取集合泛型
            Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
            Builder<?> builder = new Builder<>(dtoFieldName, field.getType(), genericType);
            mybatisLabel.mybatisLabels.add(mFunc.apply(builder).build());
            return this;
        }

        /**
         * 嵌套
         */
        public Builder<T> collection(String prefix,
                                     Class<?> entityClass,
                                     KProperty<?> func,
                                     MFunc<MybatisLabel.Builder<?, ?>> mFunc) {
            String dtoFieldName = func.getName();
            FieldCache field = MPJReflectionKit.getFieldMap(KtUtils.ref(func)).get(dtoFieldName);
            //获取集合泛型
            Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
            MybatisLabel.Builder<?, ?> builder = new MybatisLabel.Builder<>(prefix, dtoFieldName, entityClass, field.getType(), genericType, false);
            mybatisLabel.mybatisLabels.add(mFunc.apply(builder).build());
            return this;
        }

        public Builder<T> association(Class<?> child, KProperty<?> dtoField) {
            return association(null, child, dtoField);
        }

        /**
         * 嵌套
         */
        public Builder<T> association(String index, Class<?> child, KProperty<?> dtoField) {
            Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(KtUtils.ref(dtoField));
            String dtoFieldName = dtoField.getName();
            FieldCache field = fieldMap.get(dtoFieldName);
            Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
            MybatisLabel.Builder<?, ?> builder;
            builder = new MybatisLabel.Builder<>(index, dtoFieldName, child, field.getType(), (Class<?>) field.getType(), true);
            mybatisLabel.mybatisLabels.add(builder.build());
            return this;
        }

        public Builder<T> association(Class<?> child, KProperty<?> dtoField,
                                      MFunc<MybatisLabel.Builder<?, ?>> collection) {
            return association(null, child, dtoField, collection);
        }

        /**
         * 嵌套
         */
        public Builder<T> association(String index, Class<?> child, KProperty<?> dtoField,
                                      MFunc<MybatisLabel.Builder<?, ?>> collection) {
            String dtoFieldName = dtoField.getName();
            FieldCache field = MPJReflectionKit.getFieldMap(KtUtils.ref(dtoField)).get(dtoFieldName);
            Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
            MybatisLabel.Builder<?, ?> builder = new MybatisLabel.Builder<>(index, dtoFieldName, child, field.getType(), (Class<?>) field.getType(), false);
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
