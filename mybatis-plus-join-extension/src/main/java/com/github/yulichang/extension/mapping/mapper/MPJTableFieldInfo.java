package com.github.yulichang.extension.mapping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.github.yulichang.adapter.AdapterHelper;
import com.github.yulichang.annotation.EntityMapping;
import com.github.yulichang.annotation.FieldMapping;
import com.github.yulichang.toolkit.ReflectionKit;
import com.github.yulichang.toolkit.SpringContentUtils;
import com.github.yulichang.toolkit.TableHelper;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.segments.SelectCache;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 字段属性
 *
 * @author yulichang
 * @since 1.2.0
 */
@Data
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
        this.entityType = entityType;
        this.isMappingEntity = true;
        this.isMappingField = false;
        initField(field);
        if (mapping.tag() != Object.class) {
            this.joinClass = mapping.tag();
        }
        this.isThrowExp = mapping.isThrowExp();
        initThisField(mapping.thisField());
        initJoinField(mapping.joinField());
        this.isRemoveBindField = checkArr(mapping.select()) &&
                (!Arrays.asList(mapping.select()).contains(this.joinProperty.trim()) &&
                        !Arrays.asList(mapping.select()).contains(this.joinColumn.trim()));
        this.wrapper = new MPJMappingWrapper(this.joinClass, mapping.first(), checkArr(mapping.select()) ?
                (this.isRemoveBindField ? propToColumn(this.joinClass, mapping.select(), this.joinProperty) :
                        propToColumn(this.joinClass, mapping.select(), null)) : null,
                mapping.apply(), mapping.condition(), mapping.last(), mapping.orderByAsc(), mapping.orderByDesc());
    }

    public MPJTableFieldInfo(Class<?> entityType, FieldMapping mappingField, Field field) {
        this.entityType = entityType;
        this.isMappingEntity = false;
        this.isMappingField = true;
        field.setAccessible(true);
        this.field = field;
        this.property = field.getName();
        this.isCollection = Collection.class.isAssignableFrom(field.getType());
        if (this.isCollection && !List.class.isAssignableFrom(this.field.getType())) {
            throw ExceptionUtils.mpe("对多关系的数据结构目前只支持 <List> 暂不支持其他Collection实现 " + this.field.getType().getTypeName());
        }
        this.joinClass = mappingField.tag();
        this.isThrowExp = mappingField.isThrowExp();
        initThisField(mappingField.thisField());
        initJoinField(mappingField.joinField());
        this.isRemoveBindField = !mappingField.select().equals(this.joinColumn.trim()) &&
                !mappingField.select().equals(this.joinProperty.trim());
        this.wrapper = new MPJMappingWrapper(this.joinClass, mappingField.first(), this.isRemoveBindField ?
                propToColumn(this.joinClass, new String[]{mappingField.select()}, this.joinProperty) :
                propToColumn(this.joinClass, new String[]{mappingField.select()}, null), mappingField.apply(),
                mappingField.condition(), mappingField.last(), mappingField.orderByAsc(), mappingField.orderByDesc());
        initBindField(mappingField.select());
    }

    private void initBindField(String bindName) {
        TableInfo info = TableHelper.getAssert(this.joinClass);
        //根据属性名查询
        Field field = info.getFieldList().stream().filter(i -> i.getProperty().equals(bindName))
                .findFirst().map(f -> getField(this.joinClass, f)).orElse(null);
        if (field == null && bindName.equals(info.getKeyProperty())) {
            field = ReflectionKit.getFieldList(joinClass).stream().filter(f ->
                    f.getName().equals(info.getKeyProperty())).findFirst().orElse(null);
        }
        if (field == null) {
            //根据字段查询
            field = info.getFieldList().stream()
                    .filter(i -> i.getColumn().equals(bindName))
                    .map(f -> getField(this.joinClass, f)).findFirst().orElse(null);
            if (field == null && bindName.equals(info.getKeyColumn())) {
                field = ReflectionKit.getFieldList(joinClass).stream().filter(f ->
                        f.getName().equals(info.getKeyProperty())).findFirst().orElse(null);
            }
            if (field == null) {
                throw ExceptionUtils.mpe("字段不存在 " + this.joinClass.getName() + " ，" + bindName);
            }
        }
        this.bindField = field;
        this.bindField.setAccessible(true);
    }

    private void initJoinField(String joinField) {
        if (StringUtils.isNotBlank(joinField)) {
            this.joinProperty = joinField;
        } else {
            TableInfo info = getTableInfo(this.joinClass);
            Assert.isTrue(info.havePK(), "实体未定义主键 %s ", this.joinClass.getName());
            this.joinProperty = info.getKeyProperty();
        }

        TableInfo joinTableInfo = getTableInfo(this.joinClass);
        TableFieldInfo joinFieldInfo = joinTableInfo.getFieldList().stream().filter(f ->
                f.getProperty().equals(this.joinProperty)).findFirst().orElse(null);
        if (joinFieldInfo == null) {
            if (AdapterHelper.getAdapter().mpjHasPK(joinTableInfo) && this.joinProperty.equals(joinTableInfo.getKeyProperty())) {
                this.joinColumn = joinTableInfo.getKeyColumn();
                this.joinField = ReflectionKit.getFieldList(this.joinClass).stream().filter(i ->
                        i.getName().equals(joinTableInfo.getKeyProperty())).findFirst().orElse(null);
            }
        } else {
            this.joinColumn = joinFieldInfo.getColumn();
            this.joinField = getField(this.joinClass, joinFieldInfo);
        }
        Assert.notNull(this.joinField, "注解属性joinField不存在 %s , %s", this.joinClass.getName(),
                StringUtils.isBlank(this.joinProperty) ? "主键" : this.joinProperty);
        Assert.notNull(this.joinColumn, "注解属性joinField不存在 %s , %s", this.joinClass.getName(),
                StringUtils.isBlank(this.joinProperty) ? "主键" : this.joinProperty);
        this.joinField.setAccessible(true);
    }

    private void initThisField(String thisField) {
        if (StringUtils.isNotBlank(thisField)) {
            this.thisProperty = thisField;
        } else {
            TableInfo info = getTableInfo(this.entityType);
            Assert.isTrue(info.havePK(), "实体未定义主键 %s ", this.entityType.getName());
            this.thisProperty = info.getKeyProperty();
        }

        TableInfo tableInfo = getTableInfo(this.entityType);
        if (AdapterHelper.getAdapter().mpjHasPK(tableInfo) && this.thisProperty.equals(tableInfo.getKeyProperty())) {
            this.thisField = ReflectionKit.getFieldList(ClassUtils.getUserClass(entityType)).stream().filter(f ->
                    f.getName().equals(tableInfo.getKeyProperty())).findFirst().orElse(null);
            Assert.notNull(this.thisField, "注解属性thisField不存在 %s , %s", entityType.getName(),
                    StringUtils.isBlank(this.thisProperty) ? "主键" : this.thisProperty);
            this.thisColumn = tableInfo.getKeyColumn();
        } else {
            TableFieldInfo fieldInfo = tableInfo.getFieldList().stream().filter(f ->
                    f.getProperty().equals(this.thisProperty)).findFirst().orElse(null);
            Assert.notNull(fieldInfo, "注解属性thisField不存在 %s , %s", entityType.getName(),
                    StringUtils.isBlank(this.thisProperty) ? "主键" : this.thisProperty);
            this.thisField = getField(this.entityType, fieldInfo);
            this.thisColumn = fieldInfo.getColumn();
        }
        this.thisField.setAccessible(true);
    }

    private void initField(Field field) {
        field.setAccessible(true);
        this.field = field;
        this.property = field.getName();
        this.isCollection = Collection.class.isAssignableFrom(field.getType());
        if (this.isCollection && !List.class.isAssignableFrom(this.field.getType())) {
            throw ExceptionUtils.mpe("对多关系的数据结构目前只支持 <List> 暂不支持其他Collection实现 " + this.field.getType().getTypeName());
        }
        if (Map.class.isAssignableFrom(field.getType())) {
            throw ExceptionUtils.mpe("映射查询不支持Map结构 <%s.%s>", this.entityType.getSimpleName(), field.getName());
        } else {
            if (field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) field.getGenericType();
                Type type = t.getActualTypeArguments()[0];
                if (type instanceof ParameterizedType) {
                    if (((ParameterizedType) type).getRawType() == Map.class) {
                        throw ExceptionUtils.mpe("映射查询不支持Map结构 <%s.%s>", this.entityType.getSimpleName(), field.getName());
                    }
                }
            }
        }

        if (List.class.isAssignableFrom(field.getType())) {
            if (field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType t = (ParameterizedType) field.getGenericType();
                this.joinClass = (Class<?>) t.getActualTypeArguments()[0];
            }
        } else {
            this.joinClass = field.getType();
        }
    }

    private boolean checkArr(String[] arr) {
        if (Objects.isNull(arr) || arr.length == 0) {
            return false;
        }
        return Arrays.stream(arr).anyMatch(StringUtils::isNotBlank);
    }

    private String propToColumn(Class<?> tag, String[] arr, String joinC) {
        Map<String, SelectCache> mapField = ColumnCache.getMapField(tag);
        List<String> args = null;
        if (checkArr(arr)) {
            args = Arrays.stream(arr).filter(StringUtils::isNotBlank).map(c -> {
                if (mapField.containsKey(c)) {
                    return mapField.get(c).getColumn();
                }
                return c;
            }).collect(Collectors.toList());
            if (StringUtils.isNotBlank(joinC)) {
                if (mapField.containsKey(joinC)) {
                    args.add(mapField.get(joinC).getColumn());
                }
            }
        }
        return Optional.ofNullable(args).map(i -> String.join(StringPool.COMMA, i)).orElse(null);
    }

    public BaseMapper<?> getJoinMapper() {
        if (this.joinMapper == null) {
            MPJTableInfo joinTableInfo = MPJTableInfoHelper.getTableInfo(this.joinClass);
            if (joinTableInfo == null) {
                throw ExceptionUtils.mpe("未注册 mapper " + this.joinClass.getName());
            }
            this.joinMapper = SpringContentUtils.getMapper(joinTableInfo.getEntityClass());
        }
        return this.joinMapper;
    }

    private TableInfo getTableInfo(Class<?> clazz) {
        return TableHelper.getAssert(clazz);
    }

    private Field getField(Class<?> table, TableFieldInfo tableFieldInfo) {
        return AdapterHelper.getAdapter().mpjGetField(tableFieldInfo, () ->
                ReflectionKit.getFieldMap(table).get(tableFieldInfo.getProperty()));
    }

    public void fieldSet(Object o, Object val) {
        try {
            this.field.set(o, val);
        } catch (Exception e) {
            throw ExceptionUtils.mpe("无法设置关联字段，请检查关联字段数据类型是否匹配 " + this.entityType.getName() +
                    " , " + this.field.getName() + " , " + o.getClass().getName());
        }
    }

    public Object thisFieldGet(Object o) {
        try {
            return getThisField().get(o);
        } catch (Exception e) {
            throw ExceptionUtils.mpe("无法获取当前关联字段，请检查关联字段是否匹配 " + this.entityType.getName() + " , " +
                    this.thisField.getName() + " , " + o.getClass().getName());
        }
    }

    public Object joinFieldGet(Object o) {
        try {
            return getJoinField().get(o);
        } catch (Exception e) {
            throw ExceptionUtils.mpe("无法设置关联字段，请检查关联字段数据类型是否匹配 " + this.joinClass.getName() + " , " +
                    this.joinField.getName() + " , " + o.getClass().getName());
        }
    }

    public Object bindFieldGet(Object o) {
        try {
            return getBindField().get(o);
        } catch (Exception e) {
            throw ExceptionUtils.mpe("无法设置关联字段，请检查关联字段数据类型是否匹配 " + this.joinClass.getName() + " , " +
                    this.bindField.getName() + " , " + o.getClass().getName());
        }
    }

    public void joinFieldSetNull(Object o) {
        try {
            this.joinField.set(o, null);
        } catch (Exception e) {
            throw ExceptionUtils.mpe("无法设置关联字段，请检查关联字段数据类型是否匹配 " + this.entityType.getName() +
                    " , " + this.joinField.getName() + " , " + o.getClass().getName());
        }
    }

    public void removeJoinField(List<?> joinList) {
        if (this.isMappingEntity() && this.isRemoveBindField()) {
            joinList.forEach(this::joinFieldSetNull);
        }
    }

    public static <T> void bind(MPJTableFieldInfo fieldInfo, T i, List<?> data) {
        if (fieldInfo.isCollection()) {
            fieldInfo.fieldSet(i, data);
        } else {
            if (data.size() > 1 && fieldInfo.isThrowExp()) {
                throw ExceptionUtils.mpe("Expected one result (or null) to be returned by select, but found: " +
                        data.size() + " , " + fieldInfo.getProperty());
            } else {
                fieldInfo.fieldSet(i, data.stream().findFirst().orElse(null));
            }
        }
    }
}
