package com.github.yulichang.wrapper.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.MPJReflectionKit;
import com.github.yulichang.wrapper.resultmap.MFunc;
import com.github.yulichang.wrapper.resultmap.MybatisLabel;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings({"unchecked", "unused"})
public interface QueryLabel<Children> {

    void addLabel(MybatisLabel<?, ?> label);

    Children getChildren();

    /**
     * 一对多查询 调用此方法发必需要调用对应的 left join / right join ... 连表方法，否则会报错
     * <p>
     * 举例 UserDO AddressDO 为一对多关系  UserDTO 为结果类
     * <pre>
     *     MPJLambdaQueryWrapper<UserDO> wrapper = new MPJLambdaQueryWrapper<UserDO>();
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
     * @param <S>      包装类
     * @param <C>      对多数据库实体类
     * @param <Z>      包装类集合泛型
     * @param <F>      包装类集合字段泛型
     */
    default <S, C, Z, F extends java.util.Collection<?>> Children selectCollection(Class<C> child, SFunction<S, F> dtoField) {
        String dtoFieldName = LambdaUtils.getName(dtoField);
        Class<S> dtoClass = LambdaUtils.getEntityClass(dtoField);
        Map<String, Field> fieldMap = MPJReflectionKit.getFieldMap(dtoClass);
        Field field = fieldMap.get(dtoFieldName);
        Class<?> genericType = MPJReflectionKit.getGenericType(field);
        MybatisLabel.Builder<C, Z> builder;
        if (genericType == null || genericType.isAssignableFrom(child)) {
            //找不到集合泛型 List List<?> List<Object> ， 直接查询数据库实体
            builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType());
        } else {
            Class<Z> ofType = (Class<Z>) genericType;
            builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), ofType, true);
        }
        addLabel(builder.build());
        return getChildren();
    }

    /**
     * 一对多查询 调用此方法发必需要调用对应的 left join / right join ... 连表方法，否则会报错
     * <p>
     * 举例 UserDO AddressDO 为一对多关系  UserDTO 为结果类
     * <pre>
     *   MPJLambdaQueryWrapper<UserDO> wrapper = new MPJLambdaQueryWrapper();
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
     * @param <S>        包装类
     * @param <C>        对多数据库实体类
     * @param <Z>        包装类集合泛型
     * @param <F>        包装类集合字段泛型
     */
    default <S, C, Z, F extends java.util.Collection<Z>> Children selectCollection(Class<C> child,
                                                                                   SFunction<S, F> dtoField,
                                                                                   MFunc<MybatisLabel.Builder<C, Z>> collection) {
        String dtoFieldName = LambdaUtils.getName(dtoField);
        Class<S> dtoClass = LambdaUtils.getEntityClass(dtoField);
        Field field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
        //获取集合泛型
        Class<?> genericType = MPJReflectionKit.getGenericType(field);
        Class<Z> ofType = (Class<Z>) genericType;
        MybatisLabel.Builder<C, Z> builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), ofType, false);
        MybatisLabel.Builder<C, Z> czBuilder = collection.apply(builder);
        addLabel(czBuilder.build());
        return getChildren();
    }

    /**
     * 对一查询 用法参考 selectCollection
     *
     * @since 1.3.0
     */
    default <S, C, F> Children selectAssociation(Class<C> child, SFunction<S, F> dtoField) {
        String dtoFieldName = LambdaUtils.getName(dtoField);
        Class<S> dtoClass = LambdaUtils.getEntityClass(dtoField);
        Map<String, Field> fieldMap = MPJReflectionKit.getFieldMap(dtoClass);
        Field field = fieldMap.get(dtoFieldName);
        Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
        MybatisLabel.Builder<C, F> builder;
        builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), (Class<F>) field.getType(), true);
        addLabel(builder.build());
        return getChildren();
    }

    /**
     * 对一查询 用法参考 selectCollection
     *
     * @since 1.3.0
     */
    default <S, C, F> Children selectAssociation(Class<C> child, SFunction<S, F> dtoField,
                                                 MFunc<MybatisLabel.Builder<C, F>> collection) {
        String dtoFieldName = LambdaUtils.getName(dtoField);
        Class<S> dtoClass = LambdaUtils.getEntityClass(dtoField);
        Field field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
        Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
        MybatisLabel.Builder<C, F> builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), (Class<F>) child, false);
        MybatisLabel.Builder<C, F> cfBuilder = collection.apply(builder);
        addLabel(cfBuilder.build());
        return getChildren();
    }
}
