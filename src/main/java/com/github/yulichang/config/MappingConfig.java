package com.github.yulichang.config;

import com.baomidou.mybatisplus.core.metadata.MPJTableInfoHelper;
import com.baomidou.mybatisplus.core.metadata.MPJTableMapperHelper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 关系映射配置
 *
 * @author yulichang
 * @since 1.2.0
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MappingConfig implements ApplicationListener<ApplicationReadyEvent> {

    @Override
    @SuppressWarnings("NullableProblems")
    public void onApplicationEvent(ApplicationReadyEvent event) {
        TableInfoHelper.getTableInfos().forEach(i ->
                MPJTableInfoHelper.initTableInfo(i.getEntityType(), MPJTableMapperHelper.get(i.getEntityType())));
    }
}
