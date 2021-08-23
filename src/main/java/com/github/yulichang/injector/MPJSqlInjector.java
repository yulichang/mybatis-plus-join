package com.github.yulichang.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.MPJTableAliasHelper;
import com.baomidou.mybatisplus.core.metadata.MPJTableInfoHelper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ClassUtils;
import com.github.yulichang.method.*;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.GenericTypeResolver;

import java.util.List;
import java.util.Objects;

/**
 * SQL 注入器
 *
 * @author yulichang
 * @see DefaultSqlInjector
 */
@ConditionalOnMissingBean(DefaultSqlInjector.class)
public class MPJSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> list = super.getMethodList(mapperClass);
        list.add(new SelectJoinCount());
        list.add(new SelectJoinOne());
        list.add(new SelectJoinList());
        list.add(new SelectJoinPage());
        list.add(new SelectJoinMap());
        list.add(new SelectJoinMaps());
        list.add(new SelectJoinMapsPage());
        return list;
    }

    @Override
    public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
        MPJTableAliasHelper.init(Objects.requireNonNull(getSuperClassGenericType(mapperClass, Mapper.class, 0)));
        super.inspectInject(builderAssistant, mapperClass);
        TableInfoHelper.getTableInfos().forEach(i ->
                MPJTableInfoHelper.initTableInfo(builderAssistant, i.getEntityType(), mapperClass));
    }

    public static Class<?> getSuperClassGenericType(final Class<?> clazz, final Class<?> genericIfc, final int index) {
        Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(ClassUtils.getUserClass(clazz), genericIfc);
        return null == typeArguments ? null : typeArguments[index];
    }
}
