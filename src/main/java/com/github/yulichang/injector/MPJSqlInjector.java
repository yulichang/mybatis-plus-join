package com.github.yulichang.injector;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.*;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.github.yulichang.mapper.MPJTableMapperHelper;
import com.github.yulichang.method.*;
import com.github.yulichang.method.mp.SelectCount;
import com.github.yulichang.method.mp.SelectList;
import com.github.yulichang.method.mp.SelectMaps;
import com.github.yulichang.method.mp.SelectMapsPage;
import com.github.yulichang.method.mp.SelectObjs;
import com.github.yulichang.method.mp.SelectOne;
import com.github.yulichang.method.mp.SelectPage;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * SQL 注入器
 *
 * @author yulichang
 * @see DefaultSqlInjector
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnMissingBean({DefaultSqlInjector.class, AbstractSqlInjector.class, ISqlInjector.class})
public class MPJSqlInjector extends DefaultSqlInjector {

    private static final List<String> METHOD_LIST = Arrays.asList("SelectOne", "SelectCount",
            "SelectMaps", "SelectMapsPage", "SelectObjs", "SelectList", "SelectPage");


    /**
     * 升级到 mybatis plus 3.4.3.2 后对之前的版本兼容
     */
    @SuppressWarnings("unused")
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> list = Stream.of(
                new Insert(),
                new Delete(),
                new DeleteByMap(),
                new DeleteById(),
                new DeleteBatchByIds(),
                new Update(),
                new UpdateById(),
                new SelectById(),
                new SelectBatchByIds(),
                new SelectByMap()
        ).collect(toList());
        list.addAll(getJoinMethod());
        list.addAll(getSelectMethod());
        return list;
    }

    /**
     * mybatis plus 3.4.3.2
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        List<AbstractMethod> list = super.getMethodList(mapperClass, tableInfo);
        list.removeIf(i -> METHOD_LIST.contains(i.getClass().getSimpleName()));
        list.addAll(getSelectMethod());
        list.addAll(getJoinMethod());
        return list;
    }

    private List<AbstractMethod> getJoinMethod() {
        List<AbstractMethod> list = new ArrayList<>();

        String version = MybatisPlusVersion.getVersion();
        String[] split = version.split("\\.");
        int v1 = Integer.parseInt(split[0]);
        int v2 = Integer.parseInt(split[1]);
        if ((v1 == 3 && v2 >= 5) || v1 > 3) {
            list.add(new SelectJoinCount(SqlMethod.SELECT_JOIN_COUNT.getMethod()));
            list.add(new SelectJoinOne(SqlMethod.SELECT_JOIN_ONE.getMethod()));
            list.add(new SelectJoinList(SqlMethod.SELECT_JOIN_LIST.getMethod()));
            list.add(new SelectJoinPage(SqlMethod.SELECT_JOIN_PAGE.getMethod()));
            list.add(new SelectJoinMap(SqlMethod.SELECT_JOIN_MAP.getMethod()));
            list.add(new SelectJoinMaps(SqlMethod.SELECT_JOIN_MAPS.getMethod()));
            list.add(new SelectJoinMapsPage(SqlMethod.SELECT_JOIN_MAPS_PAGE.getMethod()));
        } else {
            list.add(new SelectJoinCount());
            list.add(new SelectJoinOne());
            list.add(new SelectJoinList());
            list.add(new SelectJoinPage());
            list.add(new SelectJoinMap());
            list.add(new SelectJoinMaps());
            list.add(new SelectJoinMapsPage());
        }
        return list;
    }

    private List<AbstractMethod> getSelectMethod() {
        List<AbstractMethod> list = new ArrayList<>();
        list.add(new SelectOne());
        list.add(new SelectCount());
        list.add(new SelectMaps());
        list.add(new SelectMapsPage());
        list.add(new SelectObjs());
        list.add(new SelectList());
        list.add(new SelectPage());
        return list;
    }

    @Override
    public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
        Class<?> modelClass = getSuperClassGenericType(mapperClass, Mapper.class, 0);
        super.inspectInject(builderAssistant, mapperClass);
        MPJTableMapperHelper.init(modelClass, mapperClass);
    }

    public static Class<?> getSuperClassGenericType(final Class<?> clazz, final Class<?> genericIfc, final int index) {
        Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(ClassUtils.getUserClass(clazz), genericIfc);
        return null == typeArguments ? null : typeArguments[index];
    }
}
