package com.baomidou.mybatisplus.core.metadata;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.github.yulichang.annotation.EntityMapping;
import com.github.yulichang.annotation.FieldMapping;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.mapping.ResultFlag;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.Reflector;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

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


    private static final Log logger = LogFactory.getLog(TableInfoHelper.class);


    /**
     * 储存反射类表信息
     */
    private static final Map<Class<?>, MPJTableInfo> TABLE_INFO_CACHE = new ConcurrentHashMap<>();

    /**
     * 默认表主键名称
     */
    private static final String DEFAULT_ID_NAME = "id";


    /**
     * <p>
     * 获取实体映射表信息
     * </p>
     *
     * @param clazz 反射实体类
     * @return 数据库表反射信息
     */
    public static MPJTableInfo getTableInfo(Class<?> clazz) {
        if (clazz == null || ReflectionKit.isPrimitiveOrWrapper(clazz) || clazz == String.class || clazz.isInterface()) {
            return null;
        }
        return TABLE_INFO_CACHE.get(clazz);
    }

    /**
     * <p>
     * 实体类反射获取表信息【初始化】
     * </p>
     *
     * @param clazz       反射实体类
     * @param mapperClass mapperClass
     */
    public synchronized static void initTableInfo(MapperBuilderAssistant builderAssistant, Class<?> clazz, Class<?> mapperClass) {
        MPJTableInfo targetTableInfo = TABLE_INFO_CACHE.get(clazz);
        final Configuration configuration = builderAssistant.getConfiguration();
        if (targetTableInfo != null) {
            Configuration oldConfiguration = targetTableInfo.getTableInfo().getConfiguration();
            if (!oldConfiguration.equals(configuration)) {
                // 不是同一个 Configuration,进行重新初始化
                initTableInfo(configuration, builderAssistant.getCurrentNamespace(), clazz, mapperClass);
            }
            return;
        }
        initTableInfo(configuration, builderAssistant.getCurrentNamespace(), clazz, mapperClass);
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
     * @return 数据库表反射信息
     */
    public synchronized static MPJTableInfo initTableInfo(Configuration configuration, String currentNamespace, Class<?> clazz, Class<?> mapperClass) {
        MPJTableInfo info = TABLE_INFO_CACHE.get(clazz);
        if (info != null) {
            return info;
        }
        /* 没有获取到缓存信息,则初始化 */
        MPJTableInfo mpjTableInfo = new MPJTableInfo();
        mpjTableInfo.setMapperClass(mapperClass);
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        if (tableInfo != null) {
            mpjTableInfo.setTableInfo(tableInfo);
            initMapping(mpjTableInfo);
            /* 添加缓存 */
            TABLE_INFO_CACHE.put(clazz, mpjTableInfo);
            return mpjTableInfo;
        }

        tableInfo = new TableInfo(clazz);
        mpjTableInfo.setTableInfo(tableInfo);
        tableInfo.setCurrentNamespace(currentNamespace);
        tableInfo.setConfiguration(configuration);
        GlobalConfig globalConfig = GlobalConfigUtils.getGlobalConfig(configuration);

        /* 初始化表名相关 */
        final String[] excludeProperty = initTableName(clazz, globalConfig, tableInfo);

        List<String> excludePropertyList = excludeProperty != null && excludeProperty.length > 0 ? Arrays.asList(excludeProperty) : Collections.emptyList();

        /* 初始化字段相关 */
        initTableFields(clazz, globalConfig, mpjTableInfo, excludePropertyList);

        /* 自动构建 resultMap */
        initResultMapIfNeed(tableInfo);

        /* 添加缓存 */
        TABLE_INFO_CACHE.put(clazz, mpjTableInfo);

        /* 缓存 lambda */
        LambdaUtils.installCache(tableInfo);

        /* 初始化映射关系 */
        initMapping(mpjTableInfo);
        return mpjTableInfo;
    }

    /**
     * 自动构建 resultMap 并注入(如果条件符合的话)
     */
    private static void initResultMapIfNeed(TableInfo tableInfo) {
        if (tableInfo.isAutoInitResultMap() && null == tableInfo.getResultMap()) {
            String id = tableInfo.getCurrentNamespace() + ".mybatis-plus-join_" + tableInfo.getEntityType().getSimpleName();
            tableInfo.setResultMap(id);
            if (tableInfo.getConfiguration().getResultMapNames().contains(id)) {
                tableInfo.getConfiguration().getResultMap(id);
            }
            List<ResultMapping> resultMappings = new ArrayList<>();
            if (tableInfo.havePK()) {
                ResultMapping idMapping = new ResultMapping.Builder(tableInfo.getConfiguration(), tableInfo.getKeyProperty(),
                        tableInfo.getKeyColumn(), tableInfo.getKeyType())
                        .flags(Collections.singletonList(ResultFlag.ID)).build();
                resultMappings.add(idMapping);
            }
            if (CollectionUtils.isNotEmpty(tableInfo.getFieldList())) {
                tableInfo.getFieldList().forEach(i -> resultMappings.add(i.getResultMapping(tableInfo.getConfiguration())));
            }
            ResultMap resultMap = new ResultMap.Builder(tableInfo.getConfiguration(), id, tableInfo.getEntityType(),
                    resultMappings).build();
            tableInfo.getConfiguration().addResultMap(resultMap);
        }
    }

    /**
     * <p>
     * 初始化 表数据库类型,表名,resultMap
     * </p>
     *
     * @param clazz        实体类
     * @param globalConfig 全局配置
     * @param tableInfo    数据库表反射信息
     * @return 需要排除的字段名
     */
    private static String[] initTableName(Class<?> clazz, GlobalConfig globalConfig, TableInfo tableInfo) {
        /* 数据库全局配置 */
        GlobalConfig.DbConfig dbConfig = globalConfig.getDbConfig();
        TableName table = clazz.getAnnotation(TableName.class);

        String tableName = clazz.getSimpleName();
        String tablePrefix = dbConfig.getTablePrefix();
        String schema = dbConfig.getSchema();
        boolean tablePrefixEffect = true;
        String[] excludeProperty = null;

        if (table != null) {
            if (StringUtils.isNotBlank(table.value())) {
                tableName = table.value();
                if (StringUtils.isNotBlank(tablePrefix) && !table.keepGlobalPrefix()) {
                    tablePrefixEffect = false;
                }
            } else {
                tableName = initTableNameWithDbConfig(tableName, dbConfig);
            }
            if (StringUtils.isNotBlank(table.schema())) {
                schema = table.schema();
            }
            /* 表结果集映射 */
            if (StringUtils.isNotBlank(table.resultMap())) {
                tableInfo.setResultMap(table.resultMap());
            }
            tableInfo.setAutoInitResultMap(table.autoResultMap());
            excludeProperty = table.excludeProperty();
        } else {
            tableName = initTableNameWithDbConfig(tableName, dbConfig);
        }

        String targetTableName = tableName;
        if (StringUtils.isNotBlank(tablePrefix) && tablePrefixEffect) {
            targetTableName = tablePrefix + targetTableName;
        }
        if (StringUtils.isNotBlank(schema)) {
            targetTableName = schema + StringPool.DOT + targetTableName;
        }

        tableInfo.setTableName(targetTableName);

        return excludeProperty;
    }

    /**
     * 根据 DbConfig 初始化 表名
     *
     * @param className 类名
     * @param dbConfig  DbConfig
     * @return 表名
     */
    private static String initTableNameWithDbConfig(String className, GlobalConfig.DbConfig dbConfig) {
        String tableName = className;
        // 开启表名下划线申明
        if (dbConfig.isTableUnderline()) {
            tableName = StringUtils.camelToUnderline(tableName);
        }
        // 大写命名判断
        if (dbConfig.isCapitalMode()) {
            tableName = tableName.toUpperCase();
        } else {
            // 首字母小写
            tableName = StringUtils.firstToLowerCase(tableName);
        }
        return tableName;
    }

    /**
     * <p>
     * 初始化 表主键,表字段
     * </p>
     *
     * @param clazz        实体类
     * @param globalConfig 全局配置
     * @param mpjTableInfo 数据库表反射信息
     */
    private static void initTableFields(Class<?> clazz, GlobalConfig globalConfig, MPJTableInfo mpjTableInfo, List<String> excludeProperty) {
        /* 数据库全局配置 */
        GlobalConfig.DbConfig dbConfig = globalConfig.getDbConfig();
        ReflectorFactory reflectorFactory = mpjTableInfo.getTableInfo().getConfiguration().getReflectorFactory();
        Reflector reflector = reflectorFactory.findForClass(clazz);
        List<Field> list = getAllFields(clazz);
        // 标记是否读取到主键
        boolean isReadPK = false;
        // 是否存在 @TableId 注解
        boolean existTableId = isExistTableId(list);
        // 是否存在 @TableLogic 注解
        boolean existTableLogic = isExistTableLogic(list);

        List<TableFieldInfo> fieldList = new ArrayList<>(list.size());
        for (Field field : list) {
            if (excludeProperty.contains(field.getName())) {
                continue;
            }

            /* 主键ID 初始化 */
            if (existTableId) {
                TableId tableId = field.getAnnotation(TableId.class);
                if (tableId != null) {
                    if (isReadPK) {
                        throw ExceptionUtils.mpe("@TableId can't more than one in Class: \"%s\".", clazz.getName());
                    } else {
                        initTableIdWithAnnotation(dbConfig, mpjTableInfo.getTableInfo(), field, tableId, reflector);
                        isReadPK = true;
                        continue;
                    }
                }
            } else if (!isReadPK) {
                isReadPK = initTableIdWithoutAnnotation(dbConfig, mpjTableInfo.getTableInfo(), field, reflector);
                if (isReadPK) {
                    continue;
                }
            }

            final TableField tableField = field.getAnnotation(TableField.class);

            /* 有 @TableField 注解的字段初始化 */
            if (tableField != null) {
                fieldList.add(new TableFieldInfo(dbConfig, mpjTableInfo.getTableInfo(), field, tableField, reflector, existTableLogic));
                continue;
            }

            /* 无 @TableField  注解的字段初始化 */
            fieldList.add(new TableFieldInfo(dbConfig, mpjTableInfo.getTableInfo(), field, reflector, existTableLogic));
        }

        /* 字段列表 */
        mpjTableInfo.getTableInfo().setFieldList(fieldList);


        /* 未发现主键注解，提示警告信息 */
        if (!isReadPK) {
            logger.warn(String.format("Can not find table primary key in Class: \"%s\".", clazz.getName()));
        }
    }

    /**
     * <p>
     * 判断主键注解是否存在
     * </p>
     *
     * @param list 字段列表
     * @return true 为存在 @TableId 注解;
     */
    public static boolean isExistTableId(List<Field> list) {
        return list.stream().anyMatch(field -> field.isAnnotationPresent(TableId.class));
    }

    /**
     * <p>
     * 判断逻辑删除注解是否存在
     * </p>
     *
     * @param list 字段列表
     * @return true 为存在 @TableId 注解;
     */
    public static boolean isExistTableLogic(List<Field> list) {
        return list.stream().anyMatch(field -> field.isAnnotationPresent(TableLogic.class));
    }

    private static boolean isExistMapping(Class<?> clazz) {
        return ReflectionKit.getFieldList(ClassUtils.getUserClass(clazz)).stream().anyMatch(field -> field.isAnnotationPresent(EntityMapping.class));
    }

    private static boolean isExistMappingField(Class<?> clazz) {
        return ReflectionKit.getFieldList(ClassUtils.getUserClass(clazz)).stream().anyMatch(field -> field.isAnnotationPresent(FieldMapping.class));
    }

    /**
     * <p>
     * 主键属性初始化
     * </p>
     *
     * @param dbConfig  全局配置信息
     * @param tableInfo 表信息
     * @param field     字段
     * @param tableId   注解
     * @param reflector Reflector
     */
    private static void initTableIdWithAnnotation(GlobalConfig.DbConfig dbConfig, TableInfo tableInfo,
                                                  Field field, TableId tableId, Reflector reflector) {
        boolean underCamel = tableInfo.isUnderCamel();
        final String property = field.getName();
        if (field.getAnnotation(TableField.class) != null) {
            logger.warn(String.format("This \"%s\" is the table primary key by @TableId annotation in Class: \"%s\",So @TableField annotation will not work!",
                    property, tableInfo.getEntityType().getName()));
        }
        /* 主键策略（ 注解 > 全局 ） */
        // 设置 Sequence 其他策略无效
        if (IdType.NONE == tableId.type()) {
            tableInfo.setIdType(dbConfig.getIdType());
        } else {
            tableInfo.setIdType(tableId.type());
        }

        /* 字段 */
        String column = property;
        if (StringUtils.isNotBlank(tableId.value())) {
            column = tableId.value();
        } else {
            // 开启字段下划线申明
            if (underCamel) {
                column = StringUtils.camelToUnderline(column);
            }
            // 全局大写命名
            if (dbConfig.isCapitalMode()) {
                column = column.toUpperCase();
            }
        }
        final Class<?> keyType = reflector.getGetterType(property);
        if (keyType.isPrimitive()) {
            logger.warn(String.format("This primary key of \"%s\" is primitive !不建议如此请使用包装类 in Class: \"%s\"",
                    property, tableInfo.getEntityType().getName()));
        }
        tableInfo.setKeyRelated(checkRelated(underCamel, property, column))
                .setKeyColumn(column)
                .setKeyProperty(property)
                .setKeyType(keyType);
    }

    /**
     * <p>
     * 主键属性初始化
     * </p>
     *
     * @param tableInfo 表信息
     * @param field     字段
     * @param reflector Reflector
     * @return true 继续下一个属性判断，返回 continue;
     */
    private static boolean initTableIdWithoutAnnotation(GlobalConfig.DbConfig dbConfig, TableInfo tableInfo,
                                                        Field field, Reflector reflector) {
        final String property = field.getName();
        if (DEFAULT_ID_NAME.equalsIgnoreCase(property)) {
            if (field.getAnnotation(TableField.class) != null) {
                logger.warn(String.format("This \"%s\" is the table primary key by default name for `id` in Class: \"%s\",So @TableField will not work!",
                        property, tableInfo.getEntityType().getName()));
            }
            String column = property;
            if (dbConfig.isCapitalMode()) {
                column = column.toUpperCase();
            }
            final Class<?> keyType = reflector.getGetterType(property);
            if (keyType.isPrimitive()) {
                logger.warn(String.format("This primary key of \"%s\" is primitive !不建议如此请使用包装类 in Class: \"%s\"",
                        property, tableInfo.getEntityType().getName()));
            }
            tableInfo.setKeyRelated(checkRelated(tableInfo.isUnderCamel(), property, column))
                    .setIdType(dbConfig.getIdType())
                    .setKeyColumn(column)
                    .setKeyProperty(property)
                    .setKeyType(keyType);
            return true;
        }
        return false;
    }

    /**
     * 判定 related 的值
     * <p>
     * 为 true 表示不符合规则
     *
     * @param underCamel 驼峰命名
     * @param property   属性名
     * @param column     字段名
     * @return related
     */
    public static boolean checkRelated(boolean underCamel, String property, String column) {
        column = StringUtils.getTargetColumn(column);
        String propertyUpper = property.toUpperCase(Locale.ENGLISH);
        String columnUpper = column.toUpperCase(Locale.ENGLISH);
        if (underCamel) {
            // 开启了驼峰并且 column 包含下划线
            return !(propertyUpper.equals(columnUpper) ||
                    propertyUpper.equals(columnUpper.replace(StringPool.UNDERSCORE, StringPool.EMPTY)));
        } else {
            // 未开启驼峰,直接判断 property 是否与 column 相同(全大写)
            return !propertyUpper.equals(columnUpper);
        }
    }

    /**
     * <p>
     * 获取该类的所有属性列表
     * </p>
     *
     * @param clazz 反射类
     * @return 属性集合
     */
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fieldList = ReflectionKit.getFieldList(ClassUtils.getUserClass(clazz));
        return fieldList.stream()
                .filter(field -> {
                    /* 过滤注解非表字段属性 */
                    TableField tableField = field.getAnnotation(TableField.class);
                    return (tableField == null || tableField.exist());
                }).collect(toList());
    }

    /**
     * 初始化映射相关
     */
    public static void initMapping(MPJTableInfo mpjTableInfo) {
        // 是否存在 @MPJMapping 注解
        boolean existMapping = isExistMapping(mpjTableInfo.getTableInfo().getEntityType());
        mpjTableInfo.setHasMapping(existMapping);
        // 是否存在 @MPJMappingField 注解
        boolean existMappingField = isExistMappingField(mpjTableInfo.getTableInfo().getEntityType());
        mpjTableInfo.setHasMappingField(existMappingField);
        mpjTableInfo.setHasMappingOrField(existMapping || existMappingField);
        /* 关系映射初始化 */
        List<MPJTableFieldInfo> mpjFieldList = new ArrayList<>();
        List<Field> fields = ReflectionKit.getFieldList(ClassUtils.getUserClass(mpjTableInfo.getTableInfo().getEntityType()));
        for (Field field : fields) {
            if (existMapping) {
                EntityMapping mapping = field.getAnnotation(EntityMapping.class);
                if (mapping != null) {
                    mpjFieldList.add(new MPJTableFieldInfo(mpjTableInfo.getTableInfo().getEntityType(), mapping, field));
                }
            }
            if (existMappingField) {
                FieldMapping mapping = field.getAnnotation(FieldMapping.class);
                if (mapping != null) {
                    mpjFieldList.add(new MPJTableFieldInfo(mpjTableInfo.getTableInfo().getEntityType(), mapping, field));
                }
            }
        }
        /* 映射字段列表 */
        mpjTableInfo.setFieldList(mpjFieldList);
    }
}
