package com.github.yulichang.mybatisplusjoin.solon.plugin;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.GlobalConfigUtils;
import com.github.yulichang.config.ConfigProperties;
import com.github.yulichang.config.MPJInterceptorConfig;
import com.github.yulichang.config.enums.IfAbsentEnum;
import com.github.yulichang.config.enums.LogicDelTypeEnum;
import com.github.yulichang.extension.mapping.config.MappingConfig;
import com.github.yulichang.injector.MPJSqlInjector;
import com.github.yulichang.toolkit.SpringContentUtils;
import com.github.yulichang.toolkit.reflect.GenericTypeUtils;
import com.github.yulichang.wrapper.enums.IfAbsentSqlKeyWordEnum;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.solon.MybatisAdapter;
import org.apache.ibatis.solon.integration.MybatisAdapterManager;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.Props;
import org.noear.solon.core.event.AppLoadEndEvent;
import org.noear.solon.core.util.GenericUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class XPluginImpl implements Plugin {

    @Override
    public void start(AppContext context) throws Throwable {
        /* 设置 MPJSqlInjector
        反射修改GlobalConfigUtils.GLOBAL_CONFIG属性
        修改final修饰的变量会出现警告
        使用solon方式配置不生效采用的“曲线救国”方式
        issue https://gitee.com/noear/solon/issues/I8SJ0Z */
        Field field = GlobalConfigUtils.class.getDeclaredField("GLOBAL_CONFIG");
        field.setAccessible(true);
        Field modifier = Field.class.getDeclaredField("modifiers");
        modifier.setAccessible(true);
        modifier.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(null, new ConcurrentHashMap<String, GlobalConfig>() {
            @Override
            @SuppressWarnings("NullableProblems")
            public GlobalConfig putIfAbsent(String key, GlobalConfig value) {
                value.setSqlInjector(new MPJSqlInjector());
                return super.putIfAbsent(key, value);
            }
        });
        GenericTypeUtils.setGenericTypeResolver(GenericUtil::resolveTypeArguments);
        // SpringContext兼容
        SpringContentUtils.setSpringContext(new SpringContentUtils.SpringContext() {
            @Override
            public <T> T getBean(Class<T> clazz) {
                return context.getBean(clazz);
            }

            @Override
            public <T> Map<String, T> getBeansOfType(Class<T> clazz) {
                return context.getBeansMapOfType(clazz);
            }
        });
        // 读取配置
        Props prop = context.cfg().getProp("mybatis-plus-join");
        ConfigProperties.banner = prop.getBool("banner", ConfigProperties.banner);
        ConfigProperties.subTableLogic = prop.getBool("subTableLogic", ConfigProperties.subTableLogic);
        ConfigProperties.msCache = prop.getBool("msCache", ConfigProperties.msCache);
        ConfigProperties.tableAlias = prop.get("tableAlias", ConfigProperties.tableAlias);
        ConfigProperties.joinPrefix = prop.get("joinPrefix", ConfigProperties.joinPrefix);
        ConfigProperties.logicDelType = prop.getOrDefault("logicDelType", ConfigProperties.logicDelType, val ->
                Arrays.stream(LogicDelTypeEnum.values()).filter(e -> e.name().equalsIgnoreCase(val)).findFirst()
                        .orElseThrow(() -> ExceptionUtils.mpe("mybatis-plus-join.logicDelType 配置错误")));
        ConfigProperties.mappingMaxCount = prop.getInt("mappingMaxCount", ConfigProperties.mappingMaxCount);
        ConfigProperties.ifAbsent = prop.getOrDefault("ifAbsent", ConfigProperties.ifAbsent, val ->
                Arrays.stream(IfAbsentEnum.values()).filter(e -> e.name().equalsIgnoreCase(val)).findFirst()
                        .map(m -> (BiPredicate<Object, IfAbsentSqlKeyWordEnum>) (o, ifAbsentSqlKeyWordEnum) -> m.test(o))
                        .orElseThrow(() -> ExceptionUtils.mpe("mybatis-plus-join.ifAbsent 配置错误")));
        // 后续操作
        context.onEvent(AppLoadEndEvent.class, e -> {
            List<SqlSessionFactory> sqlSessionFactoryList = MybatisAdapterManager.getAll().values().stream()
                    .map(MybatisAdapter::getFactory).collect(Collectors.toList());
            new MPJInterceptorConfig(sqlSessionFactoryList, ConfigProperties.banner);
            MappingConfig.init();
        });
    }
}