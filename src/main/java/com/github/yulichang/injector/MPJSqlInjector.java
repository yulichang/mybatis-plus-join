package com.github.yulichang.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.github.yulichang.method.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import java.util.List;

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
        list.add(new SelectJoinOne());
        list.add(new SelectJoinList());
        list.add(new SelectJoinPage());
        list.add(new SelectJoinMap());
        list.add(new SelectJoinMaps());
        list.add(new SelectJoinMapsPage());
        return list;
    }
}
