package com.github.yulichang.adapter.v3431;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.github.yulichang.adapter.base.ITableInfoAdapter;
import com.github.yulichang.adapter.base.metadata.OrderFieldInfo;
import com.github.yulichang.adapter.base.tookit.VersionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yulichang
 * @since 1.4.7
 */
public class TableInfoAdapter3431 implements ITableInfoAdapter {

    private static final boolean v = VersionUtils.compare(MybatisPlusVersion.getVersion(), "3.4.3") < 0;

    @Override
    public List<OrderFieldInfo> mpjGetOrderField(TableInfo tableInfo) {
        return v ? null : tableInfo.getOrderByFields().stream().map(f ->
                new OrderFieldInfo(f.getColumn(), f.getOrderByType(), f.getOrderBySort())).collect(Collectors.toList());
    }
}
