package com.github.yulichang.adapter;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.github.yulichang.adapter.base.IAdapter;
import com.github.yulichang.adapter.v33x.AdapterV33x;

public class AdapterHelper {

    public static IAdapter getAdapter() {
        String version = MybatisPlusVersion.getVersion();
        if (version.startsWith("3.3.")) {
            return new AdapterV33x();
        }
        return new Adapter();
    }
}
