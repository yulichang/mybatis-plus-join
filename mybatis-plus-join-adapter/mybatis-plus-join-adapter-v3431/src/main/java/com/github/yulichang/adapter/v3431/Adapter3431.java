package com.github.yulichang.adapter.v3431;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.github.yulichang.adapter.base.IAdapter;
import com.github.yulichang.adapter.base.metadata.OrderFieldInfo;
import com.github.yulichang.adapter.base.tookit.VersionUtils;
import com.github.yulichang.adapter.jsqlparser.v46.JSqlParserHelperV46;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author yulichang
 * @since 1.4.7
 */
@AllArgsConstructor
public class Adapter3431 implements IAdapter {

    private static final boolean v = VersionUtils.compare(MybatisPlusVersion.getVersion(), "3.4.3") < 0;

    @Override
    public List<OrderFieldInfo> mpjGetOrderField(TableInfo tableInfo) {
        return v ? null : tableInfo.getOrderByFields().stream().map(f ->
                new OrderFieldInfo(f.getColumn(), f.getOrderByType(), f.getOrderBySort())).collect(Collectors.toList());
    }

    @Override
    public void parserColum(String alias, String from, String selectSql, Consumer<String> columConsumer) {
        JSqlParserHelperV46.parserColum(alias, from, selectSql, columConsumer);
    }
}
