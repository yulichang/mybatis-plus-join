package com.baomidou.mybatisplus.core.metadata;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.annotation.MPJMapping;
import com.github.yulichang.base.MPJBaseMapper;
import com.github.yulichang.exception.MPJException;
import com.github.yulichang.toolkit.SpringContentUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
     * 实体类
     */
    private final Class<?> entityType;
    /**
     * 属性
     */
    private Field field;
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
    private final String thisProperty;
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
    private final Class<?> joinClass;
    /**
     * 映射属性名
     */
    private final String joinProperty;
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
    private MPJBaseMapper<?> joinMapper;
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
    public MPJTableFieldInfo(Class<?> entityType, MPJMapping mapping, Field field1) {
        initField(field1);
        this.entityType = entityType;
        this.joinClass = mapping.tag();
        this.isThrowExp = mapping.isThrowExp();
        this.thisMapKey = StringUtils.isBlank(mapping.thisMapKey()) ? null : mapping.thisMapKey();
        this.joinMapKey = StringUtils.isBlank(mapping.joinMapKsy()) ? null : mapping.joinMapKsy();
        this.wrapper = new MPJMappingWrapper(mapping);
        if (this.isCollection && this.field.getType() != List.class && this.field.getType() != ArrayList.class) {
            throw new MPJException("对多关系的数据结构目前只支持 <List> 暂不支持其他Collection实现 " + this.field.getType().getTypeName());
        }
        if (StringUtils.isNotBlank(mapping.joinField())) {
            this.joinProperty = mapping.joinField();
        } else {
            TableInfo info = getTableInfo(this.joinClass);
            Assert.isTrue(info.havePK(), "实体未定义主键 %s ", this.joinClass.getName());
            this.joinProperty = info.getKeyProperty();
        }
        if (StringUtils.isNotBlank(mapping.thisField())) {
            this.thisProperty = mapping.thisField();
        } else {
            TableInfo info = getTableInfo(this.entityType);
            Assert.isTrue(info.havePK(), "实体未定义主键 %s ", this.entityType.getName());
            this.thisProperty = info.getKeyProperty();
        }
    }

    private void initField(Field field) {
        field.setAccessible(true);
        this.field = field;
        this.isCollection = Collection.class.isAssignableFrom(field.getType());

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
    }

    @SuppressWarnings("ConstantConditions")
    public Field getThisField() {
        if (this.thisField == null) {
            TableInfo tableInfo = getTableInfo(this.entityType);
            if (tableInfo.havePK() && this.thisProperty.equals(tableInfo.getKeyProperty())) {
                this.thisField = ReflectionKit.getFieldList(ClassUtils.getUserClass(entityType)).stream().filter(f ->
                        f.getName().equals(tableInfo.getKeyProperty())).findFirst().orElse(null);
                Assert.notNull(this.thisField, "MPJMapping注解thisField不存在 %s , %s", entityType.getName(),
                        StringUtils.isBlank(this.thisProperty) ? "主键" : this.thisProperty);
                this.thisColumn = tableInfo.getKeyColumn();
            } else {
                TableFieldInfo fieldInfo = tableInfo.getFieldList().stream().filter(f ->
                        f.getField().getName().equals(this.thisProperty)).findFirst().orElse(null);
                Assert.notNull(fieldInfo, "MPJMapping注解thisField不存在 %s , %s", entityType.getName(),
                        StringUtils.isBlank(this.thisProperty) ? "主键" : this.thisProperty);
                this.thisField = fieldInfo.getField();
                this.thisColumn = fieldInfo.getColumn();
            }
            this.thisField.setAccessible(true);
        }
        return this.thisField;
    }

    public String getJoinColumn() {
        if (StringUtils.isBlank(this.joinColumn)) {
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
            Assert.notNull(this.joinField, "MPJMapping注解thisField不存在 %s , %s", this.joinClass.getName(),
                    StringUtils.isBlank(this.joinProperty) ? "主键" : this.joinProperty);
            Assert.notNull(this.joinColumn, "MPJMapping注解thisField不存在 %s , %s", this.joinClass.getName(),
                    StringUtils.isBlank(this.joinProperty) ? "主键" : this.joinProperty);
            this.joinField.setAccessible(true);
        }
        return this.joinColumn;
    }

    public String getThisMapKey() {
        if (this.thisMapKey == null) {
            this.thisMapKey = getJoinColumn();
        }
        return this.thisMapKey;
    }

    public Field getJoinField() {
        if (this.joinField == null) {
            this.getJoinColumn();
        }
        return this.joinField;
    }


    public String getJoinMapKey() {
        if (this.joinMapKey == null) {
            this.joinMapKey = getJoinColumn();
        }
        return joinMapKey;
    }

    public MPJBaseMapper<?> getJoinMapper() {
        if (this.joinMapper == null) {
            MPJTableInfo joinTableInfo = MPJTableInfoHelper.getTableInfos().stream().filter(table ->
                    table.getTableInfo().getEntityType() == this.joinClass).findFirst().orElse(null);
            if (joinTableInfo == null) {
                throw new MPJException("未注册 mapper " + this.joinClass.getName());
            }
            this.joinMapper = (MPJBaseMapper<?>) SpringContentUtils.getApplicationContext().getBean(joinTableInfo.getMapperClass());
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

    public static <T> void bind(MPJTableFieldInfo fieldInfo, T i, List<?> data) {
        if (!fieldInfo.isCollection()) {
            if (data.size() > 1 && fieldInfo.isThrowExp()) {
                throw new MPJException("Expected one result (or null) to be returned by select, but found: " +
                        data.size() + " , " + fieldInfo.getField().getName());
            } else {
                fieldInfo.fieldSet(i, data.stream().findFirst().orElse(null));
            }
        } else {
            fieldInfo.fieldSet(i, data);
        }
    }

    public static void bindMap(MPJTableFieldInfo fieldInfo, Map<String, Object> i, List<?> data) {
        if (!fieldInfo.isCollection()) {
            if (data.size() > 1 && fieldInfo.isThrowExp()) {
                throw new MPJException("Expected one result (or null) to be returned by select, but found: " +
                        data.size() + " , " + fieldInfo.getField().getName());
            } else {
                i.put(fieldInfo.getField().getName(), data.stream().findFirst().orElse(null));
            }
        } else {
            i.put(fieldInfo.getField().getName(), data);
        }
    }


}
