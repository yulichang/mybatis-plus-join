package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.support.FieldCache;
import com.github.yulichang.toolkit.support.LambdaMeta;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.github.yulichang.wrapper.interfaces.MConsumer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 填充
 *
 * @auther yulichang
 * @since 1.5.1
 */
@SuppressWarnings({"unchecked", "unused"})
public final class FillUtils {

    public static void fill(Object data, SFunction<?, ?> field, SFunction<?, ?> tagField) {
        fill(data, field, tagField, (MConsumer<MPJLambdaWrapper<?>>) null);
    }

    public static void fill(Object data, SFunction<?, ?> field, SFunction<?, ?> tagField, MConsumer<MPJLambdaWrapper<?>> consumer) {
        LambdaMeta meta = LambdaUtils.getMeta(tagField);
        FieldCache fieldCache = MPJReflectionKit.getFieldMap(meta.getInstantiatedClass()).get(meta.getName());
        fill(data, field, getTagClass(fieldCache.getField()), tagField, consumer);
    }

    public static void fill(Object data, SFunction<?, ?> field, Class<?> oneClass, SFunction<?, ?> tagField) {
        fill(data, field, oneClass, tagField, null);
    }

    public static void fill(Object data, SFunction<?, ?> field, Class<?> oneClass, SFunction<?, ?> tagField, MConsumer<MPJLambdaWrapper<?>> consumer) {
        TableInfo tableInfo = TableHelper.getAssert(oneClass);
        Assert.notNull(tableInfo.getKeyProperty(), "not found key property by class %s", oneClass.getName());
        fill(data, field, new SF(oneClass, tableInfo.getKeyProperty()), tagField, consumer);
    }

    public static void fill(Object data, SFunction<?, ?> field, SFunction<?, ?> oneField, SFunction<?, ?> tagField) {
        fill(data, field, oneField, tagField, null);
    }

    public static void fill(Object data, SFunction<?, ?> field, SFunction<?, ?> oneField, SFunction<?, ?> tagField, MConsumer<MPJLambdaWrapper<?>> consumer) {
        doFill(data, field, oneField, tagField, consumer);
    }

    private static <W> void doFill(Object data, SFunction<?, ?> field, SFunction<?, ?> oneField, SFunction<?, ?> tagField, MConsumer<MPJLambdaWrapper<?>> consumer) {
        if (data == null || field == null || oneField == null || tagField == null) {
            return;
        }
        if (data instanceof IPage) {
            doFill(((IPage<?>) data).getRecords(), field, oneField, tagField, consumer);
            return;
        }
        if (data instanceof Collection) {
            Collection<?> collection = (Collection<?>) data;
            if (collection.isEmpty() || collection.stream().allMatch(Objects::isNull)) {
                return;
            }
        }

        LambdaMeta sourceMeta = LambdaUtils.getMeta(field);
        FieldCache sourceCache = MPJReflectionKit.getFieldMap(sourceMeta.getInstantiatedClass()).get(sourceMeta.getName());

        LambdaMeta tagMeta = LambdaUtils.getMeta(tagField);
        FieldCache tagCache = MPJReflectionKit.getFieldMap(tagMeta.getInstantiatedClass()).get(tagMeta.getName());

        FieldCache oneCache;
        if (oneField instanceof SF) {
            SF sf = (SF) oneField;
            oneCache = MPJReflectionKit.getFieldMap(sf.getClazz()).get(sf.getName());
        } else {
            LambdaMeta oneMeta = LambdaUtils.getMeta(oneField);
            oneCache = MPJReflectionKit.getFieldMap(oneMeta.getInstantiatedClass()).get(oneMeta.getName());
        }

        Class<?> wrapperClass = (oneField instanceof SF) ? ((SF) oneField).clazz : LambdaUtils.getEntityClass(oneField);
        MPJLambdaWrapper<W> wrapper = new MPJLambdaWrapper<W>((Class<W>) wrapperClass) {

            @Override
            public <R> MPJLambdaWrapper<W> in(SFunction<R, ?> column, Collection<?> coll) {
                if (column instanceof SF) {
                    SF sf = (SF) column;
                    TableInfo tableInfo = TableHelper.getAssert(sf.getClazz());
                    return super.in(alias + StringPool.DOT + tableInfo.getKeyColumn(), coll);
                }
                return super.in(column, coll);
            }

            @Override
            public <R> MPJLambdaWrapper<W> eq(SFunction<R, ?> column, Object val) {
                if (column instanceof SF) {
                    SF sf = (SF) column;
                    TableInfo tableInfo = TableHelper.getAssert(sf.getClazz());
                    return super.eq(alias + StringPool.DOT + tableInfo.getKeyColumn(), val);
                }
                return super.eq(column, val);
            }
        };

        boolean many = Collection.class.isAssignableFrom(tagCache.getType());

        if (data instanceof Collection) {
            List<?> collection = ((Collection<?>) data).stream().filter(Objects::nonNull).collect(Collectors.toList());
            Set<Object> sourceSet = collection.stream().map(sourceCache::getFieldValue).collect(Collectors.toSet());
            if (!sourceSet.isEmpty()) {
                wrapper.in(oneField, sourceSet);
                if (consumer != null) {
                    consumer.accept(wrapper);
                }
                List<?> list = wrapper.list();
                Map<Object, ?> map;
                if (many)
                    map = list.stream().collect(Collectors.groupingBy(oneCache::getFieldValue));
                else
                    map = list.stream().collect(Collectors.toMap(oneCache::getFieldValue, i -> i));
                //匹配
                collection.forEach(item -> tagCache.setFieldValue(item, map.get(sourceCache.getFieldValue(item))));
            }
        } else {
            Object sourceVal = sourceCache.getFieldValue(data);
            wrapper.eq(oneField, sourceVal);
            if (consumer != null) {
                consumer.accept(wrapper);
            }
            Object x = many ? wrapper.list(tagCache.getType()) : wrapper.one(tagCache.getType());
            if (x != null) {
                tagCache.setFieldValue(data, x);
            }
        }
    }

    private static <X> Class<X> getTagClass(Field field) {
        if (Collection.class.isAssignableFrom(field.getType())) {
            return (Class<X>) MPJReflectionKit.getGenericType(field);
        } else {
            return (Class<X>) field.getType();
        }
    }

    @Getter
    @AllArgsConstructor
    private static class SF implements SFunction<Object, Object> {
        private Class<?> clazz;
        private String name;


        @Override
        public Object apply(Object o) {
            throw new UnsupportedOperationException();
        }
    }
}
