package com.github.yulichang.adapter;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.yulichang.adapter.base.ITableInfoAdapter;
import com.github.yulichang.adapter.v33x.TableInfoAdapterV33x;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class AdapterHelper {

    private static final ITableInfoAdapter adapter;

    static {
        String version = MybatisPlusVersion.getVersion();
        if (StringUtils.isNotBlank(version) && version.startsWith("3.3.")) {
            adapter = new TableInfoAdapterV33x();
        } else {
            adapter = new TableInfoAdapter();
        }
    }

    public static ITableInfoAdapter getTableInfoAdapter() {
        return adapter;
    }
}
