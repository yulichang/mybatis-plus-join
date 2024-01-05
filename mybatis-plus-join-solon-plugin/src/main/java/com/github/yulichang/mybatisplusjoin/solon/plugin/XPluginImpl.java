package com.github.yulichang.mybatisplusjoin.solon.plugin;

import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
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
import org.noear.solon.Utils;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.core.Props;
import org.noear.solon.core.event.AppLoadEndEvent;
import org.noear.solon.core.util.GenericUtil;

import javax.sql.DataSource;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

public class XPluginImpl implements Plugin {

    @Override
    public void start(AppContext context) {
        // MPJSqlInjector
        context.subWrapsOfType(DataSource.class, bw -> context.cfg().putIfAbsent(Utils.isEmpty(bw.name()) ?
                        "mybatis.globalConfig.sqlInjector" : ("mybatis." + bw.name() + ".globalConfig.sqlInjector"),
                MPJSqlInjector.class.getName()));
        // setGenericTypeResolver
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
        Prop prop = new Prop(context.cfg().getProp("mybatis-plus-join"));
        ConfigProperties.banner = prop.get("banner", Boolean::parseBoolean);
        ConfigProperties.subTableLogic = prop.get("subTableLogic", Boolean::parseBoolean);
        ConfigProperties.msCache = prop.get("msCache", Boolean::parseBoolean);
        ConfigProperties.tableAlias = prop.get("tableAlias", Function.identity());
        ConfigProperties.joinPrefix = prop.get("joinPrefix", Function.identity());
        ConfigProperties.logicDelType = prop.get("logicDelType", val ->
                Arrays.stream(LogicDelTypeEnum.values()).filter(e -> e.name().equalsIgnoreCase(val)).findFirst()
                        .orElseThrow(() -> ExceptionUtils.mpe("mybatis-plus-join.logicDelType 配置错误")));
        ConfigProperties.mappingMaxCount = prop.get("mappingMaxCount", Integer::parseInt);
        ConfigProperties.ifAbsent = prop.get("ifAbsent", val ->
                Arrays.stream(IfAbsentEnum.values()).filter(e -> e.name().equalsIgnoreCase(val)).findFirst()
                        .map(m -> (BiPredicate<Object, IfAbsentSqlKeyWordEnum>) (o, enums) -> m.test(o))
                        .orElseThrow(() -> ExceptionUtils.mpe("mybatis-plus-join.ifAbsent 配置错误")));
        // 后续操作
        context.onEvent(AppLoadEndEvent.class, e -> {
            List<SqlSessionFactory> sqlSessionFactoryList = MybatisAdapterManager.getAll().values().stream()
                    .map(MybatisAdapter::getFactory).collect(Collectors.toList());
            new MPJInterceptorConfig(sqlSessionFactoryList, ConfigProperties.banner);
            MappingConfig.init();
        });
    }

    private static class Prop {

        private final Properties props;

        public Prop(Props props) {
            this.props = new Properties();
            props.forEach((k, v) -> this.props.put(k.toString()
                    .replaceAll("[-_]", "").toUpperCase(Locale.ENGLISH), v));
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String key, Function<String, T> convert) {
            try {
                return Optional.ofNullable(this.props.get(key.toUpperCase(Locale.ENGLISH)))
                        .map(v -> convert.apply(v.toString()))
                        .orElse((T) ConfigProperties.class.getDeclaredField(key).get(null));
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }
}