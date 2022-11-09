package com.github.yulichang.test;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.github.yulichang.test.mapper.UserMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

@Component
@SuppressWarnings("unused")
public class ListenerEvent implements ApplicationListener<ApplicationContextEvent> {

    @Resource
    private UserMapper userMapper;

    @PostConstruct
    public void init() {
        List<TableInfo> infos = TableInfoHelper.getTableInfos();
    }

    @Override
    public void onApplicationEvent(@NotNull ApplicationContextEvent applicationContextEvent) {

    }
}
