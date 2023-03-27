package com.github.yulichang.adapter;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.github.yulichang.adapter.base.ITableInfoAdapter;
import com.github.yulichang.adapter.v33x.TableInfoAdapterV33x;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class AdapterHelper {

    public static ITableInfoAdapter getTableInfoAdapter() {
        String version = MybatisPlusVersion.getVersion();
        if (version.startsWith("3.3.")) {
            return new TableInfoAdapterV33x();
        }
        return new TableInfoAdapter();
    }
}
