package com.example.mp.mybatis.plus.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.example.mp.mybatis.plus.method.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * SQL 注入器
 *
 * @see DefaultSqlInjector
 */
@Component
public class MySqlInjector extends DefaultSqlInjector {

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
