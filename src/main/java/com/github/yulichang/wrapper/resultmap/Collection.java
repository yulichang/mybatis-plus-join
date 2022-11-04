package com.github.yulichang.wrapper.resultmap;

import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
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
            this.collection = new Collection<>();
            collection.property = property;
            collection.entityClass = entityClass;
            collection.javaType = javaType;
            collection.ofType = ofType;
            collection.resultList = new ArrayList<>();
            autoBuild(auto, entityClass, ofType);
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
                        collection.resultList.add(pkBuild(tableInfo));
                    }
                    collection.resultList.addAll(tableInfo.getFieldList().stream().map(build).collect(Collectors.toList()));
                } else {
                    if (tableInfo.havePK() && tagMap.containsKey(tableInfo.getKeyProperty())) {
                        collection.resultList.add(pkBuild(tableInfo));
                    } else {
                        collection.resultList.addAll(tableInfo.getFieldList().stream().filter(i ->
                                tagMap.containsKey(i.getProperty())).map(build).collect(Collectors.toList()));
                    }
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
