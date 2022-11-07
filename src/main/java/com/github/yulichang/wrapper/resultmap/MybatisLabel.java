package com.github.yulichang.wrapper.resultmap;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.ReflectionKit;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
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

    private LabelType labelType;

    private String property;

    private Class<E> entityClass;

    private Class<?> javaType;

    private Class<T> ofType;

    private List<Result> resultList;

    //collection嵌套
    //    private List<Collection> collectionList;

    private MybatisLabel() {
    }

    @SuppressWarnings("unused")
    public static class Builder<E, T> {

        private final MybatisLabel<E, T> mybatisLabel;

        /**
         * 自动构建
         */
        @SuppressWarnings("unchecked")
        public Builder(LabelType labelType, String property, Class<E> entityClass, Class<?> javaType) {
            this.mybatisLabel = new MybatisLabel<>();
            mybatisLabel.labelType = labelType;
            mybatisLabel.property = property;
            mybatisLabel.entityClass = entityClass;
            mybatisLabel.javaType = javaType;
            mybatisLabel.ofType = (Class<T>) entityClass;
            mybatisLabel.resultList = new ArrayList<>();
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
        public Builder(LabelType labelType, String property, Class<E> entityClass, Class<?> javaType, Class<T> ofType, boolean auto) {
            this.mybatisLabel = new MybatisLabel<>();
            mybatisLabel.labelType = labelType;
            mybatisLabel.property = property;
            mybatisLabel.entityClass = entityClass;
            mybatisLabel.javaType = javaType;
            mybatisLabel.ofType = ofType;
            mybatisLabel.resultList = new ArrayList<>();
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

        public MybatisLabel<E, T> build() {
            return mybatisLabel;
        }

        private void autoBuild(boolean auto, Class<E> entityClass, Class<T> tagClass) {
            TableInfo tableInfo = TableInfoHelper.getTableInfo(entityClass);
            Map<String, Field> tagMap = ReflectionKit.getFieldMap(tagClass);
            if (auto && !tagMap.isEmpty()) {
                Function<TableFieldInfo, Result> build = field -> {
                    Result result = new Result();
                    result.setId(false);
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
