package com.baomidou.mybatisplus.core.metadata;

import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.github.yulichang.annotation.EntityMapping;
import com.github.yulichang.annotation.FieldMapping;
import com.github.yulichang.exception.MPJException;
import com.github.yulichang.mapper.MPJTableFieldInfo;
import com.github.yulichang.mapper.MPJTableInfo;
import com.github.yulichang.toolkit.MPJReflectionKit;
import com.github.yulichang.toolkit.TableHelper;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 拷贝 {@link TableInfoHelper}
 *
 * <p>用于构建resultType(DTO)对应的TableInfo
 * <p>拷贝这个类用于更好的兼容mybatis-plus的全部功能
 * <p>由于 {@link TableInfo} 权限限制,所以新建 com.baomidou.mybatisplus.core.metadata 这个包
 * <p>为什么不把 {@link TableInfo} 这个类拷贝出来? 因为无法限制用户使用那个版本, 而TableInfo会随着版本而改动,
 * 使用 mybatis-plus 的TableInfo能够兼容所有版本,也能跟好的维护
 *
 * @author yulichang
 * @see TableInfoHelper
 */
public class MPJTableInfoHelper {

    /**
     * 储存反射类表信息
     */
    private static final Map<Class<?>, MPJTableInfo> TABLE_INFO_CACHE = new ConcurrentHashMap<>();

    /**
     * <p>
     * 获取实体映射表信息
     * </p>
     *
     * @param clazz 反射实体类
     * @return 数据库表反射信息
     */
    public static MPJTableInfo getTableInfo(Class<?> clazz) {
        if (clazz == null || MPJReflectionKit.isPrimitiveOrWrapper(clazz) || clazz == String.class || clazz.isInterface()) {
            return null;
        }
        return TABLE_INFO_CACHE.get(clazz);
    }

    /**
     * <p>
     * 获取所有实体映射表信息
     * </p>
     *
     * @return 数据库表反射信息集合
     */
    public static List<MPJTableInfo> getTableInfos() {
        return Collections.unmodifiableList(new ArrayList<>(TABLE_INFO_CACHE.values()));
    }

    /**
     * <p>
     * 实体类反射获取表信息【初始化】
     * </p>
     *
     * @param clazz 反射实体类
     */
    public synchronized static void initTableInfo(Class<?> clazz, Class<?> mapperClass) {
        MPJTableInfo info = TABLE_INFO_CACHE.get(clazz);
        if (info != null) {
            return;
        }
        MPJTableInfo mpjTableInfo = new MPJTableInfo();
        mpjTableInfo.setMapperClass(mapperClass);
        mpjTableInfo.setEntityClass(clazz);
        TableInfo tableInfo = TableHelper.get(clazz);
        if (tableInfo == null) {
            if (mapperClass != null) {
                return;
            }
        }
        mpjTableInfo.setDto(tableInfo == null);
        mpjTableInfo.setTableInfo(tableInfo);
        initMapping(mpjTableInfo);
        TABLE_INFO_CACHE.put(clazz, mpjTableInfo);
    }

    private static boolean isExistMapping(Class<?> clazz) {
        return ReflectionKit.getFieldList(ClassUtils.getUserClass(clazz)).stream().anyMatch(field -> field.isAnnotationPresent(EntityMapping.class));
    }

    private static boolean isExistMappingField(Class<?> clazz) {
        return ReflectionKit.getFieldList(ClassUtils.getUserClass(clazz)).stream().anyMatch(field -> field.isAnnotationPresent(FieldMapping.class));
    }

    /**
     * 初始化映射相关
     */
    public static void initMapping(MPJTableInfo mpjTableInfo) {
        // 是否存在 @EntityMapping 注解
        boolean existMapping = isExistMapping(mpjTableInfo.getEntityClass());
        mpjTableInfo.setHasMapping(existMapping);
        // 是否存在 @FieldMapping 注解
        boolean existMappingField = isExistMappingField(mpjTableInfo.getEntityClass());
        mpjTableInfo.setHasMappingField(existMappingField);
        mpjTableInfo.setHasMappingOrField(existMapping || existMappingField);
        /* 关系映射初始化 */
        List<MPJTableFieldInfo> mpjFieldList = new ArrayList<>();
        List<Field> fields = ReflectionKit.getFieldList(ClassUtils.getUserClass(mpjTableInfo.getEntityClass()));
        for (Field field : fields) {
            if (existMapping) {
                EntityMapping mapping = field.getAnnotation(EntityMapping.class);
                if (mapping != null) {
                    mpjFieldList.add(new MPJTableFieldInfo(mpjTableInfo.getEntityClass(), mapping, field));
                }
            }
            if (existMappingField) {
                FieldMapping mapping = field.getAnnotation(FieldMapping.class);
                if (mapping != null) {
                    mpjFieldList.add(new MPJTableFieldInfo(mpjTableInfo.getEntityClass(), mapping, field));
                }
            }
        }
        /* 映射字段列表 */
        mpjTableInfo.setFieldList(mpjFieldList);
    }

    /**
     * 复制tableInfo对象
     * 由于各个版本的MP的TableInfo对象存在差异，为了兼容性采用反射，而不是getter setter
     */
    public static TableInfo copyAndSetTableName(TableInfo tableInfo, String tableName) {
        try {
            TableInfo table;
            try {
                table = TableInfo.class.getDeclaredConstructor(Class.class).newInstance(tableInfo.getEntityType());
            } catch (Exception e) {
                table = TableInfo.class.getDeclaredConstructor(Configuration.class, Class.class).newInstance(tableInfo.getConfiguration(), tableInfo.getEntityType());
            }
            //反射拷贝对象
            Field[] fields = TableInfo.class.getDeclaredFields();
            for (Field f : fields) {
                f.setAccessible(true);
                f.set(table, f.get(tableInfo));
            }
            table.setTableName(tableName);
            return table;
        } catch (Exception e) {
            throw new MPJException("TableInfo 对象拷贝失败 -> " + tableInfo.getEntityType().getName());
        }
    }
}
