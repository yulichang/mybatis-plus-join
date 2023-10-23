package com.github.yulichang.adapter;

import com.baomidou.mybatisplus.core.MybatisPlusVersion;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.github.yulichang.adapter.base.ITableInfoAdapter;
import com.github.yulichang.adapter.base.tookit.VersionUtils;
import com.github.yulichang.adapter.v33x.TableInfoAdapterV33x;
import com.github.yulichang.adapter.v3431.TableInfoAdapter3431;

/**
 * @author yulichang
 * @since 1.4.3
 */
public class AdapterHelper {

    private static final ITableInfoAdapter adapter;

    static {
        String version = MybatisPlusVersion.getVersion();
        if (VersionUtils.compare(version, "3.5.4") >= 0) {
            adapter = new TableInfoAdapter();
        } else if (VersionUtils.compare(version, "3.4.0") >= 0) {
            adapter = new TableInfoAdapter3431();
        } else if (VersionUtils.compare(version, "3.3.0") >= 0) {
            adapter = new TableInfoAdapterV33x();
        } else {
            throw ExceptionUtils.mpe("MPJ需要MP版本3.3.0+，当前MP版本%s", version);
        }
    }

    public static ITableInfoAdapter getTableInfoAdapter() {
        return adapter;
    }
}
