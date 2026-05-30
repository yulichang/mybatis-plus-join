package com.github.yulichang.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.core.injector.methods.SelectById;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.github.yulichang.method.mp.*;
import com.github.yulichang.toolkit.MPJTableMapperHelper;
import com.github.yulichang.toolkit.ReflectionKit;
import com.github.yulichang.toolkit.TableHelper;
import lombok.Getter;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.session.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * SQL 注入器
 *
 * @author yulichang
 * @see DefaultSqlInjector
 */
@Getter
public class MPJSqlInjector extends DefaultSqlInjector {

    private AbstractSqlInjector sqlInjector;

    public MPJSqlInjector() {
    }

    public MPJSqlInjector(ISqlInjector sqlInjector) {
        if (Objects.nonNull(sqlInjector) && sqlInjector instanceof AbstractSqlInjector) {
            this.sqlInjector = (AbstractSqlInjector) sqlInjector;
        }
    }

    @Override
    public List<AbstractMethod> getMethodList(Configuration configuration, Class<?> mapperClass, TableInfo tableInfo) {
        if (Objects.nonNull(sqlInjector)) {
            return methodFilter(sqlInjector.getMethodList(configuration, mapperClass, tableInfo));
        }
        return methodFilter(super.getMethodList(configuration, mapperClass, tableInfo));
    }

    private List<AbstractMethod> methodFilter(List<AbstractMethod> list) {
        String packageStr = SelectById.class.getPackage().getName();
        List<String> methodList = Arrays.asList(
                "Update",
                "Delete",
                "SelectOne",
                "SelectCount",
                "SelectMaps",
                "SelectMapsPage",
                "SelectObjs",
                "SelectList",
                "SelectPage");
        list.removeIf(i -> methodList.contains(i.getClass().getSimpleName()) &&
                Objects.equals(packageStr, i.getClass().getPackage().getName()));
        addAll(list, getWrapperMethod());
        return list;
    }


    private List<AbstractMethod> getWrapperMethod() {
        List<AbstractMethod> list = new ArrayList<>();
        list.add(new Delete());
        list.add(new SelectOne());
        list.add(new SelectCount());
        list.add(new SelectMaps());
        list.add(new SelectMapsPage());
        list.add(new SelectObjs());
        list.add(new SelectList());
        list.add(new SelectPage());
        list.add(new Update());
        return list;
    }

    private void addAll(List<AbstractMethod> source, List<AbstractMethod> addList) {
        for (AbstractMethod method : addList) {
            if (source.stream().noneMatch(m -> m.getClass().getSimpleName().equals(method.getClass().getSimpleName()))) {
                source.add(method);
            }
        }
    }

    @Override
    public void inspectInject(MapperBuilderAssistant builderAssistant, Class<?> mapperClass) {
        Class<?> modelClass = ReflectionKit.getSuperClassGenericType(mapperClass, Mapper.class, 0);
        super.inspectInject(builderAssistant, mapperClass);
        MPJTableMapperHelper.init(modelClass, mapperClass);
        Supplier<Class<?>> supplier = () -> {
            try {
                return extractModelClassOld(mapperClass);
            } catch (Throwable throwable) {
                return null;
            }
        };
        TableHelper.init(modelClass, supplier.get());
    }

    @SuppressWarnings("IfStatementWithIdenticalBranches")
    protected Class<?> extractModelClassOld(Class<?> mapperClass) {
        Type[] types = mapperClass.getGenericInterfaces();
        ParameterizedType target = null;
        for (Type type : types) {
            if (type instanceof ParameterizedType) {
                Type[] typeArray = ((ParameterizedType) type).getActualTypeArguments();
                if (ArrayUtils.isNotEmpty(typeArray)) {
                    for (Type t : typeArray) {
                        if (t instanceof TypeVariable || t instanceof WildcardType) {
                            break;
                        } else {
                            target = (ParameterizedType) type;
                            break;
                        }
                    }
                }
                break;
            }
        }
        return target == null ? null : (Class<?>) target.getActualTypeArguments()[0];
    }
}
