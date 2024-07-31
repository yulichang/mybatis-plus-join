package com.github.yulichang.kt.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.kt.resultmap.MybatisLabel;
import com.github.yulichang.kt.resultmap.MybatisLabelFree;
import com.github.yulichang.toolkit.KtUtils;
import com.github.yulichang.toolkit.MPJReflectionKit;
import com.github.yulichang.toolkit.support.FieldCache;
import com.github.yulichang.wrapper.interfaces.MFunction;
import com.github.yulichang.wrapper.resultmap.Label;
import kotlin.reflect.KProperty;

import java.util.Collection;
import java.util.Map;

/**
 * 对一或对多查询
 *
 * @author yulichang
 * @since 1.4.6
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public interface QueryLabel<Children> {

    void addLabel(Label<?> label, boolean isCollection);

    Children getChildren();

    /**
     * 一对多查询 调用此方法发必需要调用对应的 left join / right join ... 连表方法，否则会报错
     * <p>
     * 举例 UserDO AddressDO 为一对多关系  UserDTO 为结果类
     * <pre>
     *     MPJLambdaWrapper&lt;UserDO&gt; wrapper = new MPJLambdaWrapper&lt;UserDO&gt;();
     *     wrapper.selectAll(UserDO.class)
     *            .selectCollection(AddressDO.class, UserDTO::getAddressListDTO)
     *            .leftJoin(AddressDO.class, ...... )
     *            .eq(...)
     *            ...
     * <pre/>
     * 会自动将 AddressDO类中相同属性的字段 以mybatis<collection>的方式映射到UserDTO.addressListDTO属性中
     *
     * @since 1.3.0
     *
     * @param child    连表数据库实体类
     * @param dtoField 包装类对应的属性
     */
    default Children selectCollection(Class<?> child, KProperty<?> dtoField) {
        return selectCollection(null, child, dtoField);
    }

    default Children selectCollection(String prefix, Class<?> child, KProperty<?> dtoField) {
        String dtoFieldName = dtoField.getName();
        Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(KtUtils.ref(dtoField));
        FieldCache field = fieldMap.get(dtoFieldName);
        Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
        MybatisLabel.Builder<?, ?> builder;
        if (genericType == null || genericType.isAssignableFrom(child)) {
            //找不到集合泛型 List List<?> List<Object> ， 直接查询数据库实体
            builder = new MybatisLabel.Builder<>(prefix, dtoFieldName, child, field.getType());
        } else {
            builder = new MybatisLabel.Builder<>(prefix, dtoFieldName, child, field.getType(), genericType, true);
        }
        addLabel(builder.build(), true);
        return getChildren();
    }

    /**
     * 一对多查询 调用此方法发必需要调用对应的 left join / right join ... 连表方法，否则会报错
     * <p>
     * 举例 UserDO AddressDO 为一对多关系  UserDTO 为结果类
     * <pre>
     *   MPJLambdaWrapper&lt;UserDO&gt; wrapper = new MPJLambdaWrapper();
     *   wrapper.selectAll(UserDO.class)
     *      .selectCollection(AddressDO.class, UserDTO::getAddressListDTO, map -> map
     *           .id(AddressDO::getId, AddressDTO::getId)                 //如果属性名一致 可以传一个
     *           .result(AddressDO::getUserId)                            //如果属性名一致 可以传一个
     *           .result(AddressDO::getAddress, AddressDTO::getAddress))) //如果属性名一致 可以传一个
     *      .leftJoin(AddressDO.class, ...... )
     *      .eq(...)
     *      ...
     * <pre/>
     *
     * 会自动将 AddressDO类中指定的字段 以mybatis<collection>的方式映射到UserDTO.addressListDTO属性中
     *
     * @since 1.3.0
     *
     * @param child      连表数据库实体类
     * @param dtoField   包装类对应的属性
     * @param collection collection标签内容
     */
    default Children selectCollection(Class<?> child,
                                      KProperty<?> dtoField,
                                      MFunction<MybatisLabel.Builder<?, ?>> collection) {
        return selectCollection(null, child, dtoField, collection);
    }

    default Children selectCollection(KProperty<?> dtoField, MFunction<MybatisLabelFree.Builder<?>> collection) {
        //自由映射必须存在泛型Z
        String dtoFieldName = dtoField.getName();
        FieldCache field = MPJReflectionKit.getFieldMap(KtUtils.ref(dtoField)).get(dtoFieldName);
        //获取集合泛型
        Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
        MybatisLabelFree.Builder<?> builder = new MybatisLabelFree.Builder<>(dtoFieldName, field.getType(), genericType);
        MybatisLabelFree.Builder<?> czBuilder = collection.apply(builder);
        addLabel(czBuilder.build(), true);
        return getChildren();
    }

    default Children selectCollection(String prefix,
                                      Class<?> child,
                                      KProperty<?> dtoField,
                                      MFunction<MybatisLabel.Builder<?, ?>> collection) {
        String dtoFieldName = dtoField.getName();
        FieldCache field = MPJReflectionKit.getFieldMap(KtUtils.ref(dtoField)).get(dtoFieldName);
        //获取集合泛型
        Class<?> genericType = MPJReflectionKit.getGenericType(field.getField());
        MybatisLabel.Builder<?, ?> builder = new MybatisLabel.Builder<>(prefix, dtoFieldName, child, field.getType(), genericType, false);
        MybatisLabel.Builder<?, ?> czBuilder = collection.apply(builder);
        addLabel(czBuilder.build(), true);
        return getChildren();
    }

    /**
     * 对一查询 用法参考 selectCollection
     *
     * @since 1.3.0
     */
    default Children selectAssociation(Class<?> child, KProperty<?> dtoField) {
        return selectAssociation(null, child, dtoField);
    }

    default Children selectAssociation(String prefix, Class<?> child, KProperty<?> dtoField) {
        String dtoFieldName = dtoField.getName();
        Map<String, FieldCache> fieldMap = MPJReflectionKit.getFieldMap(KtUtils.ref(dtoField));
        FieldCache field = fieldMap.get(dtoFieldName);
        Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
        MybatisLabel.Builder<?, ?> builder;
        builder = new MybatisLabel.Builder<>(StringUtils.isBlank(prefix) ? null : prefix,
                dtoFieldName, child, field.getType(), (Class<?>) field.getType(), true);
        addLabel(builder.build(), false);
        return getChildren();
    }

    /**
     * 对一查询 用法参考 selectCollection
     *
     * @since 1.3.0
     */
    default Children selectAssociation(Class<?> child, KProperty<?> dtoField,
                                       MFunction<MybatisLabel.Builder<?, ?>> collection) {
        return selectAssociation(null, child, dtoField, collection);
    }

    default Children selectAssociation(KProperty<?> dtoField,
                                       MFunction<MybatisLabelFree.Builder<?>> collection) {
        String dtoFieldName = dtoField.getName();
        FieldCache field = MPJReflectionKit.getFieldMap(KtUtils.ref(dtoField)).get(dtoFieldName);
        Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
        MybatisLabelFree.Builder<?> builder = new MybatisLabelFree.Builder<>(dtoFieldName, field.getType(), (Class<?>) field.getType());
        MybatisLabelFree.Builder<?> cfBuilder = collection.apply(builder);
        addLabel(cfBuilder.build(), false);
        return getChildren();
    }

    default Children selectAssociation(String prefix, Class<?> child, KProperty<?> dtoField,
                                       MFunction<MybatisLabel.Builder<?, ?>> collection) {
        String dtoFieldName = dtoField.getName();
        FieldCache field = MPJReflectionKit.getFieldMap(KtUtils.ref(dtoField)).get(dtoFieldName);
        Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
        MybatisLabel.Builder<?, ?> builder = new MybatisLabel.Builder<>(StringUtils.isBlank(prefix) ? null : prefix,
                dtoFieldName, child, field.getType(), (Class<?>) field.getType(), false);
        MybatisLabel.Builder<?, ?> cfBuilder = collection.apply(builder);
        addLabel(cfBuilder.build(), false);
        return getChildren();
    }
}
