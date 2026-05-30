package com.github.yulichang.adapter;

import lombok.Getter;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class AdapterHelper {

    @Getter
    private static IAdapter adapter = new Adapter();

    public static void setAdapter(IAdapter adapter) {
        AdapterHelper.adapter = adapter;
    }
}
