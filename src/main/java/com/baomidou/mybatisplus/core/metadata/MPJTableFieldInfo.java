package com.baomidou.mybatisplus.core.metadata;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.github.yulichang.annotation.EntityMapping;
import com.github.yulichang.annotation.FieldMapping;
import com.github.yulichang.exception.MPJException;
import com.github.yulichang.toolkit.SpringContentUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 字段属性
 *
 * @author yulichang
 * @see TableFieldInfo
 * @since 1.2.0
 */
@Getter
@ToString
@EqualsAndHashCode
public class MPJTableFieldInfo {

    /**
     * 是否是实体映射
     */
    private final boolean isMappingEntity;
    /**
     * 是否是属性映射
     */
    private final boolean isMappingField;
    /**
     * 字段映射是绑定的字段
     */
    private Field bindField;
    /**
     * 是否移除绑定字段
     */
    private final boolean isRemoveBindField;
    /**
     * 实体类
     */
    private final Class<?> entityType;
    /**
     * 属性
     */
    private Field field;
    /**
     * 属性名
     */
    private String property;
    /**
     * 数据结构是否是Map或者List<Map>
     */
    private boolean fieldIsMap;
    /**
     * 属性是否是集合
     */
    private boolean isCollection;
    /**
     * 当前映射属性
     */
    private Field thisField;
    /**
     * 当前类实体属性
     */
    private String thisProperty;
    /**
     * 当前字段信息
     */
    private String thisColumn;
    /**
     * 当前字段信息
     */
    private String thisMapKey;
    /**
     * 映射实体类
     */
    private Class<?> joinClass;
    /**
     * 映射属性名
     */
    private String joinProperty;
    /**
     * 映射属性数据库列名
     */
    private String joinColumn;
    /**
     * 映射属性字段
     */
    private Field joinField;
    /**
     * fieldIsMap 为true时使用
     * 映射查询Map 的key
     * 默认为 关联字段的数据库列名
     */
    private String joinMapKey;
    /**
     * 关联的mapper引用
     */
    private BaseMapper<?> joinMapper;
    /**
     * 关联查询条件配置
     */
    private final MPJMappingWrapper wrapper;
    /**
     * 一对一查询结果数量不匹配是是否抛出异常
     */
    private final boolean isThrowExp;

    /**
     * 初始化关联字段信息
     */
    public MPJTableFieldInfo(Class<?> entityType, EntityMapping mapping, Field field) {
        this.isMappingEntity = true;
        this.isMappingField = false;
        initField(field);
        if (mapping.tag() != Object.class) {
            this.joinClass = mapping.tag();
        }
        this.entityType = entityType;
        this.isThrowExp = mapping.isThrowExp();
        initThisField(mapping.thisMapKey(), mapping.thisField());
        initJoinField(mapping.joinMapKey(), mapping.joinField());
        this.isRemoveBindField = StringUtils.isNotBlank(mapping.select()) && (!Arrays.asList(mapping.select().split(
                StringPool.COMMA)).contains(this.joinColumn.trim()));
        this.wrapper = new MPJMappingWrapper(mapping.first(), StringUtils.isBlank(mapping.select()) ? null :
                (this.isRemoveBindField ? this.joinColumn + StringPool.COMMA + mapping.select() : mapping.select()),
                mapping.apply(), mapping.condition(), mapping.last(), mapping.orderByAsc(), mapping.orderByDesc());
    }

    public MPJTableFieldInfo(Class<?> entityType, FieldMapping mappingField, Field field) {
        this.isMappingEntity = false;
        this.isMappingField = true;
        field.setAccessible(true);
        this.field = field;
        this.property = field.getName();
        this.isCollection = Collection.class.isAssignableFrom(field.getType());
        if (this.isCollection && !List.class.isAssignableFrom(this.field.getType())) {
            throw new MPJException("对多关系的数据结构目前只支持 <List> 暂不支持其他Collection实现 " + this.field.getType().getTypeName());
        }
        this.joinClass = mappingField.tag();
        this.entityType = entityType;
        this.isThrowExp = mappingField.isThrowExp();
        initThisField(mappingField.thisMapKey(), mappingField.thisField());
        initJoinField(mappingField.joinMapKey(), mappingField.joinField());
        this.isRemoveBindField = !mappingField.select().equals(this.joinColumn.trim());
        this.wrapper = new MPJMappingWrapper(mappingField.first(), this.isRemoveBindField ? this.joinColumn +
                StringPool.COMMA + mappingField.select() : mappingField.select(), mappingField.apply(),
                mappingField.condition(), mappingField.last(), mappingField.orderByAsc(), mappingField.orderByDesc());
        initBindField(mappingField.select());
    }

    private void initBindField(String bindName) {
        TableInfo info = TableInfoHelper.getTableInfo(this.joinClass);
        Field field = info.getFieldList().stream()
                .filter(i -> i.getColumn().equals(bindName))
                .map(TableFieldInfo::getField).findFirst().orElse(null);
        if (field == null && bindName.equals(info.getKeyColumn())) {
            field = ReflectionKit.getFieldList(joinClass).stream().filter(f ->
                    f.getName().equals(info.getKeyProperty())).findFirst().orElse(null);
        }
        if (field == null) {
            throw new MPJException("字段不存在 " + this.joinClass.getName() + " ，" + bindName);
        }
        this.bindField = field;
    }

    private void initJoinField(String joinMapKey, String joinField) {
        if (StringUtils.isNotBlank(joinField)) {
            this.joinProperty = joinField;
        } else {
            TableInfo info = getTableInfo(this.joinClass);
            Assert.isTrue(info.havePK(), "实体未定义主键 %s ", this.joinClass.getName());
            this.joinProperty = info.getKeyProperty();
        }

        TableInfo joinTableInfo = getTableInfo(this.joinClass);
        TableFieldInfo joinFieldInfo = joinTableInfo.getFieldList().stream().filter(f ->
                f.getField().getName().equals(this.joinProperty)).findFirst().orElse(null);
        if (joinFieldInfo == null) {
            if (joinTableInfo.havePK() && this.joinProperty.equals(joinTableInfo.getKeyProperty())) {
                this.joinColumn = joinTableInfo.getKeyColumn();
                this.joinField = ReflectionKit.getFieldList(this.joinClass).stream().filter(i ->
                        i.getName().equals(joinTableInfo.getKeyProperty())).findFirst().orElse(null);
            }
        } else {
            this.joinColumn = joinFieldInfo.getColumn();
            this.joinField = joinFieldInfo.getField();
        }
        Assert.notNull(this.joinField, "注解属性thisField不存在 %s , %s", this.joinClass.getName(),
                StringUtils.isBlank(this.joinProperty) ? "主键" : this.joinProperty);
        Assert.notNull(this.joinColumn, "注解属性thisField不存在 %s , %s", this.joinClass.getName(),
                StringUtils.isBlank(this.joinProperty) ? "主键" : this.joinProperty);
        this.joinField.setAccessible(true);
        this.joinMapKey = StringUtils.isBlank(joinMapKey) ? this.joinColumn : joinMapKey;
    }

    private void initThisField(String thisMapKey, String thisField) {
        if (StringUtils.isNotBlank(thisField)) {
            this.thisProperty = thisField;
        } else {
            TableInfo info = getTableInfo(this.entityType);
            Assert.isTrue(info.havePK(), "实体未定义主键 %s ", this.entityType.getName());
            this.thisProperty = info.getKeyProperty();
        }

        TableInfo tableInfo = getTableInfo(this.entityType);
        if (tableInfo.havePK() && this.thisProperty.equals(tableInfo.getKeyProperty())) {
            this.thisField = ReflectionKit.getFieldList(ClassUtils.getUserClass(entityType)).stream().filter(f ->
                    f.getName().equals(tableInfo.getKeyProperty())).findFirst().orElse(null);
            Assert.notNull(this.thisField, "注解属性thisField不存在 %s , %s", entityType.getName(),
                    StringUtils.isBlank(this.thisProperty) ? "主键" : this.thisProperty);
            this.thisColumn = tableInfo.getKeyColumn();
        } else {
            TableFieldInfo fieldInfo = tableInfo.getFieldList().stream().filter(f ->
                    f.getField().getName().equals(this.thisProperty)).findFirst().orElse(null);
            Assert.notNull(fieldInfo, "注解属性thisField不存在 %s , %s", entityType.getName(),
                    StringUtils.isBlank(this.thisProperty) ? "主键" : this.thisProperty);
            this.thisField = fieldInfo.getField();
            this.thisColumn = fieldInfo.getColumn();
        }
        this.thisField.setAccessible(true);
        this.thisMapKey = StringUtils.isBlank(thisMapKey) ? this.thisColumn : thisMapKey;
    }

    private void initField(Field field) {
        field.setAccessible(true);
        this.field = field;
        this.property = field.getName();
        this.isCollection = Collection.class.isAssignableFrom(field.getType());
        if (this.isCollection && !List.class.isAssignableFrom(this.field.getType())) {
            throw new MPJException("对多关系的数据结构目前只支持 <List> 暂不支持其他Collection实现 " + this.field.getType().getTypeName());
        }
        if (Map.class.isAssignableFrom(field.getType())) {
            this.fieldIsMap = true;
        } else {
            if (field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) field.getGenericType();
                Type type = t.getActualTypeArguments()[0];
                if (type instanceof ParameterizedType) {
                    this.fieldIsMap = ((ParameterizedType) type).getRawType() == Map.class;
                } else {
                    this.fieldIsMap = false;
                }
            } else {
                this.fieldIsMap = false;
            }
        }

        if (!this.fieldIsMap) {
            if (List.class.isAssignableFrom(field.getType())) {
                if (field.getGenericType() instanceof ParameterizedType) {
                    ParameterizedType t = (ParameterizedType) field.getGenericType();
                    this.joinClass = (Class<?>) t.getActualTypeArguments()[0];
                }
            } else {
                this.joinClass = field.getType();
            }
        }
    }

    public BaseMapper<?> getJoinMapper() {
        if (this.joinMapper == null) {
            MPJTableInfo joinTableInfo = MPJTableInfoHelper.getTableInfos().stream().filter(table ->
                    table.getTableInfo().getEntityType() == this.joinClass).findFirst().orElse(null);
            if (joinTableInfo == null) {
                throw new MPJException("未注册 mapper " + this.joinClass.getName());
            }
            this.joinMapper = (BaseMapper<?>) SpringContentUtils.getApplicationContext().getBean(joinTableInfo.getMapperClass());
        }
        return this.joinMapper;
    }

    private TableInfo getTableInfo(Class<?> clazz) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
        if (tableInfo == null) {
            throw new MPJException("未注册 mapper " + clazz.getName());
        }
        return tableInfo;
    }


    public void fieldSet(Object o, Object val) {
        try {
            this.field.set(o, val);
        } catch (Exception e) {
            throw new MPJException("无法设置关联字段，请检查关联字段数据类型是否匹配 " + this.entityType.getName() +
                    " , " + this.field.getName() + " , " + o.getClass().getName());
        }
    }

    public Object thisFieldGet(Object o) {
        try {
            return getThisField().get(o);
        } catch (Exception e) {
            throw new MPJException("无法获取当前关联字段，请检查关联字段是否匹配 " + this.entityType.getName() + " , " +
                    this.thisField.getName() + " , " + o.getClass().getName());
        }
    }

    public Object joinFieldGet(Object o) {
        try {
            return getJoinField().get(o);
        } catch (Exception e) {
            throw new MPJException("无法设置关联字段，请检查关联字段数据类型是否匹配 " + this.joinClass.getName() + " , " +
                    this.joinField.getName() + " , " + o.getClass().getName());
        }
    }

    public Object bindFieldGet(Object o) {
        try {
            return getBindField().get(o);
        } catch (Exception e) {
            throw new MPJException("无法设置关联字段，请检查关联字段数据类型是否匹配 " + this.joinClass.getName() + " , " +
                    this.bindField.getName() + " , " + o.getClass().getName());
        }
    }

    public void joinFieldSetNull(Object o) {
        try {
            this.joinField.set(o, null);
        } catch (Exception e) {
            throw new MPJException("无法设置关联字段，请检查关联字段数据类型是否匹配 " + this.entityType.getName() +
                    " , " + this.joinField.getName() + " , " + o.getClass().getName());
        }
    }

    @SuppressWarnings({"unchecked", "RedundantCast"})
    public void removeJoinField(List<?> joinList) {
        if (this.isMappingEntity() && this.isRemoveBindField()) {
            if (this.isFieldIsMap()) {
                ((List<Map<String, Object>>) joinList).forEach(m -> m.remove(this.getJoinMapKey()));
            } else {
                joinList.forEach(this::joinFieldSetNull);
            }
        }
    }

    public static <T> void bind(MPJTableFieldInfo fieldInfo, T i, List<?> data) {
        if (fieldInfo.isCollection()) {
            fieldInfo.fieldSet(i, data);
        } else {
            if (data.size() > 1 && fieldInfo.isThrowExp()) {
                throw new MPJException("Expected one result (or null) to be returned by select, but found: " +
                        data.size() + " , " + fieldInfo.getField().getName());
            } else {
                fieldInfo.fieldSet(i, data.stream().findFirst().orElse(null));
            }
        }
    }

    public static void bindMap(MPJTableFieldInfo fieldInfo, Map<String, Object> i, List<?> data) {
        if (fieldInfo.isCollection()) {
            i.put(fieldInfo.getField().getName(), data);
        } else {
            if (data.size() > 1 && fieldInfo.isThrowExp()) {
                throw new MPJException("Expected one result (or null) to be returned by select, but found: " +
                        data.size() + " , " + fieldInfo.getField().getName());
            } else {
                i.put(fieldInfo.getField().getName(), data.stream().findFirst().orElse(null));
            }
        }
    }
}
