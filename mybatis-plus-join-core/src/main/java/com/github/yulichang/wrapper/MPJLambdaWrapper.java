package com.github.yulichang.wrapper;

import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.toolkit.LambdaUtils;
import com.github.yulichang.toolkit.*;
import com.github.yulichang.toolkit.support.ColumnCache;
import com.github.yulichang.wrapper.enums.BaseFuncEnum;
import com.github.yulichang.wrapper.interfaces.LambdaJoin;
import com.github.yulichang.wrapper.interfaces.Query;
import com.github.yulichang.wrapper.interfaces.on.OnFunction;
import com.github.yulichang.wrapper.resultmap.MFunc;
import com.github.yulichang.wrapper.resultmap.MybatisLabel;
import com.github.yulichang.wrapper.segments.Select;
import com.github.yulichang.wrapper.segments.SelectAlias;
import com.github.yulichang.wrapper.segments.SelectFunc;
import com.github.yulichang.wrapper.segments.SelectNormal;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 参考 {@link com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper}
 * Lambda 语法使用 Wrapper
 *
 * @author yulichang
 * @see MPJWrappers
 */
@SuppressWarnings({"unused", "unchecked"})
public class MPJLambdaWrapper<T> extends MPJAbstractLambdaWrapper<T, MPJLambdaWrapper<T>>
        implements Query<MPJLambdaWrapper<T>>, LambdaJoin<MPJLambdaWrapper<T>, T> {

    /**
     * 查询表
     */
    private final SharedString from = new SharedString();
    /**
     * 主表别名
     */
    private final SharedString alias = new SharedString(Constant.TABLE_ALIAS);
    /**
     * 查询的字段
     */
    @Getter
    private final List<Select> selectColumns = new ArrayList<>();
    /**
     * 映射关系
     */
    @Getter
    private final List<MybatisLabel<?, ?>> resultMapMybatisLabel = new ArrayList<>();
    /**
     * 是否构建是否存在一对多
     */
    @Getter
    private boolean resultMap = false;
    /**
     * 是否自定义resultMap 自动构建不算
     */
    @Getter
    private boolean customResult = false;
    /**
     * 查询字段 sql
     */
    private SharedString sqlSelect = new SharedString();
    /**
     * 是否 select distinct
     */
    private boolean selectDistinct = false;
    /**
     * 表序号
     */
    private int tableIndex = 1;
    /**
     * 连表关键字 on 条件 func 使用
     */
    @Getter
    private String keyWord;
    /**
     * 副表逻辑删除开关
     */
    private boolean subLogicSql = ConfigProperties.subTableLogic;
    /**
     * 主表逻辑删除开关
     */
    private boolean logicSql = true;

    /**
     * 不建议直接 new 该实例，使用 MPJWrappers.<UserDO>lambdaQuery()
     */
    public MPJLambdaWrapper() {
        super.initNeed();
    }


    /**
     * 不建议直接 new 该实例，使用 MPJWrappers.<UserDO>lambdaQuery()
     */
    MPJLambdaWrapper(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
                     Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments,
                     SharedString lastSql, SharedString sqlComment, SharedString sqlFirst,
                     Map<Class<?>, Integer> subTable, String keyWord, Class<?> joinClass) {
        super.setEntity(entity);
        super.setEntityClass(entityClass);
        this.paramNameSeq = paramNameSeq;
        this.paramNameValuePairs = paramNameValuePairs;
        this.expression = mergeSegments;
        this.sqlSelect = sqlSelect;
        this.lastSql = lastSql;
        this.sqlComment = sqlComment;
        this.sqlFirst = sqlFirst;
        this.subTable = subTable;
        this.keyWord = keyWord;
        this.joinClass = joinClass;
    }


    /**
     * sql去重
     * select distinct
     */
    public MPJLambdaWrapper<T> distinct() {
        this.selectDistinct = true;
        return typedThis;
    }

    @Override
    @SafeVarargs
    public final <S> MPJLambdaWrapper<T> select(SFunction<S, ?>... columns) {
        if (ArrayUtils.isNotEmpty(columns)) {
            for (SFunction<S, ?> s : columns) {
                SelectNormal cache = getCache(s);
                selectColumns.add(cache);
            }
        }
        return typedThis;
    }

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
    public <S, C, Z, F extends java.util.Collection<?>> MPJLambdaWrapper<T> selectCollection(Class<C> child, SFunction<S, F> dtoField) {
        String dtoFieldName = LambdaUtils.getName(dtoField);
        Class<S> dtoClass = LambdaUtils.getEntityClass(dtoField);
        Map<String, Field> fieldMap = MPJReflectionKit.getFieldMap(dtoClass);
        Field field = fieldMap.get(dtoFieldName);
        this.resultMap = true;
        Class<?> genericType = MPJReflectionKit.getGenericType(field);
        MybatisLabel.Builder<C, Z> builder;
        if (genericType == null || genericType.isAssignableFrom(child)) {
            //找不到集合泛型 List List<?> List<Object> ， 直接查询数据库实体
            builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType());
        } else {
            Class<Z> ofType = (Class<Z>) genericType;
            builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), ofType, true);
        }
        this.resultMapMybatisLabel.add(builder.build());
        return typedThis;
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
    public <S, C, Z, F extends java.util.Collection<Z>> MPJLambdaWrapper<T>
    selectCollection(Class<C> child, SFunction<S, F> dtoField, MFunc<MybatisLabel.Builder<C, Z>> collection) {
        String dtoFieldName = LambdaUtils.getName(dtoField);
        Class<S> dtoClass = LambdaUtils.getEntityClass(dtoField);
        Field field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
        this.resultMap = true;
        //获取集合泛型
        Class<?> genericType = MPJReflectionKit.getGenericType(field);
        Class<Z> ofType = (Class<Z>) genericType;
        MybatisLabel.Builder<C, Z> builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), ofType, false);
        MybatisLabel.Builder<C, Z> czBuilder = collection.apply(builder);
        this.customResult = czBuilder.hasCustom();
        this.resultMapMybatisLabel.add(czBuilder.build());
        return typedThis;
    }

    /**
     * 对一查询 用法参考 selectCollection
     *
     * @since 1.3.0
     */
    public <S, C, F> MPJLambdaWrapper<T> selectAssociation(Class<C> child, SFunction<S, F> dtoField) {
        String dtoFieldName = LambdaUtils.getName(dtoField);
        Class<S> dtoClass = LambdaUtils.getEntityClass(dtoField);
        Map<String, Field> fieldMap = MPJReflectionKit.getFieldMap(dtoClass);
        Field field = fieldMap.get(dtoFieldName);
        Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
        this.resultMap = true;
        MybatisLabel.Builder<C, F> builder;
        builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), (Class<F>) field.getType(), true);
        this.resultMapMybatisLabel.add(builder.build());
        return typedThis;
    }

    /**
     * 对一查询 用法参考 selectCollection
     *
     * @since 1.3.0
     */
    public <S, C, F> MPJLambdaWrapper<T> selectAssociation(Class<C> child, SFunction<S, F> dtoField,
                                                           MFunc<MybatisLabel.Builder<C, F>> collection) {
        String dtoFieldName = LambdaUtils.getName(dtoField);
        Class<S> dtoClass = LambdaUtils.getEntityClass(dtoField);
        Field field = MPJReflectionKit.getFieldMap(dtoClass).get(dtoFieldName);
        this.resultMap = true;
        Assert.isFalse(Collection.class.isAssignableFrom(field.getType()), "association 不支持集合类");
        MybatisLabel.Builder<C, F> builder = new MybatisLabel.Builder<>(dtoFieldName, child, field.getType(), (Class<F>) child, false);
        MybatisLabel.Builder<C, F> cfBuilder = collection.apply(builder);
        this.customResult = cfBuilder.hasCustom();
        this.resultMapMybatisLabel.add(cfBuilder.build());
        return typedThis;
    }


    @Override
    public <E> MPJLambdaWrapper<T> select(Class<E> entityClass, Predicate<TableFieldInfo> predicate) {
        TableInfo info = TableInfoHelper.getTableInfo(entityClass);
        Map<String, SelectNormal> cacheMap = ColumnCache.getMapField(entityClass);
        info.getFieldList().stream().filter(predicate).collect(Collectors.toList()).forEach(
                i -> selectColumns.add(cacheMap.get(i.getProperty())));
        return typedThis;
    }

    @Override
    public <E> MPJLambdaWrapper<T> selectAsClass(Class<E> source, Class<?> tag) {
        List<SelectNormal> normalList = ColumnCache.getListField(source);
        Map<String, Field> fieldMap = MPJReflectionKit.getFieldMap(tag);
        for (SelectNormal cache : normalList) {
            if (fieldMap.containsKey(cache.getColumProperty())) {
                selectColumns.add(cache);
            }
        }
        return typedThis;
    }

    @Override
    public <S> MPJLambdaWrapper<T> selectAs(SFunction<S, ?> column, String alias) {
        selectColumns.add(new SelectAlias(getCache(column), alias));
        return typedThis;
    }

    public <S> MPJLambdaWrapper<T> selectFunc(BaseFuncEnum funcEnum, SFunction<S, ?> column, String alias) {
        selectColumns.add(new SelectFunc(getCache(column), alias, funcEnum));
        return typedThis;
    }

    @Override
    public MPJLambdaWrapper<T> selectFunc(BaseFuncEnum funcEnum, Object column, String alias) {
        selectColumns.add(new SelectFunc(alias, funcEnum, column.toString()));
        return typedThis;
    }

    public final MPJLambdaWrapper<T> selectAll(Class<?> clazz) {
        selectColumns.addAll(ColumnCache.getListField(clazz));
        return typedThis;
    }

    /**
     * 查询条件 SQL 片段
     */
    @Override
    public String getSqlSelect() {
        if (StringUtils.isBlank(sqlSelect.getStringValue()) && CollectionUtils.isNotEmpty(selectColumns)) {
            String s = selectColumns.stream().map(i -> {
                String str = Constant.TABLE_ALIAS + getDefaultSelect(i.getClazz(), (i.getClazz() == getEntityClass() && !i.isLabel())) + StringPool.DOT + i.getColumn();
                return i.isFunc() ? (String.format(i.getFunc().getSql(), str) + Constant.AS + i.getAlias()) : (i.isHasAlias() ? (str + Constant.AS + i.getAlias()) : str);
            }).collect(Collectors.joining(StringPool.COMMA));
            sqlSelect.setStringValue(s);
        }
        return sqlSelect.getStringValue();
    }

    /**
     * 获取连表部分语句
     */
    public String getFrom() {
        if (StringUtils.isBlank(from.getStringValue())) {
            StringBuilder value = new StringBuilder();
            for (MPJLambdaWrapper<?> wrapper : onWrappers) {
                String tableName = TableInfoHelper.getTableInfo(wrapper.getJoinClass()).getTableName();
                value.append(wrapper.getKeyWord())
                        .append(tableName)
                        .append(Constant.SPACE_TABLE_ALIAS)
                        .append(subTable.get(wrapper.getJoinClass()))
                        .append(Constant.ON)
                        .append(wrapper.getExpression().getNormal().getSqlSegment());
            }
            from.setStringValue(value.toString());
        }
        return from.getStringValue();
    }

    public String getAlias() {
        return alias.getStringValue();
    }


    public boolean getSelectDistinct() {
        return selectDistinct;
    }

    /**
     * 用于生成嵌套 sql
     * <p>故 sqlSelect 不向下传递</p>
     */
    @Override
    protected MPJLambdaWrapper<T> instance() {
        return instance(null, null);
    }

    protected MPJLambdaWrapper<T> instance(String keyWord, Class<?> joinClass) {
        return new MPJLambdaWrapper<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
                new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(), SharedString.emptyString(),
                this.subTable, keyWord, joinClass);
    }

    @Override
    public void clear() {
        super.clear();
        sqlSelect.toNull();
        from.toNull();
        selectColumns.clear();
        subTable.clear();
    }

    /**
     * 关闭副表逻辑删除
     * <p>
     * 副表逻辑删除默认在where语句中
     * 但有时候需要让它出现在on语句中, 这两种写法区别还是很大的
     * 所以可以关闭副表逻辑删除, 通过on语句多条件, 自己实现on语句的逻辑删除
     */
    public MPJLambdaWrapper<T> disableSubLogicDel() {
        this.subLogicSql = false;
        return typedThis;
    }

    public MPJLambdaWrapper<T> enableSubLogicDel() {
        this.subLogicSql = true;
        return typedThis;
    }

    /**
     * 关闭主表逻辑删除
     */
    public MPJLambdaWrapper<T> disableLogicDel() {
        this.logicSql = false;
        return typedThis;
    }

    public MPJLambdaWrapper<T> enableLogicDel() {
        this.logicSql = true;
        return typedThis;
    }

    /**
     * 副表部分逻辑删除支持
     */
    public String getSubLogicSql() {
        if (subLogicSql) {
            if (CollectionUtils.isEmpty(subTable)) {
                return StringPool.EMPTY;
            }
            return subTable.entrySet().stream().map(entry -> LogicInfoUtils.getLogicInfo(entry.getValue(),
                    entry.getKey())).collect(Collectors.joining(StringPool.SPACE));
        }
        return StringPool.EMPTY;
    }

    /**
     * 主表部分逻辑删除支持
     */
    public boolean getLogicSql() {
        return this.logicSql;
    }

    @Override
    public <R> MPJLambdaWrapper<T> join(String keyWord, Class<R> clazz, OnFunction<T> function) {
        MPJLambdaWrapper<T> apply = function.apply(instance(keyWord, clazz));
        subTable.put(clazz, tableIndex);
        onWrappers.add(apply);
        tableIndex++;
        return typedThis;
    }
}
