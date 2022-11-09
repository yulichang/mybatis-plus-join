package com.github.yulichang.test;

import com.github.yulichang.test.mapper.UserMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Component
@SuppressWarnings("unused")
public class ListenerEvent implements ApplicationListener<ApplicationContextEvent> {
    @Resource
    private HikariDataSource hikariDataSource;
    @Resource
    private UserMapper userMapper;
    private SqlSessionFactory sqlSessionFactory;

    @PostConstruct
    public void init() {
        System.out.println("1");
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationContextEvent applicationContextEvent) {

    }
}
