package com.baomidou.mybatisplus.core.metadata;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Author Gy.13
 * @Description: 用于构建查询返回列，由于mybatis-plus条件构造的select无法实现通过传入VO实体类查询想要的列，需要一个一个指定
 * @Date: 2022/8/5 09:39
 */
@SuppressWarnings("deprecation")
public class MPJResultHelper {


    private static final Log logger = LogFactory.getLog(MPJResultHelper.class);


    /**
     * 储存反射VO信息
     */
    private static final Map<Class<?>, Set<String>> VO_INFO_CACHE = new ConcurrentHashMap<>();


    /**
     * @param sourceEntityClass
     * @param resultEntityClass
     * @Author Gy.13
     * @Description: 获取VO实体映射表信息
     * @return: java.util.Set<java.lang.String>
     * @Date: 2022/8/5 09:59
     */
    public static Set<String> getVoTableInfo(Class<?> sourceEntityClass, Class<?> resultEntityClass) {
        if (resultEntityClass == null || ReflectionKit.isPrimitiveOrWrapper(resultEntityClass) || resultEntityClass == String.class || resultEntityClass.isInterface()) {
            return null;
        }
        Set<String> strings = VO_INFO_CACHE.get(resultEntityClass);
        if (strings == null) {
            Set<String> set = new HashSet<>();
            MPJTableInfo info = MPJTableInfoHelper.getTableInfo(sourceEntityClass);
            Assert.notNull(info, "table can not be find");
            List<Field> allFields = TableInfoHelper.getAllFields(resultEntityClass);
            Assert.notNull(allFields, "table can not be find");
            Set<String> fieldNames = allFields.stream().collect(Collectors.groupingBy(Field::getName)).keySet();
            info.getTableInfo().getFieldList().forEach(
                    i -> {
                        if (fieldNames.contains(i.getProperty())) {
                            set.add(i.getColumn());
                        }
                    });
            /* 添加缓存 */
            VO_INFO_CACHE.put(resultEntityClass, set);
        }
        return VO_INFO_CACHE.get(resultEntityClass);
    }
}
