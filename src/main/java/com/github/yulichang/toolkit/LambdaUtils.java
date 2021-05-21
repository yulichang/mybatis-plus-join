package com.github.yulichang.toolkit;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.support.*;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Locale.ENGLISH;

/**
 * copy {@link com.baomidou.mybatisplus.core.toolkit.LambdaUtils}
 */
public final class LambdaUtils {

    /* ******* 自定义方法 *********** */
    public static <T> String getName(SFunction<T, ?> fn) {
        return PropertyNamer.methodToProperty(extract(fn).getImplMethodName());
    }


    @SuppressWarnings("unchecked")
    public static <T> Class<T> getEntityClass(SFunction<T, ?> fn) {
        return (Class<T>) extract(fn).getInstantiatedClass();
    }
    /* ******* 自定义方法 结束 以下代码均为拷贝 *********** */

    /**
     * 字段映射
     */
    private static final Map<String, Map<String, ColumnCache>> COLUMN_CACHE_MAP = new ConcurrentHashMap<>();

    /**
     * 该缓存可能会在任意不定的时间被清除
     *
     * @param func 需要解析的 lambda 对象
     * @param <T>  类型，被调用的 Function 对象的目标类型
     * @return 返回解析后的结果
     */
    public static <T> LambdaMeta extract(SFunction<T, ?> func) {
        try {
            Method method = func.getClass().getDeclaredMethod("writeReplace");
            return new SerializedLambdaMeta((SerializedLambda) ReflectionKit.setAccessible(method).invoke(func));
        } catch (NoSuchMethodException e) {
            if (func instanceof Proxy) return new ProxyLambdaMeta((Proxy) func);
            String message = "Cannot find method writeReplace, please make sure that the lambda composite class is currently passed in";
            throw new MybatisPlusException(message);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new MybatisPlusException(e);
        }
    }

    /**
     * 格式化 key 将传入的 key 变更为大写格式
     *
     * <pre>
     *     Assert.assertEquals("USERID", formatKey("userId"))
     * </pre>
     *
     * @param key key
     * @return 大写的 key
     */
    public static String formatKey(String key) {
        return key.toUpperCase(ENGLISH);
    }

    /**
     * 将传入的表信息加入缓存
     *
     * @param tableInfo 表信息
     */
    public static void installCache(TableInfo tableInfo) {
        COLUMN_CACHE_MAP.put(tableInfo.getEntityType().getName(), createColumnCacheMap(tableInfo));
    }

    /**
     * 缓存实体字段 MAP 信息
     *
     * @param info 表信息
     * @return 缓存 map
     */
    private static Map<String, ColumnCache> createColumnCacheMap(TableInfo info) {
        Map<String, ColumnCache> map;

        if (info.havePK()) {
            map = CollectionUtils.newHashMapWithExpectedSize(info.getFieldList().size() + 1);
            map.put(formatKey(info.getKeyProperty()), new ColumnCache(info.getKeyColumn(), info.getKeySqlSelect()));
        } else {
            map = CollectionUtils.newHashMapWithExpectedSize(info.getFieldList().size());
        }

        info.getFieldList().forEach(i ->
                map.put(formatKey(i.getProperty()), new ColumnCache(i.getColumn(), i.getSqlSelect(), i.getMapping()))
        );
        return map;
    }

    /**
     * 获取实体对应字段 MAP
     *
     * @param clazz 实体类
     * @return 缓存 map
     */
    public static Map<String, ColumnCache> getColumnMap(Class<?> clazz) {
        return CollectionUtils.computeIfAbsent(COLUMN_CACHE_MAP, clazz.getName(), key -> {
            TableInfo info = TableInfoHelper.getTableInfo(clazz);
            return info == null ? null : createColumnCacheMap(info);
        });
    }

}
