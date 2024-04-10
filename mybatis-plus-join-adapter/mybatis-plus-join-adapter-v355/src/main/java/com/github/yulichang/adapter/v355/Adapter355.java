package com.github.yulichang.adapter.v355;

import com.github.yulichang.adapter.base.IAdapter;
import com.github.yulichang.adapter.jsqlparser.v46.JSqlParserHelperV46;

import java.util.function.Consumer;

/**
 * @author yulichang
 * @since 1.4.12
 */
public class Adapter355 implements IAdapter {

    @Override
    public void parserColum(String alias, String from, String selectSql, Consumer<String> columConsumer) {
        JSqlParserHelperV46.parserColum(alias, from, selectSql, columConsumer);
    }
}