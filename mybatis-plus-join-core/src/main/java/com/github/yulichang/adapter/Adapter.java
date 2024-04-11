package com.github.yulichang.adapter;

import com.github.yulichang.adapter.base.IAdapter;
import com.github.yulichang.adapter.jsqlparser.JSqlParserHelper;

import java.util.function.Consumer;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class Adapter implements IAdapter {

    @Override
    public void parserColum(String alias, String from, String selectSql, Consumer<String> columConsumer) {
        JSqlParserHelper.parserColum(alias, from, selectSql, columConsumer);
    }
}
